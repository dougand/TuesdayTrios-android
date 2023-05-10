package com.oakcottagestudios.batleague;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Button;

//import com.flurry.android.FlurryAgent;
import com.oakcottagestudios.batleague.averages.LeagueAverages;
import com.oakcottagestudios.batleague.averages.TeamAverage;

import java.util.List;
import java.util.prefs.Preferences;

/**
 * Created by Doug on 26/05/2015.
 */
public class UserSettings extends PreferenceActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	    // Display the fragment as the main content.
	    FragmentManager mFragmentManager = getFragmentManager();
	    FragmentTransaction mFragmentTransaction = mFragmentManager
			    .beginTransaction();
	    PrefsFragment mPrefsFragment = new PrefsFragment();
	    mFragmentTransaction.replace(android.R.id.content, mPrefsFragment);
	    mFragmentTransaction.commit();
    }

	@Override
    protected void onStart()
    {
        super.onStart();
        //FlurryAgent.onStartSession(this);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        //FlurryAgent.onEndSession(this);
    }

	@Override
	public boolean isValidFragment( String fragmentName )
	{
		return true;
	}


	/**
	 * This fragment shows the preferences for the first header.
	 */
	public static class PrefsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
	{
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.settings);

			rebuildView();
		}

		void rebuildView()
		{

	        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
	        String myTeam = prefs.getString("prefTeamName", "None");
	        String myPlayer = prefs.getString("prefPlayerName", "None" );

	        ListPreference prefTeams = (ListPreference)findPreference("prefTeamName");
	        ListPreference prefPlayers = (ListPreference)findPreference("prefPlayerName");

	        LeagueAverages averages = LeagueAverages.getInstance();

			prefPlayers.setSummary( myPlayer );

	        String[] teamnames = new String[averages.numTeams+1];
	        teamnames[0]="None";
			for( int t=0; t<averages.numTeams; t++ )
	        {
	            TeamAverage team = averages.teamAverages.elementAt(t);
	            teamnames[t + 1] = team.teamName;

	            if( myTeam.contentEquals( team.teamName))
	            {
	                int numPlayers = team.teamMembers.size();
	                String[] playernames = new String[numPlayers+1];
	                playernames[0]="None";
	                for( int p=0; p<numPlayers; p++)
	                {
	                    playernames[p+1] = team.teamMembers.elementAt(p).prefName;
	                }
	                prefPlayers.setEntries(playernames );
	                prefPlayers.setEntryValues( playernames );
	            }
	        }
	        prefTeams.setEntries( teamnames );
	        prefTeams.setEntryValues( teamnames );

		}


		//@override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
		{
			rebuildView();
		}


		@Override
		public void onResume() {
			super.onResume();
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
			prefs.registerOnSharedPreferenceChangeListener(this);
		}

		@Override
		public void onPause() {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
			prefs.unregisterOnSharedPreferenceChangeListener(this);
			super.onPause();
		}

	}




}
