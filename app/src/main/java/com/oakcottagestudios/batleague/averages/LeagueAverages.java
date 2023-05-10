package com.oakcottagestudios.batleague.averages;

import android.util.Log;

import com.oakcottagestudios.batleague.DownloadListener;
import com.oakcottagestudios.batleague.HtmlDownload;

import java.util.Vector;

/**
 * Created by Doug on 18/02/2015.
 */


public class LeagueAverages extends HtmlDownload
{
	private static LeagueAverages instance = null;


	public Vector<TeamAverage> teamAverages;
	public int numTeams = 0;

	public Vector<String> columnTitles;


	protected LeagueAverages()
	{}	// Exists only to defeat instantiation.

	public static LeagueAverages getInstance()
	{
		if(instance == null)
		{
			instance = new LeagueAverages();
		}
		return instance;
	}


	public void Create( DownloadListener target, String _Url, int size )
	{
		init( LEAGUE_AVERAGES, "AVERAGES", size );
//		tableType = LEAGUE_AVERAGES;
//		LogID = "AVERAGES";     //  pass params to HtmlDownload
		Url = _Url;
//		expectedSize = size*1024;

		listener = target;

		new FileDownloader().execute( Url );
	}

	@Override
	public void setDownloadState(int state)
	{
		super.setDownloadState( state );

		if( downloadState == STATE_FINISHED )
			extractTableData();

		listener.downloadStateChanged( LEAGUE_AVERAGES, state );

	}


	public void extractTableData()
	{
		//  Get the data
		Vector<Vector<String>>table = tableList.elementAt(1);
		int rowCount = table.size();

		teamAverages = new Vector<TeamAverage>();
		numTeams = 0;

		TeamAverage currentTeam = null;

		for( int r=0; r<rowCount; r++ )
		{
			Vector<String> row = table.elementAt(r);

			if( r==0 )
			{
				//  Get the Column headers from the first row
				columnTitles = new Vector<>();
				for( int c=0; c<row.size(); c++ )
					columnTitles.addElement( row.elementAt(c) );
			}

			//  Is it a team name subheader?
			if( (row.elementAt(1)).equals( columnTitles.elementAt(1) ) )
			{
				//  "Hcp" == "Hcp"
				String tname = row.elementAt(0);
				Log.d(LogID, "Team Name: "+tname  );

				currentTeam = new TeamAverage( tname );
				teamAverages.addElement( currentTeam );
				numTeams++;
			}
			else
			{
				//  It's a player line
				currentTeam.AddMember( row );
			}
		}


	}



}
