package com.oakcottagestudios.batleague;

/**
 * Created by Doug on 16/02/2015.
 */
public interface Globals
{
	static final int STATE_DOWNLOADING = 0;
	static final int STATE_DISPLAYING = 1;


	static final int LEAGUE_TABLE = 0;
	static final int LEAGUE_AVERAGES = 1;
	static final int LEAGUE_FIXTURES = 2;
	static final int LEAGUE_RESULTS = 3;
	static final int SEASON_HIGHS = 4;


	static final int STATE_IDLE = 0;
	static final int STATE_CONNECTING = 1;
	static final int STATE_PARSING = 2;
	static final int STATE_FINISHED = 9;

	static final int STATE_DOWNLOAD_PROGRESS = 100;	//	+ 0-100



	static final int STATE_CONNECTION_ERROR = -1;
	static final int STATE_PARSE_ERROR = -2;
	static final int STATE_DOWNLOAD_ERROR = -3;
	static final int STATE_DATA_ERROR = -4;
	static final int STATE_CANCELLED = -9;

	static final int ITEM_NORMAL = 0;
	static final int ITEM_TITLE = 1;
	static final int ITEM_HIGHLIGHT = 2;
	static final int JUSTIFY_CENTRE = 64;
	static final int JUSTIFY_RIGHT = 128;

	static final int TABLE_BACKCOL_1 = 0xffffffd0;
	static final int TABLE_BACKCOL_2 = 0xffffffa0;

	static final int AVERAGES_BACKCOL_1 = 0xffd0ffff;
	static final int AVERAGES_BACKCOL_2 = 0xffa0ffff;

	static final int RESULTS_BACKCOL_1 = 0xffffffd0;
	static final int RESULTS_BACKCOL_2 = 0xffffffa0;

	static final int FIXTURES_BACKCOL_1 = 0xffd0ffff;
	static final int FIXTURES_BACKCOL_2 = 0xffa0ffff;

	static final int HIGHS_BACKCOL_1 = 0xffffffd0;
	static final int HIGHS_BACKCOL_2 = 0xffffffa0;


}
