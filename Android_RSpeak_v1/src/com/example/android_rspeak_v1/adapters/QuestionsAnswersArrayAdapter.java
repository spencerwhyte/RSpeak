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
import com.example.android_rspeak_v1.database.Response;
import com.example.android_rspeak_v1.database.Thread;

public class QuestionsAnswersArrayAdapter extends ArrayAdapter<Thread>
{
	private final Context context;
	private final Thread[] values;
	
	public QuestionsAnswersArrayAdapter(Context context, Thread[] values)
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
	    
	    TextView questionLine = (TextView) rowView.findViewById(R.id.questionLine);
	    TextView latestResponseLine = (TextView) rowView.findViewById(R.id.latestResponseLine);
	    TextView dateLine = (TextView) rowView.findViewById(R.id.dateLine);
	    
	    if ( values != null )
	    {
		    Thread threadInstance = values[position];
		    List<Response> responses = threadInstance.getResponses();
		    DateFormat df = new SimpleDateFormat("dd/MM/yy");
		    
		    questionLine.setText( threadInstance.getQuestionContent() );
		    
		    if ( responses != null && responses.size() > 0 )
		    {
			    Response lastResponse = responses.get( responses.size() - 1 );
			    Date responseDate = new Date( lastResponse.getTimePosted() );
			    
			    latestResponseLine.setText( lastResponse.getResponseContent() );
			    dateLine.setText( df.format( responseDate ) );
		    }
		    else
		    {
		    	Date threadDate = new Date( threadInstance.getTimePosted() );
		    	dateLine.setText( df.format( threadDate ) );
		    	
		    	// put no response message and style
		    	latestResponseLine.setText( "No Response Yet" );
		    	latestResponseLine.setTextAppearance( context, R.style.no_response_line_style );
		    	latestResponseLine.setGravity( Gravity.CENTER );
		    }
	    }
	    
	    return rowView;
	}
}
