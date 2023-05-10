package com.oakcottagestudios.batleague.table;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.oakcottagestudios.batleague.Globals;
import com.oakcottagestudios.batleague.MainActivity;
import com.oakcottagestudios.batleague.R;

/**
 * Created by Doug on 10/02/2015.
 */


public class TableFragment extends Fragment implements Globals
{
	private static TableFragment instance = null;

	public TableFragment()
	{}		// Exists only to defeat instantiation.

	public static TableFragment getInstance()
	{
		if(instance == null)
			instance = new TableFragment();

		return instance;
	}


//	static View fragmentView = null;
//	static ViewGroup fragmentContainer = null;



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		//Log.d("TABLEFRAGMENT", "onCreateView()");

		// Inflate the layout for this fragment
		View fragmentView = inflater.inflate(R.layout.fragment_table, container, false);

		displayTable(fragmentView);
		return fragmentView;
	}




	void displayTable( View topview )
	{
		//Log.d("TABLEFRAGMENT", "displayTable()");

		Configuration config = getResources().getConfiguration();
		boolean isPortrait = (config.orientation == Configuration.ORIENTATION_PORTRAIT);

		LeagueTable leaguetable = LeagueTable.getInstance();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
        String myTeam = prefs.getString("prefTeamName", "notfound" );


		topview.setBackgroundColor( TABLE_BACKCOL_1 );

		TextView title = (TextView) topview.findViewById(R.id.title_table);
		title.setText(leaguetable.pageTitle);
		title.setBackgroundColor( TABLE_BACKCOL_2 );

		TableLayout table = (TableLayout) topview.findViewById(R.id.table_view);

		TableRow tr = new TableRow( MainActivity.getContext() );

		addRowElement( tr, leaguetable.titleRow.position, ITEM_TITLE );
		addRowElement( tr, leaguetable.titleRow.name, ITEM_TITLE );
		addRowElement( tr, leaguetable.titleRow.points, ITEM_TITLE );
		addRowElement( tr, leaguetable.titleRow.played, ITEM_TITLE );
		addRowElement( tr, leaguetable.titleRow.average, ITEM_TITLE );
		addRowElement( tr, leaguetable.titleRow.pinfall, ITEM_TITLE );
		if( !isPortrait )
		{
			addRowElement(tr, leaguetable.titleRow.hiGameHcp, ITEM_TITLE);
			addRowElement(tr, leaguetable.titleRow.hiGameScr, ITEM_TITLE);
			addRowElement(tr, leaguetable.titleRow.hiSeriesHcp, ITEM_TITLE);
			addRowElement(tr, leaguetable.titleRow.hiSeriesScr, ITEM_TITLE);
		}
		table.addView(tr);

		for( int r=0; r<leaguetable.numEntries; r++ )
		{
			//Log.d("TABLEFRAGMENT", "add row");

			tr = new TableRow( MainActivity.getContext() );

            int itemFlag = ITEM_NORMAL;
            if( myTeam.compareTo( leaguetable.tableRow.elementAt(r).name )==0 )
                itemFlag=ITEM_HIGHLIGHT;

			addRowElement( tr, leaguetable.tableRow.elementAt(r).position+" ", itemFlag|JUSTIFY_CENTRE );
			addRowElement( tr, leaguetable.tableRow.elementAt(r).name, itemFlag );
			addRowElement( tr, leaguetable.tableRow.elementAt(r).points, itemFlag|JUSTIFY_CENTRE );
			addRowElement( tr, leaguetable.tableRow.elementAt(r).played, itemFlag|JUSTIFY_CENTRE );
			addRowElement( tr, leaguetable.tableRow.elementAt(r).average, itemFlag|JUSTIFY_CENTRE );
			addRowElement( tr, leaguetable.tableRow.elementAt(r).pinfall, itemFlag|JUSTIFY_CENTRE );
			if( !isPortrait )
			{
				addRowElement(tr, leaguetable.tableRow.elementAt(r).hiGameHcp, itemFlag|JUSTIFY_CENTRE);
				addRowElement(tr, leaguetable.tableRow.elementAt(r).hiGameScr, itemFlag|JUSTIFY_CENTRE);
				addRowElement(tr, leaguetable.tableRow.elementAt(r).hiSeriesHcp, itemFlag|JUSTIFY_CENTRE);
				addRowElement(tr, leaguetable.tableRow.elementAt(r).hiSeriesScr, itemFlag|JUSTIFY_CENTRE);
			}

			table.addView( tr );
		}

		table.setStretchAllColumns( true );

	}


	void addRowElement( TableRow row, String item, int flags )
	{
		TextView tv = new TextView( MainActivity.getContext() );
		tv.setText(item );
		tv.setTextColor(Color.BLACK );

		row.addView( tv );

		if( (flags & ITEM_TITLE)!=0 )
		{
			TableRow.LayoutParams params = (TableRow.LayoutParams) tv.getLayoutParams();
			params.height = 80;

			tv.setLayoutParams(params); // causes layout update
			tv.setTypeface(null, Typeface.BOLD);
			tv.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
		}
		else
		{
            if( (flags & ITEM_HIGHLIGHT) != 0 )
            {
                tv.setTypeface(null, Typeface.BOLD);
                tv.setTextColor( Color.RED );
            }
			if( (flags & JUSTIFY_RIGHT) != 0 )
			{
				tv.setGravity(Gravity.RIGHT);
			}
			if( (flags & JUSTIFY_CENTRE) != 0 )
			{
				tv.setGravity(Gravity.CENTER_HORIZONTAL);
			}
		}



	}


}



