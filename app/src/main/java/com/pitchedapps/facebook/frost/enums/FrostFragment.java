package com.pitchedapps.facebook.frost.enums;

import android.support.v4.app.Fragment;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.fragments.EventsFragment;
import com.pitchedapps.facebook.frost.fragments.ProfileFragment;
import com.pitchedapps.facebook.frost.fragments.web.FriendRequestFragment;
import com.pitchedapps.facebook.frost.fragments.web.MessagesFragment;
import com.pitchedapps.facebook.frost.fragments.web.NewsFeedFragment;
import com.pitchedapps.facebook.frost.fragments.web.NotificationFragment;
import com.sromku.simple.fb.Permission;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public enum FrostFragment {
    FEED(R.string.tab_feed, CommunityMaterial.Icon.cmd_newspaper, NewsFeedFragment.class, FBURL.FEED),
    FRIENDS(R.string.tab_friends, GoogleMaterial.Icon.gmd_people, FriendRequestFragment.class, FBURL.FRIEND_REQUESTS),
    MESSAGES(R.string.tab_messages, MaterialDesignIconic.Icon.gmi_comments, MessagesFragment.class, FBURL.MESSAGES),
    NOTIFICATIONS(R.string.tab_notifications, MaterialDesignIconic.Icon.gmi_globe, NotificationFragment.class, FBURL.NOTIFICATIONS),
    PROFILE(R.string.tab_profile, CommunityMaterial.Icon.cmd_account, ProfileFragment.class, FBURL.PROFILE, R.layout.item_post_card, Permission.USER_POSTS, Permission.USER_ABOUT_ME),
    EVENTS(R.string.tab_events, GoogleMaterial.Icon.gmd_event, EventsFragment.class, FBURL.EVENTS, R.layout.item_event_card, Permission.USER_EVENTS),
    ERROR(R.string.error, null, null, null, 0);

    private int tabNameID;
    private IIcon tabIcon;
    private Class<? extends Fragment> mFragment;
    private FBURL mURL;
    private int mSingleLayoutID;
    private Permission[] mPermissionCheck;

    FrostFragment(int b, IIcon icon, Class<? extends Fragment> f, FBURL URL) {
        tabNameID = b;
        tabIcon = icon;
        mFragment = f;
        mURL = URL;
        mSingleLayoutID = R.layout.fragment_webview;
    }

    FrostFragment(int b, IIcon icon, Class<? extends Fragment> f, FBURL URL, int singleLayoutID, Permission... permissionCheck) {
        tabNameID = b;
        tabIcon = icon;
        mFragment = f;
        mURL = URL;
        mSingleLayoutID = singleLayoutID;
        mPermissionCheck = permissionCheck;
    }

    public int getTabNameID() {
        return tabNameID;
    }

    public IIcon getTabIcon() {
        return tabIcon;
    }

    public Class<? extends Fragment> getFragment() {
        return mFragment;
    }

    public FBURL getFBURL() {
        return mURL;
    }

    public int getSingleLayoutID() {
        return mSingleLayoutID;
    }

    public Permission[] getSDKPermissions() {
        return mPermissionCheck;
    }
}
