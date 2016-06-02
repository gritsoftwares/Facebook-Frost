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
            IS_TRANSPARENT = "is_transparent",
            TEXT_COLOR = "text_color",
            ACCENT_COLOR = "accent_color",
            BACKGROUND_COLOR = "bg_color",
            BACKGROUND_COLOR_TINT_1 = "bg_color_tint_1",
            BACKGROUND_COLOR_TINT_2 = "bg_color_tint_2",
            DIALOG_BACKGROUND_COLOR = "dialog_bg_color",
            TEXT_COLOR_CP = "text_color_cp",
            ACCENT_COLOR_CP = "accent_color_cp",
            BACKGROUND_COLOR_CP = "bg_color_cp",
            HEADER_TEXT_COLOR = "header_text_color",
            HEADER_TEXT_COLOR_DISABLED = "header_text_color_disabled",
            HEADER_BACKGROUND_COLOR = "header_bg_color",
            HEADER_TEXT_COLOR_CP = "header_text_color_cp",
            HEADER_BACKGROUND_COLOR_CP = "header_bg_color_cp",
            HEADER_THEME_STYLE = "header_theme_style",
            HEADER_THEME_STYLE_STRING_ID = "header_theme_style_theme_id",
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

    public boolean isTransparent() {
        return getSharedPreferences().getBoolean(IS_TRANSPARENT, false);
    }

    public void setIsTransparent(boolean b) {
        getSharedPreferences().edit().putBoolean(IS_TRANSPARENT, b).apply();
    }

    public boolean isFirstRun() {
        return getSharedPreferences().getBoolean(FIRST_RUN, true);
    }

    public void setTextColor(int i) {
        setInt(TEXT_COLOR, i);
    }

    public int getTextColor() {
        return getSharedPreferences().getInt(TEXT_COLOR, 0xff000000);
    }

    public void setTextColorCP(int i) {
        setInt(TEXT_COLOR_CP, i);
        setTextColor(i);
    }

    public int getTextColorCP() {
        return getSharedPreferences().getInt(TEXT_COLOR_CP, 0xff000000);
    }

    public void setAccentColor(int i) {
        setInt(ACCENT_COLOR, i);
    }

    public int getAccentColor() {
        return getSharedPreferences().getInt(ACCENT_COLOR, ColorUtils.cFACEBOOK);
    }

    public void setAccentColorCP(int i) {
        setInt(ACCENT_COLOR_CP, i);
        setAccentColor(i);
    }

    public int getAccentColorCP() {
        return getSharedPreferences().getInt(ACCENT_COLOR_CP, ColorUtils.cFACEBOOK);
    }


    public void setBackgroundColor(int i) {
        ColorUtils c = new ColorUtils(mContext);
        setInt(BACKGROUND_COLOR, i);
        setIsDark(ColorUtils.isColorDark(i));
        setIsTransparent(ColorUtils.isTransparent(i));
        setInt(BACKGROUND_COLOR_TINT_1, c.getTintedBackground(0.1f));
        setInt(BACKGROUND_COLOR_TINT_2, c.getTintedBackground(0.2f));
        setInt(DIALOG_BACKGROUND_COLOR, c.getDialogBackground());
    }

    public int getBackgroundColor() {
        return getSharedPreferences().getInt(BACKGROUND_COLOR, 0xffeeeeee);
    }

    public int getBackgroundColorTint1() {
        return getSharedPreferences().getInt(BACKGROUND_COLOR_TINT_1, 0xffcccccc);
    }

    public int getBackgroundColorTint2() {
        return getSharedPreferences().getInt(BACKGROUND_COLOR_TINT_2, 0xffaaaaaa);
    }

    public int getDialogBackgroundColor() {
        return getSharedPreferences().getInt(DIALOG_BACKGROUND_COLOR, 0xffcccccc);
    }

    public void setBackgroundColorCP(int i) {
        setInt(BACKGROUND_COLOR_CP, i);
        setBackgroundColor(i);
    }

    public int getBackgroundColorCP() {
        return getSharedPreferences().getInt(BACKGROUND_COLOR_CP, 0xffeeeeee);
    }

    public void setHeaderTextColor(int i) {
        setInt(HEADER_TEXT_COLOR, i);
        setInt(HEADER_TEXT_COLOR_DISABLED, new ColorUtils(mContext).getDisabledHeaderTextColor());
    }

    public int getHeaderTextColor() {
        return getSharedPreferences().getInt(HEADER_TEXT_COLOR, 0xffffffff);
    }

    public int getHeaderDisabledTextColor() {
        return getSharedPreferences().getInt(HEADER_TEXT_COLOR_DISABLED, 0xff899bc1);
    }

    public void setHeaderBackgroundColor(int i) {
        setInt(HEADER_BACKGROUND_COLOR, i);
    }

    public int getHeaderBackgroundColor() {
        return getSharedPreferences().getInt(HEADER_BACKGROUND_COLOR, ColorUtils.cFACEBOOK);
    }

    public void setHeaderTextColorCP(int i) {
        setInt(HEADER_TEXT_COLOR_CP, i);
        setHeaderTextColor(i);
    }

    public int getHeaderTextColorCP() {
        return getSharedPreferences().getInt(HEADER_TEXT_COLOR_CP, 0xffffffff);
    }

    public void setHeaderBackgroundColorCP(int i) {
        setInt(HEADER_BACKGROUND_COLOR_CP, i);
        setHeaderBackgroundColor(i);
    }

    public int getHeaderBackgroundColorCP() {
        return getSharedPreferences().getInt(HEADER_BACKGROUND_COLOR_CP, ColorUtils.cFACEBOOK);
    }

    public void setHeaderTheme(int i) {
        setInt(HEADER_THEME_STYLE, i);
    }

    public int getHeaderTheme() {
        return getSharedPreferences().getInt(HEADER_THEME_STYLE, 2);
    }

    public void setHeaderThemeStringID(Themes t) {
        setInt(HEADER_THEME_STYLE_STRING_ID, t.getStringID());
        switch (t) {
            case LIGHT:
                setHeaderBackgroundColor(0xffeeeeee);
                setHeaderTextColor(0xff000000);
                break;
            case DARK:
                setHeaderBackgroundColor(0xff121212);
                setHeaderTextColor(0xffffffff);
                break;
            case FACEBOOK:
                setHeaderBackgroundColor(ColorUtils.cFACEBOOK);
                setHeaderTextColor(0xffffffff);
                break;
            case CUSTOM:
                setHeaderBackgroundColor(getHeaderBackgroundColorCP());
                setHeaderTextColor(getHeaderTextColorCP());
                break;
            default:
                break;
        }
    }

    public int getHeaderThemeStringID() {
        return getSharedPreferences().getInt(HEADER_THEME_STYLE_STRING_ID, R.string.facebook_blue);
    }

    public void setTheme(int i) {
        setInt(THEME_STYLE, i);
    }

    public int getTheme() {
        return getSharedPreferences().getInt(THEME_STYLE, 0);
    }

    public void setThemeStringID(Themes t) {
        setInt(THEME_STYLE_STRING_ID, t.getStringID());
        switch (t) {
            case LIGHT:
                setTextColor(0xff000000);
                setBackgroundColor(0xffeeeeee);
                setAccentColor(ColorUtils.cFACEBOOK);
                break;
            case DARK:
                setTextColor(0xffffffff);
                setBackgroundColor(0xff121212);
                setAccentColor(ColorUtils.cFACEBOOK);
                break;
            case FACEBOOK:
                setTextColor(0xffffffff);
                setBackgroundColor(ColorUtils.cFACEBOOK);
                setAccentColor(0xffe91e63); //Material pink
                break;
            case CUSTOM:
                setTextColor(getTextColorCP());
                setBackgroundColor(getBackgroundColorCP());
                setAccentColor(getAccentColorCP());
                break;
            default:
                break;
        }
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
        setInt(ANIMATION_SPEED_STRING_ID, s.getStringID());
    }

    public int getAnimationSpeedStringID() {
        return getSharedPreferences().getInt(ANIMATION_SPEED_STRING_ID, R.string.normal);
    }

    public static float getAnimationSpeedFactor(Context c) {
        return c.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getFloat(ANIMATION_SPEED, 1.0f);
    }

    public void setAnimationSpeedPosition(int i) {
        setInt(ANIMATION_DIALOG_CHECKED, i);
    }

    public int getAnimationSpeedPosition() {
        return getSharedPreferences().getInt(ANIMATION_DIALOG_CHECKED, 1);
    }
}