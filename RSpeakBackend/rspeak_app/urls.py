# Hashem Shawqi and Spencer Whyte
# This script defines the correspondence between incoming urls formats
# and the appropriate request handlers

from django.conf.urls import patterns, url

from rspeak_app import views

urlpatterns = patterns('',
	# Redirect ask requests to the corresponding view (function)
	url(r'^ask/', views.ask, name='ask'),
	url(r'^answer/', views.answer, name='answer')
)