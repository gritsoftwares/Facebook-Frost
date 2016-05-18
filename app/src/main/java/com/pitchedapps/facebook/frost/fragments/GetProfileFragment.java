package com.pitchedapps.facebook.frost.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.exampleFragments.BaseFragment;
import com.pitchedapps.facebook.frost.utils.AnimUtils;
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
    private ImageView mCover, mProfile, mbEmail, mbGender, mbLocation, mbBirthday;
//    private LinearLayout mButtons;
    private Drawable iMale, iFemale;
    private SwipeRefreshLayout mRefresh;
    private Context mContext;
    private View[] viewList;
    private boolean firstRun = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mContext = getActivity();
        getViews(view);
        getData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(EXAMPLE);
    }

    private void getViews(View view) {
        mCover = (ImageView) view.findViewById(R.id.profile_cover);
        mbEmail = (ImageView) view.findViewById(R.id.profile_buttons_email);
        mbGender = (ImageView) view.findViewById(R.id.profile_buttons_gender);
        mbLocation = (ImageView) view.findViewById(R.id.profile_buttons_location);
        mbBirthday = (ImageView) view.findViewById(R.id.profile_buttons_birthday);
        mProfile = (ImageView) view.findViewById(R.id.profile_photo);
        mHeader = (TextView) view.findViewById(R.id.profile_header);
        mResult = (TextView) view.findViewById(R.id.profile_list);
//        mButtons = (LinearLayout) view.findViewById(R.id.profile_buttons);
        mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.profile_refresh);

        if (firstRun) {
            viewList = new View[] {mbEmail, mbGender, mbBirthday, mbLocation, mResult};
            AnimUtils.setVisibility(viewList, AnimUtils.V.INVISIBLE);
        }

        int buttonColor = ContextCompat.getColor(mContext, R.color.profile_buttons);

        //Buttons
        Drawable iEmail = new IconicsDrawable(mContext)
                .icon(MaterialDesignIconic.Icon.gmi_email)
                .color(buttonColor)
                .sizeDp(24);
        mbEmail.setImageDrawable(iEmail);
        iMale = new IconicsDrawable(mContext)
                .icon(MaterialDesignIconic.Icon.gmi_male_alt)
                .color(buttonColor)
                .sizeDp(24);
        iFemale = new IconicsDrawable(mContext)
                .icon(MaterialDesignIconic.Icon.gmi_female)
                .color(buttonColor)
                .sizeDp(24);
        Drawable iMaleFemale = new IconicsDrawable(mContext)
                .icon(MaterialDesignIconic.Icon.gmi_male_female)
                .color(buttonColor)
                .sizeDp(24);
        mbGender.setImageDrawable(iMaleFemale);
        Drawable iLocation = new IconicsDrawable(mContext)
                .icon(MaterialDesignIconic.Icon.gmi_globe)
                .color(buttonColor)
                .sizeDp(24);
        mbLocation.setImageDrawable(iLocation);
        Drawable iBirthday = new IconicsDrawable(mContext)
                .icon(MaterialDesignIconic.Icon.gmi_cake)
                .color(buttonColor)
                .sizeDp(24);
        mbBirthday.setImageDrawable(iBirthday);

        //Refresh
        mRefresh.setEnabled(true);
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
                .add(Profile.Properties.DEVICES)
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

                if (response.getGender().equalsIgnoreCase("male")) {
                    mbGender.setImageDrawable(iMale);
                } else if (response.getGender().equalsIgnoreCase("female")) {
                    mbGender.setImageDrawable(iFemale);
                }

                if (firstRun) {
                    AnimUtils.sequentialFadeIn(mContext, viewList, 300);
                    firstRun = false;
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
