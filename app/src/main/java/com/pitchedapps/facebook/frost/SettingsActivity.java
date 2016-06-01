package com.pitchedapps.facebook.frost;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.pitchedapps.facebook.frost.customViews.FrostPreferenceView;
import com.pitchedapps.facebook.frost.utils.AnimUtils;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;
import com.pitchedapps.facebook.frost.utils.Utils;

/**
 * Created by Allan Wang on 2016-05-22.
 */
public class SettingsActivity extends AppCompatActivity {

    private ViewGroup mFrame;
    private FrostPreferenceView mCurrent;
    private Context mContext;
    private FrostPreferences fPrefs;
    private boolean colorChanged = false;

    /*
     * A ViewGroup container that can actively add the preference framelayout
     * Supports beautiful circular reveals
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_empty_settings);
        mContext = this;
        fPrefs = new FrostPreferences(mContext);
        mFrame = (ViewGroup) findViewById(R.id.settings_frame);
        mCurrent = newPreferenceView();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        updateStatusBar();
    }

    public void colorChanged() {
        colorChanged = true;
        mFPVanimation(newPreferenceView()); //replicate view with new colors
    }

    public void reload() {
        mFPVanimation(newPreferenceView()); //layout change; reload
    }

    @Override
    public void onBackPressed() {
        if (colorChanged) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("colorChange", true);
            startActivity(intent);
        }
        super.onBackPressed();
    }


    private FrostPreferenceView newPreferenceView() {
        FrostPreferenceView mFPV = new FrostPreferenceView(mContext);
        mFrame.addView(mFPV);
        updateBaseTheme();
        return mFPV;
    }

    private void mFPVanimation(final FrostPreferenceView mFPV) {
        mFPV.bringToFront();
        mFPV.setVisibility(View.INVISIBLE);
        Point p = Utils.getScreenSize(mContext);

        AnimUtils.circleReveal(mContext, mFPV, p.x / 2, p.y / 2, Utils.getScreenDiagonal(mContext) / 2, new AnimUtils.AnimUtilsInterface() {
            @Override
            public void onAnimationEnd() {
                mFrame.removeView(mCurrent);
                mCurrent = mFPV;
//                updateStatusBar();
            }
        });
    }

    private void updateStatusBar() {
//            if (ColorUtils.isColorBright(fPrefs.getBackgroundColor())) {
                getWindow().setStatusBarColor(0x30000000);
//            } else {
//                getWindow().setStatusBarColor(Color.TRANSPARENT);
//            }
//        }
    }

    private void updateBaseTheme() {
        if (fPrefs.isDark()) {
            if (fPrefs.isTransparent()) {
                setTheme(R.style.Frost_Theme_Transparent);
            }  else {
                setTheme(R.style.Frost_Theme);
            }
        } else {
            if (fPrefs.isTransparent()) {
                setTheme(R.style.Frost_Theme_Light_Transparent);
            } else {
                setTheme(R.style.Frost_Theme_Light);
            }
        }
    }
}
