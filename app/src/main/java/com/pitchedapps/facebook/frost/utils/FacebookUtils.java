package com.pitchedapps.facebook.frost.utils;

import android.content.Context;

import com.sromku.simple.fb.entities.Profile;

public class FacebookUtils {

    public static Context context; //TODO delete this after deleting example
    private static Profile uProfile;
    private static String myProfileURL;

    public static Profile getProfile() {
        return uProfile;
    }

    public static void saveProfile(Profile p) {
        uProfile = p;
        myProfileURL = uProfile.getPicture();
    }

    public static String getProfileURL() {
        return myProfileURL;
    }
}
