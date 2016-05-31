package com.pitchedapps.facebook.frost.customViews;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mikepenz.iconics.IconicsDrawable;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.SettingsActivity;
import com.pitchedapps.facebook.frost.enums.Themes;
import com.pitchedapps.facebook.frost.utils.FragmentUtils;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;
import com.pitchedapps.facebook.frost.utils.Utils;

import java.util.Random;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

/**
 * Created by Allan Wang on 2016-05-22.
 */
public class FrostPreferenceView extends RelativeLayout {

    private Context mContext;

    public FrostPreferenceView(Context c) {
        super(c);
        mContext = c;
        initializeViews();
    }

    public FrostPreferenceView(Context c, AttributeSet attrs) {
        super(c, attrs);
        mContext = c;
        initializeViews();
    }

    public FrostPreferenceView(Context c, AttributeSet attrs, int defStyle) {
        super(c, attrs, defStyle);
        mContext = c;
        initializeViews();
    }

    private void initializeViews() {
        FrostPreferences fPrefs = new FrostPreferences(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.preferences_frost, this);

        View mViewGroup = findViewById(R.id.preferences_frame);
        mViewGroup.setBackgroundColor(fPrefs.getBackgroundColor());

//        Toolbar mToolbar = (Toolbar) findViewById(R.id.preference_toolbar);
//        mToolbar.setTitle("TEST");
//        mToolbar.setTitleTextColor(fPrefs.getHeaderTextColor());
//        mToolbar.setBackgroundColor(fPrefs.getHeaderBackgroundColor());
//        mToolbar.setPadding(mToolbar.getPaddingLeft(), mToolbar.getPaddingTop() + Utils.getStatusBarHeight(), mToolbar.getPaddingRight(), mToolbar.getPaddingBottom());
        TextView tHeader = (TextView) findViewById(R.id.preferences_header);
        tHeader.setTextColor(fPrefs.getHeaderTextColor());
        tHeader.setBackgroundColor(fPrefs.getHeaderBackgroundColor());
        tHeader.setPadding(tHeader.getPaddingLeft(), tHeader.getPaddingTop() + Utils.getStatusBarHeight(), tHeader.getPaddingRight(), tHeader.getPaddingBottom());
//        tHeader.setHeight(Utils.getStatusBarHeight() + tHeader.getHeight());

        PreferenceText cAnimations = (PreferenceText) findViewById(R.id.preferences_anim);
        cAnimations.initialize(s(R.string.animation_speed));
        final Button animValue = cAnimations.getValueTextView();
        animValue.setText(s(fPrefs.getAnimationSpeedStringID()));
        animValue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialogWithSingleChoiceItems(mContext, animValue).addAnimSpeedOptions();
            }
        });

        PreferenceText cThemes = (PreferenceText) findViewById(R.id.preferences_theme);
        cThemes.initialize(s(R.string.theme));
        final Button themeValue = cThemes.getValueTextView();
        themeValue.setText(s(fPrefs.getThemeStringID()));
        themeValue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialogWithSingleChoiceItems(mContext, themeValue).addThemeOptions();
            }
        });


//        t.setBackgroundColor(randomColor());

        ColorPicker cText = (ColorPicker) findViewById(R.id.preferences_text_color);
        ColorPicker cBG = (ColorPicker) findViewById(R.id.preferences_background_color);
        if (fPrefs.getThemeStringID() != Themes.CUSTOM.getStringID()) {
            cText.setVisibility(GONE);

            cBG.setVisibility(GONE);
        } else {
            cText.setText(getResources().getString(R.string.text_color));
            cText.setPrefKey(FrostPreferences.TEXT_COLOR_CP);

            cBG.setText(getResources().getString(R.string.background_color));
            cBG.setPrefKey(FrostPreferences.BACKGROUND_COLOR_CP);
        }

        PreferenceText cHeaderThemes = (PreferenceText) findViewById(R.id.preferences_header_theme);
        cHeaderThemes.initialize(s(R.string.header_theme));
        final TextView headerThemeValue = cHeaderThemes.getValueTextView();
        headerThemeValue.setText(s(fPrefs.getHeaderThemeStringID()));
        headerThemeValue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialogWithSingleChoiceItems(mContext, headerThemeValue).addHeaderThemeOptions();
            }
        });


//        t.setBackgroundColor(randomColor());

        ColorPicker cHeaderText = (ColorPicker) findViewById(R.id.preferences_header_text_color);
        ColorPicker cHeaderBG = (ColorPicker) findViewById(R.id.preferences_header_background_color);

        if (fPrefs.getHeaderThemeStringID() != Themes.CUSTOM.getStringID()) {
            cHeaderText.setVisibility(GONE);

            cHeaderBG.setVisibility(GONE);
        } else {
            cHeaderText.setText(getResources().getString(R.string.header_text_color));
            cHeaderText.setPrefKey(FrostPreferences.HEADER_TEXT_COLOR_CP);

            cHeaderBG.setText(getResources().getString(R.string.header_background_color));
            cHeaderBG.setPrefKey(FrostPreferences.HEADER_BACKGROUND_COLOR_CP);
        }
//        mViewGroup.setPadding(0, Utils.getStatusBarHeight(), 0, Utils.getNavBarHeight());
    }


    private String s(int id) {
        return getResources().getString(id);
    }
}
