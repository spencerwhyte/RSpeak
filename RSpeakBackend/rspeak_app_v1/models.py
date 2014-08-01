from django.db import models

# Create your models here.
class  Device(models.Model):
	device_id = models.CharField(primary_key=True, max_length=16)
	credit_points = models.PositiveIntegerField(default=1)
	device_type = models.CharField(max_length=8)
	push_notification_id = models.CharField(max_length=64)

	# when printing an instance of this class in python shell you will get custom output
	def __unicode__(self):
		return "Device with ID " + self.device_id


class Question(models.Model):
	surrogate = models.AutoField(primary_key=True)
	question_id = models.PositiveIntegerField(default=1)
	asker_device = models.ForeignKey('Device')
	time_posted = models.DateTimeField(auto_now_add=True)
	question_content = models.CharField(max_length=350)

	# when printing an instance of this class in python shell you will get custom output
	def __unicode__(self):
		return "Question with ID " + self.question_id

class Thread(models.Model):
	thread_id = models.CharField(primary_key=True, max_length=16)
	question_id = models.PositiveIntegerField(default=1)
	asker_device = models.ForeignKey('Device')
	is_stopped = models.BooleanField(default=False)

	# when printing an instance of this class in python shell you will get custom output
	def __unicode__(self):
		return "Thread with ID " + self.thread_id

class Response(models.Model):
	response_id = models.CharField(primary_key=True, max_length=16)
	thread = models.ForeignKey('Thread')
	responder_device = models.ForeignKey('Device')
	time_posted = models.DateTimeField(auto_now_add=True)
	response_content = models.CharField(max_length=350)

	# when printing an instance of this class in python shell you will get custom output
	def __unicode__(self):
		return "Response with ID " + self.response_id