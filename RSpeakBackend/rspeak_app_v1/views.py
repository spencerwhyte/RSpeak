# Hashem Shawqi and Spencer Whyte
# This file contains all the HTTP Request Handlers for the app

import random
import json
import time

from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from models import Device, Question, Thread, Response
from notifications import Updates, sendSyncNotification
from utils import random_alphanumeric

 
def retrieve_csrf(request):
	"""
	returns csrf token
	"""
	t = Template("{% csrf_token %}")
	c = Context({})
	return t.render(c)

@csrf_exempt
def print_db(request):
	"""
	print out all tables in the database.
	FOR DEBUGGING PURPOSES ONLY
	"""
	devices = Device.objects.all()
	questions = Question.objects.all()
	threads = Thread.objects.all()
	responses = Response.objects.all()

	print "DEVICES TABLE: \n" + str(devices)
	print "QUESTIONS TABLE: \n" + str(questions)
	print "THREADS TABLE: \n" + str(threads)
	print "RESPONSES TABLE: \n" + str(responses)

	return HttpResponse( json.dumps({}), content_type="application/json" )


@csrf_exempt
def retrieve_credit_score(request):
	"""
	When a device wants to retrieve its current credit
	We will need:
	1. Only the device id
	"""
	if request.method == 'POST':
		json_data = json.loads( request.body )

		try:
			device_id = json_data['device_id']
		except KeyError:
			print "Error: A posted question did not have a JSON object with the required properties"
		else:
			# See if the device id already exists
			try:
				device = Device.objects.get( device_id=device_id )
			except Device.DoesNotExist:
				device = None
			if device:
				return HttpResponse( json.dumps({ 'credit_points' : device.credit_points }), content_type="application/json" )
			else:
				return HttpResponse( json.dumps({ 'credit_points' : -1 }), content_type="application/json" )

@csrf_exempt
def register_device(request):
	"""
	Request handler when a new device installs the app
	We will need:
	1. Device id
	2. Device type
	3. Push notification id
	these will ensure proper communication with the device
	"""
	if request.method == 'POST':
		json_data = json.loads( request.body )

		try:
			device_id = json_data['device_id']
			device_type = json_data['device_type']
		except KeyError:
			print "Error: A posted question did not have a JSON object with the required properties"
		else:
			# See if the device id already exists
			try:
				device = Device.objects.get( device_id=device_id )
			except Device.DoesNotExist:
				device = None
			if device:
				return HttpResponse( json.dumps({ 'valid_id' : False }), content_type="application/json" )
			else:
				device = Device( device_id=device_id, device_type=device_type )
				device.save()
				return HttpResponse( json.dumps({ 'valid_id' : True }), content_type="application/json" )


@csrf_exempt
def register_push_notification_id(request):
	"""
	If the device didn't have the push notification id ready
	when registering then 
	"""
	if request.method == 'POST':
		json_data = json.loads( request.body )

		try:
			device_id = json_data['device_id']
			push_notification_id = json_data['push_notification_id']
		except KeyError:
			print "Error: A posted question did not have a JSON object with the required properties"
		else:
			# See if the device id already exists
			device = Device.objects.filter( device_id=device_id )

			if device.exists():
				device[0].push_notification_id = push_notification_id
				device[0].save()
				return HttpResponse( json.dumps({ 'valid_id' : True }), content_type="application/json" )
			else:
				return HttpResponse( json.dumps({ 'valid_id' : False }), content_type="application/json" )


@csrf_exempt
def ask(request):
	"""
	Request handler when someone posts a question
	1. Add question content to the database
	2. Select random active answerer
	3. Put the question in the answerer's update stack
	4. Send push notification to the answerer's device to retrieve updates
	"""
	if request.method == 'POST':
		json_data = json.loads( request.body )

		try:
			question_id = json_data['question_id']
			question_content = json_data['content']
			asker_device_id = json_data['device_id']
		except KeyError:
			print "Error: A posted question did not have a JSON object with the required properties"
		else:
			# then add question to database
			question = Question( question_id=question_id, asker_device_id=asker_device_id, question_content=question_content )
			question.save()

			# then select a random device to send the question to
			all_devices = Device.objects.all().values()
			random_device = random.choice( all_devices ) if len( all_devices ) > 1 else None

			# ensure that we've a valid answerer device
			if random_device is None:
				return
			while len( all_devices ) > 1 and random_device['device_id'] is asker_device_id:
				random_device = random.choice( all_devices )

			# find a unique thread id
			thread_id = random_alphanumeric( 16 )
			response_thread = Thread.objects.filter( thread_id=thread_id )
			while response_thread.exists():
				thread_id = random_alphanumeric( 16 )
				response_thread = Thread.objects.filter( thread_id=thread_id )


			# Start the thread between the asker device and the random device	
			response_thread = Thread( thread_id=thread_id, question_id=question.question_id, asker_device_id=asker_device_id, answerer_device=random_device )
			response_thread.save()
			print "response thread with id: " + str(response_thread.thread_id)

			# add question to answerer_device update stack
			QuestionUpdates.add_update( device_id, { 'thread_id' : thread_id, 'content' : content, 'time' : int( time.time() ) })

			return HttpResponse( json.dumps({ 'question_id' : question_id, 'thread_id' : response_thread.thread_id }), content_type="application/json" )


@csrf_exempt
def respond(request):
	"""
	Request handler when someone posts a response
	1. Add response content to the database
	2. Send push notification to client device
	3. Update the credit of the responder
	"""
	if request.method == 'POST':
		json_data = json.loads( request.body )

		try:
			thread_id = json_data['thread_id']
			response_content = json_data['content']
			device_id = json_data['device_id']
		except KeyError:
			print "Error: A posted response did not have a JSON object with the required properties"
		else:
			# check that the thread id and the device ids are valid
			thread = Thread.objects.filter( thread_id=thread_id )
			device = Device.objects.filter( device_id=device_id )

			if thread.exists() and device.exists():
				# add response to database
				response = Response( thread=thread[0], responder_device=device[0], response_content=response_content )
				response.save()

				# add update to the other device
				asker_device = thread[0].asker_device
				answerer_device = thread[0].answerer_device

				if asker_device.device_id is device.device_id:
					ResponseUpdates.add_update( answerer_device, { 'thread_id' : thread_id, 'content' : response_content, 'time' : int( time.time()  } )
				
				elif answerer_device.device_id is device.device_id:
					ResponseUpdates.add_update( asker_device, { 'thread_id' : thread_id, 'content' : response_content, 'time' : int( time.time()  } )

				return HttpResponse( json.dumps({}), content_type="application/json" )


@csrf_exempt
def retrieve_updates(request):
	"""
	Request handler to update client model after receiving a push notification
	1. Check queue to see if the client has any updates on standby
	2. Send the client their update
	3. Get ack from client
	4. Empty the queue
	"""
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
				question_updates = QuestionUpdates.get_updates( device_id )
				response_updates = ResponseUpdates.get_updates( device_id )

				return HttpResponse( json.dumps({ 'question_updates' : question_updates, 'response_updates' : response_updates }), content_type="application/json" )