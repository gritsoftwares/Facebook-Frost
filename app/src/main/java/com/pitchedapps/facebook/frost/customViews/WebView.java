package com.pitchedapps.facebook.frost.customViews;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.pitchedapps.facebook.frost.utils.AnimUtils;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.pitchedapps.facebook.frost.webHelpers.WebThemer;

import im.delight.android.webview.AdvancedWebView;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public class WebView implements AdvancedWebView.Listener {



    //    https://touch.facebook.com/
    public enum FB {

        FEED("https://touch.facebook.com/");

        private String url;

        FB(String s) {
            url = s;
        }

        public String getLink() {
            return url;
        }
    }

    private AdvancedWebView mWebView;
    private SwipeRefreshLayout mRefresh;
    private FB mURL;
    private boolean firstRun = true;
    private Context mContext;

    public void url(FB s) {
        mURL = s;
    }

    public WebView(AdvancedWebView web, FB url, Activity activity) {
        mWebView = web;
        mWebView.setVisibility(View.INVISIBLE);
        mURL = url;
        mContext = activity;
        mWebView.setListener(activity, this);
        mWebView.loadUrl(mURL.getLink());

    }

    public static boolean exists() {
        return true;
    }

    public void addRefresher(SwipeRefreshLayout refresh) {
        mRefresh = refresh;
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.reload();
            }
        });
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {
        if (mRefresh != null) mRefresh.setRefreshing(false);
        if (firstRun) {
            firstRun = false;
            AnimUtils.circleReveal(mWebView, 0, 0, Utils.getScreenDiagonal(mContext), Utils.getScreenDiagonal(mContext));
        }
        WebThemer.injectTheme(mContext, mWebView);

        // http://stackoverflow.com/a/12039477/4407321
//        mWebView.setBackgroundColor(Color.TRANSPARENT);
        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
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
