package com.pitchedapps.facebook.frost.enums;

import android.support.v4.app.Fragment;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.fragments.DemoFragment;
import com.pitchedapps.facebook.frost.fragments.GetPostsFragment;
import com.pitchedapps.facebook.frost.fragments.NewsFeedFragment;
import com.pitchedapps.facebook.frost.fragments.ProfileFragment;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public enum FrostFragment {
    FEED(R.string.tab_feed, CommunityMaterial.Icon.cmd_newspaper, NewsFeedFragment.class),
    FRIENDS(R.string.tab_friends, GoogleMaterial.Icon.gmd_people, GetPostsFragment.class),
    NOTIFICATIONS(R.string.tab_notifications, MaterialDesignIconic.Icon.gmi_globe, DemoFragment.class),
    PROFILE(R.string.tab_profile, CommunityMaterial.Icon.cmd_account, ProfileFragment.class),
    ERROR(R.string.error, null, null);

    private int tabNameID;
    private IIcon tabIcon;
    private Class<? extends Fragment> mFragment;

    FrostFragment(int b, IIcon icon, Class<? extends Fragment> f) {
        tabNameID = b;
        tabIcon = icon;
        mFragment = f;
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
}
