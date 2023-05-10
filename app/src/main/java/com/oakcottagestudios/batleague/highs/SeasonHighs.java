package com.oakcottagestudios.batleague.highs;

import com.oakcottagestudios.batleague.DownloadListener;
import com.oakcottagestudios.batleague.HtmlDownload;

import java.util.Vector;

/**
 * Created by Doug on 13/05/2015.
 */
public class SeasonHighs extends HtmlDownload
{

    public int numTables;
    public Vector<ScoreTable> scoreTableList;



    private static SeasonHighs instance = null;

    protected SeasonHighs()
    {}	// Exists only to defeat instantiation.

    public static SeasonHighs getInstance()
    {
        if(instance == null)
        {
            instance = new SeasonHighs();
        }
        return instance;
    }



    public void Create( DownloadListener target, String _Url, int size )
    {
        init( SEASON_HIGHS, "HIGHS", size );
        Url = _Url;
        listener = target;

        new FileDownloader().execute( Url );
    }


    @Override
    public void setDownloadState(int state)
    {
        super.setDownloadState( state );

        if( downloadState == STATE_FINISHED )
            extractTableData();

        listener.downloadStateChanged( LEAGUE_FIXTURES, state );

    }


    public void extractTableData()
    {
        numTables = tableList.size()-1;

        if( numTables <= 0 )
            return;

        scoreTableList = new Vector<>();


        for( int t=0; t<numTables; t++ )
        {
            //  Date
            Vector<Vector<String>>table = tableList.elementAt(t+1);

            boolean titleSet = false;
            ScoreTable highScores = new ScoreTable();
            scoreTableList.addElement( highScores );

            for( int r=0; r<table.size(); r++ )
            {
                Vector<String> row = table.elementAt(r);
                if( row.size()>1 )  //  skip blank rows
                {
                    if( !titleSet )
                    {
                        highScores.setTitles( row );
                        titleSet = true;
                    }
                    else
                    {
                        highScores.addScores( row );
                    }
                }

            }


//            Vector<String> row = table.elementAt(0);
//            FixtureList currentWeek = new FixtureList( row.elementAt(0));
//            fixtureList.addElement( currentWeek );
//
//            //  List
//            table = tableList.elementAt(w*2 +2);
//            int rowcount = table.size();
//            for( int r=0; r<rowcount; r++ )
//            {
//                row = table.elementAt(r);
//                int colcount = row.size();
//                for( int c=0; c<colcount; c++ )
//                    currentWeek.AddFixture( row.elementAt(c));
//            }
//

        }

    }
}
