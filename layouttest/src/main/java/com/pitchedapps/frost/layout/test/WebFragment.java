package com.pitchedapps.frost.layout.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Allan Wang on 2016-05-24.
 */
public class WebFragment extends Fragment {

    private WebView mWeb;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_feed, container, false);

        mWeb = (WebView) rootView.findViewById(R.id.news_feed_webview);
//        mWebView.initializeViews(FBURL.FEED, getActivity());
////        mFab = (FloatingActionButton) rootView.findViewById(R.id.news_feed_fab);
////        mFab.hide(false);
//        mWebView.setOnScrollChangedCallback(new FullWebView.OnScrollChangedCallback() {
//            @Override
//            public void onScroll(int l, int t) {
//                e("scroll " + l + " " + t);
//            }
//        });
        mWeb.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
//                Log.i(TAG, "Finished loading URL: " +url);
//                if (progressBar.isShowing()) {
//                    progressBar.dismiss();
//                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                Log.e(TAG, "Error: " + description);
//                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
//                alertDialog.setTitle("Error");
//                alertDialog.setMessage(description);
//                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        return;
//                    }
//                });
//                alertDialog.show();
            }
        });
        mWeb.loadUrl("http://www.google.com");

        return rootView;
    }
}
