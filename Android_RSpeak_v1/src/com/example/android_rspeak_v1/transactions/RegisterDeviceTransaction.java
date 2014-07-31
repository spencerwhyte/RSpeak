package com.example.android_rspeak_v1.transactions;

import java.util.HashMap;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android_rspeak_v1.database.HTTPRequest;
import com.example.android_rspeak_v1.database.HTTPRequestsDataSource;

public class RegisterDeviceTransaction 
{
	private static char[] symbols;
	static 
	{
		StringBuilder tmp = new StringBuilder();
		
		for ( char ch = '0'; ch <= '9'; ++ch )
		{
			tmp.append( ch );
		}
		for ( char ch = 'a'; ch <= 'Z'; ++ch )
		{
			tmp.append( ch );
		}
		
		symbols = tmp.toString().toCharArray();
	}
 
	private Context context;
	private HTTPRequest request;
	private String device_id;
	
	public RegisterDeviceTransaction( Context context )
	{
		this.context = context;
	}
	
	public void beginTransaction()
	{	
		SharedPreferences device_properties = context.getSharedPreferences( "DEVICE_PROPERTIES", 0 );
		GCMManager gcmManager = new GCMManager( context );
		Random random = new Random();
		device_id = device_properties.getString( HTTPRequest.DATA_DEVICE_ID, null );
		
		// only go through with the transaction if there is no device id already registered
		if ( device_id == null )
		{
			// first create a random 16 character String
			char[] id = new char[ 16 ];
			for ( int i = 0; i < 16; i++ )
			{
				id[ i ] = symbols[ random.nextInt( symbols.length ) ];
			}
			device_id = new String( id );
			
			// get the push_notification_id
			String push_notification_id = gcmManager.getRegistrationId( context );
			
			// then create the JSON object for the http request
			HashMap<String, String> params = new HashMap<String, String>();
		    params.put( HTTPRequest.DATA_DEVICE_ID, device_id );
		    params.put( HTTPRequest.DATA_DEVICE_TYPE, "ANDROID" );
		    params.put( HTTPRequest.DATA_PUSH_NOTIFICATION_ID, push_notification_id );
		    JSONObject request_data = new JSONObject(params);
			
			// then add the question request to the database
			HTTPRequestsDataSource requestSource = new HTTPRequestsDataSource( context );
			requestSource.open();
			this.request = requestSource.createRequest( HTTPRequest.Type.POST, HTTPRequest.BASE_URL + HTTPRequest.URL_REGISTER_DEVICE, request_data.toString() );
			requestSource.close();
			
			// then try to send the request to the server
			request.startRequest( successListener(), errorListener() );
		}
	}
	
	// if the request is successful
	private Response.Listener<JSONObject> successListener()
	{
		return new Response.Listener<JSONObject>() 
		{
			@Override
			public void onResponse( JSONObject response )
			{
				boolean valid_id = false;
				
				try
				{
					valid_id = response.getBoolean( HTTPRequest.DATA_VALID_ID );
				}
				catch( JSONException e )
				{
					Log.e( "error", "The JSON response received after registering the device couldn't be parsed" );
				}
				
				// Remove the request from the database
				HTTPRequestsDataSource requestSource = new HTTPRequestsDataSource( context );
				requestSource.open();
				requestSource.deleteRequest( request.getID() );
				requestSource.close();
				
				if ( !valid_id ) // start a new request with a new random ID
				{
					beginTransaction();
				}
				else // put the id in the shared preferences
				{
					SharedPreferences device_properties = context.getSharedPreferences( "DEVICE_PROPERTIES", 0 );
					device_properties.edit().putString( HTTPRequest.DATA_DEVICE_ID, device_id );
				}
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
				Log.e( "error", "failed to register the device with the server" );
			}
		};
	}
}
