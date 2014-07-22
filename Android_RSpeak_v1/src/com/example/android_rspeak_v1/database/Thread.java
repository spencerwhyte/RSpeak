package com.example.android_rspeak_v1.database;

import java.util.List;

import android.text.format.DateFormat;


public class Thread {
	private String thread_id;
	private long question_id;
	private String other_device_id;
	private boolean is_stopped;
	List<Response> responses;
	
	public String getThreadID()
	{
		return thread_id;
	}
	
	public void setThreadID(String new_thread_id)
	{
		thread_id = new_thread_id;
	}
	
	public long getQuestionID()
	{
		return question_id;
	}
	
	public void setQuestionID( long question_id )
	{
		question_id = question_id;
	}
	
	public String getOtherDeviceID()
	{
		return other_device_id;
	}
	
	public void setOtherDeviceID(String new_other_device_id)
	{
		other_device_id = new_other_device_id;
	}
	
	public boolean getIsStopped()
	{
		return is_stopped;
	}
	
	public void setIsStopped(boolean new_is_stopped)
	{
		is_stopped = new_is_stopped;
	}
	
	public List<Response> getResponses()
	{
		return responses;
	}
	
	public void setResponses(List<Response> new_responses)
	{
		responses = new_responses;
	}
}
