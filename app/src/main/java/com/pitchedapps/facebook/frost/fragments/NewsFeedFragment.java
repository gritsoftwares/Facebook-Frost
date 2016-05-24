package com.pitchedapps.facebook.frost.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.clans.fab.FloatingActionButton;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.customViews.FullWebView;
import com.pitchedapps.facebook.frost.enums.FBURL;
import com.pitchedapps.facebook.frost.webHelpers.FrostWebView;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public class NewsFeedFragment extends Fragment implements FullWebView.OnScrollChangedCallback{

    private FullWebView mWebView;
    private FloatingActionButton mFab;

    public NewsFeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_feed, container, false);

        mWebView = (FullWebView) rootView.findViewById(R.id.news_feed_webview);
        mWebView.initializeViews(FBURL.FEED, getActivity());
        mFab = (FloatingActionButton) rootView.findViewById(R.id.news_feed_fab);
//        mFab.hide(false);
        mWebView.setOnScrollChangedCallback(new FullWebView.OnScrollChangedCallback() {
            @Override
            public void onScroll(int l, int t) {
                e("scroll " + l + " " + t);
            }
        });

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

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mFab.hide(false);
//    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onScroll(int l, int t) {
        e("scroll " + l + " " + t);

    }
}
