package com.pitchedapps.facebook.frost.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

/**
 * Created by Allan Wang on 2016-05-18.
 */
public class AnimUtils {

    public enum V {
        VISIBLE(0),
        INVISIBLE(1),
        GONE(2);
        V(int i) {
            v = i;
        }
        private final int v;
        public int getInt() {
            return v;
        }
    }

    public static void setVisibility(View[] viewList, V visibility) {
        if (viewList == null) return;
        switch (visibility.getInt()) {
            default:
                for (View v : viewList) {
                    v.setVisibility(View.VISIBLE);
                }
                break;
            case 1:
                for (View v : viewList) {
                    v.setVisibility(View.INVISIBLE);
                }
                break;
            case 2:
                for (View v : viewList) {
                    v.setVisibility(View.GONE);
                }
                break;
        }

    }

    public static void sequentialFadeIn(Context c, View[] viewList, int duration) {
        if (viewList == null) return;
        for (int i = 0; i < viewList.length; i++) {
            Animation fadeIn = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
            fadeIn.setDuration(duration);
            fadeIn.setInterpolator(c, android.R.interpolator.accelerate_quint);
            fadeIn.setStartOffset(i * duration / 5);
            viewList[i].startAnimation(fadeIn);
            viewList[i].setVisibility(View.VISIBLE);
        }
    }
}
