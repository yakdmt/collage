package com.stereo23.collage.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.stereo23.collage.MainActivity;
import com.stereo23.collage.R;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class DownloadTask extends AsyncTask<ArrayList<String>, String, Void> {
    String outputPath;
    ProgressDialog progress;
    Context context;
    public DownloadTaskResponse delegate;

    public DownloadTask (Context _context, DownloadTaskResponse _delegate){
        this.delegate = _delegate;
        this.context = _context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(context, context.getString(R.string.search),
                context.getString(R.string.please_wait), true);
        outputPath = context.getFilesDir().getAbsolutePath();

    }

    @Override
    protected Void doInBackground(ArrayList<String>... parameters) {
        int count;
        for (int i=0; i<parameters[0].toArray().length; i++){
            try {
                URL url = new URL(parameters[0].get(i));
                URLConnection connection = url.openConnection();
                connection.connect();
                int lengthOfFile = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/collage/"+ System.currentTimeMillis() + ".jpg");
                Log.d(MainActivity.TAG, Environment.getExternalStorageDirectory().getAbsolutePath());

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
                }
                catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }
            }
        return null;
    }
        @Override
        protected void onProgressUpdate(String... progressString) {
            progress.setMessage(progressString[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            delegate.onDownloadComplete();
            progress.dismiss();
        }

}
