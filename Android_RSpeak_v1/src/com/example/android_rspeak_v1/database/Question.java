package com.example.android_rspeak_v1.database;

public class Question 
{
	private long question_id;
	private long time_posted;
	private String question_content;
	
	public long getQuestionId()
	{
		return question_id;
	}
	
	public void setQuestionId( long question_id )
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
}
