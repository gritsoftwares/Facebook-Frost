package com.pitchedapps.facebook.glass;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.pitchedapps.facebook.glass.exampleFragments.MainFragment;
import com.pitchedapps.facebook.glass.fragments.DemoFragment;
import com.pitchedapps.facebook.glass.utils.Utils;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLoginListener;

import java.util.List;

/**
 * Created by 7681 on 2016-05-16.
 */
public class MainActivity extends AppCompatActivity {
    protected static final String TAG = "FB_Glass";
    private Button mButtonLogin;
    private ProfilePictureView mProfilePicture;
    private TextView mTextStatus;
    private RelativeLayout mStartLayout;
    private RelativeLayout mMainLayout;

    private SimpleFacebook mSimpleFacebook;

    public static int[] tab10() {
        return new int[] {
                R.string.demo_tab_1,
                R.string.demo_tab_2,
                R.string.demo_tab_3,
                R.string.demo_tab_4,
                R.string.demo_tab_5,
                R.string.demo_tab_6,
                R.string.demo_tab_7,
                R.string.demo_tab_8,
                R.string.demo_tab_9,
                R.string.demo_tab_10
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSimpleFacebook = SimpleFacebook.getInstance(this);

        // test local language
        Utils.updateLanguage(getApplicationContext(), "en");
//        Utils.printHashKey(getApplicationContext());

//        addFragment();

        setContentView(R.layout.activity_main);
        mMainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        mStartLayout = (RelativeLayout) findViewById(R.id.startup_layout);


//        if (savedInstanceState == null) {
//            mMainLayout.setVisibility(View.GONE);
//
//
//        }

        mButtonLogin = (Button) findViewById(R.id.button_login_splash);
//        mProfilePicture = (ProfilePictureView) findViewById(R.id.profilePicture_splash);
        mTextStatus = (TextView) findViewById(R.id.text_status_splash);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("test demo");
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewGroup tab = (ViewGroup) findViewById(R.id.tab);
        if (tab != null) { //TODO see if necessary
            tab.addView(LayoutInflater.from(this).inflate(R.layout.demo_basic, tab, false));
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);

        FragmentPagerItems pages = new FragmentPagerItems(this);
        for (int titleResId : tab10()) {
            pages.add(FragmentPagerItem.of(getString(titleResId), DemoFragment.class));
        }

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), pages);

        //TODO fix nullables
        if (viewPager != null) {
            viewPager.setAdapter(adapter);
        }
        if (viewPagerTab != null) {
            viewPagerTab.setViewPager(viewPager);
        }

        setUIState();
        setLogin();


    }

    @Override
    protected void onResume() {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSimpleFacebook.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Login example.
     */
    private void setLogin() {
        // Login listener
        final OnLoginListener onLoginListener = new OnLoginListener() {

            @Override
            public void onFail(String reason) {
                mTextStatus.setText(reason);
                Log.w(TAG, "Failed to login");
            }

            @Override
            public void onException(Throwable throwable) {
                mTextStatus.setText("Exception: " + throwable.getMessage());
                Log.e(TAG, "Bad thing happened", throwable);
            }

            @Override
            public void onLogin(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
                // change the state of the button or do whatever you want
                mTextStatus.setText("Logged in");
                revealMainLayout();
            }

            @Override
            public void onCancel() {
                mTextStatus.setText("Cancelled");
                Log.w(TAG, "Canceled the login");
            }

        };

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mSimpleFacebook.login(onLoginListener);
            }
        });
    }

    private void setUIState() {
        if (mSimpleFacebook.isLogin()) {
            revealMainLayout();
//        } else {
//            loggedOutUIState();
        }
    }

//    private void loggedInUIState() {
//        Intent intent = new Intent(this, SmartTabActivity.class);
//        startActivity(intent);
//
//    }

//    private void loggedOutUIState() {
//        mButtonLogin.setVisibility(View.VISIBLE);
//        mProfilePicture.setVisibility(View.GONE);
//    }

    private void revealMainLayout() {
        ViewTreeObserver viewTreeObserver = mMainLayout.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    circularRevealActivity();
                    mMainLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });
        }
    }

    private void circularRevealActivity() {

        int cx = mMainLayout.getWidth() / 2;
        int cy = mMainLayout.getHeight() / 2;

        float finalRadius = Math.max(mMainLayout.getWidth(), mMainLayout.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(mMainLayout, cx, cy, 0, finalRadius);
        circularReveal.setDuration(1000);

        // make the view visible and start the animation
        mMainLayout.setVisibility(View.VISIBLE);
        mStartLayout.setVisibility(View.GONE);
        circularReveal.start();
    }

    private void addFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, new MainFragment());
        fragmentTransaction.commit();
    }
}
