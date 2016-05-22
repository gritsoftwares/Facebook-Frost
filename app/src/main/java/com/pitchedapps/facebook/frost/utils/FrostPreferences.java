package com.pitchedapps.facebook.frost.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.pitchedapps.facebook.frost.enums.AnimSpeed;

/**
 * Created by Allan Wang on 2016-05-21.
 */

public class FrostPreferences {

    public static final String
            PREFERENCE_NAME = "frost_preferences",
            FIRST_RUN = "first_run",
            IS_DARK = "is_dark",
            TEXT_COLOR = "text_color",
            BACKGROUND_COLOR = "bg_color",
            THEME_STYLE = "theme_style",
            ANIMATION_SPEED = "animation_speed",
            ANIMATION_DIALOG_CHECKED = "animation_dialog_checked";

    private final Context mContext;

    public FrostPreferences(Context c) {
        mContext = c;
    }

    private SharedPreferences getSharedPreferences() {
        return mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public int getInt(String key) {
        return getSharedPreferences().getInt(key, 0);
    }

    public void setInt(String s, int i) {
        getSharedPreferences().edit().putInt(s, i).apply();
    }

    public void setFirstRun(boolean firstRun) {
        getSharedPreferences().edit().putBoolean(FIRST_RUN, firstRun).apply();
    }

    public boolean isDark() {
        return getSharedPreferences().getBoolean(IS_DARK, false);
    }

    public void setIsDark(boolean b) {
        getSharedPreferences().edit().putBoolean(IS_DARK, b).apply();
    }

    public boolean isFirstRun() {
        return getSharedPreferences().getBoolean(FIRST_RUN, true);
    }

    public void setTextColor(int i) {
        getSharedPreferences().edit().putInt(TEXT_COLOR, i).apply();
    }

    public int getTextColor() {
        return getSharedPreferences().getInt(TEXT_COLOR, 0xff000000);
    }

    public void setBackgroundColor(int i) {
        getSharedPreferences().edit().putInt(BACKGROUND_COLOR, i).apply();
        setIsDark(ColorUtils.isColorDark(i));
    }

    public int getBackgroundColor() {
        return getSharedPreferences().getInt(BACKGROUND_COLOR, 0xffeeeeee);
    }

    public void setTheme(int i) {
        getSharedPreferences().edit().putInt(THEME_STYLE, i).apply();
    }

    public int getTheme() {
        return getSharedPreferences().getInt(THEME_STYLE, 0);
    }

    public void setAnimationSpeedFactor(AnimSpeed s) {
        getSharedPreferences().edit().putFloat(ANIMATION_SPEED, s.getSpeedFactor()).apply();
    }

    public float getAnimationSpeedFactor() {
        return getSharedPreferences().getFloat(ANIMATION_SPEED, 1.0f);
    }

    public static float getAnimationSpeedFactor(Context c) {
        return c.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getFloat(ANIMATION_SPEED, 1.0f);
    }

    public void setAnimationSpeedPosition(int i) {
        getSharedPreferences().edit().putInt(ANIMATION_DIALOG_CHECKED, i).apply();
    }

    public int getAnimationSpeedPosition() {
        return getSharedPreferences().getInt(ANIMATION_DIALOG_CHECKED, 0);
    }
}