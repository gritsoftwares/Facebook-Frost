package com.pitchedapps.facebook.frost.customViews;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Allan Wang on 2016-05-27.
 */
public class SwitchPreference extends android.preference.SwitchPreference {

    public SwitchPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwitchPreference(Context context) {
        super(context);
    }
}
