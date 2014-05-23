import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class RSpeakSQLiteHelper extends SQLiteOpenHelper {

	// threads database keyterms
	public static final String TABLE_THREADS = "threads";
	public static final String THREADS_COLUMN_ID = "thread_id";
	public static final String THREADS_COLUMN_OTHER_DEVICE_ID = "other_device_id";
	public static final String THREADS_COLUMN_QUESTION_CONTENT = "question_content";
	public static final String THREADS_COLUMN_IS_STOPPED = "is_stopped";
	public static final String THREADS_COLUMN_ON_ASKER_DEVICE = "currently_on_asker_device";
	public static final String THREADS_COLUMN_TIME_POSTED = "time_posted";
	
	// responses database keyterms
	public static final String TABLE_RESPONSES = "responses";
	public static final String RESPONSES_COLUMN_RESPONSE_ID = "response_id";
	public static final String RESPONSES_COLUMN_THREAD_ID = "thread_id";
	public static final String RESPONSES_COLUMN_RESPONSE_CONTENT = "response_content";
	public static final String RESPONSES_COLUMN_TIME_POSTED = "time_posted";

	// further helper keyterms
	private static final String RSPEAK_DATABASE_NAME = "rspeak.db";
	private static final int RSPEAK_DATABASE_VERSION = 1;
	
	// database creation statements
	private static final String CREATE_THREADS_DATABASE = "create table "
			+ TABLE_THREADS 
			+ "(" + THREADS_COLUMN_ID + " text primary key not null, "
			+ THREADS_COLUMN_OTHER_DEVICE_ID + " text not null, "
			+ THREADS_COLUMN_QUESTION_CONTENT + " text not null "
			+ THREADS_COLUMN_IS_STOPPED + " numeric, "
			+ THREADS_COLUMN_ON_ASKER_DEVICE + " numeric, "
			+ THREADS_COLUMN_TIME_POSTED + " integer);";
	
	private static final String CREATE_RESPONSES_DATABASE = "create table "
			+ TABLE_RESPONSES
			+ "(" + RESPONSES_COLUMN_RESPONSE_ID + " integer primary key autoincrement, "
			+ RESPONSES_COLUMN_THREAD_ID + " text not null, "
			+ RESPONSES_COLUMN_RESPONSE_CONTENT + " text not null, "
			+ RESPONSES_COLUMN_TIME_POSTED + " integer);";
	
	public RSpeakSQLiteHelper(Context context)
	{
		super(context, RSPEAK_DATABASE_NAME, null, RSPEAK_DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database)
	{
		database.execSQL( CREATE_THREADS_DATABASE );
		database.execSQL( CREATE_RESPONSES_DATABASE );
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
	{
		Log.w( RSpeakSQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_THREADS);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPONSES);
		onCreate( database );
	}
}
