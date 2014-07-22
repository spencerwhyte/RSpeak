package com.example.android_rspeak_v1.database;

import java.util.List;

public class Question 
{
	private long question_id;
	private long time_posted;
	private String question_content;
	private boolean currently_on_asker_device;
	private List<Thread> threads;
	
	public long getQuestionID()
	{
		return question_id;
	}
	
	public void setQuestionID( long question_id )
	{
		this.question_id = question_id;
	}
	
	public String getQuestionContent()
	{
		return question_content;
	}
	
	public void setQuestionContent(String new_question_content)
	{
		question_content = new_question_content;
	}
	
	public long getTimePosted()
	{
		return time_posted;
	}
	
	public void setTimePosted(long new_time_posted)
	{
		time_posted = new_time_posted;
	}
	
	public boolean isCurrentlyOnAskerDevice()
	{
		return currently_on_asker_device;
	}
	
	public void setCurrentlyOnAskerDevice(boolean new_currently_on_asker_device)
	{
		currently_on_asker_device = new_currently_on_asker_device;
	}
	
	public List<Thread> getThreads()
	{
		return threads;
	}
	
	public void setThreads( List<Thread> threads )
	{
		this.threads = threads;
	}
}
