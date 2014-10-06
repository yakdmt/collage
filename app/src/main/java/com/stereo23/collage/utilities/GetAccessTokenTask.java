package com.stereo23.collage.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.stereo23.collage.MainActivity;
import com.stereo23.collage.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class GetAccessTokenTask extends AsyncTask<Object, Void, String> {

    Context context;
    String tokenURLString;
    ProgressDialog progress;

    public GetAccessTokenTask(Context _context) {
        this.context = _context;
        this.tokenURLString = MainActivity.TOKENURL + "?client_id=" +
                context.getString(R.string.client_id) +
                "&client_secret=" + context.getString(R.string.client_secret) + "&redirect_uri=" +
                MainActivity.CALLBACKURL + "&grant_type=authorization_code";
    }

    @Override
    protected void onPreExecute(){
        progress = ProgressDialog.show(context, context.getString(R.string.loading),
                context.getString(R.string.please_wait), true);
    }

    @Override
    protected String doInBackground(Object[] params) {
        String accessToken = PreferenceManager.getDefaultSharedPreferences(context).getString("accessToken", "null");
        if (accessToken.equals("null")){
            try {
                URL url = new URL(tokenURLString);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.setDoOutput(true);
                OutputStreamWriter outputStreamWriter =
                        new OutputStreamWriter(httpsURLConnection.getOutputStream());
                String requestToken = PreferenceManager.getDefaultSharedPreferences(context).
                        getString("requestToken", "null");
                outputStreamWriter.write("client_id="+context.getString(R.string.client_id)+
                        "&client_secret="+ context.getString(R.string.client_secret) +
                        "&grant_type=authorization_code" +
                        "&redirect_uri="+MainActivity.CALLBACKURL+
                        "&code=" + requestToken);
                outputStreamWriter.flush();
                String response = StaticMethods.streamToString(httpsURLConnection.getInputStream());
                JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                accessToken = jsonObject.getString("access_token"); //Here is your ACCESS TOKEN
                PreferenceManager.getDefaultSharedPreferences(context).edit().
                        putString("accessToken", accessToken).commit();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return accessToken;

    }

    @Override
    protected void onPostExecute(String accessToken){
        progress.dismiss();
        Toast.makeText(context,accessToken,Toast.LENGTH_SHORT).show();
        if (!accessToken.equals("null")){
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("accessToken",accessToken);
            context.startActivity(intent);
        }

    }
}
