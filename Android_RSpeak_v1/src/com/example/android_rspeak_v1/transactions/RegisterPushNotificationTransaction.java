package com.example.android_rspeak_v1.transactions;

import java.util.HashMap;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android_rspeak_v1.database.HTTPRequest;
import com.example.android_rspeak_v1.database.HTTPRequestsDataSource;

public class RegisterPushNotificationTransaction 
{
	private Context context;
	private HTTPRequest request;
	GCMManager gcmManager;
	
	public RegisterPushNotificationTransaction( Context context )
	{
		this.context = context;
		this.gcmManager = new GCMManager( context );
	}
	
	public RegisterPushNotificationTransaction( Context context, HTTPRequest request )
	{
		this.context = context;
		this.request = request;
		this.gcmManager = new GCMManager( context );
	}
	
	public void beginTransaction()
	{
		if ( request == null )
		{
			SharedPreferences device_properties = context.getSharedPreferences( "DEVICE_PROPERTIES", 0 );
			String device_id = device_properties.getString( HTTPRequest.DATA_DEVICE_ID, null );
			String push_notification_id = gcmManager.getRegistrationId( context );
			
			// if there is no device id then register a new one
			if ( device_id == null )
			{
				RegisterDeviceTransaction register_device_transaction = new RegisterDeviceTransaction( context );
				register_device_transaction.beginTransaction();
			}
			else if ( push_notification_id != null )
			{
				// then create the JSON object for the http request
				HashMap<String, String> params = new HashMap<String, String>();
			    params.put( HTTPRequest.DATA_DEVICE_ID, device_id );
			    params.put( HTTPRequest.DATA_PUSH_NOTIFICATION_ID, push_notification_id );
			    JSONObject request_data = new JSONObject(params);
				
				// then add the question request to the database
				HTTPRequestsDataSource requestSource = new HTTPRequestsDataSource( context );
				requestSource.open();
				this.request = requestSource.createRequest( HTTPRequest.Type.POST, HTTPRequest.BASE_URL + HTTPRequest.URL_REGISTER_PUSH_NOTIFICATION_ID, request_data.toString() );
				requestSource.close();
				
				// then try to send the request to the server
				request.startRequest( successListener(), errorListener() );
			}
		}
		else
		{
			request.startRequest( successListener(), errorListener() );
		}
	}
	
	// if the request is successful remove the HTTP request
	// from the database
	public Response.Listener<JSONObject> successListener()
	{
		return new Response.Listener<JSONObject>() 
		{
			@TargetApi(Build.VERSION_CODES.GINGERBREAD)
			@Override
			public void onResponse( JSONObject response )
			{
				// Remove the request from the database
				HTTPRequestsDataSource requestSource = new HTTPRequestsDataSource( context );
				requestSource.open();
				requestSource.deleteRequest( request.getID() );
				requestSource.close();
				
				SharedPreferences device_properties = context.getSharedPreferences( "DEVICE_PROPERTIES", 0 );
				Editor editor = device_properties.edit().putBoolean( HTTPRequest.DATA_PUSH_NOTIFICATION_ID_SET, true );
				if ( Build.VERSION.SDK_INT < 9 )
				{
					editor.commit();
				} 
				else 
				{
					editor.apply();
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
				Log.e( "error", "failed to register GCM push notification id" );
			}
		};
	}
}
