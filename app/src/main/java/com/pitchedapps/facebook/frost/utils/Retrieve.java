package com.pitchedapps.facebook.frost.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Allan Wang on 2016-05-20.
 */
public class Retrieve {

    public static void setPhoto(Context c, ImageView v, String url) {
        Glide.with(c)
                .load(url)
                .centerCrop()
                .into(v);
    }

    public static void setProfilePhoto(Context c, ImageView v, String id) {
        Glide.with(c)
                .load(getProfilePhotoUrl(id))
                .fitCenter()
                .into(v);
    }

    public static String getProfilePhotoUrl(String id) {
        return "https://graph.facebook.com/" + id + "/picture?type=large";
        //could be normal, small, square
    }
}
