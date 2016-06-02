package com.pitchedapps.facebook.frost.utils;

import android.content.Context;
import android.graphics.Color;

import java.util.Random;

/**
 * Created by Allan Wang on 2016-05-22.
 */
public class ColorUtils {

    private Context mContext;
    public static int cFACEBOOK = 0xff3b5998;

    public static boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5;
    }

    public static boolean isTransparent(int color) {
        float alpha = Color.alpha(color);
        return (alpha != 255.0f);
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
        float a = (Color.alpha(background));
        float r = (Color.red(tint) * ratio) + (Color.red(background) * inverseRation);
        float g = (Color.green(tint) * ratio) + (Color.green(background) * inverseRation);
        float b = (Color.blue(tint) * ratio) + (Color.blue(background) * inverseRation);
        return Color.argb((int) a, (int) r, (int) g, (int) b);
    }

    public int getDialogBackground() {
        int background = getTintedBackground(0.1f);
        float a = (Color.alpha(background));
        if (a > 155) return background;
        float r = (Color.red(background));
        float g = (Color.green(background));
        float b = (Color.blue(background));
        return Color.argb(155, (int) r, (int) g, (int) b);
    }

    public int getTintedHeaderBackground(float ratio) {
        int background = new FrostPreferences(mContext).getHeaderBackgroundColor();
        int tint = isColorDark(background) ? 0xffffffff : 0xff000000;
        final float inverseRation = 1f - ratio;
        float a = (Color.alpha(background));
        float r = (Color.red(tint) * ratio) + (Color.red(background) * inverseRation);
        float g = (Color.green(tint) * ratio) + (Color.green(background) * inverseRation);
        float b = (Color.blue(tint) * ratio) + (Color.blue(background) * inverseRation);
        return Color.argb((int) a, (int) r, (int) g, (int) b);
    }

    public int getDisabledHeaderTextColor() {
        FrostPreferences fPrefs = new FrostPreferences(mContext);
        int textColor = fPrefs.getHeaderTextColor();
        int bgColor = fPrefs.getHeaderBackgroundColor();

        final float ratio = 0.4f;
        final float inverseRation = 1f - ratio;

        float r = (Color.red(textColor) * ratio) + (Color.red(bgColor) * inverseRation);
        float g = (Color.green(textColor) * ratio) + (Color.green(bgColor) * inverseRation);
        float b = (Color.blue(textColor) * ratio) + (Color.blue(bgColor) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    public int getDisabledTextColor() {
        FrostPreferences fPrefs = new FrostPreferences(mContext);
        int textColor = fPrefs.getTextColor();
        int bgColor = fPrefs.getBackgroundColor();

        final float ratio = 0.4f;
        final float inverseRation = 1f - ratio;

        float r = (Color.red(textColor) * ratio) + (Color.red(bgColor) * inverseRation);
        float g = (Color.green(textColor) * ratio) + (Color.green(bgColor) * inverseRation);
        float b = (Color.blue(textColor) * ratio) + (Color.blue(bgColor) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    public static int randomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
