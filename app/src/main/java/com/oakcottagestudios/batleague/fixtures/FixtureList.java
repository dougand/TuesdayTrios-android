package com.oakcottagestudios.batleague.fixtures;

import java.util.Vector;

/**
 * Created by Doug on 19/03/2015.
 */
public class FixtureList
{
	public String weekName;
	public Vector<String> teamNames;

	public FixtureList(String name)
	{
		weekName = name;
		teamNames = new Vector<>();
	}

	public void AddFixture(  String fixture )
	{
		teamNames.addElement( fixture );
	}

	public int getTeamCount()
	{
		return teamNames.size();
	}

}
