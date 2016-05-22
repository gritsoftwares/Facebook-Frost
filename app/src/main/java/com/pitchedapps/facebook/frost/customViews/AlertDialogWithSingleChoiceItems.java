package com.pitchedapps.facebook.frost.customViews;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.enums.AnimSpeed;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;

/**
 * Created by Allan Wang on 2016-05-22.
 */
public class AlertDialogWithSingleChoiceItems {

    private Context mContext;
    private FrostPreferences fPrefs;
    private String mTitle;
    private CharSequence[] animSpeedOptions;
    private AnimSpeed[] animSpeedValues = {AnimSpeed.RELAXED, AnimSpeed.NORMAL, AnimSpeed.FAST, AnimSpeed.LIGHTNING};

    public AlertDialogWithSingleChoiceItems(Context c, String title) {
        mContext = c;
        fPrefs = new FrostPreferences(mContext);
        mTitle = title;
        animSpeedOptions = new CharSequence[] {s(R.string.slow), s(R.string.normal), s(R.string.fast), s(R.string.lightning)};
    }

    public void addAnimSpeedOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.Frost_AlertDialog);
        builder.setTitle(mTitle);
        builder.setSingleChoiceItems(animSpeedOptions, fPrefs.getAnimationSpeedPosition(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fPrefs.setAnimationSpeedPosition(which);
                fPrefs.setAnimationSpeedFactor(animSpeedValues[which]);
                dialog.cancel();
            }
        });

        AlertDialog mDialog = builder.create();
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.show();
    }

    private String s(int id){
        return mContext.getResources().getString(id);
    }
}
