package com.pitchedapps.facebook.frost;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
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
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.pitchedapps.facebook.frost.dialogs.Changelog;
import com.pitchedapps.facebook.frost.dialogs.LogoutDialog;
import com.pitchedapps.facebook.frost.dialogs.OverlayCommentView;
import com.pitchedapps.facebook.frost.interfaces.OnBackPressListener;
import com.pitchedapps.facebook.frost.interfaces.OnTabIconPressListener;
import com.pitchedapps.facebook.frost.utils.AnimUtils;
import com.pitchedapps.facebook.frost.utils.ColorUtils;
import com.pitchedapps.facebook.frost.utils.FragmentUtils;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Comment;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.sromku.simple.fb.utils.Attributes;
import com.sromku.simple.fb.utils.PictureAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

/**
 * Created by 7681 on 2016-05-16.
 */
public class MainActivity extends AppCompatActivity {

    private Button mButtonLogin;
    private TextView mTextStatus;
    private RelativeLayout mStartLayout, mFullLayout;
    private CoordinatorLayout mMainLayout;
    private ViewPager mViewPager;
    private SmartTabLayout mViewPagerTab;
    private Context mContext;
    private ImageView mAvatar;
    private List<OnBackPressListener> mBackPressedListeners = new ArrayList<>();
    private List<OnTabIconPressListener> mOnTabIconPressListener = new ArrayList<>();
    private Toolbar mToolbar;
    private FrostPreferences fPrefs;
    private FloatingActionButton mFAB;
    private Drawable fCreate, fRefresh;
    private FragmentPagerItemAdapter mFPIAdapter;

    private SimpleFacebook mSimpleFacebook;

    private boolean fromError = false, blockBack = false, backPressedWhenBlocked = false;
    private int fadeUnfade = 300; //Used for mTextStatus transitions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        mContext = this;
        mSimpleFacebook = SimpleFacebook.getInstance(this);
        fPrefs = new FrostPreferences(mContext);
        // test local language
        Utils.updateLanguage(getApplicationContext(), "en");
//        Utils.printHashKey(getApplicationContext()); //for testing

        setContentView(R.layout.activity_full);

        mFullLayout = (RelativeLayout) findViewById(R.id.full_layout);
        mMainLayout = (CoordinatorLayout) findViewById(R.id.main_layout);
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

        mFAB = (FloatingActionButton) findViewById(R.id.fab);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showSimpleSnackbar(mContext, mMainLayout, "TEST");
            }
        });


        fCreate = new IconicsDrawable(mContext)
                .icon(GoogleMaterial.Icon.gmd_create)
                .color(fPrefs.getHeaderTextColor())
                .sizeDp(24);
        fRefresh = new IconicsDrawable(mContext)
                .icon(GoogleMaterial.Icon.gmd_refresh)
                .color(fPrefs.getHeaderTextColor())
                .sizeDp(24);

        mFAB.setImageDrawable(fCreate);

        setSupportActionBar(mToolbar);


//        padMain();
        addTabbedContent();
        setUIState();
        setUIColors();
        setLogin();
    }

    private void setUIColors() {
        mToolbar.setBackgroundColor(fPrefs.getHeaderBackgroundColor());
        mToolbar.setTitleTextColor(fPrefs.getHeaderTextColor());
        mToolbar.getOverflowIcon().setColorFilter(fPrefs.getHeaderTextColor(), PorterDuff.Mode.SRC_ATOP);
        mViewPagerTab.setBackgroundColor(fPrefs.getHeaderBackgroundColor());
        mViewPagerTab.setDefaultTabTextColor(fPrefs.getHeaderTextColor());

        if (fPrefs.isDark()) {
            mFullLayout.setBackgroundColor(fPrefs.getBackgroundColor());
        } else {
            mFullLayout.setBackgroundColor(new ColorUtils(mContext).getTintedBackground(0.1f));
        }
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(fPrefs.getHeaderBackgroundColor()); //TODO fix this
//        getWindow().setNavigationBarColor(fPrefs.getBackgroundColor()); //TODO fix this

        mFAB.setColorNormal(fPrefs.getHeaderBackgroundColor());
        mFAB.setColorPressed(new ColorUtils(mContext).getTintedHeaderBackground(0.2f));


    }

    public void loadComments(List<Comment> comments, String postID) {
        OverlayCommentView overlayCommentView = new OverlayCommentView();
        overlayCommentView.initialize(comments, postID);
        overlayCommentView.show(getSupportFragmentManager(), "comment_overlay");
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

        if (data != null) {
            if (data.getExtras().containsKey("colorChange")) {
                if (data.getBooleanExtra("colorChange", false)) this.recreate();
            }
        }
    }

    public void addOnBackPressedListener(OnBackPressListener listener) {
        mBackPressedListeners.add(listener);
    }

    public void removeOnBackPressedListener(OnBackPressListener listener) {
        mBackPressedListeners.remove(listener);
    }

    @Override
    public void onBackPressed() {
        if (blockBack) {
            backPressedWhenBlocked = true;
            return;
        }
        boolean overridden = false;
        for (OnBackPressListener listener : mBackPressedListeners) {
            if (listener != null) {
                overridden |= listener.backPressed();
            }
        }
        if (!overridden) super.onBackPressed();
    }

    private void checkBlockedBackPress() {
        blockBack = false;
        if (backPressedWhenBlocked) {
            backPressedWhenBlocked = false;
            onBackPressed();
        }
    }

    public void addOnTabPressedListener(OnTabIconPressListener listener) {
        mOnTabIconPressListener.add(listener);
    }

    public void removeOnTabPressedListener(OnTabIconPressListener listener) {
        mOnTabIconPressListener.remove(listener);
    }

    private void scrollToTop(int position) {
        if (mOnTabIconPressListener == null) return;
        for (OnTabIconPressListener listener : mOnTabIconPressListener) {
            if (listener != null) {
                listener.tabIconPressed(position);
            }
        }
    }

    private void updateBaseTheme() {
        if (fPrefs.isDark()) {
            if (fPrefs.isTransparent()) {
                setTheme(R.style.Frost_Theme_Transparent);
            }  else {
                setTheme(R.style.Frost_Theme);
            }
        } else {
            if (fPrefs.isTransparent()) {
                setTheme(R.style.Frost_Theme_Light_Transparent);
            } else {
                setTheme(R.style.Frost_Theme_Light);
            }
        }
    }

    private void updateBaseThemeOpaque() {
        if (fPrefs.isDark()) {
            setTheme(R.style.Frost_Theme);
        } else {
            setTheme(R.style.Frost_Theme_Light);
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
        int lX = mToolbar.getWidth() - mToolbar.getHeight() * 2; //TODO make this read clicks
        int lY = mToolbar.getHeight();


        switch (item.getItemId()) {
            case R.id.sendemail:
                Utils.sendEmailWithDeviceInfo(mContext);
                break;
            case R.id.changelog:
                new Changelog(mContext).showWithCircularReveal(lX, (int) (lY * 2.2));
                Set<String> grantedPermissions = SimpleFacebook.getInstance().getGrantedPermissions();
//                e("PERMISSIONS " + grantedPermissions);
                break;
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                LogoutDialog mLogout = new LogoutDialog();
                mLogout.show(getSupportFragmentManager(), "logout_dialog");
                break;
            default:
                Toast.makeText(mContext, "Not  yet added; Stay tuned", //TODO add
                        Toast.LENGTH_LONG).show();
                break;
        }
        return true; //TODO change to true
    }

    public void logout(final Point p) {
        final OnLogoutListener onLogoutListener = new OnLogoutListener() {
            @Override
            public void onLogout() {
                // change the state of the button or do whatever you want
                mButtonLogin.setVisibility(View.VISIBLE);
                mTextStatus.setText(s(R.string.logged_out));

                android.webkit.CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookies(null);

                revealSplashLayout(p);
            }
        };
//        updateBaseThemeOpaque();
        mSimpleFacebook.logout(onLogoutListener);
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

                //reload viewpager
                mViewPager.setAdapter(mFPIAdapter);

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
                                    public void onAnimationStart(Animation animation) {
                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        mTextStatus.setText(String.format(s(R.string.welcome), response.getFirstName() + " " + response.getLastName()));
                                        AnimUtils.fadeIn(mContext, mTextStatus, 0, fadeUnfade);
                                        checkBlockedBackPress();
                                        revealMainLayout();
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {
                                    }
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
            updateMainNavBarColor();
        } else {
            getWindow().setNavigationBarColor(fPrefs.getHeaderBackgroundColor());
        }
    }

    private void updateMainNavBarColor() {
        if (fPrefs.isDark()) {
            getWindow().setNavigationBarColor(fPrefs.getBackgroundColor());
        } else {
            getWindow().setNavigationBarColor(new ColorUtils(mContext).getTintedBackground(0.1f));
        }
    }

    private void revealMainLayout() {
        blockBack = true;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                revealMain();
                updateMainNavBarColor();
            }
        }, 2000); // afterDelay will be executed after (secs*1000) milliseconds.
    }

    private void revealSplashLayout(Point p) {
        double finalRadius = Utils.getScreenDiagonal(mContext);
        AnimUtils.fadeOut(mContext, mMainLayout, finalRadius * 0.05, finalRadius * 0.09);
        AnimUtils.circleReveal(mContext, mStartLayout, p.x, p.y, finalRadius, new AnimUtils.AnimUtilsInterface() {
            @Override
            public void onAnimationEnd() {
                getWindow().setNavigationBarColor(fPrefs.getHeaderBackgroundColor());
            }
        });
    }

    //Animations
    public void revealMain() {
        int x = mStartLayout.getWidth() / 2;
        int y = mStartLayout.getHeight() / 2;
        double finalRadius = Utils.getScreenDiagonal(mContext) / 2;

        AnimUtils.fadeIn(mContext, mMainLayout, finalRadius * 0.3, finalRadius * 0.3);

        // create the animator for this view (the start radius is zero)
        AnimUtils.circleHide(mContext, mStartLayout, x, y, finalRadius, finalRadius * 0.72, new AnimUtils.AnimUtilsInterface() {
            @Override
            public void onAnimationEnd() {
                checkBlockedBackPress();
                updateBaseTheme();
            }
        });
    }

    private void padMain() {
        ViewCompat.setOnApplyWindowInsetsListener(mMainLayout, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                final int statusBar = insets.getSystemWindowInsetTop();
                final int navigationBar = insets.getSystemWindowInsetBottom();
                mMainLayout.setPadding(0, 0, 0, navigationBar);
                mToolbar.setPadding(mToolbar.getPaddingLeft(), mToolbar.getPaddingTop() + statusBar, mToolbar.getPaddingRight(), mToolbar.getPaddingBottom());
                Utils.saveStatusBarHeight(statusBar);
                Utils.saveNavBarHeight(navigationBar);
                return insets;
            }
        });
    }

    private void addTabbedContent() {
        final LayoutInflater inflater = LayoutInflater.from(mContext);

        mViewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                ImageView icon = (ImageView) inflater.inflate(R.layout.smart_tab_icon, container,
                        false);

                icon.setImageDrawable(new IconicsDrawable(mContext)
                        .icon(FragmentUtils.getFrostFragment(position).getTabIcon())
                        .sizeDp(24));
                return icon;
            }
        });

        mViewPagerTab.setOnTabClickListener(new SmartTabLayout.OnTabClickListener() {
            @Override
            public void onTabClicked(int position) {
                if (position == mViewPager.getCurrentItem()) {
                    scrollToTop(position);
//                    FragmentStatePagerItemAdapter fSPIA = new FragmentStatePagerItemAdapter(thi)
//                    switch (position) {
//                        case 1:
//                            break;
//                        default:
//                            break;
//                    }
                }
            }
        });

        mViewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mFAB.setImageDrawable(fCreate);
                        mFAB.show(true);
                        break;
                    case 1:
                        mFAB.setImageDrawable(fRefresh);
                        mFAB.show(true);
                        break;
                    default:
                        mFAB.hide(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        FragmentPagerItems pages = new FragmentPagerItems(mContext);

        for (int i = 0; i < 4; i++) {
            pages.add(FragmentPagerItem.of(s(FragmentUtils.getFrostFragment(i).getTabNameID()), FragmentUtils.getFrostFragment(i).getFragment()));
        }

        mFPIAdapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), pages);

        mViewPager.setAdapter(mFPIAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPagerTab.setViewPager(mViewPager);

    }


    //Because I am lazy
    private String s(int id) {
        return getResources().getString(id);
    }
}
