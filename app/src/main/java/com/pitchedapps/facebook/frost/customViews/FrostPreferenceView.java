package com.pitchedapps.facebook.frost.customViews;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.enums.Themes;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;
import com.pitchedapps.facebook.frost.utils.Utils;

import java.util.Random;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

/**
 * Created by Allan Wang on 2016-05-22.
 */
public class FrostPreferenceView extends FrameLayout {

    private Random rnd;
    private FragmentManager fManager;
    private Activity mActivity;
    private static int statusBar, navigationBar;

    public FrostPreferenceView(Context c) {
        super(c);
    }

    public FrostPreferenceView(Context c, AttributeSet attrs) {
        super(c, attrs);
    }

    public FrostPreferenceView(Context c, AttributeSet attrs, int defStyle) {
        super(c, attrs, defStyle);
    }

    public void initialize(FragmentManager f, Activity a) {
        fManager = f;
        mActivity = a;
        initializeViews();
    }

    private void initializeViews() {
        rnd = new Random();
        FrostPreferences fPrefs = new FrostPreferences(mActivity);
        LayoutInflater inflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.preferences_frost, this);

        View mViewGroup = findViewById(R.id.preferences_frame);
        mViewGroup.setBackgroundColor(fPrefs.getBackgroundColor());

        PreferenceText cAnimations = (PreferenceText) findViewById(R.id.preferences_anim);
        cAnimations.initialize(s(R.string.animation_speed));
        final TextView animValue = cAnimations.getValueTextView();
        animValue.setText(s(fPrefs.getAnimationSpeedStringID()));
        animValue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialogWithSingleChoiceItems(mActivity, animValue).addAnimSpeedOptions();
            }
        });

        PreferenceText cThemes = (PreferenceText) findViewById(R.id.preferences_theme);
        cThemes.initialize(s(R.string.theme));
        final TextView themeValue = cThemes.getValueTextView();
        themeValue.setText(s(fPrefs.getThemeStringID()));
        themeValue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialogWithSingleChoiceItems(mActivity, themeValue).addThemeOptions();
            }
        });


//        t.setBackgroundColor(randomColor());

        ColorPicker cText = (ColorPicker) findViewById(R.id.preferences_text_color);
        if (fPrefs.getThemeStringID() != Themes.CUSTOM.getStringID()) {
            cText.setVisibility(GONE);
        } else {
            cText.initialize(fManager, mActivity);
            cText.setText(getResources().getString(R.string.text_color));
            cText.setPrefKey(FrostPreferences.TEXT_COLOR_CP);
        }

        ColorPicker cBG = (ColorPicker) findViewById(R.id.preferences_background_color);
        if (fPrefs.getThemeStringID() != Themes.CUSTOM.getStringID()) {
            cBG.setVisibility(GONE);
        } else {
            cBG.initialize(fManager, mActivity);
            cBG.setText(getResources().getString(R.string.background_color));
            cBG.setPrefKey(FrostPreferences.BACKGROUND_COLOR_CP);
        }

        mViewGroup.setPadding(0, Utils.getStatusBarHeight(), 0, Utils.getNavBarHeight());
    }

    private int randomColor() {
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    private String s(int id) {
        return getResources().getString(id);
    }
}
