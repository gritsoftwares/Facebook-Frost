package com.pitchedapps.facebook.frost.utils;

import android.content.Context;
import android.graphics.Color;

/**
 * Created by Allan Wang on 2016-05-22.
 */
public class ColorUtils {

    private Context mContext;

    public static boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5;
    }

    public static boolean isColorBright(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness <= 0.2;
    }

    public ColorUtils(Context c) {
        mContext = c;
    }

    public int getTransparentOverlay(float a) {
        int alpha = (int) (a * 255);
        return (new FrostPreferences(mContext).isDark()) ? Color.argb(alpha, 255, 255, 255) : Color.argb(alpha, 0, 0, 0);
    }

    public int getTintedBackground(float ratio) {
        int background = new FrostPreferences(mContext).getBackgroundColor();
        int tint = isColorDark(background) ? 0xffffffff : 0xff000000;
        final float inverseRation = 1f - ratio;
        float a = (Color.alpha(tint));
        float r = (Color.red(tint) * ratio) + (Color.red(background) * inverseRation);
        float g = (Color.green(tint) * ratio) + (Color.green(background) * inverseRation);
        float b = (Color.blue(tint) * ratio) + (Color.blue(background) * inverseRation);
        return Color.argb((int) a, (int) r, (int) g, (int) b);
    }
}
