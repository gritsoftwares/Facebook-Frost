package com.pitchedapps.facebook.frost.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.adapters.EmptyAdapter;
import com.pitchedapps.facebook.frost.adapters.PostAdapter;
import com.pitchedapps.facebook.frost.customViews.FullWebView;
import com.pitchedapps.facebook.frost.customViews.HeaderProfile;
import com.pitchedapps.facebook.frost.customViews.PostCard;
import com.pitchedapps.facebook.frost.enums.FBURL;
import com.pitchedapps.facebook.frost.enums.PostHeader;
import com.pitchedapps.facebook.frost.exampleFragments.BaseFragment;
import com.pitchedapps.facebook.frost.utils.AnimUtils;
import com.pitchedapps.facebook.frost.utils.SharedObjects;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.actions.Cursor;
import com.sromku.simple.fb.entities.Post;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnPostsListener;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.sromku.simple.fb.utils.Attributes;
import com.sromku.simple.fb.utils.PictureAttributes;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

public class ProfileFragment extends BaseFragment {

    private final static String EXAMPLE = "Profile";
    private static final String BUNDLE_RECYCLER_LAYOUT = "ProfileFragment.recycler.layout";
    private static final String FEED_QUERY =
            "message,story,type,id,full_picture,updated_time,actions,from,link,likes.limit(25).summary(true){pic_square},comments.limit(25).summary(true){attachment},shares";

    private SwipeRefreshLayout mRefresh;
    private RecyclerView mRV;
    private Context mContext;
    private FullWebView mWebView;
    private Profile mProfile;
    private ViewGroup mViewGroup;
    private boolean firstRun = true;
    private boolean sdkVersion = true;
    private Cursor<List<Post>> mCursor;
    private List<Post> mResponse;
    private PostAdapter mAdapter;
    private LinearLayoutManager mLLM;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mContext = getActivity();
        Set<String> grantedPermissions = SimpleFacebook.getInstance().getGrantedPermissions();
        getViews(view);
        if (grantedPermissions.contains(Permission.USER_POSTS.getValue()) && grantedPermissions.contains(Permission.USER_ABOUT_ME.getValue())) {
            getData();
        } else {
            sdkVersion = false;
            mViewGroup = (ViewGroup) view.findViewById(R.id.profile_viewGroup);
            mWebView = new FullWebView(mContext);
            mWebView.initializeViews(FBURL.PROFILE, getActivity());
            mViewGroup.removeView(mRefresh);
            mViewGroup.addView(mWebView);
            Utils.showSimpleSnackbar(mContext, container, "Permissions not granted; using webview");
        }


//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
//                null, getResources().getDimensionPixelSize(R.dimen.dividers_height), false, true));
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!sdkVersion) mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!sdkVersion) mWebView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!sdkVersion) mWebView.onStop();
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
    @Override //TODO see if necessary
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null && sdkVersion) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mRV.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRV != null && sdkVersion) {
            outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRV.getLayoutManager().onSaveInstanceState());
        }
    }

    private void getViews(View view) {

        mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.profile_refresh);
        mRV = (RecyclerView) view.findViewById(R.id.profile_rv);
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
        PictureAttributes pictureAttributes = Attributes.createPictureAttributes();
        pictureAttributes.setHeight(500);
        pictureAttributes.setWidth(500);
        pictureAttributes.setType(PictureAttributes.PictureType.SQUARE);
        Profile.Properties properties = new Profile.Properties.Builder()
                .add(Profile.Properties.PICTURE, pictureAttributes)
                .add(Profile.Properties.AGE_RANGE)
                .add(Profile.Properties.BIRTHDAY)
                .add(Profile.Properties.BIO)
                .add(Profile.Properties.COVER)
//                .add(Profile.Properties.CURRENCY)
//                .add(Profile.Properties.DEVICES)
                .add(Profile.Properties.EDUCATION)
                .add(Profile.Properties.EMAIL)
                .add(Profile.Properties.FAVORITE_TEAMS)
                .add(Profile.Properties.FAVORITE_ATHLETES)
                .add(Profile.Properties.FIRST_NAME)
                .add(Profile.Properties.GENDER)
                .add(Profile.Properties.HOMETOWN)
                .add(Profile.Properties.INTERESTED_IN)
                .add(Profile.Properties.ID)
                .add(Profile.Properties.INSTALLED)
                .add(Profile.Properties.LANGUAGE)
                .add(Profile.Properties.LOCALE)
                .add(Profile.Properties.LOCATION)
                .add(Profile.Properties.LAST_NAME)
                .add(Profile.Properties.LINK)
                .add(Profile.Properties.MIDDLE_NAME)
                .add(Profile.Properties.NAME)
                .add(Profile.Properties.POLITICAL)
                .add(Profile.Properties.QUOTES)
                .add(Profile.Properties.RELATIONSHIP_STATUS)
                .add(Profile.Properties.RELIGION)
                .add(Profile.Properties.TIMEZONE)
                .add(Profile.Properties.UPDATED_TIME)
                .add(Profile.Properties.VERIFIED)
                .add(Profile.Properties.WEBSITE)
                .add(Profile.Properties.WORK)
                .build();

        SimpleFacebook.getInstance().getProfile(properties, new OnProfileListener() {

            @Override
            public void onThinking() {
                mRefresh.setRefreshing(true);
            }

            @Override
            public void onException(Throwable throwable) {
//                mRefresh.setRefreshing(false);
                Utils.showSimpleSnackbar(mContext, mRefresh, throwable.getMessage());
                getTimeline();
            }

            @Override
            public void onFail(String reason) {
//                mRefresh.setRefreshing(false);
                Utils.showSimpleSnackbar(mContext, mRefresh, reason);
                getTimeline();
            }

            @Override
            public void onComplete(Profile response) {
                mRefresh.setRefreshing(false);
                mProfile = response;
                SharedObjects.saveProfile(response);
                getTimeline();
//                updateProfileContent(response);
            }
        });


    }

    public void getTimeline() {
        SimpleFacebook.getInstance().getPosts(Post.PostType.ALL, FEED_QUERY, 25, new OnPostsListener() {

//            @Override
//            public void onThinking() {
//                mRefresh.setRefreshing(true);
//            }

            @Override
            public void onException(Throwable throwable) {
                mRefresh.setRefreshing(false);
                Utils.showSimpleSnackbar(mContext, mRefresh, throwable.getMessage());
            }

            @Override
            public void onFail(String reason) {
                mRefresh.setRefreshing(false);
                Utils.showSimpleSnackbar(mContext, mRefresh, reason);
            }

            @Override
            public void onComplete(List<Post> response) {
                mRefresh.setRefreshing(false);
                mCursor = getCursor();
                updatePostContent(response);
            }
        });
    }

    private void updatePostContent(List<Post> response) {
        if (mResponse == null) {
            mResponse = response;
            mAdapter = new PostAdapter(mContext, mResponse, PostHeader.PROFILE, mProfile, mCursor);
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


            mAdapter.addPosts(response);

            mLLM.onRestoreInstanceState(p);
//            mLLM.scrollToPosition(prevLastPost);
//            mLLM.scrollToPositionWithOffset(prevLastPost, 100);

//            mResponse.addAll(response);
        }
        mRV.setAdapter(mAdapter);

        if (firstRun) {
//            AnimUtils.fadeIn(mContext, mRV, 0, 1000);
            AnimUtils.circleReveal(mContext, mRV, 0, 0, Utils.getScreenDiagonal(mContext));
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

    private void printProfileData(Profile response) {
        try {
            for (Field f : response.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (Modifier.isStatic(f.getModifiers())) {
                    continue;
                }
                e(f.getName() + " " + f.get(response));
            }
        } catch (IllegalAccessException e) {
            //do nothing
        }
    }

    public void scrollToTop() {
        e("GGG");
        if (mLLM == null) return;
        e("HHH");
        mLLM.scrollToPosition(0);
//        mLLM.smoothScrollToPosition(mRV, RecyclerView.State, 0);
    }


}
