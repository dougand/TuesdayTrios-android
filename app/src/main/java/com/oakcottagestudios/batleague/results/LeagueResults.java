package com.oakcottagestudios.batleague.results;

import android.util.Log;

import com.oakcottagestudios.batleague.DownloadListener;
import com.oakcottagestudios.batleague.HtmlDownload;

import java.util.Vector;

/**
 * Created by Doug on 18/02/2015.
 */
public class LeagueResults extends HtmlDownload
{
	public String pageTitle;

	public Vector<Match> matchList;
	public int numEntries;


	private static LeagueResults instance = null;

	protected LeagueResults()
	{}	// Exists only to defeat instantiation.

	public static LeagueResults getInstance()
	{
		if(instance == null)
		{
			instance = new LeagueResults();
		}
		return instance;
	}


	public void Create( DownloadListener target, String _Url, int size )
	{
		init( LEAGUE_RESULTS, "RESULTS", size );
		//tableType = LEAGUE_RESULTS;
		//LogID = "RESULTS";     //  pass params to HtmlDownload
		Url = _Url;
		//expectedSize = size*1024;

		listener = target;

		new FileDownloader().execute( Url );
	}

	@Override
	public void setDownloadState(int state)
	{

		if( state == STATE_FINISHED )
		try
		{
			extractTableData();
		}
		catch( Exception e )
		{
			Log.e( LogID, e.toString() );
			state = STATE_DATA_ERROR;
		}

		super.setDownloadState( state );
		listener.downloadStateChanged( LEAGUE_RESULTS, state );

	}


	public void extractTableData()
	{

		//  Get the page title
		Vector<Vector<String>> table = tableList.elementAt(0);
		Vector<String> row = table.elementAt(0);
		pageTitle = row.elementAt(1);

		//  Get the data
		table = tableList.elementAt(1);
		int rowCount = table.size();

		matchList = new Vector<>();
		numEntries = 0;
		//tempData = new Vector<String>();

		Match currentMatch = null;
		TeamScores leftTeam = null, rightTeam = null;

		String[] leftData = new String[7];
		String[] rightData = new String[7];

		for( int r=0; r<rowCount; r++ )
		{
			Vector<String> trow = table.elementAt(r);
			String nameStr = trow.elementAt(0);
			String gameStr = trow.elementAt(1);

			for( int i=0; i<7; i++ )
			{
				leftData[i] = trow.elementAt(i);
				rightData[i] = trow.elementAt(8+i);
			}

			if( nameStr.length() > 0 )      //  not a blank line
			{
				if( gameStr.startsWith("Rnd") )
				{
					//  It's a team name line
					Log.d( LogID, r+": Team Names" );
					leftTeam = new TeamScores( leftData[0] );
					rightTeam = new TeamScores( rightData[0] );

					currentMatch = new Match(leftTeam, rightTeam, false);
					matchList.addElement( currentMatch );
				}
				else if( nameStr.startsWith("Scratch"))
				{
					Log.d( LogID, r+": Scratch Scores" );
					if( leftTeam != null ) leftTeam.addScratchScores( leftData );
					if( rightTeam != null ) rightTeam.addScratchScores( rightData );
				}
				else if( nameStr.startsWith("Handicap"))
				{
					Log.d( LogID, r+": Handicap" );
					if( leftTeam != null ) leftTeam.addHandicapScores( leftData );
					if( rightTeam != null ) rightTeam.addHandicapScores( rightData );
				}
				else if( nameStr.startsWith("Total"))
				{
					Log.d( LogID, r+": Total Scores" );
					if( leftTeam != null ) leftTeam.addTotalScores( leftData );
					if( rightTeam != null ) rightTeam.addTotalScores( rightData );
				}
				else if( nameStr.startsWith("Points"))
				{
					Log.d( LogID, r+": Points" );
					if( currentMatch!=null ) currentMatch.setPoints( leftData, rightData );
				}
				else if(  trow.elementAt(5).length()== 0 && trow.elementAt(6).length()== 0 && trow.elementAt(13).length()== 0 && trow.elementAt(14).length()== 0)
				{
					//  It's a team name line with no match
					Log.d( LogID, r+": Team (no match)" );
					leftTeam = new TeamScores( leftData[0] );
					rightTeam = new TeamScores( rightData[0] );

					currentMatch = new Match(leftTeam, rightTeam, true);
					matchList.addElement( currentMatch );
				}
				else
				{
					//  It's a player score line
					Log.d( LogID, r+": Player" );
					if( leftTeam != null ) leftTeam.addPlayer( leftData );
					if( rightTeam != null ) rightTeam.addPlayer( rightData );
				}
			}

//			tempData.addElement(trow.elementAt(0) );
		}

		numEntries = matchList.size();

	}



}

