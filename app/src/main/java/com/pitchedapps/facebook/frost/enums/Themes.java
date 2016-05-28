package com.pitchedapps.facebook.frost.enums;

import com.pitchedapps.facebook.frost.R;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public enum Themes {

    LIGHT(R.string.light),
    DARK(R.string.dark),
    FACEBOOK(R.string.facebook_blue),
    CUSTOM(R.string.custom);

    private int id;

    Themes(int i) {
        id = i;
    }

    public int getStringID() {
        return id;
    }

}
