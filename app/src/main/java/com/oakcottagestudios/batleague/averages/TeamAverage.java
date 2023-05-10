package com.oakcottagestudios.batleague.averages;

import java.util.Vector;

/**
 * Created by Doug on 17/03/2015.
 */
public class TeamAverage
{
	public String teamName;
	public Vector<IndividualAverage> teamMembers;

	public TeamAverage(String name)
	{
		teamName = name;
		teamMembers = new Vector<>();
	}

	public void AddMember(  Vector<String> row )
	{
		IndividualAverage member = new IndividualAverage( row );
		teamMembers.addElement( member );
	}

	public int getTeamCount()
	{
		return teamMembers.size();
	}

}
