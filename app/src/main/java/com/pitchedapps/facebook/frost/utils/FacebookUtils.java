package com.pitchedapps.facebook.frost.utils;

import android.content.Context;

import com.sromku.simple.fb.entities.Profile;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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

    public static String printResponseData(Object response) {
        StringBuilder s = new StringBuilder();
        try {
            for (Field f : response.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (Modifier.isStatic(f.getModifiers())) {
                    continue;
                }
                s.append(f.getName()).append(" ").append(f.get(response)).append("\n");
            }
        } catch (IllegalAccessException e) {
            //do nothing
        }
        return s.toString();
    }
}
