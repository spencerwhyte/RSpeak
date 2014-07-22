package com.example.android_rspeak_v1.fragments;

import java.util.List;

import com.example.android_rspeak_v1.activities.BrowseConversationActivity;
import com.example.android_rspeak_v1.adapters.BrowseThreadsListAdapter;
import com.example.android_rspeak_v1.adapters.QuestionsAnswersArrayAdapter;
import com.example.android_rspeak_v1.database.Question;
import com.example.android_rspeak_v1.database.Thread;
import com.example.android_rspeak_v1.database.QuestionsDataSource;
import com.example.android_rspeak_v1.database.RSpeakSQLiteHelper;
import com.example.android_rspeak_v1.database.ThreadsDataSource;
import com.example.android_rspeak_v1.fragments.QuestionsAnswersListFragment.QuestionOrigin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class BrowseThreadsFragment extends ListFragment
{
	private static final String QUESTION_ID = "question_id";
	Thread[] threadsArray;
	
	public static final BrowseThreadsFragment newInstance( long question_id )
	{
		BrowseThreadsFragment fragment = new BrowseThreadsFragment();
		Bundle bundle = new Bundle();
		bundle.putLong( QUESTION_ID, question_id );
		fragment.setArguments( bundle );
		return fragment;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		List<Thread> threads;
		long question_id = getArguments().getLong( QUESTION_ID );
			
        ThreadsDataSource threadsDataSource = new ThreadsDataSource( getActivity() );
        threadsDataSource.open();
        
        threads = threadsDataSource.getThreadsByQuestionID(question_id);
        threadsArray = threads.toArray( new Thread[ threads.size() ] );
		     
		BrowseThreadsListAdapter threads_adapter = new BrowseThreadsListAdapter( getActivity(), threadsArray );
  		
        setListAdapter( threads_adapter );
        threadsDataSource.close();
     }

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// open the conversation of that particular thread in a new activity
		Intent intent = new Intent( getActivity(), BrowseConversationActivity.class );
	    intent.putExtra( RSpeakSQLiteHelper.THREADS_COLUMN_ID, threadsArray[ position ].getThreadID() );
	    startActivity(intent);
	}
}
