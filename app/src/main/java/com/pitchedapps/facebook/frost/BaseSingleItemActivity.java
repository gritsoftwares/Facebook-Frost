package com.pitchedapps.facebook.frost;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.pitchedapps.facebook.frost.interfaces.OnBackPressListener;
import com.pitchedapps.facebook.frost.interfaces.OnTabIconPressListener;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;
import com.sromku.simple.fb.SimpleFacebook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 7681 on 2016-05-16.
 */
public class BaseSingleItemActivity extends AppCompatActivity {

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
    private FrostPreferences fPrefs;
    public int textColor, backgroundColor;
    private FloatingActionButton mFAB;
    private Drawable fCreate, fRefresh;
    private FragmentPagerItemAdapter mFPIAdapter;
    private ImageView mToolbarImageView;
    private Toolbar mToolbar;
    private ViewGroup mNestedScrollViewGroup;
    private Drawable mToolbarImage;
    private View mNestedView;
    private String mToolbarImageURL;
    private SimpleFacebook mSimpleFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mSimpleFacebook = SimpleFacebook.getInstance(this);
        fPrefs = new FrostPreferences(mContext);
        textColor = fPrefs.getTextColor();
        backgroundColor = fPrefs.getBackgroundColor();
        setContentView(R.layout.activity_single_image_collapse);

        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.activity_single_collapsing_toolbar);
//        mCollapsingToolbarLayout.setBackgroundColor(backgroundColor);

        mToolbar = (Toolbar) findViewById(R.id.activity_single_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setBackgroundColor(backgroundColor);

        mNestedScrollViewGroup = (ViewGroup) findViewById(R.id.activity_single_nested_scroll_view);

        mToolbarImageView = (ImageView) findViewById(R.id.activity_single_toolbar_image);

        mToolbarImageURL = getToolbarImageURL();

        if (mToolbarImageURL != null) {
            Glide.with(mContext)
                    .load(mToolbarImageURL)
                    .into(mToolbarImageView);
        }

        if (mNestedView != null) {
            mNestedScrollViewGroup.addView(mNestedView);
        }

        updateBaseTheme();
    }

    public String getToolbarImageURL() {
        return null;
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

    private void updateBaseTheme() {
        if (fPrefs.isDark()) {
            setTheme(R.style.Frost_Theme);
        } else {
            setTheme(R.style.Frost_Theme_Light);
        }
    }

    //Because I am lazy
    private String s(int id) {
        return getResources().getString(id);
    }
}
