package com.pitchedapps.facebook.frost.enums;

import com.pitchedapps.facebook.frost.R;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public enum AnimSpeed {

    RELAXED(0, 1.5f, R.string.relaxed),
    NORMAL(1, 1.0f, R.string.normal),
    FAST(2, 0.7f, R.string.fast),
    LIGHTNING(3, 0.2f, R.string.lightning);

    private float speed;
    private int value, id;

    AnimSpeed(int v, float f, int i) {
        value = v;
        speed = f;
        id = i;
    }

    public int getInt() {
        return value;
    }

    public float getSpeedFactor() {
        return speed;
    }

    public int getStringID() {
        return id;
    }

    public static AnimSpeed fromInt(int i) {
        switch (i) {
            case 0: return RELAXED;
            case 1: return NORMAL;
            case 2: return FAST;
            case 3: return LIGHTNING;
            default: return NORMAL;
        }
    }

    public static int getSize() {
        return 3;
    }

}
