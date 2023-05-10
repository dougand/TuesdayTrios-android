package com.oakcottagestudios.batleague;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
//import androidx.appcompat.app.ActionBarActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

//import com.flurry.android.FlurryAgent;
import com.oakcottagestudios.batleague.averages.AveragesFragment;
import com.oakcottagestudios.batleague.averages.LeagueAverages;
import com.oakcottagestudios.batleague.fixtures.FixturesFragment;
import com.oakcottagestudios.batleague.fixtures.LeagueFixtures;
import com.oakcottagestudios.batleague.highs.HighsFragment;
import com.oakcottagestudios.batleague.highs.SeasonHighs;
import com.oakcottagestudios.batleague.results.LeagueResults;
import com.oakcottagestudios.batleague.results.ResultsFragment;
import com.oakcottagestudios.batleague.table.LeagueTable;
import com.oakcottagestudios.batleague.table.TableFragment;
import com.oakcottagestudios.batleague.view.SlidingTabLayout;


public class MainActivity extends AppCompatActivity implements DownloadListener, Globals
{
	private static final int RESULT_SETTINGS = 100;

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	SlidingTabLayout mSlidingTabLayout;

	static Context context;
	static int loadCount;
    static boolean firstTime = true;

	static int currentState = STATE_DOWNLOADING;


    @Override
    protected void onStart()
    {
        super.onStart();
        //FlurryAgent.onStartSession(this);

	    if( firstTime )
	    {
		    firstTime = false;
		    //FlurryAgent.logEvent( "App started");
	    }

    }

    @Override
    protected void onStop()
    {
        super.onStop();
        //FlurryAgent.onEndSession(this);
    }

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Log.d("MAINACTIVITY", "onCreate()" );

		super.onCreate(savedInstanceState);
		context = getApplicationContext();


		if( currentState == STATE_DISPLAYING )
		{
			setMainDisplay();
		}
		else
		{
			setContentView(R.layout.activity_start);

			//  Kick off the table downloads
			loadCount = 0;
			LeagueTable.getInstance().Create(this, "http://www.ttleague.net/Table.htm", 22 );
			LeagueAverages.getInstance().Create(this, "http://www.ttleague.net/Teams.htm", 120 );
			LeagueResults.getInstance().Create(this, "http://www.ttleague.net/Results.htm", 74 );
			LeagueFixtures.getInstance().Create(this, "http://www.ttleague.net/Fixtures.htm", 10 );
			SeasonHighs.getInstance().Create(this, "http://www.ttleague.net/Highs.htm", 18 );
		}
	}


	public static Context getContext()
	{
		return context;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if( id == R.id.action_settings )
		{
			Intent i = new Intent( this, UserSettings.class );
			startActivityForResult( i, RESULT_SETTINGS );
            //FlurryAgent.logEvent("Settings");
			return true;
		}
        else if( id == R.id.action_about )
        {
            Intent i = new Intent( this, About.class );
            startActivity(i);
           // FlurryAgent.logEvent("About");
            return true;
        }

		return super.onOptionsItemSelected(item);
	}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
                //showUserSettings();
                //TableFragment.getInstance().refreshView();
                mViewPager.getAdapter().notifyDataSetChanged();
                break;

        }

    }




    public void setMainDisplay()
	{
		Log.d("MAINACTIVITY", "setMainDisplay()" );

		setContentView(R.layout.activity_main);

		// Create the adapter
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(5);

		//  Set up the sliding Tabs
		mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
		mSlidingTabLayout.setViewPager(mViewPager);


	}

	@Override
	public void downloadStateChanged(int senderID, int newState)
	{
		//Log.d("MAINACTIVITY", "DownloadStateListener: " + senderID + " - "+newState );

		switch( senderID )
		{
			case LEAGUE_TABLE:
				if( newState == STATE_FINISHED)
					loadCount++;
				break;

			case LEAGUE_AVERAGES:
				if( newState == STATE_FINISHED)
					loadCount++;
				break;

			case LEAGUE_RESULTS:
				if( newState == STATE_FINISHED)
					loadCount++;
				break;

			case LEAGUE_FIXTURES:
				if( newState == STATE_FINISHED)
					loadCount++;
				break;

			case SEASON_HIGHS:
				if( newState == STATE_FINISHED)
					loadCount++;
				break;
		}

		if( newState >= STATE_DOWNLOAD_PROGRESS )
		{
			ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
			pb.setProgress( HtmlDownload.totalDownloaded * 100 / HtmlDownload.totalExpectedSize );

		}


		if( loadCount == 5 )
		{
			currentState = STATE_DISPLAYING;
			setMainDisplay();
			//FlurryAgent.logEvent("Download OK");
		}

	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter
	{

		public SectionsPagerAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int position)
		{
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class below).

			switch( position )
				{
					case 0:
						return TableFragment.getInstance();

					case 1:
						return AveragesFragment.getInstance();

					case 2:
						return ResultsFragment.getInstance();

					case 3:
						return FixturesFragment.getInstance();

					case 4:
						return HighsFragment.getInstance();
				}

			return null;
		}

		@Override
		public int getCount()
		{
			// Show 4 total pages.
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			Locale l = Locale.getDefault();
			switch( position )
			{
				case 0:
					return getString(R.string.title_section1);//.toUpperCase(l);
				case 1:
					return getString(R.string.title_section2);//.toUpperCase(l);
				case 2:
					return getString(R.string.title_section3);//.toUpperCase(l);
				case 3:
					return getString(R.string.title_section4);//.toUpperCase(l);
				case 4:
					return getString(R.string.title_section5);//.toUpperCase(l);
			}
			return null;
		}

        @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return POSITION_NONE;
        }
	}


}
