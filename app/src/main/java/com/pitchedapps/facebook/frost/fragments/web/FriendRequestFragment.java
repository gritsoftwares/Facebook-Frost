package com.pitchedapps.facebook.frost.fragments.web;

import com.pitchedapps.facebook.frost.enums.FBURL;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public class FriendRequestFragment extends BaseWebviewFragment {

    @Override
    public FBURL returnFBURL() {
        return FBURL.FRIEND_REQUESTS;
    }

}
