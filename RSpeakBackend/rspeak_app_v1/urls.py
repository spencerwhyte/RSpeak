# Hashem Shawqi and Spencer Whyte
# This script defines the correspondence between incoming urls formats
# and the appropriate request handlers

from django.conf.urls import patterns, url

urlpatterns = patterns('',
	# Redirect ask requests to the corresponding view (function)
	url(r'^retrieve/csrf/$', 'rspeak_app_v1.views.retrieve_csrf', name='retrieve_csrf'),
	url(r'^print/db/$', 'rspeak_app_v1.views.print_db', name='print_db'),
	url(r'^retrieve/credit_score/$', 'rspeak_app_v1.views.retrieve_credit_score', name='retrieve_credit_score'),
	url(r'^register/device/$', 'rspeak_app_v1.views.register_device', name='register_device'),
	url(r'^register/push_notification_id/$', 'rspeak_app_v1.views.register_push_notification_id', name='register_push_notification_id'),
	url(r'^ask/$', 'rspeak_app_v1.views.ask', name='ask'),
	url(r'^respond/$', 'rspeak_app_v1.views.respond', name='respond'),
	url(r'^retrieve/updates/$', 'rspeak_app_v1.rspeak_app_v1.views.retrieve_updates', name='retrieve_updates'),
)