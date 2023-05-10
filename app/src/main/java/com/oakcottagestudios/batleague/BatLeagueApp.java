package com.oakcottagestudios.batleague;

import android.app.Application;
import android.content.pm.PackageInfo;

//import com.flurry.android.FlurryAgent;

/**
 * Created by Doug on 08/06/2015.
 */
public class BatLeagueApp extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        // configure Flurry
       // FlurryAgent.setLogEnabled(true);

        try
        {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            //FlurryAgent.setVersionName(pInfo.versionName);
        }
        catch(Exception e){}

        // init Flurry
       // FlurryAgent.init(this, "3W5VZBTVFVJZKWP7B9K8");
    }
}
