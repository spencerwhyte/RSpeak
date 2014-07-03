package com.example.client_database_v1;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;


public class ThreadsDataSource {

	// Database Fields
	private SQLiteDatabase database;
	private RSpeakSQLiteHelper dbHelper;
	private String[] allColumns = { RSpeakSQLiteHelper.THREADS_COLUMN_ID,
			RSpeakSQLiteHelper.THREADS_COLUMN_OTHER_DEVICE_ID,
			RSpeakSQLiteHelper.THREADS_COLUMN_QUESTION_CONTENT,
			RSpeakSQLiteHelper.THREADS_COLUMN_IS_STOPPED,
			RSpeakSQLiteHelper.THREADS_COLUMN_ON_ASKER_DEVICE,
			RSpeakSQLiteHelper.THREADS_COLUMN_TIME_POSTED };
	
	public ThreadsDataSource(Context context, RSpeakSQLiteHelper new_dbHelper)
	{
		dbHelper = new_dbHelper;
	}
	
	public void open() throws SQLException
	{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public Thread createThread( String thread_id, 
			String other_device_id, 
			String question_content, 
			boolean is_stopped, 
			boolean currently_on_asker_device, 
			long time_posted )
	{
		ContentValues values = new ContentValues();
		
		values.put(RSpeakSQLiteHelper.THREADS_COLUMN_ID, thread_id);
		values.put(RSpeakSQLiteHelper.THREADS_COLUMN_OTHER_DEVICE_ID, other_device_id);
		values.put(RSpeakSQLiteHelper.THREADS_COLUMN_QUESTION_CONTENT, question_content);
		values.put(RSpeakSQLiteHelper.THREADS_COLUMN_IS_STOPPED, is_stopped);
		values.put(RSpeakSQLiteHelper.THREADS_COLUMN_ON_ASKER_DEVICE, currently_on_asker_device);
		values.put(RSpeakSQLiteHelper.THREADS_COLUMN_TIME_POSTED, time_posted);
		
		database.insert(RSpeakSQLiteHelper.TABLE_THREADS, null, values);
		Cursor cursor = database.query(RSpeakSQLiteHelper.TABLE_THREADS,
				allColumns, 
				RSpeakSQLiteHelper.THREADS_COLUMN_ID + " = " + thread_id,
				null,
				null,
				null,
				null);
		cursor.moveToFirst();
		Thread newThread = cursorToThread(cursor);
		cursor.close();
		return newThread;
	}
	
	public void deleteThread(Thread thread)
	{
		String thread_id = thread.getThreadID();
		System.out.println("Thread with id " + thread_id + " and is being deleted.");
		
		// First delete all responses with that thread id then delete the actual thread
		database.delete(RSpeakSQLiteHelper.TABLE_RESPONSES, 
				RSpeakSQLiteHelper.RESPONSES_COLUMN_THREAD_ID + " = " + thread_id,
				null);
		
		database.delete(RSpeakSQLiteHelper.TABLE_THREADS, 
				RSpeakSQLiteHelper.THREADS_COLUMN_ID + " = " + thread_id, 
				null);
	}
	
	private List<Thread> queryAllThreads(String conditions)
	{
		List<Thread> threads = new ArrayList<Thread>();
		
		Cursor cursor = database.query(RSpeakSQLiteHelper.TABLE_THREADS,
				allColumns,
				conditions,
				null,
				null,
				null,
				null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) 
		{
			Thread thread = cursorToThread(cursor);
			threads.add(thread);
			cursor.moveToNext();
		}
		
		cursor.close();
		return threads;
	}
	
	public List<Thread> getAllThreads()
	{
		return queryAllThreads( null );
	}
	
	public List<Thread> getLocallyAskedThreads()
	{
		return queryAllThreads( RSpeakSQLiteHelper.THREADS_COLUMN_ON_ASKER_DEVICE + " = 1" );
	}
	
	public List<Thread> getForeignAskedThreads()
	{
		return queryAllThreads( RSpeakSQLiteHelper.THREADS_COLUMN_ON_ASKER_DEVICE + " = 0" );
	}
	
	private Thread cursorToThread(Cursor cursor)
	{
		Thread thread = new Thread();
		
		thread.setThreadID(
				cursor.getString(
						cursor.getColumnIndex(RSpeakSQLiteHelper.THREADS_COLUMN_ID)));
		thread.setOtherDeviceID(
				cursor.getString(
						cursor.getColumnIndex(RSpeakSQLiteHelper.THREADS_COLUMN_OTHER_DEVICE_ID)));
		thread.setQuestionContent(
				cursor.getString(
						cursor.getColumnIndex(RSpeakSQLiteHelper.THREADS_COLUMN_QUESTION_CONTENT)));
		thread.setIsStopped(
				cursor.getInt(
						cursor.getColumnIndex(RSpeakSQLiteHelper.THREADS_COLUMN_IS_STOPPED)) > 0);
		thread.setCurrentlyOnAskerDevice(
				cursor.getInt(
						cursor.getColumnIndex(RSpeakSQLiteHelper.THREADS_COLUMN_ON_ASKER_DEVICE)) > 0);
		thread.setTimePosted(
				cursor.getLong(
						cursor.getColumnIndex(RSpeakSQLiteHelper.RESPONSES_COLUMN_TIME_POSTED)));
		
		return thread;
	}
}
