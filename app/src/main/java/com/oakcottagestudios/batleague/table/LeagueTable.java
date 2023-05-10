package com.oakcottagestudios.batleague.table;

import com.oakcottagestudios.batleague.DownloadListener;
import com.oakcottagestudios.batleague.HtmlDownload;

import java.util.Vector;

/**
 * Created by Doug on 18/02/2015.
 */
public class LeagueTable extends HtmlDownload
{

	public String pageTitle;

	public TableEntry titleRow;
	public Vector<TableEntry> tableRow;
	public int numEntries;



	private static LeagueTable instance = null;

	protected LeagueTable()
	{}		// Exists only to defeat instantiation.

	public static LeagueTable getInstance()
	{
		if(instance == null)
			instance = new LeagueTable();

		return instance;
	}



	public void Create( DownloadListener target, String _Url, int size )
	{
		init( LEAGUE_TABLE, "TABLE", size );
//		tableType = LEAGUE_TABLE;
//		LogID = "TABLE";     //  pass params to HtmlDownload
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

		listener.downloadStateChanged( LEAGUE_TABLE, state );

	}


	public void extractTableData()
	{
		//  Get the page title
		Vector<Vector<String>> table = tableList.elementAt(1);
		Vector<String> row = table.elementAt(0);
		pageTitle = row.elementAt(0);

		//  Get the data
		table = tableList.elementAt(2);
		int rowCount = table.size();

		tableRow = new Vector<TableEntry>();

		for( int r=0; r<rowCount; r++ )
		{
			if( r==0 )
				titleRow = new TableEntry( table.elementAt(r) );
			else
				tableRow.addElement( new TableEntry( table.elementAt(r) ) );
		}

		numEntries = tableRow.size();
	}

}
