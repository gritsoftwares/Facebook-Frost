package com.pitchedapps.facebook.frost.fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pitchedapps.facebook.frost.exampleFragments.BaseFragment;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.sromku.simple.fb.utils.Attributes;
import com.sromku.simple.fb.utils.PictureAttributes;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

public class GetProfileFragment extends BaseFragment {

    private final static String EXAMPLE = "Get profile";

    private TextView mResult;
    private Button mGetButton;
    private TextView mMore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_example_action, container, false);
        mResult = (TextView) view.findViewById(R.id.result);
        mMore = (TextView) view.findViewById(R.id.load_more);
        mMore.setPaintFlags(mMore.getPaint().getFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mGetButton = (Button) view.findViewById(R.id.button);
        mGetButton.setText(EXAMPLE);
        disableLoadMore();
        mGetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void disableLoadMore() {
        mMore.setOnClickListener(null);
        mMore.setVisibility(View.INVISIBLE);
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
//                        .add(Profile.Properties.CURRENCY)
                .add(Profile.Properties.COVER)
                .build();

//                 SimpleFacebook.getInstance().getProfile(new OnProfileListener() {
        SimpleFacebook.getInstance().getProfile(properties, new OnProfileListener() {

            @Override
            public void onThinking() {
                showDialog();
            }

            @Override
            public void onException(Throwable throwable) {
                hideDialog();
                mResult.setText(throwable.getMessage());
            }

            @Override
            public void onFail(String reason) {
                hideDialog();
                mResult.setText(reason);
            }

            @Override
            public void onComplete(Profile response) {
                hideDialog();
                String str = Utils.toHtml(response);
//                        mResult.setText(str);
                mResult.setText(Html.fromHtml(str));
            }
        });
    }
}
