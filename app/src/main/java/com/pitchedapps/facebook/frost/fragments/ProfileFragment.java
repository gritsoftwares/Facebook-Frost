package com.pitchedapps.facebook.frost.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.adapters.EmptyAdapter;
import com.pitchedapps.facebook.frost.adapters.PostAdapter;
import com.pitchedapps.facebook.frost.exampleFragments.BaseFragment;
import com.pitchedapps.facebook.frost.utils.AnimUtils;
import com.pitchedapps.facebook.frost.utils.SharedObjects;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Post;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnPostsListener;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.sromku.simple.fb.utils.Attributes;
import com.sromku.simple.fb.utils.PictureAttributes;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

public class ProfileFragment extends BaseFragment {

    private final static String EXAMPLE = "Profile";
    private static final String BUNDLE_RECYCLER_LAYOUT = "ProfileFragment.recycler.layout";

    private SwipeRefreshLayout mRefresh;
    private RecyclerView mRV;
    private Context mContext;
    private Profile mProfile;
    private boolean firstRun = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mContext = getActivity();
        getViews(view);
        getData();


//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
//                null, getResources().getDimensionPixelSize(R.dimen.dividers_height), false, true));
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.setHasFixedSize(true);

        return view;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        getActivity().setTitle(EXAMPLE);
//    }

    /*
     * Saves recyclerview scroll position
     * http://stackoverflow.com/a/29166336/4407321
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mRV.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRV.getLayoutManager().onSaveInstanceState());
    }

    private void getViews(View view) {

        mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.profile_refresh);
        mRV = (RecyclerView) view.findViewById(R.id.profile_rv);
        LinearLayoutManager mLLM = new LinearLayoutManager(mContext);
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
        SimpleFacebook.getInstance().getPosts(Post.PostType.ALL, "message,story,type,id,full_picture,updated_time,actions,from,link,likes,comments{attachment},shares", new OnPostsListener() {

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
                updatePostContent(response);
            }
        });
    }

    private void updatePostContent(List<Post> response) {

        PostAdapter mAdapter = new PostAdapter(mContext, response, PostAdapter.PostType.PROFILE, mProfile);
        mRV.setAdapter(mAdapter);
        if (firstRun) {
//            AnimUtils.fadeIn(mContext, mRV, 0, 1000);
            AnimUtils.circleReveal(mRV, 0, 0, Utils.getScreenDiagonal(mContext), Utils.getScreenDiagonal(mContext));
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
}
