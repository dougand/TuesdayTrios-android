package com.oakcottagestudios.batleague.results;

import android.util.Log;

/**
 * Created by Doug on 20/03/2015.
 */
public class PlayerScores
{
	public String playerName;
	public String shortName;
	public int handicap;
	public ScoreSet scores;

	public PlayerScores( String[] stringData )
	{
		playerName = stringData[0];
		//  TODO: extract hcap from name string

		int firstSpace = playerName.indexOf(' ');
		int startBracket = playerName.indexOf('(');
		int endBracket = playerName.indexOf(')');

		if( firstSpace>0 )
			shortName = playerName.substring( 0, firstSpace );
		else if( startBracket>0 )
			shortName = playerName.substring( 0, startBracket );
		else
			shortName = playerName;



		scores = new ScoreSet( stringData );

	}

	int getInt( String str )
	{
		if( str!=null)
		{

			try{
				str = str.replaceAll( "[^\\d]", "" );   //  Strip all non-digits
				int val = Integer.parseInt( str );
				return val;
			}
			catch(Exception e){
				Log.e("PLAYERSCORES", "Number format error for: "+playerName+" :'"+str+"'" );
			}
		}

		return 0;       //  If not a numeric string
	}

}
