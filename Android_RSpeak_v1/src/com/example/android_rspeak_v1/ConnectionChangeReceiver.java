package com.example.android_rspeak_v1;

import java.util.List;

import org.json.JSONObject;

import com.android.volley.Response;
import com.example.android_rspeak_v1.database.HTTPRequest;
import com.example.android_rspeak_v1.database.HTTPRequestsDataSource;
import com.example.android_rspeak_v1.transactions.AskQuestionTransaction;
import com.example.android_rspeak_v1.transactions.RegisterDeviceTransaction;
import com.example.android_rspeak_v1.transactions.RegisterPushNotificationTransaction;
import com.example.android_rspeak_v1.transactions.RespondToQuestionTransaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionChangeReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive( Context context, Intent intent )
	{
		ConnectivityManager cm = ( ConnectivityManager ) context.getSystemService( Context.CONNECTIVITY_SERVICE );
		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		if ( netInfo != null && netInfo.isConnected() ) 
		{
			HTTPRequestsDataSource requestSource = new HTTPRequestsDataSource( context );
			requestSource.open();
			List<HTTPRequest> requests = requestSource.getAllRequests();
			
			// call all failed requests once more
			for ( HTTPRequest request : requests )
			{
				Response.Listener<JSONObject> successListener = null;
				Response.ErrorListener errorListener = null;
				String url = request.getURL();
				
				if ( url.equals( HTTPRequest.BASE_URL + HTTPRequest.URL_ASK ) )
				{
					AskQuestionTransaction askQuestionTransaction = new AskQuestionTransaction( context, request );
					successListener = askQuestionTransaction.successListener();
					errorListener = askQuestionTransaction.errorListener();
				}
				
				else if ( url.equals( HTTPRequest.BASE_URL + HTTPRequest.URL_REGISTER_DEVICE ) )
				{
					RegisterDeviceTransaction registerDeviceTransaction = new RegisterDeviceTransaction( context, request );
					successListener = registerDeviceTransaction.successListener();
					errorListener = registerDeviceTransaction.errorListener();
				}
				
				else if ( url.equals( HTTPRequest.BASE_URL + HTTPRequest.URL_REGISTER_DEVICE ) )
				{
					RegisterPushNotificationTransaction registerPushNotificationTransaction = new RegisterPushNotificationTransaction( context, request );
					successListener = registerPushNotificationTransaction.successListener();
					errorListener = registerPushNotificationTransaction.errorListener();
				}
				
				else if ( url.equals( HTTPRequest.BASE_URL + HTTPRequest.URL_RESPOND ) )
				{
					RespondToQuestionTransaction respondToQuestionTransaction = new RespondToQuestionTransaction( context, request );
					successListener = respondToQuestionTransaction.successListener();
					errorListener = respondToQuestionTransaction.errorListener();
				}
				
				request.startRequest( successListener, errorListener );
			}
			
			requestSource.close();
		}
	}
}