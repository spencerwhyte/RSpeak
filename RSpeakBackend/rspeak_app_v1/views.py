# Hashem Shawqi and Spencer Whyte
# This file contains all the HTTP Request Handlers for the app

import random

from django.http import HttpResponse
from django.utils import simplejson
from rspeak_app_v1.models import Device, Question, Response
from rspeak_app_v1.utils.notifications import Updates
from notifications import Updates, sendSyncNotification


# Request handler when someone posts a question
# 1. Add question content to the database
# 2. Select random active answerer
# 3. Put the question in the answerer's update stack
# 4. Send push notification to the answerer's device to retrieve updates
def ask(request):
	if request.is_ajax():
		if request.method == 'POST':
			json_data = simplejson.loads( request.raw_post_data )

			try:
				question_content = json_data['question_content']
				asker_device_id = json_data['device_id']
			except KeyError:
				print "Error: A posted question did not have a JSON object with the required properties"
			else:
				# First add question to database
				question = Question(asker_device_id=asker_device_id, question_content=question_content)
				question.save()

				# Second select a random device to send the question to
				all_devices = Device.objects.all().values()
				random_device = random.choice( all_devices ) if len( all_devices ) > 1 else None

				# ensure that we've a valid device
				if random_device is None:
					return
				while random_device['device_id'] is asker_device_id:
					random_device = random.choice( all_devices )

				# Start the thread between the asker device and the random device
				response_thread = Thread( question_id=question.question_id )
				response_thread.save()

				# Create the notification object
				notification = { 'type' : 'question', 'thread_id' : response_thread.thread_id, 'question_content' : question_content }

				# Push the question to the device's update stack
				Updates.add_update( random_device.push_notification_id, notification )

				# Send push notification to the client device
				sendSyncNotification( random_device )

	return some response

# Request handler when someone posts a response
# 1. Add response content to the database
# 2. Send push notification to client device
# 3. Update the credit of the responder
def respond(request):
	if request.is_ajax():
		if request.method == 'POST':
			json_data = simplejson.loads( request.raw_post_data )

			try:
				question_id = json_data['question_id']
				response_content = json_data['response_content']
				responder_device_id = json_data['responder_device_id']
			except KeyError:
				print "Error: A posted response did not have a JSON object with the required properties"
			else:
				# First, add response to database
				response = Response(question_id=question_id, responder_device_id=responder_device_id, response_content=response_content)
				response.save()

	return some response

# Request handler to update client model after receiving a push notification
# 1. Check queue to see if the client has any updates on standby
# 2. Send the client their update
# 3. Get ack from client
# 4. Empty the queue
def update_thread(request):