# Hashem Shawqi and Spencer Whyte
# This module contains all code related to 
# push notifications.
# 
# The current implementation uses classmethods
# and static variables. This is a performance
# bottleneck and should be replaced with memcached
# as soon as possible.

import threading
import json
import requests
from push_notifications.models import APNSDevice, GCMDevice
from models import QuestionUpdate, ResponseUpdate, ThreadUpdate, Device


GCM_API_KEY = 'AIzaSyArtPPVAHtR4bItfPChQR063iwsEt2XmGU'
GCM_URL = 'https://android.googleapis.com/gcm/send'

class QuestionUpdates(object):
	_updates = {}

	@classmethod
	def add_update(cls, device, question):
		notify_device(question.content, device.device_id)
		questionUpdate = QuestionUpdate(device=device, question=question)
		questionUpdate.save()

	@classmethod
	def remove_updates(cls, device):
		question_updates = QuestionUpdate.objects.filter( device=device )
		update_payloads = []
		for update in question_updates:
			update_payloads.append(update.question);
			update.delete()
			#.values('question_id', 'asker_device', 'time_posted','content')
		
		return update_payloads
		
class ThreadUpdates(object):
	_updates = {}

	@classmethod
	def add_update(cls, device, thread):
		threadUpdate = ThreadUpdate(device=device, thread=thread)
		threadUpdate.save()
		

	@classmethod
	def remove_updates(cls, device):
		thread_updates = ThreadUpdate.objects.filter( device=device )
		update_payloads = []
		for update in thread_updates:
			update_payloads.append(update.thread);
			update.delete()
		return update_payloads

class ResponseUpdates(object):
	_updates = {}

	@classmethod
	def add_update(cls, device, response):
		notify_device(response.response_content, device.device_id)
		responseUpdate = ResponseUpdate(device=device, response=response)
		responseUpdate.save()

	@classmethod
	def remove_updates(cls, device):
		response_updates = ResponseUpdate.objects.filter( device=device )
		update_payloads = []
		for update in response_updates:
			update_payloads.append(update.response);
			update.delete()
		return update_payloads
	
	

def notify_device(notification, device_id):
	# Look up the device and see if is an Apple device, or a google device
	device = Device.objects.get( device_id=device_id )
	if device.os == "IOS":
		try:
			push_device = APNSDevice.objects.get(device_id=device_id)
			try:
				push_device.send_message(notification)
			except Exception:
				print "Failed to send push notification to " + device_id
				
		except APNSDevice.DoesNotExist:
			print device_id + " does not support push notifications."
	elif device.os == "Android":
		print "TODO: Missing implementation"
	

# The method inspects the type of device and uses the corresponding service to
# sends a notification in order to sync the device with the updates
def sendSyncNotification(device):
	if device.device_type is "ANDROID":
		message = """Content-Type:application/json
 Authorization:key=%{gcm_api_key}

 {
 	"registration_ids" : ["%{push_notification_id}"],
 	"collapse_key" : "update"
 }""" % { 'gcm_api_key' : GCM_API_KEY, 'push_notification_id' : device.push_notification_id }

 		# A response with a return code of 200 means transaction was succesfull
 		response = requests.post(GCM_URL, data=message)

 	# elif device.device_type is "IOS":
 		