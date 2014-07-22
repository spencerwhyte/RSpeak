package com.example.android_rspeak_v1.adapters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android_rspeak_v1.R;
import com.example.android_rspeak_v1.database.Question;
import com.example.android_rspeak_v1.database.Response;
import com.example.android_rspeak_v1.database.Thread;

public class QuestionsAnswersArrayAdapter extends ArrayAdapter<Question>
{
	private final Context context;
	private final Question[] values;
	
	public QuestionsAnswersArrayAdapter(Context context, Question[] values)
	{
		super(context, R.layout.qa_list_layout, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.qa_list_layout, parent, false);
	    
	    TextView questionLine = (TextView) rowView.findViewById(R.id.question_line);
	    TextView latestResponseLine = (TextView) rowView.findViewById(R.id.latest_response_line);
	    TextView dateLine = (TextView) rowView.findViewById(R.id.date_line);
	    
	    if ( values != null )
	    {
		    Question questionInstance = values[position];
		    List<Thread> threads = questionInstance.getThreads();
		    
		    DateFormat df = new SimpleDateFormat("dd/MM/yy");
		    
		    questionLine.setText( questionInstance.getQuestionContent() );
		    
		    Date questionDate = new Date( questionInstance.getTimePosted() );
	    	dateLine.setText( df.format( questionDate ) );
		    
	    	// if there is only one thread we'll show the information from that thread
		    if ( threads.size() == 1 )
		    {
		    	Thread threadInstance = threads.get( 0 );
		    	List<Response> responses = threadInstance.getResponses();
			    
			    if ( responses != null && responses.size() > 0 )
			    {
				    Response lastResponse = responses.get( responses.size() - 1 );
				    Date responseDate = new Date( lastResponse.getTimePosted() );
				    
				    latestResponseLine.setText( lastResponse.getResponseContent() );
				    dateLine.setText( df.format( responseDate ) );
			    }
			    else
			    {	
			    	// put no response message and style
			    	latestResponseLine.setText( "No Response Yet" );
			    	latestResponseLine.setTextAppearance( context, R.style.no_response_line_style );
			    	latestResponseLine.setGravity( Gravity.CENTER );
			    }	
		    }
		    else // show how many threads we have
		    {
		    	latestResponseLine.setText( threads.size() + " Threads" );
		    	latestResponseLine.setTextAppearance( context, R.style.no_response_line_style );
		    	latestResponseLine.setGravity( Gravity.CENTER );
		    }
	    }
	    
	    return rowView;
	}
}
