package com.oakcottagestudios.batleague.table;

import java.util.Vector;

/**
 * Created by Doug on 26/02/2015.
 */
public class TableEntry
{
	public String position;
	public String name;
	public String points;
	public String played;
	public String average;
	public String pinfall;
	public String hiGameHcp;
	public String hiGameScr;
	public String hiSeriesHcp;
	public String hiSeriesScr;


	public TableEntry( Vector<String> row )
	{
		position = row.elementAt(0);
		name = row.elementAt(1);
		points = row.elementAt(2);
		played = row.elementAt(3);
		average = row.elementAt(4);
		pinfall = row.elementAt(5);
		hiGameHcp = row.elementAt(6);
		hiGameScr = row.elementAt(7);
		hiSeriesHcp = row.elementAt(8);
		hiSeriesScr = row.elementAt(9);
	}


}


