package com.pitchedapps.facebook.frost.fragments;

import android.os.Bundle;

import com.pitchedapps.facebook.frost.adapters.BaseAdapter;
import com.pitchedapps.facebook.frost.enums.FrostFragment;
import com.pitchedapps.facebook.frost.enums.PostHeader;
import com.pitchedapps.facebook.frost.utils.FacebookUtils;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Post;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnPostsListener;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.sromku.simple.fb.utils.Attributes;
import com.sromku.simple.fb.utils.PictureAttributes;

import java.util.List;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

public class ProfileFragment extends BaseFragment<Post> {

    private static final String FEED_QUERY =
            "message,story,type,id,full_picture,updated_time,actions,from,link,likes.summary(true).limit(25){pic_square,name},comments.summary(true).limit(25).order(reverse_chronological){attachment,message,can_comment,can_like,comment_count,created_time,from,like_count},shares";

    private Profile mProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
        initialize(FrostFragment.PROFILE);
    }

    @Override
    public void getData() {
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
                showError(throwable.getMessage());
                getTimeline();
            }

            @Override
            public void onFail(String reason) {
                showError(reason);
                getTimeline();
            }

            @Override
            public void onComplete(Profile response) {
                mProfile = response;
                FacebookUtils.saveProfile(response);
                getTimeline();
            }
        });
    }

    private void showError(String s) {
        e("Profile Fragment Error: " + s);
        Utils.showSimpleSnackbar(mContext, mRefresh, s);
    }

    public void getTimeline() {
        SimpleFacebook.getInstance().getPosts(Post.PostType.ALL, FEED_QUERY, 25, new OnPostsListener() {

            @Override
            public void onException(Throwable throwable) {
                mRefresh.setRefreshing(false);
                showError(throwable.getMessage());
            }

            @Override
            public void onFail(String reason) {
                mRefresh.setRefreshing(false);
                showError(reason);
            }

            @Override
            public void onComplete(List<Post> response) {
                mRefresh.setRefreshing(false);
                mCursor = getCursor();
                updateContent(response);
            }
        });
    }

    @Override
    public void setAdapter() {
        mAdapter = new BaseAdapter<>(mContext, mResponse, mCursor, mSingleLayoutID).addHeader(PostHeader.PROFILE, mProfile);
    }

}
