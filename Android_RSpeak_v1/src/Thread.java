import android.text.format.DateFormat;


public class Thread {
	private String thread_id;
	private String other_device_id;
	private String question_content;
	private boolean is_stopped;
	private boolean currently_on_asker_device;
	private long time_posted;
	
	public String getThreadID()
	{
		return thread_id;
	}
	
	public void setThreadID(String new_thread_id)
	{
		thread_id = new_thread_id;
	}
	
	public String getOtherDeviceID()
	{
		return other_device_id;
	}
	
	public void setOtherDeviceID(String new_other_device_id)
	{
		other_device_id = new_other_device_id;
	}
	
	public String getQuestionContent()
	{
		return question_content;
	}
	
	public void setQuestionContent(String new_question_content)
	{
		question_content = new_question_content;
	}
	
	public boolean getIsStopped()
	{
		return is_stopped;
	}
	
	public void setIsStopped(boolean new_is_stopped)
	{
		is_stopped = new_is_stopped;
	}
	
	public boolean getCurrentlyOnAskerDevice()
	{
		return currently_on_asker_device;
	}
	
	public void setCurrentlyOnAskerDevice(boolean new_currently_on_asker_device)
	{
		currently_on_asker_device = new_currently_on_asker_device;
	}
	
	public long getTimePosted()
	{
		return time_posted;
	}
	
	public void setTimePosted(long new_time_posted)
	{
		time_posted = new_time_posted;
	}
}
