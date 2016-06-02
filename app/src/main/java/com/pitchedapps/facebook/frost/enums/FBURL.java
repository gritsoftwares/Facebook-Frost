package com.pitchedapps.facebook.frost.enums;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public enum FBURL {

    FEED("https://touch.facebook.com/"),
    PROFILE("https://touch.facebook.com/me/"),
    BOOKMARKS("https://touch.facebook.com/bookmarks"),
    SEARCH("https://touch.facebook.com/search"),
    EVENTS("https://touch.facebook.com/events/upcoming"),
    FRIEND_REQUESTS("https://touch.facebook.com/requests"),
    MESSAGES("https://touch.facebook.com/messages"),
    NOTIFICATIONS("https://touch.facebook.com/notifications");

    private String url;

    FBURL(String s) {
        url = s;
    }

    public String getLink() {
        return url;
    }

}
