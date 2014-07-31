package com.example.android_rspeak_v1.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class RSpeakSQLiteHelper extends SQLiteOpenHelper {

	// threads database keyterms
	public static final String TABLE_QUESTIONS = "questions";
	public static final String QUESTIONS_COLUMN_ID = "question_id";
	public static final String QUESTIONS_COLUMN_CONTENT = "question_content";
	public static final String QUESTIONS_COLUMN_TIME_POSTED = "time_posted";
	public static final String QUESTIONS_COLUMN_ON_ASKER_DEVICE = "currently_on_asker_device";
	
	public static final String TABLE_THREADS = "threads";
	public static final String THREADS_COLUMN_ID = "thread_id";
	public static final String THREADS_COLUMN_QUESTION_ID = "question_id";
	public static final String THREADS_COLUMN_IS_STOPPED = "is_stopped";
	
	// responses database keyterms
	public static final String TABLE_RESPONSES = "responses";
	public static final String RESPONSES_COLUMN_RESPONSE_ID = "response_id";
	public static final String RESPONSES_COLUMN_THREAD_ID = "thread_id";
	public static final String RESPONSES_COLUMN_RESPONSE_CONTENT = "response_content";
	public static final String RESPONSES_COLUMN_ON_RESPONDER_DEVICE = "currently_on_responder_device";
	public static final String RESPONSES_COLUMN_TIME_POSTED = "time_posted";
	
	// HTTPRequests database keyterms
	public static final String TABLE_HTTPREQUESTS = "http_requests";
	public static final String HTTPREQUESTS_COLUMN_REQUEST_ID = "request_id";
	public static final String HTTPREQUESTS_COLUMN_TYPE = "type";
	public static final String HTTPREQUESTS_COLUMN_URL = "url";
	public static final String HTTPREQUESTS_COLUMN_DATA = "data";

	// further helper keyterms
	private static final String RSPEAK_DATABASE_NAME = "rspeak.db";
	private static final int RSPEAK_DATABASE_VERSION = 1;
	
	// database creation statements
	private static final String CREATE_QUESTIONS_DATABASE = "create table "
			+ TABLE_QUESTIONS
			+ "(" + QUESTIONS_COLUMN_ID + " integer primary key autoincrement, "
			+ QUESTIONS_COLUMN_CONTENT + " text not null, "
			+ QUESTIONS_COLUMN_TIME_POSTED + " integer, "
			+ QUESTIONS_COLUMN_ON_ASKER_DEVICE + " numeric);";
			
	private static final String CREATE_THREADS_DATABASE = "create table "
			+ TABLE_THREADS 
			+ "(" + THREADS_COLUMN_ID + " text primary key not null, "
			+ THREADS_COLUMN_QUESTION_ID + " integer not null, "
			+ THREADS_COLUMN_IS_STOPPED + " numeric);";
	
	private static final String CREATE_RESPONSES_DATABASE = "create table "
			+ TABLE_RESPONSES
			+ "(" + RESPONSES_COLUMN_RESPONSE_ID + " integer primary key autoincrement, "
			+ RESPONSES_COLUMN_THREAD_ID + " text not null, "
			+ RESPONSES_COLUMN_RESPONSE_CONTENT + " text not null, "
			+ RESPONSES_COLUMN_ON_RESPONDER_DEVICE + " numeric, "
			+ RESPONSES_COLUMN_TIME_POSTED + " integer);";
	
	private static final String CREATE_HTTPREQUESTS_DATABASE = "create table "
			+ TABLE_HTTPREQUESTS
			+ "(" + HTTPREQUESTS_COLUMN_REQUEST_ID + " integer primary key autoincrement, "
			+ HTTPREQUESTS_COLUMN_TYPE + " integer not null, "
			+ HTTPREQUESTS_COLUMN_URL + " text not null, "
			+ HTTPREQUESTS_COLUMN_DATA + " text not null);";
	
	private Context context;
	
	public RSpeakSQLiteHelper(Context context)
	{
		super(context, RSPEAK_DATABASE_NAME, null, RSPEAK_DATABASE_VERSION);
		this.context = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase database)
	{
		database.execSQL( CREATE_QUESTIONS_DATABASE );
		database.execSQL( CREATE_THREADS_DATABASE );
		database.execSQL( CREATE_RESPONSES_DATABASE );
		database.execSQL( CREATE_HTTPREQUESTS_DATABASE );
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
	{
		Log.w( RSpeakSQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_THREADS);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPONSES);
		onCreate( database );
	}
	
	public void fillDatabase()
	{
		QuestionsDataSource qSource = new QuestionsDataSource( context );
		ThreadsDataSource tSource = new ThreadsDataSource( context );
		ResponsesDataSource rSource = new ResponsesDataSource( context );
		
		qSource.open();
		tSource.open();
		rSource.open();
		SQLiteDatabase db = this.getWritableDatabase();
		
		// Questions WHERE YOU WERE THE ASKER
		// Question 1	
		qSource.createQuestion(
				"when will the sun set on the 23rd of april?", 
				1401292063000L, 
				true );
		
		tSource.createThread(
				"thread 1", 
				1, 
				false );
		
		tSource.createThread(
				"thread 2", 
				1, 
				false );
		
		rSource.createResponse(
				"thread 2", 
				"who knows? it might not set at all.", 
				false, 
				1401292063000L);
		
		rSource.createResponse(
				"thread 2", 
				"Now youre starting to scare me!", 
				true, 
				1401292063000L);
		
		rSource.createResponse(
				"thread 2", 
				"Boo!!", 
				false, 
				1401292063000L);
		
		// Question 2
		qSource.createQuestion(
				"These bed bugs are killing me? Do I really need to throw out my mattress? How on earth did I get bed bugs on a 3 minute old mattress?", 
				1401292063000L, 
				true );
		
		// Question 3
		qSource.createQuestion(
				"Hey! Scream and Shout! Let it all out! Scream and Shout! Let it all out!", 
				1401292063000L, 
				true );
		
		tSource.createThread(
				"thread 3", 
				3, 
				false );
		
		rSource.createResponse(
				"thread 3", 
				"AAAAAAAAAAAAaaaaaaaaaaaaaaaaaaaaAAaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA!", 
				false, 
				1401292063000L);
		
		// Question 4
//		qSource.createQuestion(
//				"Ey baby want sum fuk?", 
//				1401292063000L, 
//				true );
//		
//		tSource.createThread(
//				"thread 4", 
//				4, 
//				false );
		
		// Question 5
//		qSource.createQuestion(
//				"How do I program dis shit?", 
//				1401292063000L, 
//				true );
//		
//		tSource.createThread(
//				"thread 5", 
//				5, 
//				false );
//		
//		rSource.createResponse(
//				"thread 5", 
//				"What are you talking about?", 
//				false, 
//				1401292063000L);
//		
//		rSource.createResponse(
//				"thread 5", 
//				"Oh! Uhm, I was just trying to cut straight into a conversation. How are you doing?", 
//				true, 
//				1401292063000L);
//
//		rSource.createResponse(
//				"thread 5", 
//				"ok?! Not bad. You?", 
//				false, 
//				1401292063000L);
//		
//		rSource.createResponse(
//				"thread 5", 
//				"Long programming homework! :<", 
//				true, 
//				1401292063000L);
				
		// Question 6
		qSource.createQuestion(
				"How much do Ottawans tip at a high-end restaurant?", 
				1401292063000L, 
				true );
		
		tSource.createThread(
				"thread 6", 
				6, 
				false );
		
		rSource.createResponse(
				"thread 6", 
				"15% if it was decent service, 25% if it was superb, more than that if you are expecting something more in return..", 
				false, 
				1401292063000L);
		
		rSource.createResponse(
				"thread 6", 
				"ok. Creepy. Bye!", 
				true, 
				1401292063000L);
		
		// Questions from FOREIGN devices
		// Question 7
		qSource.createQuestion(
				"How do you use a plunger?", 
				1401292063000L, 
				false );
		
		tSource.createThread(
				"thread 7", 
				7, 
				false );
		
		rSource.createResponse(
				"thread 7", 
				"step 1: block toilet.", 
				true, 
				1401292063000L);
		
		rSource.createResponse(
				"thread 7", 
				"step 2: place plunger onto the toilet hole.", 
				true, 
				1401292063000L);
		
		rSource.createResponse(
				"thread 7", 
				"step 3: push relentlessly.", 
				true, 
				1401292063000L);
		
//		rSource.createResponse(
//				"thread 7", 
//				"step 4: shit everywhere.", 
//				true, 
//				1401292063000L);
		
		// Question 8
		qSource.createQuestion(
				"Does anyone want to play soccer this evening?", 
				1401292063000L, 
				false );
		
		tSource.createThread(
				"thread 8", 
				8, 
				false );
		
		rSource.createResponse(
				"thread 8", 
				"Ill check with my brother.", 
				true, 
				1401292063000L);
		
		rSource.createResponse(
				"thread 8", 
				"Wait! Really? Cool.", 
				false, 
				1401292063000L);
		
		rSource.createResponse(
				"thread 8", 
				"He says hes ok. Where at?", 
				true, 
				1401292063000L);
		
		rSource.createResponse(
				"thread 8", 
				"Holy shit! Carlington Park. Never thought this would work!", 
				false, 
				1401292063000L);
		
		qSource.close();
		tSource.close();
		rSource.close();
	}
		
}
