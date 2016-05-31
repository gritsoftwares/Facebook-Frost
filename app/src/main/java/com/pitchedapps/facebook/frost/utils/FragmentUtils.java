package com.pitchedapps.facebook.frost.utils;

import com.pitchedapps.facebook.frost.enums.FrostFragment;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public class FragmentUtils {

    public static FrostFragment getFrostFragment(int position) {
        switch (position) {
            case 0:
                return FrostFragment.FEED;
            case 1:
                return FrostFragment.PROFILE;
            case 2:
                return FrostFragment.EVENTS;
            case 3:
                return FrostFragment.NOTIFICATIONS;
            default:
                return FrostFragment.ERROR;
        }
    }




}
