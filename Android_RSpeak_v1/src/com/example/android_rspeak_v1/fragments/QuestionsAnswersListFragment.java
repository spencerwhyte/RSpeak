package com.example.android_rspeak_v1.fragments;

import java.util.List;

import com.example.android_rspeak_v1.activities.AskQuestionActivity;
import com.example.android_rspeak_v1.activities.BrowseConversationActivity;
import com.example.android_rspeak_v1.adapters.QuestionsAnswersArrayAdapter;
import com.example.android_rspeak_v1.database.RSpeakSQLiteHelper;
import com.example.android_rspeak_v1.database.Thread;
import com.example.android_rspeak_v1.database.ThreadsDataSource;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class QuestionsAnswersListFragment extends ListFragment {
	
	public enum ThreadOrigin { LOCAL, FOREIGN };
	ThreadOrigin threadOrigin;
	Thread[] threadsArray;
	
	public static final QuestionsAnswersListFragment newInstance( ThreadOrigin origin )
	{
		QuestionsAnswersListFragment qa_fragment = new QuestionsAnswersListFragment();
		Bundle bundle = new Bundle(1);
		bundle.putInt( "THREAD ORIGIN", origin.ordinal() );
		qa_fragment.setArguments( bundle );
		return qa_fragment;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		List<Thread> threads;
		threadOrigin = ThreadOrigin.values()[ getArguments().getInt("THREAD ORIGIN") ];
				
        ThreadsDataSource threadsDataSource = new ThreadsDataSource( getActivity() );
        threadsDataSource.open();
        
        //dbHelper.fillDatabase();
		
		if ( threadOrigin == ThreadOrigin.LOCAL )
		{
			threads = threadsDataSource.getLocallyAskedThreads();
		}
		else // if ( threadOrigin == ThreadOrigin.FOREIGN )
		{
			threads = threadsDataSource.getForeignAskedThreads();
		}
				
		threadsArray = threads.toArray( new Thread[ threads.size() ] );
		     
		QuestionsAnswersArrayAdapter qa_adapter = new QuestionsAnswersArrayAdapter( getActivity(), threadsArray );
  		
        setListAdapter( qa_adapter );
     }

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// open the conversation of that particular thread in a new activity
		Intent intent = new Intent( getActivity(), BrowseConversationActivity.class );
	    intent.putExtra( RSpeakSQLiteHelper.THREADS_COLUMN_ID, threadsArray[ position ].getThreadID() );
	    startActivity(intent);
	}
	
}
