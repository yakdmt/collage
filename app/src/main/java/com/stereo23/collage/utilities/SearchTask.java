package com.stereo23.collage.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.stereo23.collage.MainActivity;
import com.stereo23.collage.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SearchTask extends AsyncTask<Object, Void, ArrayList<URL>>  {
    ProgressDialog progress;
    Context context;
    public SearchTaskResponse delegate;

    public SearchTask (Context _context, SearchTaskResponse _delegate) {
        this.context = _context;
        this.delegate = _delegate;
    }
    @Override
    protected void onPreExecute(){
        progress = ProgressDialog.show(context, context.getString(R.string.search),
                context.getString(R.string.please_wait), true);
    }


    @Override
    protected ArrayList<URL> doInBackground(Object[] params) {
        String searchKey = params[0].toString();
        String accessToken = PreferenceManager.getDefaultSharedPreferences(context).getString("accessToken", "null");
        String urlString = MainActivity.APIURL + "/users/search?access_token="+accessToken+"&q="+ searchKey;
        String id = null;
        String username = null;
        ArrayList<URL> resultURLList = null;
        try {
            URL url = new URL(urlString);
            InputStream inputStream = url.openConnection().getInputStream();
            String response = StaticMethods.streamToString(inputStream);
            Log.d(MainActivity.TAG,response);
            JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            if (jsonArray.length()>0) {
                JSONObject exactlyThisUser = jsonArray.getJSONObject(0);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject user = jsonArray.getJSONObject(i);
                    if (user.getString("username").equals(searchKey)) {
                        exactlyThisUser = user;
                    }
                }
                id = exactlyThisUser.getString("id");
                username = exactlyThisUser.getString("username");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (id!=null){
            urlString = MainActivity.APIURL + "/users/"+id+"/media/recent?access_token="+accessToken;
            try {
                URL url = new URL(urlString);
                InputStream inputStream = url.openConnection().getInputStream();
                String response = StaticMethods.streamToString(inputStream);
                Log.d(MainActivity.TAG,response);
                JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                JSONArray jsonImagesArray = jsonObject.getJSONArray("data");
                resultURLList = StaticMethods.parseImagesJSON(jsonImagesArray);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultURLList;
    }

    @Override
    protected void onPostExecute(ArrayList<URL> result){
        progress.dismiss();
        delegate.processFinish(result);
    }
}
