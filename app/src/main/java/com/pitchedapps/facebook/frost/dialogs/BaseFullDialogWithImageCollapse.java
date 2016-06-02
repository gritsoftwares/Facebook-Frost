package com.pitchedapps.facebook.frost.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;

/**
 * Created by Allan Wang on 2016-05-30.
 */
public class BaseFullDialogWithImageCollapse extends DialogFragment {

    public Context mContext;
    public int textColor, backgroundColor;
    public FrostPreferences fPrefs;
    private ImageView mToolbarImageView;
    private Toolbar mToolbar;
    private ViewGroup mNestedScrollViewGroup;
    private Drawable mToolbarImage;
    private View mNestedView;
    private String mToolbarImageURL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_single_image_collapse, container);
        mContext = getActivity();
        fPrefs = new FrostPreferences(mContext);
        textColor = fPrefs.getTextColor();
        backgroundColor = fPrefs.getBackgroundColor();
        getViews(view);
        return view;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        Dialog dialog = getDialog();
//        if (dialog != null) {
//            int width = ViewGroup.LayoutParams.MATCH_PARENT;
//            int height = ViewGroup.LayoutParams.MATCH_PARENT;
//            dialog.getWindow().setLayout(width, height);
//        }
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), R.style.Frost_DialogFragment_Full);
    }

    private void getViews(View view) {
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.activity_single_collapsing_toolbar);
        mCollapsingToolbarLayout.setBackgroundColor(backgroundColor);

        mToolbar = (Toolbar) view.findViewById(R.id.activity_single_toolbar);
        mToolbar.setBackgroundColor(backgroundColor);

        mNestedScrollViewGroup = (ViewGroup) view.findViewById(R.id.activity_single_nested_scroll_view);

        mToolbarImageView = (ImageView) view.findViewById(R.id.activity_single_toolbar_image);

        if (mToolbarImageURL != null) {
            Glide.with(mContext)
                    .load(mToolbarImageURL)
                    .into(mToolbarImageView);
        } else if (mToolbarImage != null) {
            mToolbarImageView.setImageDrawable(mToolbarImage);
        }

        if (mNestedView != null) {
            mNestedScrollViewGroup.addView(mNestedView);
        }
    }

    public void setToolbarImage(Drawable d) {
        mToolbarImage = d;
    }

    public void setToolbarImageURL(String url) {
        mToolbarImageURL = url;
    }

    public void addNestedView(View v) {
        mNestedView = v;
    }


}
