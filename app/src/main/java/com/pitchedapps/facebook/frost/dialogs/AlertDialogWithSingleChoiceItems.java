package com.pitchedapps.facebook.frost.dialogs;

import android.content.Context;
import android.content.DialogInterface;
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

    public AlertDialogWithSingleChoiceItems(Context c, TextView tv) {
        mContext = c;
        fPrefs = new FrostPreferences(mContext);
        mTextView = tv;
    }

    public void addAnimSpeedOptions() {
        CharSequence[] animSpeedOptions = new CharSequence[AnimSpeed.getSize()];
        for (int i = 0; i < AnimSpeed.getSize(); i++) {
            animSpeedOptions[i] = s(AnimSpeed.fromInt(i).getStringID());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.Frost_AlertDialog);
        builder.setTitle(s(R.string.animation_speed));
        builder.setSingleChoiceItems(animSpeedOptions, fPrefs.getAnimationSpeedPosition(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fPrefs.saveAnimationEnum(AnimSpeed.fromInt(which));
                mTextView.setText(s(AnimSpeed.fromInt(which).getStringID()));
                dialog.cancel();
            }
        });

        AlertDialog mDialog = builder.create();
//        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.show();
    }

    public void addThemeOptions() {
        CharSequence[] themeOptions = new CharSequence[Themes.getSize()];
        for (int i = 0; i < Themes.getSize(); i++) {
            themeOptions[i] = s(Themes.fromInt(i).getStringID());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.Frost_AlertDialog);
        builder.setTitle(s(R.string.theme));
        builder.setSingleChoiceItems(themeOptions, fPrefs.getTheme(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int origValue = fPrefs.getTheme();
                Themes theme = Themes.fromInt(which);
                fPrefs.setTheme(theme);
                mTextView.setText(s(theme.getStringID()));
                if (origValue != which) ((SettingsActivity)mContext).colorChanged();
                dialog.cancel();
            }
        });

        AlertDialog mDialog = builder.create();
//        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.show();
    }

    public void addHeaderThemeOptions() {
        CharSequence[] themeOptions = new CharSequence[Themes.getSize()];
        for (int i = 0; i < Themes.getSize(); i++) {
            themeOptions[i] = s(Themes.fromInt(i).getStringID());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.Frost_AlertDialog);
        builder.setTitle(s(R.string.header_theme));
        builder.setSingleChoiceItems(themeOptions, fPrefs.getHeaderTheme(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int origValue = fPrefs.getHeaderTheme();
                Themes theme = Themes.fromInt(which);
                fPrefs.setHeaderTheme(theme);
                mTextView.setText(s(theme.getStringID()));
                if (origValue != which) ((SettingsActivity)mContext).colorChanged();
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
