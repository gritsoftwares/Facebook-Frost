package com.pitchedapps.facebook.frost.enums;

import com.pitchedapps.facebook.frost.R;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public enum AnimSpeed {

    RELAXED(1.5f, R.string.relaxed),
    NORMAL(1.0f, R.string.normal),
    FAST(0.7f, R.string.fast),
    LIGHTNING(0.2f, R.string.lightning);

    private float speed;
    private int id;

    AnimSpeed(float f, int i) {
        speed = f;
        id = i;
    }

    public float getSpeedFactor() {
        return speed;
    }

    public int getStringID() {
        return id;
    }

}
