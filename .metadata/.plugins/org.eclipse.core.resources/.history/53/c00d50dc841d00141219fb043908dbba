package com.example.android_rspeak_v1.transactions;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android_rspeak_v1.RSpeakApplication;
import com.example.android_rspeak_v1.database.HTTPRequest;
import com.example.android_rspeak_v1.database.HTTPRequestsDataSource;
import com.example.android_rspeak_v1.database.ResponsesDataSource;

public class RespondToQuestionTransaction 
{
	private Context context;
	private HTTPRequest request;
	
	public RespondToQuestionTransaction( Context context )
	{
		this.context = context;
	}
	
	public RespondToQuestionTransaction( Context context, HTTPRequest request )
	{
		this.context = context;
		this.request = request;
	}
	
	public void beginTransaction( String thread_id, String response_content )
	{
		if ( request == null )
	    {
			// first add the response to the database
			ResponsesDataSource responsesSource = new ResponsesDataSource( context );
			responsesSource.open();
			responsesSource.createResponse( thread_id, response_content, true, System.currentTimeMillis() );
			responsesSource.close();
			
			// get the device id
			SharedPreferences device_properties = context.getSharedPreferences( "DEVICE_PROPERTIES", 0 );
			String device_id = device_properties.getString( RSpeakApplication.PROPERTY_DEVICE_ID, null );
			
			// then create the JSON object for the http request
			HashMap<String, String> params = new HashMap<String, String>();
		    params.put( HTTPRequest.DATA_DEVICE_ID, device_id );
		    params.put( HTTPRequest.DATA_THREAD_ID, thread_id );
		    params.put( HTTPRequest.DATA_CONTENT, response_content );
		    JSONObject request_data = new JSONObject(params);
			
			// then add the question request to the database
			HTTPRequestsDataSource requestSource = new HTTPRequestsDataSource( context );
			requestSource.open();
			this.request = requestSource.createRequest( HTTPRequest.Type.POST, HTTPRequest.BASE_URL + HTTPRequest.URL_RESPOND, request_data.toString() );
			requestSource.close();
	    }
		
		// then try to send the request to the server
		request.startRequest( successListener(), errorListener() );
	}
	
	// if the request is successful
	public Response.Listener<JSONObject> successListener()
	{
		return new Response.Listener<JSONObject>() 
		{
			@Override
			public void onResponse( JSONObject response )
			{
				// Remove the request from the database
				HTTPRequestsDataSource requestSource = new HTTPRequestsDataSource( context );
				requestSource.open();
				requestSource.deleteRequest( request.getID() );
				requestSource.close();
			}
		};
	}

	// if the request is not successful
	public Response.ErrorListener errorListener()
	{
		return new Response.ErrorListener() 
		{
			@Override
			public void onErrorResponse( VolleyError error )
			{
				Log.e( "error", "failed to send the response to the server:\n" + error.toString() );
			}
		};
	}
}
