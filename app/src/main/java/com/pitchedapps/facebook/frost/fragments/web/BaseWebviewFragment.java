package com.pitchedapps.facebook.frost.fragments.web;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.customViews.FullWebView;
import com.pitchedapps.facebook.frost.enums.FBURL;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public class BaseWebviewFragment extends Fragment {

    private FullWebView mWebView;

    public FBURL returnFBURL() {
        return FBURL.FEED;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_webview, container, false);

        mWebView = (FullWebView) rootView.findViewById(R.id.fragment_fullwebview);
        mWebView.initializeViews(returnFBURL(), getActivity());
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mWebView.onStop();
    }

    @Override
    public void onDestroy() {
        mWebView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
    }

}
