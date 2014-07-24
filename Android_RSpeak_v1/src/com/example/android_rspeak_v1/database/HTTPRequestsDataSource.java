package com.example.android_rspeak_v1.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class HTTPRequestsDataSource 
{
	// Database Fields
	private SQLiteDatabase database;
	private RSpeakSQLiteHelper dbHelper;
	private String[] allColumns = { 
			RSpeakSQLiteHelper.HTTPREQUESTS_COLUMN_REQUEST_ID,
			RSpeakSQLiteHelper.HTTPREQUESTS_COLUMN_TYPE,
			RSpeakSQLiteHelper.HTTPREQUESTS_COLUMN_URL,
			RSpeakSQLiteHelper.HTTPREQUESTS_COLUMN_DATA };
	
	public HTTPRequestsDataSource( Context context )
	{
		dbHelper = new RSpeakSQLiteHelper( context );
	}
	
	public void open() throws SQLException
	{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close()
	{
		dbHelper.close();
	}

	public HTTPRequest createRequest( HTTPRequest.Type type, 
			String url,
			String data )
	{
		ContentValues values = new ContentValues();
		
		values.put(RSpeakSQLiteHelper.HTTPREQUESTS_COLUMN_TYPE, type.ordinal() );
		values.put(RSpeakSQLiteHelper.HTTPREQUESTS_COLUMN_URL, url );
		values.put(RSpeakSQLiteHelper.HTTPREQUESTS_COLUMN_DATA, data );
		
		
		int request_id = (int) database.insert( RSpeakSQLiteHelper.TABLE_HTTPREQUESTS, null, values );
		Cursor cursor = database.query(RSpeakSQLiteHelper.TABLE_QUESTIONS,
				allColumns, 
				RSpeakSQLiteHelper.QUESTIONS_COLUMN_ID + " = " + request_id,
				null,
				null,
				null,
				null);
		cursor.moveToFirst();
		HTTPRequest newRequest = cursorToRequest( cursor );
		cursor.close();
		return newRequest;
	}
	
	public void deleteRequest( HTTPRequest request )
	{
		int request_id = request.getID();
		
		database.delete( RSpeakSQLiteHelper.TABLE_HTTPREQUESTS, 
				RSpeakSQLiteHelper.HTTPREQUESTS_COLUMN_REQUEST_ID + " = " + request_id, 
				null );
	}
	
	private List<HTTPRequest> queryAllRequests( String conditions )
	{
		List<HTTPRequest> requests = new ArrayList<HTTPRequest>();
		
		Cursor cursor = database.query( RSpeakSQLiteHelper.TABLE_HTTPREQUESTS,
				allColumns,
				conditions,
				null,
				null,
				null,
				null );
		
		cursor.moveToFirst();
		while ( !cursor.isAfterLast() ) 
		{
			HTTPRequest request = cursorToRequest( cursor );
			requests.add( request );
			cursor.moveToNext();
		}
		
		cursor.close();
		return requests;
	}
	
	public List<HTTPRequest> getAllRequests()
	{
		return queryAllRequests( null );
	}
	
	private HTTPRequest cursorToRequest(Cursor cursor)
	{
		HTTPRequest request = new HTTPRequest();
		
		request.setID(
				cursor.getInt(
						cursor.getColumnIndex( RSpeakSQLiteHelper.HTTPREQUESTS_COLUMN_REQUEST_ID )));
		request.setType(
				cursor.getInt(
						cursor.getColumnIndex( RSpeakSQLiteHelper.HTTPREQUESTS_COLUMN_TYPE )));
		request.setURL(
				cursor.getString(
						cursor.getColumnIndex( RSpeakSQLiteHelper.HTTPREQUESTS_COLUMN_URL )));
		request.setData(
				cursor.getString(
						cursor.getColumnIndex( RSpeakSQLiteHelper.HTTPREQUESTS_COLUMN_DATA )));
		
		return request;
	}
}
