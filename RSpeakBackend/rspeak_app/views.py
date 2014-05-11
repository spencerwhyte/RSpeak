# Hashem Shawqi and Spencer Whyte
# This file contains all the HTTP Request Handlers for the app

from django.http import HttpResponse
from django.utils import simplejson
from rspeak_app.models import Device, Question, Response

# Request handler when someone posts a question
# 1. Add question content to the database
# 2. Reroute question to other devices
def ask(request):
	if request.is_ajax():
		if request.method == 'POST':
			json_data = simplejson.loads( request.raw_post_data )

			try:
				question_content = json_data['question_content']
				asker_device_id = json_data['device_id']
			except KeyError:
				print "Error: A posted question did not have a JSON object with the correct format"
			else:
				# First add question to database
				question = Question(asker_device_id=asker_device_id, question_content=question_content)
				question.save()

				# Next route question to responders


	return some response

# Request handler when someone posts a response
# 1. Add response content to the database
# 2. Reroute the answer back to asker device
# 3. Update the credit of the responder
def answer(request):
	if request.is_ajax():
		if request.method == 'POST':
			json_data = simplejson.loads( request.raw_post_data )

			try:
				question_id = json_data['question_id']
				response_content = json_data['response_content']
				responder_device_id = json_data['responder_device_id']
			except KeyError:
				print "Error: A posted response did not have a JSON object with the correct format"
			else:
				# First, add response to database
				response = Response(question_id=question_id, responder_device_id=responder_device_id, response_content=response_content)

	return some response