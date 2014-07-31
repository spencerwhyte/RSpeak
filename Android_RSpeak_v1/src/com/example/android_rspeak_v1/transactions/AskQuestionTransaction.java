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

import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android_rspeak_v1.database.HTTPRequest;
import com.example.android_rspeak_v1.database.HTTPRequestsDataSource;
import com.example.android_rspeak_v1.database.Question;
import com.example.android_rspeak_v1.database.QuestionsDataSource;

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
	
	public void beginTransaction( String question_content )
	{
		// first add the question to the database
		QuestionsDataSource questionSource = new QuestionsDataSource( context );
		questionSource.open();
		Question question = questionSource.createQuestion( question_content, System.currentTimeMillis(), true );
		questionSource.close();
		
		// then create the JSON object for the http request
		SharedPreferences gcm_preferences = ((RSpeakApplication) context.getApplicationContext()).getGCMPreferences();
		HashMap<String, String> params = new HashMap<String, String>();
	    params.put( HTTPRequest.DATA_DEVICE_ID, gcm_preferences.getString( RSpeakApplication.PROPERTY_DEVICE_ID, null ));
	    params.put( HTTPRequest.DATA_QUESTION_ID, String.valueOf( question.getID() ));
	    params.put( HTTPRequest.DATA_CONTENT, question_content );
	    JSONObject request_data = new JSONObject(params);
		
		// then add the question request to the database
		HTTPRequestsDataSource requestSource = new HTTPRequestsDataSource( context );
		requestSource.open();
		this.request = requestSource.createRequest( HTTPRequest.Type.POST, HTTPRequest.BASE_URL + HTTPRequest.URL_ASK, request_data.toString() );
		requestSource.close();
		
		// then try to send the request to the server
		request.startRequest( successListener(), errorListener() );
	}
	
	// if the request is successful
	private Response.Listener<JSONObject> successListener()
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
	private Response.ErrorListener errorListener()
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
