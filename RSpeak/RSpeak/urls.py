from django.conf.urls import patterns, include, url

from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
	# redirect all incoming requests to the rspeak_app
	url(r'^v1/', include('rspeak_app_v1.urls', namespace="rspeak_app_v1", app_name='rspeak_app_v1')),
)
