package com.oakcottagestudios.batleague.results;

import java.util.Vector;

/**
 * Created by Doug on 20/03/2015.
 */
public class TeamScores
{
	public String teamName;
	public Vector<PlayerScores> players;
	public ScoreSet scratchScores;
	public ScoreSet handicapScores;
	public ScoreSet totalScores;


	public TeamScores( String name )
	{
		teamName = name;
		players = new Vector<>();
	}

	public void addPlayer( String[]data )
	{
		players.addElement( new PlayerScores(data));
	}

	public void addScratchScores( String[]data )
	{
		scratchScores = new ScoreSet( data );
	}

	public void addHandicapScores( String[]data )
	{
		handicapScores = new ScoreSet( data );
	}

	public void addTotalScores( String[]data )
	{
		totalScores = new ScoreSet( data );
	}

}
