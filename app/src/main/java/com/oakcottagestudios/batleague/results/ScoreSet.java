package com.oakcottagestudios.batleague.results;

/**
 * Created by Doug on 25/03/2015.
 */
public class ScoreSet
{
	public int game1;
	public int game2;
	public int game3;
	public int scrSeries;
	public int hcpSeries;

	public  ScoreSet( String[] stringData )
	{
		game1 = getInt( stringData[1]);
		game2 = getInt( stringData[2]);
		game3 = getInt( stringData[3]);
		scrSeries = getInt( stringData[5]);
		hcpSeries = getInt( stringData[6]);
	}

	static int getInt( String str )
	{
		if( str!=null)
		{

			try{
				str = str.replaceAll( "[^\\d]", "" );   //  Strip all non-digits
				int val = Integer.parseInt( str );
				return val;
			}
			catch(Exception e){
				//Log.e("PLAYERSCORES", "Number format error for: " + playerName + " :'" + str + "'");
			}
		}

		return 0;       //  If not a numeric string
	}



}
