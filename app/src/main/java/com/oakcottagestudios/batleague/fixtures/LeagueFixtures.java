package com.oakcottagestudios.batleague.fixtures;

import com.oakcottagestudios.batleague.DownloadListener;
import com.oakcottagestudios.batleague.HtmlDownload;

import java.util.Vector;

/**
 * Created by Doug on 18/02/2015.
 */
public class LeagueFixtures extends HtmlDownload
{
	public int numWeeks;
	public Vector<FixtureList> fixtureList;



	private static LeagueFixtures instance = null;

	protected LeagueFixtures()
	{}	// Exists only to defeat instantiation.

	public static LeagueFixtures getInstance()
	{
		if(instance == null)
		{
			instance = new LeagueFixtures();
		}
		return instance;
	}



	public void Create( DownloadListener target, String _Url, int size )
	{
		init( LEAGUE_FIXTURES, "FIXTURES", size );
		//tableType = LEAGUE_FIXTURES;
		//LogID = "FIXTURES";     //  pass params to HtmlDownload
		Url = _Url;
		//expectedSize = size*1024;

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
		//  Raw table data is...
		//	    page title,
		//      date
		//      team list
		//      date
		//      team list
		//      etc...

		numWeeks = tableList.size()/2;

		if( numWeeks == 0 )
			return;

		fixtureList = new Vector<>();


		for( int w=0; w<numWeeks; w++ )
		{
			//  Date
			Vector<Vector<String>>table = tableList.elementAt(w*2 +1);
			Vector<String> row = table.elementAt(0);
			FixtureList currentWeek = new FixtureList( row.elementAt(0));
			fixtureList.addElement( currentWeek );

			//  List
			table = tableList.elementAt(w*2 +2);
			int rowcount = table.size();
			for( int r=0; r<rowcount; r++ )
			{
				row = table.elementAt(r);
				int colcount = row.size();
				for( int c=0; c<colcount; c++ )
					currentWeek.AddFixture( row.elementAt(c));
			}


		}

	}



}
