package com.pitchedapps.facebook.frost.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.exampleFragments.BaseFragment;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.sromku.simple.fb.utils.Attributes;
import com.sromku.simple.fb.utils.PictureAttributes;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

public class GetProfileFragment extends BaseFragment {

    private final static String EXAMPLE = "Profile";

    private TextView mResult, mHeader;
    private ImageView mCover, mProfile;
    private SwipeRefreshLayout mRefresh;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mContext = getActivity();
        mCover = (ImageView) view.findViewById(R.id.profile_cover);
        mProfile = (ImageView) view.findViewById(R.id.profile_photo);
        mHeader = (TextView) view.findViewById(R.id.profile_header);
        mResult = (TextView) view.findViewById(R.id.profile_list);
        mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.profile_refresh);
        mRefresh.setEnabled(true);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        getData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(EXAMPLE);
    }

    private void getData() {
        PictureAttributes pictureAttributes = Attributes.createPictureAttributes();
        pictureAttributes.setHeight(500);
        pictureAttributes.setWidth(500);
        pictureAttributes.setType(PictureAttributes.PictureType.SQUARE);
        Profile.Properties properties = new Profile.Properties.Builder()
                .add(Profile.Properties.PICTURE, pictureAttributes)
                .add(Profile.Properties.FIRST_NAME)
                .add(Profile.Properties.LAST_NAME)
                .add(Profile.Properties.BIRTHDAY)
                .add(Profile.Properties.LOCATION)
                .add(Profile.Properties.LOCALE)
                .add(Profile.Properties.LANGUAGE)
                .add(Profile.Properties.AGE_RANGE)
                .add(Profile.Properties.EMAIL)
                .add(Profile.Properties.GENDER)
                .add(Profile.Properties.EDUCATION)
//                        .add(Profile.Properties.CURRENCY)
                .add(Profile.Properties.COVER)
                .build();

        SimpleFacebook.getInstance().getProfile(properties, new OnProfileListener() {

            @Override
            public void onThinking() {
                mRefresh.setRefreshing(true);
            }

            @Override
            public void onException(Throwable throwable) {
                mRefresh.setRefreshing(false);
                mResult.setText(throwable.getMessage());
            }

            @Override
            public void onFail(String reason) {
                mRefresh.setRefreshing(false);
                mResult.setText(reason);
            }

            @Override
            public void onComplete(Profile response) {
                mRefresh.setRefreshing(false);

//                printProfileData(response);

                String str = Utils.toHtml(response);
                mResult.setText(Html.fromHtml(str));

//                e(response.getAgeRange().getMax() + "-" + response.getAgeRange().getMin());
                if (!response.getCover().toString().equals("null")) { //TODO learn more about cover
                    Glide.with(mContext)
                            .load(response.getCover().toString())
                            .centerCrop()
                            .into(mCover);
                }

                if (response.getPicture() != null) {
                    Glide.with(mContext)
                            .load(response.getPicture())
                            .centerCrop()
                            .into(mProfile);
                }

                String header = response.getFirstName() + " " + response.getLastName();
                if (header.equals("null null")) {
                    mHeader.setText("Unnamed");
                } else {
                    mHeader.setText(header);
                }
            }
        });
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
