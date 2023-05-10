package com.oakcottagestudios.batleague;

import android.content.pm.PackageInfo;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

//import com.flurry.android.FlurryAgent;


public class About extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        try
        {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

            TextView version = (TextView) findViewById(R.id.textView_aboutAppVersion);
            version.setText("Version "+pInfo.versionName);

        }
        catch( Exception e )
        {
            Log.e("ABOUT", "Exception: "+e );
        }




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
       // FlurryAgent.onEndSession(this);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_about, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings)
//        {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
