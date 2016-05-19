package com.pitchedapps.facebook.frost;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.pitchedapps.facebook.frost.fragments.GetPhotosFragment;
import com.pitchedapps.facebook.frost.fragments.GetPostsFragment;
import com.pitchedapps.facebook.frost.fragments.DemoFragment;
import com.pitchedapps.facebook.frost.fragments.ProfileFragment;
import com.pitchedapps.facebook.frost.utils.AnimUtils;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;

import java.util.List;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

/**
 * Created by 7681 on 2016-05-16.
 */
public class MainActivity extends AppCompatActivity {
    private Button mButtonLogin;
    private TextView mTextStatus;
    private RelativeLayout mStartLayout;
    private RelativeLayout mMainLayout;

    private Toolbar mToolbar;

    private SimpleFacebook mSimpleFacebook;

    private boolean logoutClick = false, blockBack = false, backPressedWhenBlocked = false;
    private int lX = 0, lY = 0;

    public static int[] tabs = {
            R.string.demo_tab_1,
            R.string.demo_tab_2,
            R.string.demo_tab_3,
            R.string.demo_tab_4,
            R.string.demo_tab_5
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);

        mSimpleFacebook = SimpleFacebook.getInstance(this);

        // test local language
        Utils.updateLanguage(getApplicationContext(), "en");
//        Utils.printHashKey(getApplicationContext());

        setContentView(R.layout.activity_full);

        mMainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        mStartLayout = (RelativeLayout) findViewById(R.id.startup_layout);
        mButtonLogin = (Button) findViewById(R.id.button_login_splash);
        mTextStatus = (TextView) findViewById(R.id.text_status_splash);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.facebook_blue_dark)); //TODO fix this

        padMain();
        addTabbedContent();
        setUIState();
        setLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(this);
//        updateProfile();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSimpleFacebook.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (blockBack) {
            backPressedWhenBlocked = true;
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        lX = (int) (mToolbar.getWidth() - mToolbar.getHeight() * 2); //TODO make this read clicks
        lY = (int) (mToolbar.getHeight() * 4.2);
//        e("toolbar dimen " + lX + " " + lY);

        final OnLogoutListener onLogoutListener = new OnLogoutListener() {

            @Override
            public void onLogout() {
                // change the state of the button or do whatever you want
                mTextStatus.setText("Logged out");
                revealSplashLayout();
            }
        };


        switch (item.getItemId()) {
            case R.id.sendemail:
                Utils.sendEmailWithDeviceInfo(this);
                break;
            case R.id.update_url:
                Utils.openLinkInChromeCustomTab(this, getResources().getString(R.string.host_update_url));
                break;
            case R.id.logout:
                mSimpleFacebook.logout(onLogoutListener);
//                logoutClick = true;
                break;
            default:
                Toast.makeText(this, "Not  yet added; Stay tuned", //TODO add
                        Toast.LENGTH_LONG).show();
                break;
        }
//        Log.e("AS", " " + super.onOptionsItemSelected(item));
        return false; //TODO change to true
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
                e("MainActivity Login Fail");
            }

            @Override
            public void onException(Throwable throwable) {
                mTextStatus.setText("Exception: " + throwable.getMessage());
                e("MainActivity Exception: " + throwable);
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
                e("MainActivity Login Cancel");
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
//            revealMainLayout();
            mStartLayout.setVisibility(View.GONE);
            mMainLayout.setVisibility(View.VISIBLE);
        } else {

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
        blockBack = true;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                revealMain();
            }
        }, 2000); // afterDelay will be executed after (secs*1000) milliseconds.
    }

    private void revealSplashLayout() {
        double finalRadius = Math.max(mMainLayout.getWidth(), mMainLayout.getHeight());
        finalRadius *= 1.2;

        AnimUtils.fadeOut(this, mMainLayout, finalRadius * 0.2, finalRadius * 0.3);
        AnimUtils.circleReveal(mStartLayout, lX, lY, finalRadius * 1.2, finalRadius);
    }



//    private void updateProfile() {
//        if (mProfilePicture == null) return;
//        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;
//
//        Profile profile = Profile.getCurrentProfile();
//        if (enableButtons && profile != null) {
//            mProfilePicture.setProfileId(profile.getId());
//            mTextStatus.setText(getString(R.string.hello_user, profile.getFirstName()));
//        } else {
//            mProfilePicture.setProfileId(null);
////            mTextStatus.setText(null);
//        }
//    }

    //Animations
    public void revealMain() {
        int x = mStartLayout.getWidth() / 2;
        int y = mStartLayout.getHeight() / 2;
        double finalRadius = Math.max(mStartLayout.getWidth(), mStartLayout.getHeight());
        finalRadius *= 0.6;

        AnimUtils.fadeIn(this, mMainLayout, finalRadius * 0.3, finalRadius * 0.5);

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(mStartLayout, x, y, (int) finalRadius, 0).setDuration((long) (finalRadius * 1.2));

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mStartLayout.setVisibility(View.GONE);
                blockBack = false;
                if (backPressedWhenBlocked) {
                    backPressedWhenBlocked = false;
                    onBackPressed();
                }
            }
        });
        anim.start();
    }

    private void padMain() {
        ViewCompat.setOnApplyWindowInsetsListener(mMainLayout, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                final int statusBar = insets.getSystemWindowInsetTop();
                final int navigationBar = insets.getSystemWindowInsetBottom();
                mMainLayout.setPadding(0, statusBar, 0, navigationBar);
                return insets;
            }
        });
    }


    private void addTabbedContent() {
        ViewGroup tab = (ViewGroup) findViewById(R.id.tab);
        if (tab != null) { //TODO see if necessary
            tab.addView(LayoutInflater.from(this).inflate(R.layout.demo_basic, tab, false));
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);

        FragmentPagerItems pages = new FragmentPagerItems(this);
        for (int i = 0; i < tabs.length; i++) {
            switch (i) {
                case 0:
                    pages.add(FragmentPagerItem.of(getString(tabs[i]), ProfileFragment.class));
                    break;
                case 1:
                    pages.add(FragmentPagerItem.of(getString(tabs[i]), GetPhotosFragment.class));
                    break;
                case 3:
                    pages.add(FragmentPagerItem.of(getString(tabs[i]), GetPostsFragment.class));
                    break;
                default:
                    pages.add(FragmentPagerItem.of(getString(tabs[i]), DemoFragment.class));
                    break;
            }
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
    }

}
