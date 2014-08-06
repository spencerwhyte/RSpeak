package com.example.android_rspeak_v1.fragments;


import com.example.android_rspeak_v1.R;
import com.example.android_rspeak_v1.transactions.AskQuestionTransaction;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

public class AskQuestionFragment extends Fragment
{
	View fragmentView;
	
	public static AskQuestionFragment newInstance()
	{
		AskQuestionFragment fragment = new AskQuestionFragment();
				
		return fragment;
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedBundle) 
	{
		fragmentView = inflater.inflate( R.layout.fragment_ask_question, container, false );
		ImageButton send_question =  (ImageButton) fragmentView.findViewById( R.id.send_question );
		
		// set the focus on the edit text box by default
		EditText question = (EditText) fragmentView.findViewById( R.id.question );
		question.requestFocus();
		
		send_question.setOnClickListener( getSendButtonListener() );
		
	    return fragmentView;
	}
	
	// When send is clicked create an http request with their question
	// then go back to previous activity
	public OnClickListener getSendButtonListener()
	{
		return new OnClickListener() 
		{
			@Override
			public void onClick( final View v )
			{
				EditText question = (EditText) fragmentView.findViewById( R.id.question );
				String questionString = question.getText().toString();
				
				if ( questionString != null )
				{
					AskQuestionTransaction ask_question_transaction = new AskQuestionTransaction( getActivity() );
					ask_question_transaction.beginTransaction( questionString );
					
					getActivity().finish();
				}
			}
		};
	}
}
