package com.oakcottagestudios.batleague.fixtures;

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


public class FixturesFragment extends Fragment implements Globals
{

	private static FixturesFragment instance = null;

	public FixturesFragment()
	{}		// Exists only to defeat instantiation.

	public static FixturesFragment getInstance()
	{
		if(instance == null)
			instance = new FixturesFragment();

		return instance;
	}

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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
        String myTeam = prefs.getString("prefTeamName", "notfound");
        //String myPlayer = prefs.getString("prefPlayerName", "notfound" );

        LeagueFixtures fixtures = LeagueFixtures.getInstance();
		topview.setBackgroundColor( FIXTURES_BACKCOL_1 );


		TextView title = (TextView) topview.findViewById(R.id.title_table);
		title.setText("Next Fixtures");
		title.setBackgroundColor( FIXTURES_BACKCOL_2 );


		TableLayout table = (TableLayout) topview.findViewById(R.id.table_view);


		for( int w = 0; w<fixtures.numWeeks; w++ )
		{
			FixtureList fix = fixtures.fixtureList.elementAt(w);

			int teamColour = ((w&1)==0)? FIXTURES_BACKCOL_1 : FIXTURES_BACKCOL_2;

			TableRow tr = new TableRow( MainActivity.getContext() );
			tr.setBackgroundColor( teamColour );
			addRowElement(tr, fix.weekName, ITEM_TITLE);
			table.addView(tr);

			for( int t = 0; t < fix.getTeamCount(); t += 2 )
			{
				tr = new TableRow( MainActivity.getContext() );
				tr.setBackgroundColor( teamColour );

                int itemFlagLeft = ITEM_NORMAL|JUSTIFY_RIGHT;
                int itemFlagRight = ITEM_NORMAL;
                if( fix.teamNames.elementAt(t).indexOf(myTeam)>=0)
                    itemFlagLeft |= ITEM_HIGHLIGHT;
                if( fix.teamNames.elementAt(t+1).indexOf(myTeam)>=0)
                    itemFlagRight |=ITEM_HIGHLIGHT;

                addRowElement(tr, fix.teamNames.elementAt(t), itemFlagLeft);
				addRowElement(tr, " : ", ITEM_NORMAL|JUSTIFY_CENTRE);
				addRowElement(tr, fix.teamNames.elementAt(t+1), itemFlagRight);
				table.addView(tr);

			}


		}

		table.setStretchAllColumns( true );

	}

	void addRowElement( TableRow row, String item, int flags )
	{

		TextView tv = new TextView( MainActivity.getContext() );
		tv.setText( item );
		tv.setTextColor(Color.BLACK);

		row.addView( tv );

		if( (flags & ITEM_TITLE)!=0 )
		{
			TableRow.LayoutParams params = (TableRow.LayoutParams) tv.getLayoutParams();
			params.span = 3;
			params.gravity = Gravity.CENTER_HORIZONTAL;
			params.height = 80;

			tv.setLayoutParams(params); // causes layout update
			tv.setTypeface(null, Typeface.BOLD);
			tv.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
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

