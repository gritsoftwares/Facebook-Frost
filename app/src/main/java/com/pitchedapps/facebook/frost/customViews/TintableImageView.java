package com.pitchedapps.facebook.frost.customViews;

/**
 * Created by Allan Wang on 2016-05-20.
 */

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.pitchedapps.facebook.frost.utils.ColorUtils;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;

/**
 * https://gist.github.com/tylerchesley/5d15d859be4f3ce31213
 *
 * http://stackoverflow.com/a/17788095
 */
public class TintableImageView extends ImageView {

    private ColorStateList tint;
    private int[][] states = new int[][] {
            new int[] { android.R.attr.state_selected},
            new int[] {-android.R.attr.state_selected}
    };

    public TintableImageView(Context context) {
        super(context);
        init(context);
    }

    public TintableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TintableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        FrostPreferences fPrefs = new FrostPreferences(context);

        int[] colors = new int[] {
                fPrefs.getHeaderTextColor(),
                fPrefs.getHeaderDisabledTextColor()
        };

        tint = new ColorStateList(states, colors);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (tint != null && tint.isStateful()) {
            updateTintColor();
        }
    }

    public void setColorFilter(ColorStateList tint) {
        this.tint = tint;
        super.setColorFilter(tint.getColorForState(getDrawableState(), 0));
    }

    private void updateTintColor() {
        int color = tint.getColorForState(getDrawableState(), 0);
        setColorFilter(color);
    }

}