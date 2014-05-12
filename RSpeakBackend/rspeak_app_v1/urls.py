# Hashem Shawqi and Spencer Whyte
# This script defines the correspondence between incoming urls formats
# and the appropriate request handlers

from django.conf.urls import patterns, url

from rspeak_app_v1 import views

urlpatterns = patterns('',
	# Redirect ask requests to the corresponding view (function)
	url(r'^ask/', views.ask, name='ask'),
	url(r'^respond/', views.respond, name='respond'),
	url(r'^update/thread', views.update_thread, name='update_thread'),
)