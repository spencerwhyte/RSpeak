# Hashem Shawqi and Spencer Whyte
# This file contains all the HTTP Request Handlers for the app

import random
import json

from django.http import HttpResponse
from rspeak_app_v1.models import Device, Question, Thread, Response
from rspeak_app_v1.utils.notifications import Updates
from utils.notifications import Updates, sendSyncNotification
from django.views.decorators.csrf import csrf_exempt

# returns csrf token
def retrieve_csrf(request):
	t = Template("{% csrf_token %}")
	c = Context({})
	return t.render(c)


# Request handler when a new device installs the app
# We will need:
# 1. Device id
# 2. Device type
# 3. Push notification id
# these will ensure proper communication with the device
@csrf_exempt
def register_device(request):
	if request.method == 'POST':
		json_data = json.loads( request.body )

		try:
			device_id = json_data['device_id']
			device_type = json_data['device_type']
		except KeyError:
			print "Error: A posted question did not have a JSON object with the required properties"
		else:
			# See if the device id already exists
			print "looking for preexisting id in db"
			try:
				device = Device.objects.get( device_id=device_id )
			except Device.DoesNotExist:
				device = None
			if device:
				print "found pre-existing id"
				return HttpResponse( json.dumps({ 'valid_id' : False }), content_type="application/json" )
			else:
				print "didn't find pre-existing id"
				device = Device( device_id=device_id, device_type=device_type )
				device.save()
				return HttpResponse( json.dumps({ 'valid_id' : True }), content_type="application/json" )

# If the device didn't have the push notification id ready
# when registering then 
@csrf_exempt
def register_push_notification_id(request):
	if request.method == 'POST':
		json_data = json.loads( request.body )

		try:
			device_id = json_data['device_id']
			push_notification_id = json_data['push_notification_id']
		except KeyError:
			print "Error: A posted question did not have a JSON object with the required properties"
		else:
			# See if the device id already exists
			device = Device.objects.get( device_id=device_id )

			if len(device) is not 0:
				device[0].push_notification_id = push_notification_id
				device[0].save()


# Request handler when someone posts a question
# 1. Add question content to the database
# 2. Select random active answerer
# 3. Put the question in the answerer's update stack
# 4. Send push notification to the answerer's device to retrieve updates
@csrf_exempt
def ask(request):
	if request.method == 'POST':
		json_data = json.loads( request.body )

		try:
			question_id = json_data['question_id']
			question_content = json_data['content']
			asker_device_id = json_data['device_id']
		except KeyError:
			print "Error: A posted question did not have a JSON object with the required properties"
		else:
			# Find device with the assigned id
			device = Device.objects.get( device_id=asker_device_id )

			# then add question to database
			question = Question( question_id=question_id, asker_device_id=device, question_content=question_content )
			question.save()
			print "added question to db"

			# then select a random device to send the question to
			all_devices = Device.objects.all().values()
			random_device = random.choice( all_devices ) if len( all_devices ) > 1 else None

			# ensure that we've a valid device
			if random_device is None:
				return
			while len( all_devices ) > 1 and random_device['device_id'] is asker_device_id:
				random_device = random.choice( all_devices )
			print "Found random device: " + str(random_device)

			# Start the thread between the asker device and the random device
			print "getting response thread"
			response_thread = Thread( question_id=question.question_id, asker_device_id=device )
			response_thread.save()

			return HttpResponse( json.dumps({ 'question_id' : question_id, 'thread_id' : response_thread.thread_id }), content_type="application/json" )


# Request handler when someone posts a response
# 1. Add response content to the database
# 2. Send push notification to client device
# 3. Update the credit of the responder
@csrf_exempt
def respond(request):
	if request.method == 'POST':
		json_data = json.loads( request.body )

		try:
			thread = json_data['thread_id']
			response_content = json_data['content']
			device_id = json_data['device_id']
		except KeyError:
			print "Error: A posted response did not have a JSON object with the required properties"
		else:
			# First, add response to database
			response = Response(thread_id=thread_id, responder_device_id=responder_device_id, response_content=response_content)
			response.save()


# Request handler to update client model after receiving a push notification
# 1. Check queue to see if the client has any updates on standby
# 2. Send the client their update
# 3. Get ack from client
# 4. Empty the queue
@csrf_exempt
def update_thread(request):
	if request.method == 'POST':
		json_data = json.loads( request.body )

		try:
			device_id = json_data['device_id']
		except KeyError:
			print "Error: A posted response did not have a JSON object with the required properties"
		else:

			# try sending the updates to the user
			# if this fails in any way (IOError) then make sure the updates
			# are back on the updates stack (else they won't reach the client).
			try:
				# retrieve updates and send them to the client device
				updates = Updates.get_updates( device_id )

				if updates is not None:
					return HttpResponse( json.dumps({ 'updates' : updates }), content_type="application/json" )
			except IOError:
				print "Error: The update_thread HTTP request was aborted"
			else:
				# put the updates back in the stack
				if updates is not None:
					for update in updates:
						Updates.add_update( device_id, update )

				return HttpResponse( json.dumps({ 'updates' : None }), content_type="application/json" )