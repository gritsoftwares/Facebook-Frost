package com.pitchedapps.facebook.frost.enums;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public enum FBURL {

    FEED("https://touch.facebook.com/"),
    PROFILE("https://touch.facebook.com/me/");

    private String url;

    FBURL(String s) {
        url = s;
    }

    public String getLink() {
        return url;
    }

}
