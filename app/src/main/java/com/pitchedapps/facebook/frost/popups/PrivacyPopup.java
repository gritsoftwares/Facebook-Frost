package com.pitchedapps.facebook.frost.popups;

/**
 * Created by Allan Wang on 2016-05-20.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.facebook.login.DefaultAudience;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.customViews.ImageTextCheck;
import com.pitchedapps.facebook.frost.dialogs.CreatePostDialog;
import com.pitchedapps.facebook.frost.utils.AnimUtils;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;
import com.pitchedapps.facebook.frost.utils.Utils;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

public class PrivacyPopup extends DialogFragment {

    private Context mContext;
    private int mIconSize;
    private DefaultAudience mPrivacy;
    private ImageTextCheck mOnlyMe, mFriends, mEveryone;
    private CreatePostDialog mCreatePostDialog;
    private int startX, startY;
    private LinearLayout mLinear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_privacy, container);
        mContext = getActivity();
        getViews(view);
        return view;
    }

    public void initialize(CreatePostDialog createPostDialog, int x, int y, int iconSize) {
        mCreatePostDialog = createPostDialog;
        startX = x;
        startY = y;
        mPrivacy = mCreatePostDialog.getPrivacy();
        mIconSize = iconSize;
        e("XY " + x + " " + y);
    }

    private void getViews(View view) {
        mLinear = (LinearLayout) view.findViewById(R.id.popup_privacy_container);
        mLinear.setBackgroundColor(new FrostPreferences(mContext).getDialogBackgroundColor2());
        mLinear.setVisibility(View.GONE);
        mOnlyMe = (ImageTextCheck) view.findViewById(R.id.popup_privacy_only_me);
        mFriends = (ImageTextCheck) view.findViewById(R.id.popup_privacy_friends);
        mEveryone = (ImageTextCheck) view.findViewById(R.id.popup_privacy_everyone);

        mOnlyMe.initialize(R.string.only_me, GoogleMaterial.Icon.gmd_lock, mIconSize);
        mFriends.initialize(R.string.friends, GoogleMaterial.Icon.gmd_people, mIconSize);
        mEveryone.initialize(R.string.everyone, MaterialDesignIconic.Icon.gmi_globe, mIconSize);

        setChecks();

        mOnlyMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrivacy = DefaultAudience.ONLY_ME;
                setChecks();
            }
        });

        mFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrivacy = DefaultAudience.FRIENDS;
                setChecks();
            }
        });

        mEveryone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrivacy = DefaultAudience.EVERYONE;
                setChecks();
            }
        });
    }

    private void setChecks() {
        mOnlyMe.uncheck();
        mFriends.uncheck();
        mEveryone.uncheck();

        switch (mPrivacy) {
            case ONLY_ME:
                mOnlyMe.check();
                break;
            case FRIENDS:
                mFriends.check();
                break;
            case EVERYONE:
                mEveryone.check();
                break;
            default:
                e("PrivacyPopup tag error: " + mPrivacy);
                break;
        }

        mCreatePostDialog.updatePrivacy(mPrivacy);
        dismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = new Dialog(getActivity(), R.style.Frost_DialogFragment_NoMin);
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                AnimUtils.circleReveal(mContext, mLinear, 0, 0, Utils.getScreenDiagonal(mContext));
            }
        });
        Window w = d.getWindow();
        w.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        w.setGravity(Gravity.TOP | Gravity.START);
        w.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams params = w.getAttributes();
//        params.dimAmount = 0.90f;
        params.x = startX;
        params.y = startY;
        w.setAttributes(params);
        return d;
    }

}