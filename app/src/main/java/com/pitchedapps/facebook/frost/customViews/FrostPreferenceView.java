package com.pitchedapps.facebook.frost.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.dialogs.AlertDialogWithSingleChoiceItems;
import com.pitchedapps.facebook.frost.enums.AnimSpeed;
import com.pitchedapps.facebook.frost.enums.Themes;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;
import com.pitchedapps.facebook.frost.utils.Utils;

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
        animValue.setText(s(AnimSpeed.fromInt(fPrefs.getAnimationSpeedPosition()).getStringID()));
        animValue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialogWithSingleChoiceItems(mContext, animValue).addAnimSpeedOptions();
            }
        });

        Themes theme = Themes.fromInt(fPrefs.getTheme());

        PreferenceText cThemes = (PreferenceText) findViewById(R.id.preferences_theme);
        cThemes.initialize(s(R.string.theme));
        final Button themeValue = cThemes.getValueTextView();
        themeValue.setText(s(theme.getStringID()));
        themeValue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialogWithSingleChoiceItems(mContext, themeValue).addThemeOptions();
            }
        });


//        t.setBackgroundColor(randomColor());

        ColorPicker cAccent = (ColorPicker) findViewById(R.id.preferences_accent_color);
        ColorPicker cText = (ColorPicker) findViewById(R.id.preferences_text_color);
        ColorPicker cBG = (ColorPicker) findViewById(R.id.preferences_background_color);

        switch (theme) {
            case CUSTOM:
                cAccent.setVisibility(GONE);
                cText.setVisibility(GONE);
                cBG.setVisibility(GONE);
                break;
            default:
                cAccent.setText(getResources().getString(R.string.accent_color));
                cAccent.setPrefKey(FrostPreferences.ACCENT_COLOR_CP);

                cText.setText(getResources().getString(R.string.text_color));
                cText.setPrefKey(FrostPreferences.TEXT_COLOR_CP);

                cBG.setText(getResources().getString(R.string.background_color));
                cBG.setPrefKey(FrostPreferences.BACKGROUND_COLOR_CP);
                break;
        }

        Themes headerTheme = Themes.fromInt(fPrefs.getHeaderTheme());

        PreferenceText cHeaderThemes = (PreferenceText) findViewById(R.id.preferences_header_theme);
        cHeaderThemes.initialize(s(R.string.header_theme));
        final TextView headerThemeValue = cHeaderThemes.getValueTextView();
        headerThemeValue.setText(s(headerTheme.getStringID()));
        headerThemeValue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialogWithSingleChoiceItems(mContext, headerThemeValue).addHeaderThemeOptions();
            }
        });


//        t.setBackgroundColor(randomColor());

        ColorPicker cHeaderText = (ColorPicker) findViewById(R.id.preferences_header_text_color);
        ColorPicker cHeaderBG = (ColorPicker) findViewById(R.id.preferences_header_background_color);

        switch (headerTheme) {
            case CUSTOM:
                cHeaderText.setVisibility(GONE);
                cHeaderBG.setVisibility(GONE);
                break;
            default:
                cHeaderText.setText(getResources().getString(R.string.header_text_color));
                cHeaderText.setPrefKey(FrostPreferences.HEADER_TEXT_COLOR_CP);
                cHeaderBG.setText(getResources().getString(R.string.header_background_color));
                cHeaderBG.setPrefKey(FrostPreferences.HEADER_BACKGROUND_COLOR_CP);
                break;
        }
//        mViewGroup.setPadding(0, Utils.getStatusBarHeight(), 0, Utils.getNavBarHeight());
    }


    private String s(int id) {
        return getResources().getString(id);
    }
}
