package com.oakcottagestudios.batleague.results;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
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


public class ResultsFragment extends Fragment implements Globals
{

	private static ResultsFragment instance = null;

	public ResultsFragment()
	{}		// Exists only to defeat instantiation.

	public static ResultsFragment getInstance()
	{
		if(instance == null)
			instance = new ResultsFragment();

			return instance;
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
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

		LeagueResults results = LeagueResults.getInstance();

		topview.setBackgroundColor( RESULTS_BACKCOL_1 );


		TextView title = (TextView) topview.findViewById(R.id.title_table);
		title.setText( results.pageTitle );
		title.setBackgroundColor( RESULTS_BACKCOL_2 );


		TableLayout table = (TableLayout) topview.findViewById(R.id.table_view);
		TableRow tr;



		for( int m = 0; m<results.numEntries; m++ )
		{
			Match match = results.matchList.elementAt(m);
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
			String myTeam = prefs.getString("prefTeamName", "notfound");

			int teamColour = ((m&1)==0)? RESULTS_BACKCOL_1 : RESULTS_BACKCOL_2;

			int leftTitle = ITEM_TITLE;
			int rightTitle = ITEM_TITLE;

			if( myTeam.compareTo( match.team1.teamName )==0 )
				leftTitle |= ITEM_HIGHLIGHT;

			if( myTeam.compareTo( match.team2.teamName )==0 )
				rightTitle |= ITEM_HIGHLIGHT;

			if( match.notPlayed )
			{
				tr = new TableRow( MainActivity.getContext() );
				tr.setBackgroundColor( teamColour );
				table.addView(tr);

				addRowElement(tr, match.team1.teamName, leftTitle);
				addBlanks(tr, 1);
				addRowElement( tr, match.team2.teamName, rightTitle );



			}
			else
			{
				// was played
				tr = new TableRow( MainActivity.getContext() );
				tr.setBackgroundColor( teamColour );
				table.addView(tr);

				addRowElement(tr, match.team1.teamName + " : " + match.totalPoints1, leftTitle);
				//addRowElement(tr, "<b>" + match.totalPoints1 + "</b>", ITEM_NORMAL);
				addBlanks(tr, 1);
				addRowElement( tr, match.team2.teamName + " : " + match.totalPoints2, rightTitle );
				//addRowElement( tr, "<b>"+match.totalPoints2+"</b>", ITEM_NORMAL );

				int plyrcount1 = match.team1.players.size();
				int plyrcount2 = match.team2.players.size();
				int plyrs = plyrcount1>plyrcount2 ? plyrcount1:plyrcount2;
				for( int p=0; p<plyrs; p++ )
				{
					tr = new TableRow( MainActivity.getContext() );
					tr.setBackgroundColor( teamColour );
					table.addView(tr);

					if( p<plyrcount1 )
					{
						PlayerScores ps = match.team1.players.elementAt(p);
						if( isPortrait )
							addScoreSet(tr, ps.shortName, ps.scores);
						else
							addScoreSet(tr, ps.playerName, ps.scores);

					}
					else
						addBlanks( tr, 5 );

					addBlanks( tr, 1 );

					if( p<plyrcount2 )
					{
						PlayerScores ps = match.team2.players.elementAt(p);
						if( isPortrait )
							addScoreSet(tr, ps.shortName, ps.scores);
						else
							addScoreSet(tr, ps.playerName, ps.scores);
					}
				}

				tr = new TableRow( MainActivity.getContext() );
				tr.setBackgroundColor( teamColour );
				table.addView(tr);
				addScoreSet(tr, "Scratch", match.team1.scratchScores);
				addBlanks(tr, 1);
				addScoreSet(tr, "Scratch", match.team2.scratchScores);

				tr = new TableRow( MainActivity.getContext() );
				tr.setBackgroundColor( teamColour );
				table.addView(tr);
				addScoreSet(tr, "Handicap", match.team1.handicapScores);
				addBlanks(tr, 1);
				addScoreSet(tr, "Handicap", match.team2.handicapScores);

				tr = new TableRow( MainActivity.getContext() );
				tr.setBackgroundColor( teamColour );
				table.addView(tr);
				addScoreSet(tr, "Total", match.team1.totalScores);
				addBlanks(tr, 1);
				addScoreSet( tr, "Total", match.team2.totalScores );


				tr = new TableRow( MainActivity.getContext() );
				tr.setBackgroundColor( teamColour );
				table.addView(tr);

				addRowElement( tr, "Points", ITEM_NORMAL );

				for( int i=0; i<3; i++)
					addRowElement( tr, ""+match.points1[i], ITEM_NORMAL|JUSTIFY_RIGHT );
				addBlanks( tr, 1 );
				addRowElement(tr, "" + match.points1[3], ITEM_NORMAL|JUSTIFY_RIGHT );

				addBlanks( tr, 1 );
				addRowElement( tr, "Points", ITEM_NORMAL );

				for( int i=0; i<3; i++)
					addRowElement( tr, ""+match.points2[i], ITEM_NORMAL|JUSTIFY_RIGHT );
				addBlanks( tr, 1 );
				addRowElement( tr, ""+match.points2[3], ITEM_NORMAL|JUSTIFY_RIGHT );


			}




		}

		table.setStretchAllColumns( true );

	}




	void addScoreSet( TableRow row, String name, ScoreSet scores )
	{
		addRowElement( row, name, ITEM_NORMAL );

		if( scores.game1 > 0 )
			addRowElement( row, ""+scores.game1, ITEM_NORMAL|JUSTIFY_RIGHT );
		else
			addRowElement( row, " ", ITEM_NORMAL );

		if( scores.game2 > 0 )
			addRowElement( row, ""+scores.game2, ITEM_NORMAL|JUSTIFY_RIGHT );
		else
			addRowElement( row, " ", ITEM_NORMAL );

		if( scores.game3 > 0 )
			addRowElement( row, ""+scores.game3, ITEM_NORMAL|JUSTIFY_RIGHT );
		else
			addRowElement( row, " ", ITEM_NORMAL );

		if( scores.scrSeries > 0 )
			addRowElement( row, ""+scores.scrSeries, ITEM_NORMAL|JUSTIFY_RIGHT );
		else
			addRowElement( row, " ", ITEM_NORMAL );

		if( scores.hcpSeries > 0 )
			addRowElement( row, ""+scores.hcpSeries, ITEM_NORMAL|JUSTIFY_RIGHT );
		else
			addRowElement( row, " ", ITEM_NORMAL );

	}

	void addBlanks( TableRow row, int count )
	{
		for( int i=0; i<count; i++ )
		{
			TextView tv = new TextView( MainActivity.getContext() );
			//tv.setText( "" );
			row.addView( tv );
		}

	}


	void addRowElement( TableRow row, String item, int flags )
	{

		TextView tv = new TextView( MainActivity.getContext() );
		tv.setText( Html.fromHtml(item) );
		tv.setTextColor(Color.BLACK);

		row.addView( tv );

		if( (flags & ITEM_TITLE)!=0 )
		{
			TableRow.LayoutParams params = (TableRow.LayoutParams) tv.getLayoutParams();
			params.span = 6;
			params.height = 80;
			//params.gravity = Gravity.CENTER_HORIZONTAL;
			tv.setLayoutParams(params); // causes layout update
			tv.setTypeface(null, Typeface.BOLD);
			tv.setGravity(Gravity.BOTTOM);

		}
		else
		{
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10 );
			if( (flags & JUSTIFY_RIGHT) != 0 )
			{
				tv.setGravity(Gravity.RIGHT);
			}
			if( (flags & JUSTIFY_CENTRE) != 0 )
			{
				tv.setGravity(Gravity.CENTER_HORIZONTAL);
			}
		}

		if( (flags & ITEM_HIGHLIGHT) != 0 )
		{
			tv.setTextColor(Color.RED);
		}

	}

}

