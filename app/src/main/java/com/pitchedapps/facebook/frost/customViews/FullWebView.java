package com.pitchedapps.facebook.frost.customViews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.pitchedapps.facebook.frost.MainActivity;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.enums.FBURL;
import com.pitchedapps.facebook.frost.interfaces.OnBackPressListener;
import com.pitchedapps.facebook.frost.utils.AnimUtils;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.pitchedapps.facebook.frost.webHelpers.FrostWebView;
import com.pitchedapps.facebook.frost.webHelpers.WebThemer;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public class FullWebView extends FrameLayout implements FrostWebView.Listener, OnBackPressListener {

    private FrostWebView mWebView;
    private SwipeRefreshLayout mRefresh;
    private FBURL mURL;
    private boolean firstRun = true, reload = false;
    private Activity mActivity;
    private FrostPreferences fPrefs;
//    private OnScrollChangedCallback mOnScrollChangedCallback;
//    private Context mContext;

    public FullWebView(Context c) {
        super(c);
//        mContext = c;
    }

    public FullWebView(Context c, AttributeSet attrs) {
        super(c, attrs);
//        mContext = c;
    }

    public FullWebView(Context c, AttributeSet attrs, int defStyle) {
        super(c, attrs, defStyle);
//        mContext = c;
    }

    public void initializeViews(FBURL url, Activity activity) {
//        fPrefs = new FrostPreferences(mContext);
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.webview_advanced, this);

        fPrefs = new FrostPreferences(activity);
        mURL = url;
        mActivity = activity;
        mRefresh = (SwipeRefreshLayout) findViewById(R.id.webview_refresh);
        mWebView = (FrostWebView) findViewById(R.id.webview_container);
        mWebView.setListener(activity, this);
        mWebView.loadUrl(mURL.getLink());
        mRefresh.setRefreshing(true);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });
//        mText.setTextColor(fPrefs.getTextColor());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        mWebView.onActivityResult(requestCode, resultCode, intent);
    }

    public FrostWebView getFrostWebView() {
        return mWebView;
    }

    public SwipeRefreshLayout getRefreshLayout() {
        return mRefresh;
    }

    public void refreshOn() {
        mRefresh.setRefreshing(true);
    }

    public void refreshOff() {
        mRefresh.setRefreshing(false);
    }

    public void onResume() {
        ((MainActivity) mActivity).addOnBackPressedListener(this);
        mWebView.onResume();
    }

    public void onPause() {
        ((MainActivity) mActivity).removeOnBackPressedListener(this);
        mWebView.onPause();
    }

    public void onStop() {
        ((MainActivity) mActivity).removeOnBackPressedListener(this);
    }

    public void onDestroy() {
        mWebView.onDestroy();
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

    /*
     * Scroll change listener
     * http://stackoverflow.com/a/14753235/4407321
     */

//    @Override
//    protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt)
//    {
//        super.onScrollChanged(l, t, oldl, oldt);
//        e("SSSS " + l + " " + t);
//        if(mOnScrollChangedCallback != null) mOnScrollChangedCallback.onScroll(l, t);
//    }
//
//    public OnScrollChangedCallback getOnScrollChangedCallback()
//    {
//        return mOnScrollChangedCallback;
//    }
//
//    public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback)
//    {
//        mOnScrollChangedCallback = onScrollChangedCallback;
//    }
//
//    /**
//     * Implement in the activity/fragment/view that you want to listen to the webview
//     */
//    public interface OnScrollChangedCallback
//    {
//        public void onScroll(int l, int t);
//    }


}
