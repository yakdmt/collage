package com.stereo23.collage;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.stereo23.collage.utilities.AuthWebViewClient;
import com.stereo23.collage.utilities.GetAccessTokenTask;

/**
 * Created by Username on 20.09.2014.
 */
public class AuthFragment extends Fragment {

    WebView webView;
    ProgressDialog progress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_auth, container, false);

        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        String authURLString = MainActivity.AUTHURL + "?client_id=" + getString(R.string.client_id) + "&redirect_uri=" + MainActivity.CALLBACKURL + "&response_type=code&display=touch&scope=likes+comments+relationships";
        String requestToken = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString("requestToken", "null");
        if (requestToken.equals("null")){
            progress = ProgressDialog.show(getActivity(), getString(R.string.loading),
                    getString(R.string.please_wait), true);
            webView = (WebView) getActivity().findViewById(R.id.webView);
            webView.setVisibility(View.VISIBLE);
            webView.setVerticalScrollBarEnabled(false);
            webView.setHorizontalScrollBarEnabled(false);
            webView.setWebViewClient(new AuthWebViewClient(getActivity(), progress));
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(authURLString);
        } else {
            new GetAccessTokenTask(getActivity()).execute();
        }
    }

}
