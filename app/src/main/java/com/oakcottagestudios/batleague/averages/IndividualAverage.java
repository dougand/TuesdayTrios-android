package com.oakcottagestudios.batleague.averages;

import java.util.Vector;

/**
 * Created by Doug on 17/03/2015.
 */


public class IndividualAverage
{
	public String name;
	public String prefName;     //  adjusted for use in preferences
	public String handicap;
	public String average;
	public String games;
	public String pins;
	public String hiGameScr;
	public String hiGameHcp;
	public String hiSeriesScr;
	public String hiSeriesHcp;


	public IndividualAverage( Vector<String> row )
	{
		name = row.elementAt(0);
		handicap = row.elementAt(1);
		average = row.elementAt(2);
		games = row.elementAt(3);
		pins = row.elementAt(4);
		hiGameScr = row.elementAt(5);
		hiGameHcp = row.elementAt(6);
		hiSeriesScr = row.elementAt(7);
		hiSeriesHcp = row.elementAt(8);

		prefName = name.replace("(c)", "").trim();

	}

}
