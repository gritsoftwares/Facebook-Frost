package com.pitchedapps.facebook.frost;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.PagerAdapter;
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
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.pitchedapps.facebook.frost.customViews.Changelog;
import com.pitchedapps.facebook.frost.fragments.DemoFragment;
import com.pitchedapps.facebook.frost.fragments.GetPhotosFragment;
import com.pitchedapps.facebook.frost.fragments.NewsFeedFragment;
import com.pitchedapps.facebook.frost.fragments.ProfileFragment;
import com.pitchedapps.facebook.frost.utils.AnimUtils;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.sromku.simple.fb.utils.Attributes;
import com.sromku.simple.fb.utils.PictureAttributes;

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
    private ViewPager mViewPager;
    private SmartTabLayout mViewPagerTab;
    private Context mContext;
    private ImageView mAvatar;

    private Toolbar mToolbar;

    private SimpleFacebook mSimpleFacebook;

    private boolean fromError = false, blockBack = false, backPressedWhenBlocked = false;
    private int fadeUnfade = 300; //Used for mTextStatus transitions
    private int lX = 0, lY = 0;

    public static int[] tabs = {
            R.string.demo_tab_1,
            R.string.demo_tab_2,
            R.string.demo_tab_3,
//            R.string.demo_tab_4,
            R.string.demo_tab_5
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);

        mContext = this;
        mSimpleFacebook = SimpleFacebook.getInstance(this);

        // test local language
        Utils.updateLanguage(getApplicationContext(), "en");
//        Utils.printHashKey(getApplicationContext()); //for testing

        setContentView(R.layout.activity_full);

        mMainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        mStartLayout = (RelativeLayout) findViewById(R.id.startup_layout);
        mButtonLogin = (Button) findViewById(R.id.button_login_splash);
        mTextStatus = (TextView) findViewById(R.id.text_status_splash);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        mAvatar = (ImageView) findViewById(R.id.profilePicture_splash);

        mStartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromError) {
                    revealMain();
                    fromError = false;
                }
            }
        });

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(ContextCompat.getColor(mContext, R.color.facebook_blue_dark)); //TODO fix this

        padMain();
        addTabbedContent();
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

    @Override
    public void onBackPressed() {
        if (blockBack) {
            backPressedWhenBlocked = true;
            return;
        }
        super.onBackPressed();
    }

    private void checkBlockedBackPress() {
        blockBack = false;
        if (backPressedWhenBlocked) {
            backPressedWhenBlocked = false;
            onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        lX = (mToolbar.getWidth() - mToolbar.getHeight() * 2); //TODO make this read clicks
        lY = (int) (mToolbar.getHeight() * 4.2);

        final OnLogoutListener onLogoutListener = new OnLogoutListener() {
            @Override
            public void onLogout() {
                // change the state of the button or do whatever you want
                mButtonLogin.setVisibility(View.VISIBLE);
                mTextStatus.setText(s(R.string.logged_out));
                revealSplashLayout();
            }
        };


        switch (item.getItemId()) {
            case R.id.sendemail:
                Utils.sendEmailWithDeviceInfo(mContext);
                break;
            case R.id.changelog:
//                new Changelog(mContext).show();
                new Changelog(mContext).showWithCircularReveal(lX, (int) (lY * 2.2/4.2));
//                new AlertDialogWithCircularReveal(mContext, R.layout.changelog_content).showDialog();
                break;
            case R.id.update_url:
                Utils.openLinkInChromeCustomTab(mContext, s(R.string.host_update_url));
                break;
            case R.id.logout:
                mSimpleFacebook.logout(onLogoutListener);
//                logoutClick = true;
                break;
            default:
                Toast.makeText(mContext, "Not  yet added; Stay tuned", //TODO add
                        Toast.LENGTH_LONG).show();
                break;
        }
        return true; //TODO change to true
    }

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
                mTextStatus.setText(String.format(s(R.string.exception), throwable.getMessage()));
                e("MainActivity Exception: " + throwable);
            }

            @Override
            public void onLogin(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
                // change the state of the button or do whatever you want
                mTextStatus.setText(s(R.string.logged_in));
                mButtonLogin.setVisibility(View.GONE);
                PictureAttributes pictureAttributes = Attributes.createPictureAttributes();
                pictureAttributes.setHeight(500);
                pictureAttributes.setWidth(500);
                pictureAttributes.setType(PictureAttributes.PictureType.SQUARE);
                Profile.Properties properties = new Profile.Properties.Builder()
                        .add(Profile.Properties.PICTURE, pictureAttributes)
                        .add(Profile.Properties.FIRST_NAME)
                        .add(Profile.Properties.LAST_NAME)
                        .build();

                SimpleFacebook.getInstance().getProfile(properties, new OnProfileListener() {

                    @Override
                    public void onException(Throwable throwable) {
                        continueFromError(throwable.getMessage());
                    }

                    @Override
                    public void onFail(String reason) {
                        continueFromError(reason);
                    }

                    @Override
                    public void onComplete(final Profile response) {
                        Glide.with(getApplicationContext())
                                .load(response.getPicture())
                                .into(mAvatar);

                        blockBack = true;
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Animation fadeText = AnimUtils.fadeOutAnimation(mContext, 0, fadeUnfade);
                                fadeText.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) { }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        mTextStatus.setText(String.format(s(R.string.welcome), response.getFirstName() + " " + response.getLastName()));
                                        AnimUtils.fadeIn(mContext, mTextStatus, 0, fadeUnfade);
                                        checkBlockedBackPress();
                                        revealMainLayout();
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) { }
                                });

                                mTextStatus.setVisibility(View.INVISIBLE);
                                mTextStatus.startAnimation(fadeText);
                            }
                        }, 2000);
                    }
                });
            }

            @Override
            public void onCancel() {
                mTextStatus.setText(s(R.string.cancelled));
            }

        };

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mSimpleFacebook.login(onLoginListener);
            }
        });
    }

    private void continueFromError(final String s) {
        fromError = true;
        Animation fadeText = AnimUtils.fadeOutAnimation(mContext, 0, fadeUnfade);
        fadeText.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mTextStatus.setText(String.format(s(R.string.splash_continue_from_error), s));
                AnimUtils.fadeIn(mContext, mTextStatus, 0, fadeUnfade);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mTextStatus.setVisibility(View.INVISIBLE);
        mTextStatus.startAnimation(fadeText);
    }

    private void setUIState() {
        if (mSimpleFacebook.isLogin()) { //direct reveal without animations
            mStartLayout.setVisibility(View.GONE);
            mMainLayout.setVisibility(View.VISIBLE);
        }
    }

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

        AnimUtils.fadeOut(mContext, mMainLayout, finalRadius * 0.2, finalRadius * 0.3);
        AnimUtils.circleReveal(mStartLayout, lX, lY, finalRadius * 1.2, finalRadius);
    }

    //Animations
    public void revealMain() {
        int x = mStartLayout.getWidth() / 2;
        int y = mStartLayout.getHeight() / 2;
        double finalRadius = Math.max(mStartLayout.getWidth(), mStartLayout.getHeight());
        finalRadius *= 0.6;

        AnimUtils.fadeIn(mContext, mMainLayout, finalRadius * 0.3, finalRadius * 0.5);

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(mStartLayout, x, y, (int) finalRadius, 0).setDuration((long) (finalRadius * 1.2));

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mStartLayout.setVisibility(View.GONE);
                checkBlockedBackPress();
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
                Utils.saveNavBarHeight(navigationBar);
                return insets;
            }
        });
    }

    private void addTabbedContent() {
        final LayoutInflater inflater = LayoutInflater.from(mContext);

        final Drawable tProfile = new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_account)
                .sizeDp(24);

        final Drawable tFriends = new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_account)
                .sizeDp(24);

        final Drawable tNotifications = new IconicsDrawable(mContext)
                .icon(MaterialDesignIconic.Icon.gmi_notifications)
                .sizeDp(24);

        final Drawable tNewsFeed = new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_newspaper)
                .sizeDp(24);

        mViewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                ImageView icon = (ImageView) inflater.inflate(R.layout.smart_tab_icon, container,
                        false);
                switch (position) {
                    case 0:
                        icon.setImageDrawable(tProfile);
                        break;
                    case 1:
                        icon.setImageDrawable(tFriends);
                        break;
                    case 2:
                        icon.setImageDrawable(tNotifications);
                        break;
                    case 3:
                        icon.setImageDrawable(tNewsFeed);
                        break;
                    default:
                        throw new IllegalStateException("Invalid position: " + position);
                }
                return icon;
            }
        });

        FragmentPagerItems pages = new FragmentPagerItems(mContext);

//        Fragment fProfile = new ProfileFragment();
        for (int i = 0; i < tabs.length; i++) {
            switch (i) {
                case 0:
                    pages.add(FragmentPagerItem.of(getString(tabs[i]), ProfileFragment.class));
                    break;
                case 1:
                    pages.add(FragmentPagerItem.of(getString(tabs[i]), GetPhotosFragment.class));
                    break;
                case 3:
                    pages.add(FragmentPagerItem.of(getString(tabs[i]), NewsFeedFragment.class));
                    break;
                default:
                    pages.add(FragmentPagerItem.of(getString(tabs[i]), DemoFragment.class));
                    break;
            }
        }

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), pages);

        mViewPager.setAdapter(adapter);
        mViewPagerTab.setViewPager(mViewPager);
    }

    //Because I am lazy
    private String s(int id) {
        return getResources().getString(id);
    }

}
