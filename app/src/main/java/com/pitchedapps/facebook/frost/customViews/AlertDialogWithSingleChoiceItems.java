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

    private Context mContext;
    private FrostPreferences fPrefs;
    private TextView mTextView;

    //beware of changes here; default positions are added in the preferences; change accordingly
    public static AnimSpeed[] animSpeedValues = {AnimSpeed.RELAXED, AnimSpeed.NORMAL, AnimSpeed.FAST, AnimSpeed.LIGHTNING};
    public static Themes[] themeValues = {Themes.LIGHT, Themes.DARK, Themes.FACEBOOK, Themes.CUSTOM};
//    public static Themes[] headerThemeValues = {Themes.LIGHT, Themes.DARK, Themes.CUSTOM};

    public AlertDialogWithSingleChoiceItems(Context c, TextView tv) {
        mContext = c;
        fPrefs = new FrostPreferences(mContext);
        mTextView = tv;
    }

    public void addAnimSpeedOptions() {
        CharSequence[] animSpeedOptions = new CharSequence[animSpeedValues.length];
        for (int i = 0; i < animSpeedValues.length; i++) {
            animSpeedOptions[i] = s(animSpeedValues[i].getStringID());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.Frost_AlertDialog);
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
//        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.show();
    }

    public void addThemeOptions() {
        CharSequence[] themeOptions = new CharSequence[themeValues.length];
        for (int i = 0; i < themeValues.length; i++) {
            themeOptions[i] = s(themeValues[i].getStringID());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.Frost_AlertDialog);
        builder.setTitle(s(R.string.theme));
        builder.setSingleChoiceItems(themeOptions, fPrefs.getTheme(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int origID = fPrefs.getThemeStringID();
                fPrefs.setTheme(which);
                fPrefs.setThemeStringID(themeValues[which]);
                mTextView.setText(s(themeValues[which].getStringID()));
                if (origID != themeValues[which].getStringID()) ((SettingsActivity)mContext).colorChanged();
                dialog.cancel();
            }
        });

        AlertDialog mDialog = builder.create();
//        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.show();
    }

    public void addHeaderThemeOptions() {
        CharSequence[] themeOptions = new CharSequence[themeValues.length];
        for (int i = 0; i < themeValues.length; i++) {
            themeOptions[i] = s(themeValues[i].getStringID());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.Frost_AlertDialog);
        builder.setTitle(s(R.string.header_theme));
        builder.setSingleChoiceItems(themeOptions, fPrefs.getHeaderTheme(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int origID = fPrefs.getHeaderThemeStringID();
                fPrefs.setHeaderTheme(which);
                fPrefs.setHeaderThemeStringID(themeValues[which]);
                mTextView.setText(s(themeValues[which].getStringID()));
                if (origID != themeValues[which].getStringID()) ((SettingsActivity)mContext).colorChanged();
                dialog.cancel();
            }
        });

        AlertDialog mDialog = builder.create();
//        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.show();
    }

    private String s(int id){
        return mContext.getResources().getString(id);
    }
}
