package com.stereo23.collage;

import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends ActionBarActivity {
    public static final String AUTHURL = "https://api.instagram.com/oauth/authorize/";
    public static final String TOKENURL = "https://api.instagram.com/oauth/access_token";
    public static final String APIURL = "https://api.instagram.com/v1";
    public static String CALLBACKURL = "http://none.com";
    public static String REQUESTURL = "/users/self/feed/?access_token=";
    public static final String TAG = "collageApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            String accessToken =
                    PreferenceManager.getDefaultSharedPreferences(this)
                            .getString("accessToken","null");
            if(accessToken.equals("null")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new AuthFragment())
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new MainFragment())
                        .commit();
            }
        }
        File folder = new File(Environment.getExternalStorageDirectory() + "/collage/");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            Log.d(MainActivity.TAG,"directory created successfully");
        } else {
            Log.d(MainActivity.TAG,"directory not created");
        }
        Log.d(MainActivity.TAG, Environment.getExternalStorageState());
        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Toast.makeText(MainActivity.this, Environment.getExternalStorageState(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
