package com.pitchedapps.facebook.frost.utils;

import android.content.Context;

import com.sromku.simple.fb.entities.Profile;

public class SharedObjects {

    public static Context context;
    private static Profile uProfile;

    public static Profile getProfile() {
        return uProfile;
    }

    public static void saveProfile(Profile p) {
        uProfile = p;
    }
}
