# Hashem Shawqi and Spencer Whyte
# This file contains all the HTTP Request Handlers for the app

import random
import json
import time

from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from models import Device, Question, Thread, Response
from notifications import QuestionUpdates, ResponseUpdates, ThreadUpdates
from utils import random_alphanumeric
from push_notifications.models import APNSDevice, GCMDevice



from django.core.serializers.json import Serializer as Builtin_Serializer

class Serializer(Builtin_Serializer):
	def get_dump_object(self, obj):
		dump_object = self._current or {}
		dump_object.update({'pk': obj.id})
		return dump_object


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

	return HttpResponse(json.dumps({"Responses":str(responses), "Threads":str(threads), "Questions":str(questions), "Devices":str(devices)}), content_type="application/json")


@csrf_exempt
def retrieve_credit_score(request):
	"""
	When a device wants to retrieve its current credit
	We will need:
	1. Only the device id
	"""
	if request.method == 'GET':

		try:
			device_id = request.GET.get('device_id', '')
		except KeyError:
			print "Error: A posted question did not have a JSON object with the required properties"
		else:
			# See if the device id already exists
			try:
				device = Device.objects.get(device_id=device_id)
			except Device.DoesNotExist:
				device = None
			if device:
				return HttpResponse(json.dumps({ 'credit_points' : device.credit_points }), content_type="application/json")
			else:
				return HttpResponse(json.dumps({ 'credit_points' :-1 }), content_type="application/json")

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
		json_data = json.loads(request.body)

		try:
			device_id = json_data['device_id']
			device_type = json_data['device_type']
		except KeyError:
			print "Error: A posted question did not have a JSON object with the required properties"
		else:
			# See if the device id already exists
			try:
				device = Device.objects.get(device_id=device_id)
			except Device.DoesNotExist:
				device = None
			if device:
				return HttpResponse(json.dumps({ 'valid_id' : False }), content_type="application/json")
			else:
				device = Device(device_id=device_id, device_type=device_type)
				device.save()
				return HttpResponse(json.dumps({ 'valid_id' : True }), content_type="application/json")


@csrf_exempt
def register_push_notification_id(request):
	"""
	If the device didn't have the push notification id ready
	when registering then 
	"""
	if request.method == 'POST':
		json_data = json.loads(request.body)

		try:
			device_id = json_data['device_id']
			push_notification_id = json_data['push_notification_id']
		except KeyError:
			print "Error: A posted question did not have a JSON object with the required properties"
		else:
			
			try:
				push_device = APNSDevice.objects.get(registration_id=push_notification_id)
			except APNSDevice.DoesNotExist:
				push_device = APNSDevice(registration_id=push_notification_id, device_id=device_id)
				push_device.save()
			
			# print "Pushing the message"
			# push_device.send_message("Fuck yeah, we got push!")
			
			# See if the device id already exists
			# device = Device.objects.filter( device_id=device_id )
			
			return HttpResponse(json.dumps({ 'valid_id' : True }), content_type="application/json")



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
		json_data = json.loads(request.body)

		try:
			question_content = json_data['content']
			asker_device_id = json_data['device_id']
			max_new_threads = json_data['max_new_threads']
		except KeyError:
			print "Error: A posted question did not have a JSON object with the required properties"
		else:
			
			# then add question to database
			question = Question(asker_device_id=asker_device_id, content=question_content)
			question.save()

			# We are going to start one or more threads with random devices, put the critical information about the thread in this array
			# and send it back to the device
			new_thread_ids = [];
			responder_ids = [];
			
			# then select a random device to send the question to
			all_devices = Device.objects.all()
			asker_device = all_devices.filter(device_id=asker_device_id)[0];
			print "Found asker device"
			print asker_device
			print max_new_threads
			while len(new_thread_ids) < max_new_threads:
				random_device = random.choice(all_devices) if len(all_devices) > 1 else None
				print "Chosing random device"
				print random_device
				# ensure that we've a valid answerer device
				if random_device is None:
					return
				while len(all_devices) > 1 and random_device.device_id == asker_device_id or random_device.device_id in responder_ids :
					random_device = random.choice(all_devices)

				
				print "Chose another random device"
				print random_device
				responder_ids.append(random_device.device_id)
				
				print "But I am"
				print asker_device_id
				
				# find a unique thread id

					

				
				# Start the thread between the asker device and the random device	
				response_thread = Thread(question_id=question.id, asker_device=asker_device, answerer_device=random_device)
				response_thread.save()
				new_thread_ids.append(response_thread.id)
				print "response thread with id: " + str(response_thread.id)
	
				# add question to answerer_device update stack
				QuestionUpdates.add_update(random_device, question)
				ThreadUpdates.add_update(random_device, response_thread)
			
			new_threads = []
			for i in range(0, len(new_thread_ids)):
				new_threads.append({'thread_id':new_thread_ids[i], 'responder_device_id':responder_ids[i]})

			return HttpResponse(json.dumps({ 'question_id' : question.id, 'threads' : new_threads }), content_type="application/json")


@csrf_exempt
def respond(request):
	"""
	Request handler when someone posts a response
	1. Add response content to the database
	2. Send push notification to client device
	3. Update the credit of the responder
	"""
	if request.method == 'POST':
		json_data = json.loads(request.body)

		try:
			thread_id = json_data['thread_id']
			response_content = json_data['content']
			device_id = json_data['device_id']
		except KeyError:
			print "Error: A posted response did not have a JSON object with the required properties"
		else:
			# check that the thread id and the device ids are valid
			thread = Thread.objects.filter(id=thread_id)
			device = Device.objects.filter(device_id=device_id)

			print "Passed parameter validation"
			print thread.count()
			print device.count()

			if thread.exists() and device.exists():
				# add response to database
				response = Response(thread=thread[0], responder_device=device[0], response_content=response_content)
				response.save()

				# add update to the other device
				asker_device = thread[0].asker_device
				answerer_device = thread[0].answerer_device
				
				print "Thread and device actually exist"
				print device_id
				print asker_device.device_id
				print answerer_device.device_id

				if asker_device.device_id == device_id:
					ResponseUpdates.add_update(answerer_device, response)
					print "Adding an update to the answerers queue"
					
				elif answerer_device.device_id == device_id:
					ResponseUpdates.add_update(asker_device, response)
					print "Adding an update to the askers queue"

				return HttpResponse(json.dumps({}), content_type="application/json")


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
		json_data = json.loads(request.body)

		try:
			device_id = json_data['device_id']
		except KeyError:
			print "Error: A posted response did not have a JSON object with the required properties"
		else:

			# try sending the updates to the user
			# if this fails in any way (IOError) then make sure the updates
			# are back on the updates stack (else they won't reach the client).
			try:
				print device_id
				all_devices = Device.objects.all()
				device = all_devices.filter(device_id=device_id)[0]
				
				question_updates = QuestionUpdates.remove_updates(device)
				thread_updates = ThreadUpdates.remove_updates(device)
				response_updates = ResponseUpdates.remove_updates(device)
				serializer = Serializer()
				
				serialized_question_updates = serializer.serialize(question_updates)
				
				serialized_thread_updates = serializer.serialize(thread_updates)
				serialized_response_updates = serializer.serialize(response_updates)
				print serialized_question_updates
				
				
				all_updates = '{ "question_updates" : ' + serialized_question_updates + ', "thread_updates" : ' + serialized_thread_updates+ ', "response_updates" : ' + serialized_response_updates+ '}'
							
				return HttpResponse(all_updates, content_type="application/json")
			except IOError:
				# put back all updates into the update stack
				# This doesn't look like it would actually work
				for update in question_updates:
					QuestionUpdates.add_update(device, update)
					
				for update in thread_updates:
					ThreadUpdates.add_update(device, update)

				for update in response_updates:
					ResponseUpdates.add_update(device, update)

				print "Error: failed to send updates to client"