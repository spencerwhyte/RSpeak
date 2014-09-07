# Hashem Shawqi and Spencer Whyte
# This module contains all code related to 
# push notifications.
# 
# The current implementation uses classmethods
# and static variables. This is a performance
# bottleneck and should be replaced with memcached
# as soon as possible.

import threading
import requests

GCM_API_KEY = 'AIzaSyArtPPVAHtR4bItfPChQR063iwsEt2XmGU'
GCM_URL = 'https://android.googleapis.com/gcm/send'

class Updates(object):
	_updates_lock = threading.Lock()
	
	@classmethod
	def add_update(cls, _updates, device_id, update):
		"""
		Push the update to the update stack assigned to
		the particular recipient's device id
		"""
		with cls._updates_lock:
			if device_id in cls._updates:
				cls._updates[device_id].append( update )
				print "ADD: MORE THAN ONE UPDATE WAITING"
			else:
				cls._updates[device_id] = [ update ]
				print "ADD: FIRST UPDATE"

	@classmethod
	def get_updates(cls, _updates, device_id):
		updates = None
		print "get_updates"
		with cls._updates_lock:
			print "INSIDE LOCK"
			print cls._updates
			if device_id in cls._updates:
				updates = cls._updates[device_id]
				cls._updates[device_id] = []
				print "Just popped an update"

		return updates

class QuestionUpdates(Updates):
	_updates = {}

	@classmethod
	def add_update(cls, device_id, update):
		super(QuestionUpdates, cls).add_update(QuestionUpdates._updates, device_id, update)

	@classmethod
	def get_updates(cls, device_id):
		return super(QuestionUpdates, cls).get_updates(QuestionUpdates._updates, device_id)

class ResponseUpdates(Updates):
	_updates = {}

	@classmethod
	def add_update(cls, device_id, update):
		super(ResponseUpdates, cls).add_update(ResponseUpdates._updates, device_id, update)

	@classmethod
	def get_updates(cls, device_id):
		return super(ResponseUpdates, cls).get_updates(ResponseUpdates._updates, device_id)


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
 		