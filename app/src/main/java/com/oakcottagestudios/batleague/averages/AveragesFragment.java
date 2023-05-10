package com.oakcottagestudios.batleague.averages;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
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


public class AveragesFragment extends Fragment implements Globals
{
	private static AveragesFragment instance = null;

	public AveragesFragment()
	{}	// Exists only to defeat instantiation.

	public static AveragesFragment getInstance()
	{
		if(instance == null)
			instance = new AveragesFragment();

		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		//Log.d("AVERAGESFRAGMENT", "onCreateView()");

		// Inflate the layout for this fragment
//		container.removeAllViews();
		View fragmentView = inflater.inflate(R.layout.fragment_table, container, false);

		displayTable(fragmentView);
		return fragmentView;
	}

	void displayTable( View topview )
	{
		//Log.d("AVERAGESFRAGMENT", "displayTable()");

		Configuration config = getResources().getConfiguration();
		boolean isPortrait = (config.orientation == Configuration.ORIENTATION_PORTRAIT);

		LeagueAverages averages = LeagueAverages.getInstance();

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
		String myTeam = prefs.getString("prefTeamName", "notfound");
		String myName = prefs.getString("prefPlayerName", "notfound");

		topview.setBackgroundColor( AVERAGES_BACKCOL_1 );

		TextView title = (TextView) topview.findViewById(R.id.title_table);
		title.setText("Current Averages");
		title.setBackgroundColor(AVERAGES_BACKCOL_2);

		TableLayout table = (TableLayout) topview.findViewById(R.id.table_view);

		int numElements = averages.columnTitles.size();
		if( isPortrait )
			numElements -=4;    //  Lose the HiG/HiS


		for( int t=0; t<averages.numTeams; t++ )
		{
			TeamAverage team = averages.teamAverages.elementAt(t);

			int teamColour = ((t&1)==0)? AVERAGES_BACKCOL_1 : AVERAGES_BACKCOL_2;

			int teamFlag = ITEM_TITLE;
			if( myTeam.compareTo( team.teamName )==0 )
				teamFlag|=ITEM_HIGHLIGHT;


			//  Team Title Row
			TableRow tr = new TableRow( MainActivity.getContext() );
			tr.setBackgroundColor( teamColour );
			addRowElement(tr, team.teamName, teamFlag);
			for( int c=1; c<numElements; c++ )
				addRowElement( tr, averages.columnTitles.elementAt(c), teamFlag );
			table.addView(tr);

			//  Player rows
			for( int p=0; p<team.getTeamCount(); p++ )
			{
				IndividualAverage player = team.teamMembers.elementAt(p);
				tr = new TableRow( MainActivity.getContext() );
				tr.setBackgroundColor( teamColour );

				int playerFlag = ITEM_NORMAL;

				if( player.name.startsWith( myName ))
					playerFlag|=ITEM_HIGHLIGHT;

				addRowElement( tr, player.name, playerFlag );
				addRowElement( tr, player.handicap, playerFlag );
				addRowElement( tr, player.average, playerFlag );
				addRowElement( tr, player.games, playerFlag );
				addRowElement( tr, player.pins, playerFlag );
				if( !isPortrait )
				{
					addRowElement(tr, player.hiGameScr, playerFlag);
					addRowElement(tr, player.hiGameHcp, playerFlag);
					addRowElement(tr, player.hiSeriesScr, playerFlag);
					addRowElement(tr, player.hiSeriesHcp, playerFlag);
				}
				table.addView(tr);

			}


		}

		table.setStretchAllColumns( true );

	}


	void addRowElement( TableRow row, String item, int flags )
	{
		TextView tv = new TextView( MainActivity.getContext() );
		tv.setText( item );
		tv.setTextColor(Color.BLACK );
		row.addView( tv );

		if( (flags & ITEM_TITLE)!=0 )
		{
			//TableRow.LayoutParams params = (TableRow.LayoutParams) tv.getLayoutParams();
			//params.height = 80;

			//tv.setLayoutParams(params); // causes layout update
			tv.setTypeface(null, Typeface.BOLD);
			//tv.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
		}

		if( (flags & ITEM_HIGHLIGHT) != 0 )
		{
			//tv.setTypeface(null, Typeface.BOLD);
			tv.setTextColor(Color.RED);
		}

	}


}

