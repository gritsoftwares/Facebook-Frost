package com.pitchedapps.facebook.frost.fragments;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.adapters.EmptyAdapter;
import com.pitchedapps.facebook.frost.adapters.EventAdapter;
import com.pitchedapps.facebook.frost.adapters.PostAdapter;
import com.pitchedapps.facebook.frost.customViews.FullWebView;
import com.pitchedapps.facebook.frost.enums.FBURL;
import com.pitchedapps.facebook.frost.enums.PostHeader;
import com.pitchedapps.facebook.frost.exampleFragments.BaseFragment;
import com.pitchedapps.facebook.frost.utils.AnimUtils;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.actions.Cursor;
import com.sromku.simple.fb.entities.Event;
import com.sromku.simple.fb.entities.Event.EventDecision;
import com.sromku.simple.fb.entities.Post;
import com.sromku.simple.fb.listeners.OnEventsListener;
import com.sromku.simple.fb.utils.Utils;

import java.util.List;
import java.util.Set;

public class EventsFragment extends BaseFragment {

    private final static String EXAMPLE = "Get events";

    private Context mContext;
    private TextView mResult;
    private Button mGetButton;
    private ViewGroup mViewGroup;
    private boolean firstRun = true;
    private boolean sdkVersion = true;
    private TextView mMore;
    private String mAllPages = "";
    private Cursor<List<Event>> mCursor;
    private List<Event> mResponse;
    private EventAdapter mAdapter;
    private LinearLayoutManager mLLM;
    private SwipeRefreshLayout mRefresh;
    private RecyclerView mRV;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        mContext = getActivity();
        Set<String> grantedPermissions = SimpleFacebook.getInstance().getGrantedPermissions();
        getViews(view);
//        if (grantedPermissions.contains(Permission.USER_POSTS.getValue()) && grantedPermissions.contains(Permission.USER_EVENTS.getValue())) {
            getData();
//        } else {
//            sdkVersion = false;
//            mViewGroup = (ViewGroup) view.findViewById(R.id.event_viewGroup);
//            mWebView = new FullWebView(mContext);
//            mWebView.initializeViews(FBURL.PROFILE, getActivity());
//            mViewGroup.removeView(mRefresh);
//            mViewGroup.addView(mWebView);
//            com.pitchedapps.facebook.frost.utils.Utils.showSimpleSnackbar(mContext, container, "Permissions not granted; using webview");
//        }

        mGetButton = (Button) view.findViewById(R.id.button);
        mGetButton.setText(EXAMPLE);
        mGetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAllPages = "";
                mResult.setText(mAllPages);


            }
        });
        return view;
    }

    private void getViews(View view) {

        mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.event_refresh);
        mRV = (RecyclerView) view.findViewById(R.id.event_rv);
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

    private void getData() {
        SimpleFacebook.getInstance().getEvents(EventDecision.ATTENDING, new OnEventsListener() {

            @Override
            public void onThinking() {
                mRefresh.setRefreshing(true);
            }

            @Override
            public void onException(Throwable throwable) {
                com.pitchedapps.facebook.frost.utils.Utils.showSimpleSnackbar(mContext, mRefresh, throwable.getMessage());
            }

            @Override
            public void onFail(String reason) {
               com.pitchedapps.facebook.frost.utils.Utils.showSimpleSnackbar(mContext, mRefresh, reason);
            }

            @Override
            public void onComplete(List<Event> response) {
                mRefresh.setRefreshing(false);
                mCursor = getCursor();
                updateContent(response);
            }
        });
    }

    private void updateContent(List<Event> response) {
        if (mResponse == null) {
            mResponse = response;
            mAdapter = new EventAdapter(mContext, mResponse);
        } else {
//            int prevLastPost = mAdapter.getItemCount()-1;

//            LinearLayoutManager manager = (LinearLayoutManager) mRecycler.getLayoutManager();
            Parcelable p = mLLM.onSaveInstanceState();
//            int firstItem = mLLM.findFirstVisibleItemPosition();
//            View firstItemView = mLLM.findViewByPosition(firstItem);
//            float topOffset = firstItemView.getTop();
//
//            outState.putInt(ARGS_SCROLL_POS, firstItem);
//            outState.putFloat(ARGS_SCROLL_OFFSET, topOffset);


            mAdapter.addEvents(response);

            mLLM.onRestoreInstanceState(p);
//            mLLM.scrollToPosition(prevLastPost);
//            mLLM.scrollToPositionWithOffset(prevLastPost, 100);

//            mResponse.addAll(response);
        }
        mRV.setAdapter(mAdapter);

        if (firstRun) {
//            AnimUtils.fadeIn(mContext, mRV, 0, 1000);
            AnimUtils.circleReveal(mContext, mRV, 0, 0, com.pitchedapps.facebook.frost.utils.Utils.getScreenDiagonal(mContext));
            firstRun = false;
        }
//        e("DI " + response.get(0).getMessage() + " " + response.get(0).getId() + " " + response.get(0).getObjectId());

//        String mAllPages = "";
//        // make the result more readable.
////        mAllPages += "<u>\u25B7\u25B7\u25B7 (paging) #" + getPageNum() + " \u25C1\u25C1\u25C1</u><br>";
//        mAllPages += com.sromku.simple.fb.utils.Utils.join(response.iterator(), "<br>", new com.sromku.simple.fb.utils.Utils.Process<Post>() {
//            @Override
//            public String process(Post post) {
//                e("Post " + post.getId() + " " + post.getPicture() + " " + post.getType() + " " + post.getProperties() + " " + post.getLink());
//                return "\u25CF " + (post.getMessage() == null || "null".equalsIgnoreCase(post.getMessage()) ? post.getId() : post.getMessage()) + " \u25CF";
//            }
//        });
//        mAllPages += "<br>";
//        mResult.setText(Html.fromHtml(mAllPages));
//        mResult.setTextColor(0xff000000);

        // check if more pages exist
//        if (hasNext()) {
//            enableLoadMore(getCursor());
//        } else {
//            disableLoadMore();
//        }
    }

}
