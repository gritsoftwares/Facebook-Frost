package com.pitchedapps.facebook.frost;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.facebook.login.DefaultAudience;
import com.pitchedapps.facebook.frost.utils.SharedObjects;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.utils.Logger;

public class HomeActivity extends AppCompatActivity {
    private static final String FACEBOOK_APP_ID = "420849111448972";
    private static final String APP_NAMESPACE = "pitchedapps_fb_frost";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedObjects.context = this;

        // set log to true
        Logger.DEBUG_WITH_STACKTRACE = true;

        // initialize facebook configuration
        Permission[] permissions = new Permission[]{
                // Permission.PUBLIC_PROFILE,
                Permission.EMAIL,
                Permission.USER_EVENTS,
                Permission.USER_ACTIONS_MUSIC,
                Permission.USER_FRIENDS,
                Permission.USER_GAMES_ACTIVITY,
                Permission.USER_BIRTHDAY,
                Permission.USER_POSTS,
                Permission.USER_ABOUT_ME,
                Permission.USER_PHOTOS,
                Permission.USER_TAGGED_PLACES,
                Permission.USER_MANAGED_GROUPS,
                Permission.PUBLISH_ACTION};

        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                .setAppId(FACEBOOK_APP_ID)
                .setNamespace(APP_NAMESPACE)
                .setPermissions(permissions)
                .setDefaultAudience(DefaultAudience.ONLY_ME) //TODO change later
                .setAskForAllPermissionsAtOnce(false)
                // .setGraphVersion("v2.3")
                .build();

        SimpleFacebook.setConfiguration(configuration);

        final Intent intent = new Intent(this, MainActivity.class);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right); //TODO change to slide up and fade
                finish();
            }
        }, 1000);

    }
}