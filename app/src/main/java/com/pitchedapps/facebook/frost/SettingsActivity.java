package com.pitchedapps.facebook.frost;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import com.pitchedapps.facebook.frost.customViews.FrostPreferenceView;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;
import com.pitchedapps.facebook.frost.utils.Utils;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

/**
 * Created by Allan Wang on 2016-05-22.
 */
public class SettingsActivity extends AppCompatActivity {

    private ViewGroup mFrame;
    private FrostPreferenceView mCurrent;
    private Context mContext;
    private FragmentManager fManager;
    private FrostPreferences fPrefs;

    /*
     * A ViewGroup container that can actively add the preference framelayout
     * Supports beautiful circular reveals
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fManager = getSupportFragmentManager();
        setContentView(R.layout.activity_empty_settings);
        mContext = this;
        mFrame = (ViewGroup) findViewById(R.id.settings_frame);
        mCurrent = newPreferenceView();

        fPrefs = new FrostPreferences(mContext);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        updateStatusNav();
        padMain();
    }

    public void colorChanged() {
        mFPVanimation(newPreferenceView()); //replicate view with new colors
    }

    private FrostPreferenceView newPreferenceView() {
        FrostPreferenceView mFPV = new FrostPreferenceView(mContext);
        mFPV.initialize(fManager, this);
        mFrame.addView(mFPV);
        return mFPV;
    }

    private void mFPVanimation(final FrostPreferenceView mFPV) {
        mFPV.bringToFront();
        mFPV.setVisibility(View.INVISIBLE);
        Point p = Utils.getScreenSize(mContext);
        final Animator anim =
                ViewAnimationUtils.createCircularReveal(mFPV, p.x/2, p.y/2, 0, (float) Utils.getScreenDiagonal(mContext)/2).setDuration((int) Utils.getScreenDiagonal(mContext));
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mFrame.removeView(mCurrent);
                mCurrent = mFPV;
            }
        });
        mFPV.setVisibility(View.VISIBLE);
        anim.start();
    }

    private void padMain() {
        ViewCompat.setOnApplyWindowInsetsListener(mFrame, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                final int statusBar = insets.getSystemWindowInsetTop();
                final int navigationBar = insets.getSystemWindowInsetBottom();
                mFrame.setPadding(0, statusBar, 0, navigationBar);
                Utils.saveNavBarHeight(navigationBar);
                return insets;
            }
        });
    }

    private void updateStatusNav() {
        getWindow().setStatusBarColor(fPrefs.getBackgroundColor()); //TODO fix this
        getWindow().setNavigationBarColor(fPrefs.getBackgroundColor()); //TODO fix this

    }
}
