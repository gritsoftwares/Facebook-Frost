package com.pitchedapps.facebook.frost.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.customViews.WebView;
import com.pitchedapps.facebook.frost.enums.FBURL;

import im.delight.android.webview.AdvancedWebView;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public class NewsFeedFragment extends Fragment {

    private AdvancedWebView mWebView;
    private SwipeRefreshLayout mRefresh;
    private WebView mWeb;

    public NewsFeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = inflater.inflate(R.layout.webview_advanced, container, false);

        mWebView = (AdvancedWebView) rootView.findViewById(R.id.webview_container);
        mRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.webview_refresh);

//        if (mWeb == null) {
//        e("HERE");
            mWeb = new WebView(mWebView, FBURL.FEED, getActivity());
            mWeb.addRefresher(mRefresh);

//        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
        mWeb.addBackListener();
    }

    @Override
    public void onPause() {
        mWebView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mWeb.removeBackListener();
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
