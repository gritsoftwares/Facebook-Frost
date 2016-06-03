package com.pitchedapps.facebook.frost.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.login.DefaultAudience;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.popups.PrivacyPopup;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.pitchedapps.facebook.frost.utils.ViewUtils;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Feed;
import com.sromku.simple.fb.listeners.OnPublishListener;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

/**
 * Created by Allan Wang on 2016-05-30.
 */
public class CreatePostDialog extends DialogFragment {

    private Context mContext;
    private EditText mEditText;
    private ImageView mPrivacy;
    private DefaultAudience mDefaultAudience = DefaultAudience.FRIENDS; //TODO change to privacy
    private static final int iconSize = 24;
    private ViewUtils vu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_create_post, container);
        mContext = getActivity();
        getViews(view);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), R.style.Frost_DialogFragment_AlertDialog);
    }

    private void getViews(View view) {
        vu = new ViewUtils(mContext, view);
//        view.setBackgroundColor(vu.getDialogBackgroundColor());
        vu.setDialogBG(R.id.dialog_create_container);
        mEditText = vu.editText(R.id.dialog_create_edittext);
        vu.buttonWithEditTextDependency(R.id.dialog_create_post, mEditText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setEnabled(false);

                String text = mEditText.getText().toString();
                Feed mFeed = new Feed.Builder()
                        .setMessage(text)
//                        .setName("Simple Facebook SDK for Android")
//                        .setCaption("Code less, do the same.")
//                        .setDescription("Login, publish feeds and stories, invite friends and more....")
//                        .setPicture("https://raw.githubusercontent.com/wiki/sromku/android-simple-facebook/images/android_facebook_sdk_logo.png")
//                        .setLink("https://github.com/sromku/android-simple-facebook")
                        .build();

                SimpleFacebook.getInstance().publish(mFeed, new OnPublishListener() {

                    @Override
                    public void onException(Throwable throwable) {
                        showCommentError();
                    }

                    @Override
                    public void onFail(String reason) {
                        showCommentError();
                    }

                    @Override
                    public void onThinking() {

                    }

                    @Override
                    public void onComplete(String response) {
                        mEditText.setEnabled(true);
                        mEditText.getText().clear();
                    }
                });
            }
        });

        vu.imageView(R.id.dialog_create_bottom_toolbar_camera, GoogleMaterial.Icon.gmd_photo_camera, iconSize).setAlpha(0.6f);
        vu.imageView(R.id.dialog_create_bottom_toolbar_gallery, GoogleMaterial.Icon.gmd_insert_photo, iconSize).setAlpha(0.6f);
        vu.imageView(R.id.dialog_create_bottom_toolbar_location, GoogleMaterial.Icon.gmd_location_on, iconSize).setAlpha(0.6f);
        vu.imageView(R.id.dialog_create_bottom_toolbar_tag, GoogleMaterial.Icon.gmd_label, iconSize).setAlpha(0.6f);
        mPrivacy = vu.imageView(R.id.dialog_create_bottom_toolbar_privacy, GoogleMaterial.Icon.gmd_people, iconSize);
        mPrivacy.setAlpha(0.6f);

        final CreatePostDialog cpd = this;
        mPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrivacyPopup privacyPopup = new PrivacyPopup();
                Point p = Utils.getLocation(v);
                privacyPopup.initialize(cpd, p.x, p.y, iconSize);
                privacyPopup.show(getActivity().getSupportFragmentManager(), "privacy_dialog");
            }
        });
//        ((EditText) view.findViewById(R.id.dialog_create_edittext)).setTextColor(textColor);
//        Button cancel = (Button) view.findViewById(R.id.dialog_logout_cancel);
//        Button confirm = (Button) view.findViewById(R.id.dialog_logout_confirm);
//
//        cancel.setTextColor(textColor);
//        confirm.setTextColor(textColor);
//
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//        confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((MainActivity)mContext).logout(Utils.getLocation(v));
//                dismiss();
//            }
//        });
    }

    private void showCommentError() {
        mEditText.setEnabled(true);
        Utils.showSimpleSnackbar(mContext, getView(), getResources().getString(R.string.post_fail));
    }

    public void updatePrivacy(DefaultAudience privacy) {
        mDefaultAudience = privacy;
        IIcon icon;
        switch (privacy) {
            case ONLY_ME:
                icon = GoogleMaterial.Icon.gmd_lock;
                break;
            case FRIENDS:
                icon = GoogleMaterial.Icon.gmd_people;
                break;
            case EVERYONE:
                icon = MaterialDesignIconic.Icon.gmi_globe;
                break;
            default:
                icon = GoogleMaterial.Icon.gmd_error;
                e("PrivacyPopup tag error: " + mPrivacy);
                break;
        }

        mPrivacy.setImageDrawable(new IconicsDrawable(mContext)
                .icon(icon)
                .color(vu.getTextColor())
                .sizeDp(iconSize));
    }

    public DefaultAudience getPrivacy() {
        return mDefaultAudience;
    }

}
