package com.pitchedapps.facebook.frost.enums;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public enum AnimSpeed {

    RELAXED(1.5f),
    NORMAL(1.0f),
    FAST(0.7f),
    LIGHTNING(0.2f);

    private float speed;

    AnimSpeed(float f) {
        speed = f;
    }

    public float getSpeedFactor() {
        return speed;
    }

}
