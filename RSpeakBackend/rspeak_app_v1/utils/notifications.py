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

class Updates():
	_updates = {}
	_updates_lock = threading.Lock()

	# Push the update to the update stack assigned to
	# the particular recipient's device id
	@classmethod
	def add_update(cls, device_id, update):
		with cls._updates_lock:
			if device_id in cls._updates:
				cls._updates[device_id].append( update )
			else:
				cls._updates[device_id] = [ update ]

	@classmethod
	def get_updates(cls, device_id):
		with cls._updates_lock:
			if device_id in cls._updates:
				return cls._updates[ device_id ]
			else:
				return None

	# This method acknowledges that the client received
	# the set of updates, and removes them from the stack
	@classmethod
	def ack_updates(cls, device_id, ackd_updates):
		with cls._updates_lock:
			device_updates = cls._updates[ device_id ] if device_id in cls._updates else None

			if device_updates is not None:
				for update in ackd_updates:
					device_updates.remove( update )


# The method inspects the type of device and uses the corresponding service to
# sends a notification in order to sync the device with the updates
def sendSyncNotification(device):
	if device.device_type == "ANDROID":
		message = """Content-Type:application/json
 Authorization:key=%{gcm_api_key}

 {
 	"registration_ids" : ["%{push_notification_id}"],
 	"collapse_key" : "update"
 }""" % { 'gcm_api_key' : GCM_API_KEY, 'push_notification_id' : device.push_notification_id }

 		# A response with 200 means transaction was succesfull
 		response = requests.post(GCM_URL, data=message)