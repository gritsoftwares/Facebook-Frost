package com.pitchedapps.facebook.frost.enums;

import com.pitchedapps.facebook.frost.R;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public enum Themes {

    LIGHT(0, R.string.light),
    DARK(1, R.string.dark),
    FACEBOOK(2, R.string.facebook_blue),
    CUSTOM(3, R.string.custom);

    private int value, id;

    Themes(int v, int i) {
        value = v;
        id = i;
    }

    public int getInt() {
        return value;
    }

    public int getStringID() {
        return id;
    }

    public static Themes fromInt(int i) {
        switch (i) {
            case 0: return LIGHT;
            case 1: return DARK;
            case 2: return FACEBOOK;
            case 3: return CUSTOM;
            default: return LIGHT;
        }
    }

    public static int getSize() {
        return 4;
    }
}
