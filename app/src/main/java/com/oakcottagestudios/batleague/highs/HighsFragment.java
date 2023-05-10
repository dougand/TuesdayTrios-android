package com.oakcottagestudios.batleague.highs;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
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
 * Created by Doug on 13/05/2015.
 */
public class HighsFragment  extends Fragment implements Globals
{
    private static HighsFragment instance = null;
	private String myTeam;
	private String myName;

    public HighsFragment()
    {}	// Exists only to defeat instantiation.

    public static HighsFragment getInstance()
    {
        if(instance == null)
            instance = new HighsFragment();

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        //Log.d("AVERAGESFRAGMENT", "onCreateView()");

        // Inflate the layout for this fragment
//		container.removeAllViews();
        View fragmentView = inflater.inflate(R.layout.fragment_table, container, false);

        displayTable(fragmentView);
        return fragmentView;
    }

    void displayTable( View topview )
    {
        Configuration config = getResources().getConfiguration();
        boolean isPortrait = (config.orientation == Configuration.ORIENTATION_PORTRAIT);

	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
	    myTeam = prefs.getString("prefTeamName", "notfound");
	    myName = prefs.getString("prefPlayerName", "notfound");

	    SeasonHighs highs = SeasonHighs.getInstance();

        topview.setBackgroundColor(HIGHS_BACKCOL_1);

        TextView title = (TextView) topview.findViewById(R.id.title_table);
        title.setText("Season Highs");
        title.setBackgroundColor(HIGHS_BACKCOL_2);

        TableLayout table = (TableLayout) topview.findViewById(R.id.table_view);

        int colourCount = 0;

        for( int sh=0; sh<highs.numTables; sh++ )
        {
            ScoreTable scores = highs.scoreTableList.elementAt(sh);
            int teamColour = ((colourCount&1)==0)? HIGHS_BACKCOL_1 : HIGHS_BACKCOL_2;
            colourCount++;

            int w = scores.getTableWidth();
            int h = scores.getTableHeight();

            if( isPortrait )
            {
                TableRow tr = new TableRow( MainActivity.getContext() );
                tr.setBackgroundColor(teamColour);


                addRowElement(tr, " ", ITEM_TITLE);
                for( int i=0; i<2; i++  )
                    addRowElement(tr, scores.titles.elementAt(i), ITEM_TITLE);
                addRowElement(tr, " ", ITEM_TITLE);
                table.addView(tr);

                for( int r = 0; r<h; r++ )
                {
                    tr = new TableRow(MainActivity.getContext());
                    tr.setBackgroundColor(teamColour);
                    addRowElement(tr, " ", ITEM_NORMAL);
                    for (int i = 0; i < 2; i++)
	                    addNamedElement(tr, scores.scores.elementAt(r).elementAt(i), ITEM_NORMAL);
                    table.addView(tr);
                }


                if( w>2)    //  Too wide for 1 row?
                {
                    teamColour = ((colourCount&1)==0)? HIGHS_BACKCOL_1 : HIGHS_BACKCOL_2;
                    colourCount++;

                    tr = new TableRow( MainActivity.getContext() );
                    tr.setBackgroundColor(teamColour);


                    addRowElement(tr, " ", ITEM_TITLE);
                    for( int i=2; i<w; i++  )
                        addRowElement(tr, scores.titles.elementAt(i), ITEM_TITLE);
                    addRowElement(tr, " ", ITEM_TITLE);
                    table.addView(tr);

                    for( int r = 0; r<h; r++ )
                    {
                        tr = new TableRow(MainActivity.getContext());
                        tr.setBackgroundColor(teamColour);
                        addRowElement(tr, " ", ITEM_NORMAL);
                        for (int i = 2; i < w; i++)
	                        addNamedElement(tr, scores.scores.elementAt(r).elementAt(i), ITEM_NORMAL);
                        table.addView(tr);
                    }

                }

            }
            else    //  landscape
            {
                TableRow tr = new TableRow( MainActivity.getContext() );
                tr.setBackgroundColor(teamColour);
                addRowElement(tr, " ", ITEM_TITLE);
                if( w<4 )
                    addRowElement(tr, " ", ITEM_TITLE);
                for( int i=0; i<w; i++  )
                    addRowElement(tr, scores.titles.elementAt(i), ITEM_TITLE);
                addRowElement(tr, " ", ITEM_TITLE);
                table.addView(tr);

                for( int r = 0; r<h; r++ )
                {

                    tr = new TableRow( MainActivity.getContext() );
                    tr.setBackgroundColor(teamColour);
                    addRowElement(tr, " ", ITEM_NORMAL);
                    if( w<4 )
                        addRowElement(tr, " ", ITEM_NORMAL);
                    for( int i=0; i<w; i++  )
	                    addNamedElement(tr, scores.scores.elementAt(r).elementAt(i), ITEM_NORMAL);
                    addRowElement(tr, " ", ITEM_NORMAL);
                    table.addView(tr);

                }
            }

        }

        table.setStretchAllColumns(true);

    }



	void addNamedElement( TableRow row, String item, int flags )
	{

		if( item.contains(myName) || item.contains(myTeam) )
			flags |= ITEM_HIGHLIGHT;

		addRowElement( row, item, flags );
	}



    void addRowElement( TableRow row, String item, int flags )
    {

        TextView tv = new TextView( MainActivity.getContext() );
        tv.setText(item);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);

        row.addView(tv);

        if( (flags & ITEM_TITLE)!=0 )
        {
            TableRow.LayoutParams params = (TableRow.LayoutParams) tv.getLayoutParams();
            params.height = 80;

            tv.setLayoutParams(params); // causes layout update
            tv.setTypeface(null, Typeface.BOLD);
            tv.setGravity(Gravity.BOTTOM);
        }


	    if( (flags & ITEM_HIGHLIGHT) != 0 )
	    {
		    tv.setTextColor(Color.RED);
	    }
    }


}
