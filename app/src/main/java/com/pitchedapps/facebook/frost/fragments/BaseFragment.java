package com.pitchedapps.facebook.frost.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.pitchedapps.facebook.frost.MainActivity;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.adapters.BaseAdapter;
import com.pitchedapps.facebook.frost.adapters.EmptyAdapter;
import com.pitchedapps.facebook.frost.enums.FBURL;
import com.pitchedapps.facebook.frost.enums.FrostFragment;
import com.pitchedapps.facebook.frost.interfaces.OnTabIconPressListener;
import com.pitchedapps.facebook.frost.utils.AnimUtils;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.pitchedapps.facebook.frost.webHelpers.FrostWebView;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.actions.Cursor;

import java.util.List;
import java.util.Set;

public class BaseFragment<T> extends Fragment implements OnTabIconPressListener{

    public SwipeRefreshLayout mRefresh;
    public RecyclerView mRV;
    public Context mContext;
    public FrostWebView mWebView;
    public ViewGroup mViewGroup;
    public boolean firstRun = true;
    public boolean sdkVersion = true;
    public Cursor<List<T>> mCursor;
    public List<T> mResponse;
    public BaseAdapter<T> mAdapter;
    public LinearLayoutManager mLLM;
    public FBURL mURL;
//    public BaseFragmentActions mListener;
    public int mSingleLayoutID;
    public boolean loading = false;
    private int mTitleKey;
    public FrostPreferences fPrefs;

//    public interface BaseFragmentActions {
//        public void getData(SwipeRefreshLayout mRefresh, Cursor mCursor);
//    }

    public void initialize(FrostFragment frostFragment) {
        mURL = frostFragment.getFBURL();
        mSingleLayoutID = frostFragment.getSingleLayoutID();

        Set<String> grantedPermissions = SimpleFacebook.getInstance().getGrantedPermissions();
        for (Permission p : frostFragment.getSDKPermissions()) {
            if (!grantedPermissions.contains(p.getValue())) {
                sdkVersion = false;
                break;
            }
        }
        mTitleKey = frostFragment.getTabNameID();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generic, container, false);
        mContext = getActivity();
        fPrefs = new FrostPreferences(mContext);
        getViews(view);
        if (sdkVersion) {
            getData();
        } else if (mURL != null) {
//            mViewGroup = (ViewGroup) view.findViewById(R.id.generic_viewGroup);
//            mWebView = new FrostWebView(mContext);
//            mWebView.initializeViews(mURL, getActivity());
//            mViewGroup.removeView(mRefresh);
//            mViewGroup.addView(mWebView);
            Utils.showSimpleSnackbar(mContext, container, "Permissions not granted; using webview");
        } else {
            Utils.showSimpleSnackbar(mContext, container, "Permissions not granted; link not supplied");
        }


//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
//                null, getResources().getDimensionPixelSize(R.dimen.dividers_height), false, true));
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.setHasFixedSize(true);

        return view;
    }

    public void getData() {
//        if (mListener != null) {
//            mListener.getData(mRefresh, mCursor);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!sdkVersion) mWebView.onResume();
        ((MainActivity)mContext).addOnTabPressedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!sdkVersion) mWebView.onPause();
        ((MainActivity)mContext).removeOnTabPressedListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (!sdkVersion) mWebView.onStop();
        ((MainActivity)mContext).removeOnTabPressedListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!sdkVersion) mWebView.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (!sdkVersion) mWebView.onActivityResult(requestCode, resultCode, intent);
    }

    /*
     * Saves recyclerview scroll position
     * http://stackoverflow.com/a/29166336/4407321
     */
//    @Override //TODO see if necessary
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//
//        if (savedInstanceState != null && sdkVersion) {
//            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
//            mRV.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
//        }
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        if (mRV != null && sdkVersion) {
//            outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRV.getLayoutManager().onSaveInstanceState());
//        }
//    }

    private void getViews(View view) {

        mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.generic_refresh);
        mRV = (RecyclerView) view.findViewById(R.id.generic_rv);
        mLLM = new LinearLayoutManager(mContext);
        mLLM.setOrientation(LinearLayoutManager.VERTICAL);
        mRV.setLayoutManager(mLLM);
        mRV.setAdapter(new EmptyAdapter()); //Set empty adapter so error does not occur

        if (firstRun) {
            mRV.setVisibility(View.INVISIBLE);
        }

        //Refresh
//        mRefresh.setEnabled(true);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mResponse = null;
                getData();
            }
        });

    }


    public void updateContent(List<T> response) {
        if (mResponse == null) {
            mResponse = response;
            setAdapter();
        } else {
            Parcelable p = mLLM.onSaveInstanceState();
            mAdapter.addItems(response);
            mLLM.onRestoreInstanceState(p);
        }
        mRV.setAdapter(mAdapter);

        if (firstRun) {
//            AnimUtils.fadeIn(mContext, mRV, 0, 1000);
            AnimUtils.circleReveal(mContext, mRV, 0, 0, Utils.getScreenDiagonal(mContext));
            firstRun = false;
        }
    }

    public void setAdapter() {
        mAdapter = new BaseAdapter<T>(mContext, mResponse, mCursor, mSingleLayoutID);
    }

    @Override
    public boolean tabIconPressed(int position) {
        if (FrostFragment.isVisiblePosition(position, mTitleKey)) {
            int i = mLLM.findFirstVisibleItemPosition();
            if (i < 10) {
                mRV.smoothScrollToPosition(0);
            } else {
                AnimUtils.fadeOut(mContext, mRV, 0, 300, new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mRV.scrollToPosition(0);
                        AnimUtils.fadeIn(mContext, mRV, 0, 300);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        }
        return false;
    }
}
