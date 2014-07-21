package com.example.android_rspeak_v1.fragments;

import java.util.List;

import com.example.android_rspeak_v1.R;
import com.example.android_rspeak_v1.adapters.BrowseConversationListAdapter;
import com.example.android_rspeak_v1.database.RSpeakSQLiteHelper;
import com.example.android_rspeak_v1.database.Response;
import com.example.android_rspeak_v1.database.ResponsesDataSource;
import com.example.android_rspeak_v1.database.Thread;
import com.example.android_rspeak_v1.database.ThreadsDataSource;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class AskQuestionFragment extends Fragment
{
	
	public static AskQuestionFragment newInstance()
	{
		AskQuestionFragment fragment = new AskQuestionFragment();
				
		return fragment;
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedBundle) 
	{
		View v = inflater.inflate( R.layout.fragment_ask_question, container, false );

	    return v;
	}
}
