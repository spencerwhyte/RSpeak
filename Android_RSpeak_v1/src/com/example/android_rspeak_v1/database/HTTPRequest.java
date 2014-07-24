package com.example.android_rspeak_v1.database;

public class HTTPRequest 
{
	public enum Type { GET, POST };
	
	private int request_id;
	private int type;
	private String url;
	private String data;
	
	public int getID()
	{
		return request_id;
	}
	
	public void setID( int request_id )
	{
		this.request_id = request_id;
	}
	
	public int getType()
	{
		return type;
	}
	
	public void setType( int type )
	{
		this.type =type;
	}
	
	public String getURL()
	{
		return this.url;
	}
	
	public void setURL( String url )
	{
		this.url = url;
	}
	
	public String getData()
	{
		return this.data;
	}
	
	public void setData( String data )
	{
		this.data = data;
	}
}
