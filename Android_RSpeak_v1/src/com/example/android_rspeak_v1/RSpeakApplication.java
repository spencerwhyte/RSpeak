package com.example.android_rspeak_v1;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.app.Application;


public class RSpeakApplication extends Application
{
	public static RequestQueue requestQueue;

    @Override
    public void onCreate() 
    {
        super.onCreate();
        requestQueue = Volley.newRequestQueue( getApplicationContext() );
    }
    
    public static RequestQueue getRequestQueue()
    {
    	return requestQueue;
    }
}
