package com.pitchedapps.facebook.frost.enums;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public enum V {
    VISIBLE(0),
    INVISIBLE(1),
    GONE(2);
    V(int i) {
        v = i;
    }
    private final int v;
    public int getInt() {
        return v;
    }
}
