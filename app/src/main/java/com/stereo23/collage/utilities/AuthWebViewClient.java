package com.stereo23.collage.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.stereo23.collage.MainActivity;


public class AuthWebViewClient extends WebViewClient {

    Context context;
    ProgressDialog progress;

    public AuthWebViewClient(Context _context, ProgressDialog _progress){
        this.context = _context;
        this.progress = _progress;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        progress.dismiss();
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith(MainActivity.CALLBACKURL)) {
            String parts[] = url.split("=");
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putString("requestToken", parts[1]).commit();
            view.setVisibility(View.GONE);
            progress.dismiss();
            new GetAccessTokenTask(context).execute();
        }
        return false;
    }
}
