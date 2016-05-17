package com.pitchedapps.facebook.frost;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.pitchedapps.facebook.frost.exampleFragments.GetPostsFragment;
import com.pitchedapps.facebook.frost.exampleFragments.MainFragment;
import com.pitchedapps.facebook.frost.fragments.DemoFragment;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 7681 on 2016-05-16.
 */
public class MainActivity extends AppCompatActivity {
    protected static final String TAG = "FB_Glass";
    private Button mButtonLogin;
    //    private ProfilePictureView mProfilePicture;
    private TextView mTextStatus;
    private RelativeLayout mStartLayout;
    private RelativeLayout mMainLayout;

    private SimpleFacebook mSimpleFacebook;

    private boolean logoutClick = false;
    private int lX = 0, lY = 0;
    private Button logoutMenuButton;

    public static int[] tabs = {
            R.string.demo_tab_1,
            R.string.demo_tab_2,
            R.string.demo_tab_3,
            R.string.demo_tab_4,
//            R.string.demo_tab_5,
//            R.string.demo_tab_6,
//            R.string.demo_tab_7,
//            R.string.demo_tab_8,
//            R.string.demo_tab_9,
            R.string.demo_tab_10
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSimpleFacebook = SimpleFacebook.getInstance(this);

        // test local language
        Utils.updateLanguage(getApplicationContext(), "en");
//        Utils.printHashKey(getApplicationContext());

//        addFragment();

        setContentView(R.layout.activity_full);
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
        for (int i = 0; i < tabs.length; i++) {
            switch (i) {
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

//    @Override
//    public void onBackPressed() {
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.e("MENU", menu + " ");
//        Menu mMenu = (Menu)(findViewById(R.id.main_menu));
//        logoutMenuButton = (Button)menu.getItem(2).getActionView();
//        Log.e("BUTTON", logoutMenuButton + " ");
//        int[] location = new int[2];
//        logoutMenuButton.getLocationInWindow(location);
//        int x = location[0] + logoutMenuButton.getWidth()/2;
//        int y = location[1] + logoutMenuButton.getHeight()/2;
//        Log.e("AAA", " " + new Point(x, y));
//        logoutMenuButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                Log.e("GOT", "HEREE");
//                //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
//            }
//        });

//        logoutMenuButton.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent e) {
////                x = (int) e.getX() + v.getLeft();
////                y = (int) e.getY() + v.getTop();
//                Log.e("GOT", "HsEREE");
//                return true;
//            }
//        });
        return true;
    }

//    @Override
//    public boolean onMenuOpened(int featureId, Menu menu) {
//        super.onMenuOpened(featureId, menu);
//        Log.e("MENUOPEN", featureId + " " + Window.FEATURE_ACTION_BAR);
//        if (menu != null) {
//            Button b = (Button) menu.getItem(2).getActionView();
//
//            int[] location = new int[2];
//            b.getLocationInWindow(location);
//            int x = location[0] + b.getWidth() / 2;
//            int y = location[1] + b.getHeight() / 2;
//            Log.e("AAA", " " + new Point(x, y));
//        } else {
//            Log.e("MENU", "NuLL");
//        }
//        return true;
//    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem a = menu.getItem(2);
        if (a != null) {
            Log.e("MA", a.getMenuInfo() + " ");
        }
        Log.e("IS A", a.isVisible() + " ");
        TextView t = (TextView) menu.getItem(2).getActionView();
        Log.e("TI", t.getX() + " " + t.getHeight() + " " + t.getOffsetForPosition(0.0f, 0.0f));
//        View m = t;

        Log.e("Text", t + " ");
//        int[] location = new int[2];
//        if (m != null) {
//            m.getLocationOnScreen(location);
//            Log.e("CC", Arrays.toString(location));
//            int x = location[0] + m.getWidth() / 2;
//            int y = location[1] + m.getHeight() / 2;
//            Log.e("AAA", " " + new Point(x, y));
//        } else {
//            Log.e("M", "NUL");
//        }

        t.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
//                x = (int) e.getX() + v.getLeft();
//                y = (int) e.getY() + v.getTop();
                Log.e("GOT", "HsEREE");
                return true;
            }
        });

        return true;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        super.onTouchEvent(event);
//        // MotionEvent object holds X-Y values
//        String text = event.getRawX() + " " + event.getRawY();
//        Log.e("TOUCH", text);
//        return false;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

//int a = (int)item.getActionView().getX();
//        Log.e("A", a + " ");
//        final OnLogoutListener onLogoutListener = new OnLogoutListener() {
//
//            @Override
//            public void onLogout() {
//                // change the state of the button or do whatever you want
//                mTextStatus.setText("Logged out");
//                revealSplashLayout();
//            }
//        };


        switch (item.getItemId()) {
            case R.id.changelog:
//                Button b = (Button) item.getActionView();

//                int[] location = new int[2];
//                b.getLocationInWindow(location);
//                int x = location[0] + b.getWidth()/2;
//                int y = location[1] + b.getHeight()/2;
//                Log.e("AAA", " " + new Point(x, y));

//                Rect myViewRect = new Rect();
//                b.getGlobalVisibleRect(myViewRect);
//                float cx = myViewRect.left;
//                float cy = myViewRect.exactCenterY();
//                Log.e("D", cx + " " + cy);

//                Log.e("B", b + " ");
//                int[] location = new int[2];
//                b.getLocationOnScreen(location);
//                Log.e("D", " " + b.getX());
//                Log.e("C", Arrays.toString(location));
                break;
            case R.id.logout:
//                mSimpleFacebook.logout(onLogoutListener);
//                logoutClick = true;
                break;
            default:
                Toast.makeText(this, "Not  yet added; Stay tuned", //TODO add
                        Toast.LENGTH_LONG).show();
                break;
        }
        return true;
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
//            revealMainLayout();
            mStartLayout.setVisibility(View.GONE);
            mMainLayout.setVisibility(View.VISIBLE);
        } else {

        }

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        window.setNavigationBarColor(ContextCompat.getColor(this, android.R.color.transparent));
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
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                revealMain();
            }
        }, 2000); // afterDelay will be executed after (secs*1000) milliseconds.
    }

    private void revealSplashLayout2() {
        int x = mMainLayout.getWidth() / 2;
        int y = mMainLayout.getHeight() / 2;
        int finalRadius = Math.max(mMainLayout.getWidth(), mMainLayout.getHeight());

        mStartLayout.setVisibility(View.VISIBLE);
        mStartLayout.startAnimation(fadeInAnimation((int) (finalRadius * 0.2), (int) (finalRadius * 3.3)));

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(mMainLayout, x, y, 0, finalRadius).setDuration((long) (finalRadius * 3.8));

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mMainLayout.setVisibility(View.GONE);
            }
        });
        anim.start();
    }

    private void revealSplashLayout() {
        int finalRadius = Math.max(mMainLayout.getWidth(), mMainLayout.getHeight());

        mMainLayout.setVisibility(View.GONE);
        mMainLayout.startAnimation(fadeOutAnimation((int) (finalRadius * 0.2), (int) (finalRadius * 0.3)));

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(mStartLayout, lX, lY, 0, finalRadius).setDuration((long) (finalRadius * 0.8));

        // make the view visible and start the animation
        mStartLayout.setVisibility(View.VISIBLE);
        anim.start();
    }

    private void addFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, new MainFragment());
        fragmentTransaction.commit();
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
        int finalRadius = Math.max(mStartLayout.getWidth(), mStartLayout.getHeight());

        mMainLayout.setVisibility(View.VISIBLE);
        mMainLayout.startAnimation(fadeInAnimation((int) (finalRadius * 0.2), (int) (finalRadius * 0.3)));

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(mStartLayout, x, y, finalRadius, 0).setDuration((long) (finalRadius * 0.8));

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mStartLayout.setVisibility(View.GONE);
            }
        });
        anim.start();
    }

    private Animation fadeInAnimation(int offset, int duration) {
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeInAnimation.setStartOffset(offset);
        fadeInAnimation.setDuration(duration);
        return fadeInAnimation;
    }

    private Animation fadeOutAnimation(int offset, int duration) {
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        fadeOutAnimation.setStartOffset(offset);
        fadeOutAnimation.setDuration(duration);
        return fadeOutAnimation;
    }

}
