package com.oakcottagestudios.batleague.highs;

import java.util.Vector;

/**
 * Created by Doug on 20/05/2015.
 */
public class ScoreTable
{
    int tableWidth;
    public Vector<String> titles;
    public Vector<Vector<String>> scores;

    public int getTableWidth() { return tableWidth; }
    public int getTableHeight() { return scores.size(); }



    public ScoreTable()
    {
        tableWidth = 0;
        scores = new Vector<>();
    }


    public void setTitles( Vector<String> titleList )
    {
        titles = titleList;
        tableWidth = titles.size();
    }


    public void addScores( Vector<String> scoreList )
    {
        scores.add(scoreList);
    }

}
