package com.pitchedapps.facebook.frost.customViews;

/**
 * Created by Allan Wang on 2016-05-20.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;
import com.pitchedapps.facebook.frost.utils.ViewUtils;

public class ImageTextCheck extends LinearLayout {

    private Context mContext;
    private Drawable mDrawable;
    private String mText;
    private int mSize;
    private ImageView mCheck;

    public ImageTextCheck(Context context) {
        super(context);
        mContext = context;
    }

    public ImageTextCheck(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public ImageTextCheck(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    public void initialize(int textID, IIcon icon, int size) {
        initialize(mContext.getResources().getString(textID), icon, size);
    }

    public void initialize(String text, IIcon icon, int size) {
        int textColor = new FrostPreferences(mContext).getTextColor();
        mSize = size;
        mText = text;
        mDrawable = new IconicsDrawable(mContext)
                .icon(icon)
                .color(textColor)
                .sizeDp(mSize);
        initializeViews();
    }

    private void initializeViews() {
        ViewUtils vu = new ViewUtils(mContext, this);
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.image_text_check, this);

        vu.imageView(R.id.image_text_check_image, mDrawable);
        vu.textView(R.id.image_text_check_text, mText);
        mCheck = vu.imageView(R.id.image_text_check_check, GoogleMaterial.Icon.gmd_check, mSize);
    }

    public void check() {
        mCheck.setVisibility(VISIBLE);
    }

    public void uncheck() {
        mCheck.setVisibility(INVISIBLE);
    }

}