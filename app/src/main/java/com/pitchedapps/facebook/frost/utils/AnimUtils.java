package com.pitchedapps.facebook.frost.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.pitchedapps.facebook.frost.enums.V;

/**
 * Created by Allan Wang on 2016-05-18.
 */
public class AnimUtils {

    public static void circleReveal(Context c, View v, int x, int y, double radius) {
        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(v, x, y, 0, (int) radius).setDuration((long) (radius * 0.6 * FrostPreferences.getAnimationSpeedFactor(c)));

        // make the view visible and start the animation
        v.bringToFront();
        v.setVisibility(View.VISIBLE);
        anim.start();
    }

    public static void circleReveal(Context c, View v, int x, int y, double radius, double duration) {
        duration *= FrostPreferences.getAnimationSpeedFactor(c);
        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(v, x, y, 0, (int) radius).setDuration((long) duration);

        // make the view visible and start the animation
        v.bringToFront();
        v.setVisibility(View.VISIBLE);
        anim.start();
    }

    public static void circleHide(Context c, final View v, int x, int y, double radius, double duration) {
        duration *= FrostPreferences.getAnimationSpeedFactor(c);
        Animator anim =
                ViewAnimationUtils.createCircularReveal(v, x, y, (int) radius, 0).setDuration((long) duration);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setVisibility(View.GONE);
            }
        });
        anim.start();
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

    public static Animation fadeInAnimation(Context c, double offset, double duration) {
        duration *= FrostPreferences.getAnimationSpeedFactor(c);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        fadeInAnimation.setStartOffset((int) offset);
        fadeInAnimation.setDuration((int) duration);
        return fadeInAnimation;
    }

    public static Animation fadeOutAnimation(Context c, double offset, double duration) {
        duration *= FrostPreferences.getAnimationSpeedFactor(c);
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        fadeOutAnimation.setStartOffset((int) offset);
        fadeOutAnimation.setDuration((int) duration);
        return fadeOutAnimation;
    }

    public static void fadeOut(Context c, View v, double offset, double duration) {
        v.setVisibility(View.GONE);
        v.startAnimation(fadeOutAnimation(c, offset, duration));
    }

    public static void fadeIn(Context c, View v, double offset, double duration) {
        v.setVisibility(View.VISIBLE);
        v.bringToFront();
        v.startAnimation(AnimUtils.fadeInAnimation(c, offset, duration));
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
