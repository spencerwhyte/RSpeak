package com.example.android_rspeak_v1.fragments;

import java.util.List;

import com.example.android_rspeak_v1.R;
import com.example.android_rspeak_v1.adapters.BrowseConversationListAdapter;
import com.example.android_rspeak_v1.database.Question;
import com.example.android_rspeak_v1.database.QuestionsDataSource;
import com.example.android_rspeak_v1.database.RSpeakSQLiteHelper;
import com.example.android_rspeak_v1.database.Response;
import com.example.android_rspeak_v1.database.Thread;
import com.example.android_rspeak_v1.database.ResponsesDataSource;
import com.example.android_rspeak_v1.database.ThreadsDataSource;
import com.example.android_rspeak_v1.transactions.RespondToQuestionTransaction;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class BrowseConversationFragment extends Fragment
{
	View fragmentView;
	BrowseConversationListAdapter list_adapter;
	
	public static final BrowseConversationFragment newInstance( String thread_id )
	{
		BrowseConversationFragment conversation_fragment = new BrowseConversationFragment();
		Bundle bundle = new Bundle();
		bundle.putString( RSpeakSQLiteHelper.THREADS_COLUMN_ID, thread_id );
		conversation_fragment.setArguments( bundle );
		return conversation_fragment;
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedBundle) 
	{
		List<Response> responses;
		Thread thread;
		Question question;
	    
		fragmentView = inflater.inflate( R.layout.fragment_browse_conversation, container, false );
		ImageButton send_response = (ImageButton) fragmentView.findViewById( R.id.send_response );
	    String thread_id = getArguments().getString( RSpeakSQLiteHelper.THREADS_COLUMN_ID );
			
        ResponsesDataSource responsesDataSource = new ResponsesDataSource( getActivity() );
        responsesDataSource.open();
        responses = responsesDataSource.getResponsesFromThreadId( thread_id );
        responsesDataSource.close();
        
        ThreadsDataSource threadsDataSource = new ThreadsDataSource( getActivity() );
        threadsDataSource.open();
        thread = threadsDataSource.getThreadById( thread_id );
        threadsDataSource.close();
        
        QuestionsDataSource questionsDataSource = new QuestionsDataSource( getActivity() );
        questionsDataSource.open();
        question = questionsDataSource.getQuestionById( thread.getQuestionID() );
        questionsDataSource.close();
        
        ListView conversation_list = (ListView) fragmentView.findViewById( R.id.conversation_list );
        list_adapter = new BrowseConversationListAdapter( getActivity(), responses, question );
        conversation_list.setAdapter( list_adapter );
        
        // set the focus on the edit text box by default
		EditText response = (EditText) fragmentView.findViewById( R.id.response );
		response.requestFocus();
		
		send_response.setOnClickListener( getSendButtonListener( thread_id ) );

	    return fragmentView;
	}
	
	// When send is clicked create an http request with their response
	public OnClickListener getSendButtonListener( final String thread_id )
	{
		return new OnClickListener() 
		{
			@Override
			public void onClick( final View v )
			{
				EditText response = (EditText) fragmentView.findViewById( R.id.response );
				String responseString = response.getText().toString();
				
				if ( responseString != null )
				{
					RespondToQuestionTransaction respondToQuestionTransaction = new RespondToQuestionTransaction( getActivity() );
					respondToQuestionTransaction.beginTransaction( thread_id, responseString );
					
					// update the list of responses on the screen
					ResponsesDataSource responsesDataSource = new ResponsesDataSource( getActivity() );
			        responsesDataSource.open();
			        List<Response> responses = responsesDataSource.getResponsesFromThreadId( thread_id );
			        responsesDataSource.close();
			        
			        list_adapter.updateResponses( responses );
			        list_adapter.notifyDataSetChanged();
					
					// clear edit text
					response.setText("");
					
					// collapse keyboard
					InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService( Context.INPUT_METHOD_SERVICE );
					inputManager.hideSoftInputFromWindow( fragmentView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS );
				}
			}
		};
	}

}
