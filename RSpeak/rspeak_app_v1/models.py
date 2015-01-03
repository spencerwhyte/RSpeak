from django.db import models



# Create your models here.
class Device(models.Model):
	device_id = models.CharField(primary_key=True, max_length=36)
	credit_points = models.PositiveIntegerField(default=1)
	device_type = models.CharField(max_length=32)
	push_notification_id = models.CharField(max_length=64)

	# when printing an instance of this class in python shell you will get custom output
	def __unicode__(self):
		return "Device with ID " + str( self.device_id )


class Question(models.Model):
	asker_device = models.ForeignKey('Device')
	time_posted = models.DateTimeField(auto_now_add=True)
	content = models.CharField(max_length=350)

	# when printing an instance of this class in python shell you will get custom output
	def __unicode__(self):
		return "Question with ID " + str( self.id )

class Thread(models.Model):
	question_id = question_id = models.CharField(max_length=16)
	asker_device = models.ForeignKey('Device', related_name="asker")
	answerer_device = models.ForeignKey('Device', related_name="answerer")
	is_stopped = models.BooleanField(default=False)

	# when printing an instance of this class in python shell you will get custom output
	def __unicode__(self):
		return "Thread with ID " + str( self.id )

class Response(models.Model):
	thread = models.ForeignKey('Thread')
	responder_device = models.ForeignKey('Device')
	time_posted = models.DateTimeField(auto_now_add=True)
	response_content = models.CharField(max_length=350)

	# when printing an instance of this class in python shell you will get custom output
	def __unicode__(self):
		return "Response with ID " + str( self.id )
	
class QuestionUpdate(models.Model):
	device = models.ForeignKey('Device') # The device who needs to be notified about the new question
	question=models.ForeignKey('Question')

	# when printing an instance of this class in python shell you will get custom output
	def __unicode__(self):
		return "Question Update"
	
class ThreadUpdate(models.Model):
	device = models.ForeignKey('Device') # The device who needs to be notified about a new thread
	thread=models.ForeignKey('Thread') # The thread that we want to tell the device about

	# when printing an instance of this class in python shell you will get custom output
	def __unicode__(self):
		return "Thread Update"
	
class ResponseUpdate(models.Model):
	device = models.ForeignKey('Device') # The device who needs to be notified about the new response
	response=models.ForeignKey('Response') # This is the response that we want to tell the device about

	def __unicode__(self):
		return "Response Update"
	
