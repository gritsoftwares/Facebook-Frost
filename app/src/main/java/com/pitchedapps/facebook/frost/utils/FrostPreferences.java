package com.pitchedapps.facebook.frost.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.enums.AnimSpeed;
import com.pitchedapps.facebook.frost.enums.Themes;

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
            TEXT_COLOR_CP = "text_color_cp",
            BACKGROUND_COLOR_CP = "bg_color_cp",
            HEADER_TEXT_COLOR = "header_text_color",
            HEADER_BACKGROUND_COLOR = "header_bg_color",
            THEME_STYLE = "theme_style",
            THEME_STYLE_STRING_ID = "theme_style_theme_id",
            ANIMATION_SPEED = "animation_speed",
            ANIMATION_DIALOG_CHECKED = "animation_dialog_checked",
            ANIMATION_SPEED_STRING_ID = "animation_speed_string_id";

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

    public void setTextColorCP(int i) {
        getSharedPreferences().edit().putInt(TEXT_COLOR_CP, i).apply();
        setTextColor(i);
    }

    public int getTextColorCP() {
        return getSharedPreferences().getInt(TEXT_COLOR_CP, 0xff000000);
    }

    public void setBackgroundColor(int i) {
        getSharedPreferences().edit().putInt(BACKGROUND_COLOR, i).apply();
        setIsDark(ColorUtils.isColorDark(i));
    }

    public int getBackgroundColor() {
        return getSharedPreferences().getInt(BACKGROUND_COLOR, 0xffeeeeee);
    }

    public void setBackgroundColorCP(int i) {
        getSharedPreferences().edit().putInt(BACKGROUND_COLOR_CP, i).apply();
        setBackgroundColor(i);
    }

    public int getBackgroundColorCP() {
        return getSharedPreferences().getInt(BACKGROUND_COLOR_CP, 0xffeeeeee);
    }

    public void setHeaderTextColor(int i) {
        getSharedPreferences().edit().putInt(HEADER_TEXT_COLOR, i).apply();
    }

    public int getHeaderTextColor() {
        return getSharedPreferences().getInt(HEADER_TEXT_COLOR, 0xffffffff);
    }

    public void setHeaderBackgroundColor(int i) {
        getSharedPreferences().edit().putInt(HEADER_BACKGROUND_COLOR, i).apply();
        setIsDark(ColorUtils.isColorDark(i));
    }

    public int getHeaderBackgroundColor() {
        return getSharedPreferences().getInt(HEADER_BACKGROUND_COLOR, 0xFF3B5998);
    }

    public void setTheme(int i) {
        getSharedPreferences().edit().putInt(THEME_STYLE, i).apply();
    }

    public int getTheme() {
        return getSharedPreferences().getInt(THEME_STYLE, 0);
    }

    public void setThemeStringID(Themes t) {
        getSharedPreferences().edit().putInt(THEME_STYLE_STRING_ID, t.getStringID()).apply();
    }

    public int getThemeStringID() {
        return getSharedPreferences().getInt(THEME_STYLE_STRING_ID, R.string.light);
    }


    public void saveAnimationEnum(AnimSpeed s) {
        setAnimationSpeedFactor(s);
        setAnimationSpeedStringID(s);
    }

    private void setAnimationSpeedFactor(AnimSpeed s) {
        getSharedPreferences().edit().putFloat(ANIMATION_SPEED, s.getSpeedFactor()).apply();
    }

    public float getAnimationSpeedFactor() {
        return getSharedPreferences().getFloat(ANIMATION_SPEED, 1.0f);
    }

    private void setAnimationSpeedStringID(AnimSpeed s) {
        getSharedPreferences().edit().putInt(ANIMATION_SPEED_STRING_ID, s.getStringID()).apply();
    }

    public int getAnimationSpeedStringID() {
        return getSharedPreferences().getInt(ANIMATION_SPEED_STRING_ID, R.string.normal);
    }

    public static float getAnimationSpeedFactor(Context c) {
        return c.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getFloat(ANIMATION_SPEED, 1.0f);
    }

    public void setAnimationSpeedPosition(int i) {
        getSharedPreferences().edit().putInt(ANIMATION_DIALOG_CHECKED, i).apply();
    }

    public int getAnimationSpeedPosition() {
        return getSharedPreferences().getInt(ANIMATION_DIALOG_CHECKED, 1);
    }
}