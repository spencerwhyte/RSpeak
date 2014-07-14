package com.example.android_rspeak_v1.fragments;

import java.util.List;

import com.example.android_rspeak_v1.R;
import com.example.android_rspeak_v1.adapters.BrowseConversationListAdapter;
import com.example.android_rspeak_v1.database.RSpeakSQLiteHelper;
import com.example.android_rspeak_v1.database.Response;
import com.example.android_rspeak_v1.database.ResponsesDataSource;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class BrowseConversationFragment extends Fragment
{
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
	    View v = inflater.inflate( R.layout.fragment_browse_conversation, container, false );

	    List<Response> responses;
		String thread_id = getArguments().getString( RSpeakSQLiteHelper.THREADS_COLUMN_ID );
				
        ResponsesDataSource responsesDataSource = new ResponsesDataSource( getActivity() );
        responsesDataSource.open();
        responses = responsesDataSource.getResponsesFromThreadId( thread_id );
        
        ListView conversation_list = (ListView) v.findViewById( R.id.conversation_list );
        BrowseConversationListAdapter list_adapter = new BrowseConversationListAdapter( getActivity(), responses );
        conversation_list.setAdapter( list_adapter );

	    return v;
	}

}
