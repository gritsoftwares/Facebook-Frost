package com.pitchedapps.facebook.frost.fragments.web;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.pitchedapps.facebook.frost.MainActivity;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.enums.FBURL;
import com.pitchedapps.facebook.frost.interfaces.OnBackPressListener;
import com.pitchedapps.facebook.frost.utils.AnimUtils;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.pitchedapps.facebook.frost.webHelpers.FrostWebView;
import com.pitchedapps.facebook.frost.webHelpers.WebThemer;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public class BaseWebviewFragment extends Fragment implements FrostWebView.Listener, OnBackPressListener {

    private FrostWebView mWebView;
    private SwipeRefreshLayout mRefresh;
    private FBURL mURL = null;
    private boolean firstRun = true, reload = false;
    private Activity mActivity;
    private int scrollPosition = 0;

    public FBURL returnFBURL() {
        if (mURL != null) return mURL;
        return FBURL.FEED;
    }

    public BaseWebviewFragment setURL(FBURL url) {
        mURL = url;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_webview, container, false);
        int position = FragmentPagerItem.getPosition(getArguments());
        mActivity = getActivity();
        initializeViews(rootView);
        return rootView;
    }

    public void initializeViews(View v) {
        mURL = returnFBURL();
        mRefresh = (SwipeRefreshLayout) v.findViewById(R.id.webview_refresh);
        mWebView = (FrostWebView) v.findViewById(R.id.webview_container);
        mWebView.setListener(mActivity, this);
        mWebView.loadUrl(mURL.getLink());
        mRefresh.setRefreshing(true);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });
        mWebView.setOnScrollChangedCallback(new FrostWebView.OnScrollChangedCallback() {
            @Override
            public void onScroll(int l, int t) {
                scrollPosition = t;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) mActivity).addOnBackPressedListener(this);
        mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity) mActivity).removeOnBackPressedListener(this);
        mWebView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity) mActivity).removeOnBackPressedListener(this);
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

    @Override
    public boolean backPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

    private void reload() {
        mRefresh.setRefreshing(true);
        AnimUtils.fadeOut(mActivity, mWebView, 0, 200);
        reload = true;
//        t(mActivity, "RELOADING");
        mWebView.reload();
    }

    @Override
    public void onLoadResource(String url) {
//        mRefresh.setRefreshing(true);
        WebThemer.injectTheme(mActivity, mWebView);
//        e("URL " + mWebView.getUrl() + " " + url);
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

        // http://stackoverflow.com/a/12039477/4407321
        mWebView.setBackgroundColor(Color.TRANSPARENT);
//        mWebView.setDrawingCacheBackgroundColor(0x00000000);
//        mWebView.setBackgroundColor(fPrefs.getBackgroundColor());
//        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, new Paint(0x00000000));
        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }


    @Override
    public void onPageFinished(String url) {
        mRefresh.setRefreshing(false);

//        WebThemer.injectTheme(mActivity, mWebView);

        mWebView.setBackgroundColor(Color.TRANSPARENT);

        if (firstRun || reload) {
            firstRun = false;
            reload = false;
//            final Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
            AnimUtils.circleReveal(mActivity, mWebView, 0, 0, Utils.getScreenDiagonal(mActivity));
//                }
//            }, 300); //TODO fix theme delay
        }


    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        mRefresh.setRefreshing(false);
    }

    @Override
    public void onDownloadRequested(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }

}
