# Hashem Shawqi and Spencer Whyte
# This script defines the correspondence between incoming urls formats
# and the appropriate request handlers

from django.conf.urls import patterns, url

urlpatterns = patterns('',
	# Redirect ask requests to the corresponding view (function)
	url(r'^print/db/$', 'rspeak_app_v1.views.print_db', name='print_db'),
	url(r'^account/$', 'rspeak_app_v1.views.retrieve_credit_score', name='retrieve_credit_score'),
	url(r'^device/$', 'rspeak_app_v1.views.register_device', name='register_device'), # We can 
	url(r'^register/push_notification_id/$', 'rspeak_app_v1.views.register_push_notification_id', name='register_push_notification_id'),
	url(r'^question/$', 'rspeak_app_v1.views.ask', name='ask'), # Put to this URL
	url(r'^response/$', 'rspeak_app_v1.views.respond', name='respond'), # Put to this URL, or get from it to get the latest responses that we have not heard
	url(r'^update/$', 'rspeak_app_v1.views.retrieve_updates', name='retrieve_updates'),
)