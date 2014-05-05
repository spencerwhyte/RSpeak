from django.conf.urls import patterns, include, url

from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
  
	# redirect all incoming requests to the rspeak_app
    url(r'^/', include('rpseak_app.urls')),
)
