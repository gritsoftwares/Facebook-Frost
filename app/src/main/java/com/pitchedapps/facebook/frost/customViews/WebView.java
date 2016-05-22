package com.pitchedapps.facebook.frost.customViews;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.pitchedapps.facebook.frost.MainActivity;
import com.pitchedapps.facebook.frost.enums.FBURL;
import com.pitchedapps.facebook.frost.interfaces.OnBackPressListener;
import com.pitchedapps.facebook.frost.utils.AnimUtils;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.pitchedapps.facebook.frost.webHelpers.FrostWebView;
import com.pitchedapps.facebook.frost.webHelpers.WebThemer;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public class WebView implements FrostWebView.Listener, OnBackPressListener {

    private FrostWebView mWebView;
    private SwipeRefreshLayout mRefresh;
    private FBURL mURL;
    private boolean firstRun = true, reload = false;
    private Activity mActivity;

    public WebView(FrostWebView web, FBURL url, Activity activity) {
        mWebView = web;
        mWebView.setVisibility(View.INVISIBLE);
        mURL = url;
        mActivity = activity;
        mWebView.setListener(activity, this);
        mWebView.loadUrl(mURL.getLink());

    }

    public void addRefresher(SwipeRefreshLayout refresh) {
        mRefresh = refresh;
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });
    }

    public void addBackListener() {
        ((MainActivity) mActivity).addOnBackPressedListener(this);
    }

    public void removeBackListener() {
        ((MainActivity) mActivity).removeOnBackPressedListener(this);
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
        AnimUtils.fadeOut(mActivity, mWebView, 0, 200);
        reload = true;
        mWebView.reload();
    }

    @Override
    public void onLoadResource(String url) {
        WebThemer.injectTheme(mActivity, mWebView);
//        e("URL " + mWebView.getUrl() + " " + url);
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

        // http://stackoverflow.com/a/12039477/4407321
        mWebView.setBackgroundColor(Color.TRANSPARENT);
//        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }


    @Override
    public void onPageFinished(String url) {
        if (mRefresh != null) mRefresh.setRefreshing(false);

//        WebThemer.injectTheme(mActivity, mWebView);


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

        if (mRefresh != null) mRefresh.setRefreshing(false);

    }

    @Override
    public void onDownloadRequested(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }


}
