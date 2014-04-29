from django.db import models

# Create your models here.
class  Device(models.Model):
	device_id = models.CharField(primary_key=True, max_length=16)
	credit_points = models.PositiveIntegerField(default=1)

	# when printing an instance of this class in python shell you will get custom output
	def __unicode__(self):
		return "Device with ID " + self.device_id


class Question(models.Model):
	question_id = models.CharField(primary_key=True, max_length=16)
	asker_device_id = models.ForeignKey('Device', db_column='asker_device_id')
	time_posted = models.DateTimeField(auto_now_add=True)
	question_content = models.CharField(max_length=350)

	# when printing an instance of this class in python shell you will get custom output
	def __unicode__(self):
		return "Question with ID " + self.question_id

class Response(models.Model):
	response_id = models.CharField(primary_key=True, max_length=16)
	question_id = models.ForeignKey('Question', db_column='question_id')
	responder_device_id = models.ForeignKey('Device', db_column='responder_device_id')
	time_posted = models.DateTimeField(auto_now_add=True)
	response_content = models.CharField(max_length=350)

	# when printing an instance of this class in python shell you will get custom output
	def __unicode__(self):
		return "Response with ID " + self.response_id