package com.pitchedapps.facebook.frost.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

/**
 * Created by Allan Wang on 2016-05-21.
 */

public class FrostPreferences {

    public static final String
            PREFERENCE_NAME = "frost_preferences",
            FIRST_RUN = "first_run",
            TEXT_COLOR = "text_color",
            BACKGROUND_COLOR = "bg_color",
            THEME_STYLE = "theme_style";

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
        e("C" + i);
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

}