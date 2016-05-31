package com.pitchedapps.facebook.frost.enums;

import android.support.v4.app.Fragment;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.exampleFragments.PublishFeedDialogFragment;
import com.pitchedapps.facebook.frost.exampleFragments.PublishFeedMoreFragment;
import com.pitchedapps.facebook.frost.fragments.EventsFragment;
import com.pitchedapps.facebook.frost.fragments.NewsFeedFragment;
import com.pitchedapps.facebook.frost.fragments.ProfileFragment;
import com.sromku.simple.fb.Permission;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public enum FrostFragment {
    FEED(R.string.tab_feed, CommunityMaterial.Icon.cmd_newspaper, NewsFeedFragment.class, FBURL.FEED, 0),
    FRIENDS(R.string.tab_friends, GoogleMaterial.Icon.gmd_people, PublishFeedMoreFragment.class, null, 0),
    NOTIFICATIONS(R.string.tab_notifications, MaterialDesignIconic.Icon.gmi_globe, PublishFeedDialogFragment.class, null, 0),
    PROFILE(R.string.tab_profile, CommunityMaterial.Icon.cmd_account, ProfileFragment.class, FBURL.PROFILE, R.layout.item_post_card, Permission.USER_POSTS, Permission.USER_ABOUT_ME),
    EVENTS(R.string.tab_events, GoogleMaterial.Icon.gmd_event, EventsFragment.class, null, R.layout.item_event_card, Permission.USER_EVENTS),
    ERROR(R.string.error, null, null, null, 0);

    private int tabNameID;
    private IIcon tabIcon;
    private Class<? extends Fragment> mFragment;
    private FBURL mURL;
    private int mSingleLayoutID;
    private Permission[] mPermissionCheck;

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
