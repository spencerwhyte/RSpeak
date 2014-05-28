# Hashem Shawqi and Spencer Whyte
# This script defines the correspondence between incoming urls formats
# and the appropriate request handlers

from django.conf.urls import patterns, url

urlpatterns = patterns('',
	# Redirect ask requests to the corresponding view (function)
	url(r'^ask/$', 'rspeak_app_v1.views.ask', name='ask'),
	url(r'^respond/$', 'rspeak_app_v1.views.respond', name='respond'),
	url(r'^update/thread/$', 'rspeak_app_v1.rspeak_app_v1.views.update_thread', name='update_thread'),
)