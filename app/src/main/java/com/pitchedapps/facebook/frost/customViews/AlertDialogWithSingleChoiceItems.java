package com.pitchedapps.facebook.frost.customViews;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.SettingsActivity;
import com.pitchedapps.facebook.frost.enums.AnimSpeed;
import com.pitchedapps.facebook.frost.enums.Themes;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;

/**
 * Created by Allan Wang on 2016-05-22.
 */
public class AlertDialogWithSingleChoiceItems {

    private Activity mActivity;
    private FrostPreferences fPrefs;
    private TextView mTextView;
    public static AnimSpeed[] animSpeedValues = {AnimSpeed.RELAXED, AnimSpeed.NORMAL, AnimSpeed.FAST, AnimSpeed.LIGHTNING};
    public static Themes[] themeValues = {Themes.LIGHT, Themes.DARK, Themes.CUSTOM};

    public AlertDialogWithSingleChoiceItems(Activity a, TextView tv) {
        mActivity = a;
        fPrefs = new FrostPreferences(mActivity);
        mTextView = tv;
    }

    public void addAnimSpeedOptions() {
        CharSequence[] animSpeedOptions = new CharSequence[animSpeedValues.length];
        for (int i = 0; i < animSpeedValues.length; i++) {
            animSpeedOptions[i] = s(animSpeedValues[i].getStringID());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.Frost_AlertDialog);
        builder.setTitle(s(R.string.animation_speed));
        builder.setSingleChoiceItems(animSpeedOptions, fPrefs.getAnimationSpeedPosition(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fPrefs.setAnimationSpeedPosition(which);
                fPrefs.saveAnimationEnum(animSpeedValues[which]);
                mTextView.setText(s(animSpeedValues[which].getStringID()));
                dialog.cancel();
            }
        });

        AlertDialog mDialog = builder.create();
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.show();
    }

    public void addThemeOptions() {
        CharSequence[] themeOptions = new CharSequence[themeValues.length];
        for (int i = 0; i < themeValues.length; i++) {
            themeOptions[i] = s(themeValues[i].getStringID());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.Frost_AlertDialog);
        builder.setTitle(s(R.string.animation_speed));
        builder.setSingleChoiceItems(themeOptions, fPrefs.getAnimationSpeedPosition(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int origID = fPrefs.getThemeStringID();
                fPrefs.setTheme(which);
                fPrefs.setThemeStringID(themeValues[which]);
                mTextView.setText(s(themeValues[which].getStringID()));
                if (origID != themeValues[which].getStringID()) ((SettingsActivity)mActivity).reload();
                dialog.cancel();
            }
        });

        AlertDialog mDialog = builder.create();
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.show();
    }

    private String s(int id){
        return mActivity.getResources().getString(id);
    }
}
