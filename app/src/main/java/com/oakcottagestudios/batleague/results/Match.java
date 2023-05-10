package com.oakcottagestudios.batleague.results;

/**
 * Created by Doug on 24/03/2015.
 */
public class Match
{
	public TeamScores team1;
	public TeamScores team2;
	public int[] points1 = new int[4];
	public int[] points2 = new int[4];
	public int totalPoints1;
	public int totalPoints2;
	public boolean notPlayed;

	public Match( TeamScores _team1, TeamScores _team2, boolean _notPlayed )
	{
		team1 = _team1;
		team2 = _team2;
		notPlayed = _notPlayed;
	}

	public void setPoints( String[] data1, String[] data2 )
	{
		points1[0] = ScoreSet.getInt( data1[1] );
		points1[1] = ScoreSet.getInt( data1[2] );
		points1[2] = ScoreSet.getInt( data1[3] );
		points1[3] = ScoreSet.getInt( data1[6] );

		points2[0] = ScoreSet.getInt( data2[1] );
		points2[1] = ScoreSet.getInt( data2[2] );
		points2[2] = ScoreSet.getInt( data2[3] );
		points2[3] = ScoreSet.getInt( data2[6] );

		totalPoints1 = totalPoints2 = 0;
		for( int i=0; i<4; i++ )
		{
			totalPoints1 += points1[i];
			totalPoints2 += points2[i];
		}

	}
}
