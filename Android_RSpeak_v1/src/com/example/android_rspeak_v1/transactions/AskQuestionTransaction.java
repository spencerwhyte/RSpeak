/* 
 * This class handles data relating to a question when it is submitted.
 * 
 * The tasks of this class are:
 * 1. Add the question to the local questions database.
 * 2. Send the question to the server to update the backend data model.
 * 
 */

package com.example.android_rspeak_v1.transactions;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android_rspeak_v1.database.HTTPRequest;
import com.example.android_rspeak_v1.database.HTTPRequestsDataSource;
import com.example.android_rspeak_v1.database.Question;
import com.example.android_rspeak_v1.database.QuestionsDataSource;
import com.example.android_rspeak_v1.database.ThreadsDataSource;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.android_rspeak_v1.RSpeakApplication;

public class AskQuestionTransaction 
{
	private Context context;
	private HTTPRequest request;
	
	public AskQuestionTransaction( Context context )
	{
		this.context = context;
	}
	
	public AskQuestionTransaction( Context context, HTTPRequest request )
	{
		this.context = context;
		this.request = request;
	}
	
	public void beginTransaction( String question_content )
	{
		if ( request == null )
	    {
			// first add the question to the database
			QuestionsDataSource questionSource = new QuestionsDataSource( context );
			questionSource.open();
			Question question = questionSource.createQuestion( question_content, System.currentTimeMillis(), true );
			questionSource.close();
			
			// get the device id
			SharedPreferences device_properties = context.getSharedPreferences( "DEVICE_PROPERTIES", 0 );
			String device_id = device_properties.getString( RSpeakApplication.PROPERTY_DEVICE_ID, null );
			
			// then create the JSON object for the http request
			HashMap<String, String> params = new HashMap<String, String>();
		    params.put( HTTPRequest.DATA_DEVICE_ID, device_id );
		    params.put( HTTPRequest.DATA_QUESTION_ID, String.valueOf( question.getID() ));
		    params.put( HTTPRequest.DATA_CONTENT, question_content );
		    JSONObject request_data = new JSONObject(params);
			
			// then add the question request to the database
			HTTPRequestsDataSource requestSource = new HTTPRequestsDataSource( context );
			requestSource.open();
			this.request = requestSource.createRequest( HTTPRequest.Type.POST, HTTPRequest.BASE_URL + HTTPRequest.URL_ASK, request_data.toString() );
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
				
				// get the question id and the thread id from the json response
				String question_id = null;
				String thread_id = null;
				try
				{
					question_id = response.getString( HTTPRequest.DATA_QUESTION_ID );
					thread_id = response.getString( HTTPRequest.DATA_THREAD_ID );
				}
				catch ( JSONException e )
				{
					Log.e( "error", "Error: couldn't read either question or thread id after sending question to server: " + e.toString() );
					return;
				}
				
				if ( question_id != null && thread_id != null )
				{
					ThreadsDataSource tSource = new ThreadsDataSource( context );
					tSource.open();
					
					tSource.createThread( thread_id,
							Long.parseLong( question_id ),
							false );

					tSource.close();
				}
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
				Log.e( "error", "failed to send the question to the server:\n" + error.toString() );
			}
		};
	}
}
