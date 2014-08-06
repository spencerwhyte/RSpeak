package com.example.android_rspeak_v1.database;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import com.example.android_rspeak_v1.RSpeakApplication;

public class HTTPRequest 
{
	// URL constants
	public static final String BASE_URL = "http://192.168.0.10:8000/v1";
	public static final String URL_REGISTER_DEVICE = "/register/device/";
	public static final String URL_REGISTER_PUSH_NOTIFICATION_ID = "/register/push_notification_id/";
	public static final String URL_ASK = "/ask/";
	public static final String URL_GET_CREDIT_SCORE = "/retrieve/credit_score/";
	public static final String URL_RESPOND = "/respond/";
	public static final String URL_UPDATE_THREAD = "/update/thread/";
	
	// JSON data constants
	public static final String DATA_DEVICE_ID = "device_id";
	public static final String DATA_PUSH_NOTIFICATION_ID = "push_notification_id";
	public static final String DATA_DEVICE_ID_SET = "device_id_set";
	public static final String DATA_PUSH_NOTIFICATION_ID_SET = "push_notification_id_set";
	public static final String DATA_DEVICE_TYPE = "device_type";
	public static final String DATA_QUESTION_ID = "question_id";
	public static final String DATA_THREAD_ID = "thread_id";
	public static final String DATA_CONTENT = "content";
	public static final String DATA_VALID_ID = "valid_id";
	public static final String DATA_CREDIT_POINTS = "credit_points";
	
	public enum Type { GET, POST };
	
	// variables that change form instance to instance
	private int request_id;
	private Type type;
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
	
	public Type getType()
	{
		return type;
	}
	
	public void setType( Type type )
	{
		this.type = type;
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
	
	public void startRequest( Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener )
	{
		JSONObject data_object = null;
		JsonObjectRequest request = null;
		
		if ( type == Type.GET )
		{

		}
		
		else if( type == Type.POST )
		{	
			try
			{
				data_object = new JSONObject( data );
			}
			catch( JSONException e )
			{
				Log.e( "error", "The data string from the database couldn't be converted to a JSON Ojbect." );
			}
			
			request = new JsonObjectRequest(
					Request.Method.POST,
					url,
					data_object,
					successListener,
					errorListener );
//			{
//				@Override
//                public Map<String, String> getHeaders() throws AuthFailureError 
//                {
//                	HashMap<String, String> headers = new HashMap<String, String>();
//                    headers.put("TOKEN", "99KI9Gj68CgCf70deM22Ka64chef2C40Gm2lFJ2J0G9JkDaaDAcbFfd19MfacGf3FFm8CM1hG0eDiIk8");
//
//                    return headers;
//                }
//
//                @Override 
//                protected Map<String, String> getParams() 
//                {
//                	Map<String, String> params = new HashMap<String, String>();
//
//                	return params;
//                }
//			};
		}

		RequestQueue requestQueue = RSpeakApplication.getRequestQueue();
		requestQueue.add( request );
	}
}
