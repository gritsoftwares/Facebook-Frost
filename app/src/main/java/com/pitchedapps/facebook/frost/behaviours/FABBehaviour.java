package com.pitchedapps.facebook.frost.behaviours;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.pitchedapps.facebook.frost.utils.Utils;

/**
 * Created by Allan Wang on 2016-05-27.
 * Thanks to CustomBehavior by cstew
 * https://github.com/cstew/CustomBehavior/blob/master/app/src/main/java/com/bignerdranch/android/custombehavior/ShrinkBehavior.java
 * and Tigran
 * http://stackoverflow.com/a/35427564/4407321
 */
public class FABBehaviour extends CoordinatorLayout.Behavior<FloatingActionButton> {
    private int toolbarHeight;

    public FABBehaviour(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.toolbarHeight = Utils.getToolbarHeight(context);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
        return (dependency instanceof AppBarLayout); //TODO fix move on snackbar
//        return (dependency instanceof AppBarLayout || dependency instanceof Snackbar.SnackbarLayout);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
//        if (dependency instanceof Snackbar.SnackbarLayout) {
//            e("snackdep");
//            float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
//            fab.setTranslationY(translationY);
//            return true;
//            float translationY = getFabTranslationYForSnackbar(parent, fab);
//            float percentComplete = -translationY / dependency.getHeight();
////            e("P " + percentComplete);
//            float scaleFactor = 1 - percentComplete;
//            e("SS " + scaleFactor);
////
////            fab.setScaleX(scaleFactor);
////            fab.setScaleY(scaleFactor);
//            return false;
//        } else
        if (dependency instanceof AppBarLayout) {
//            e("appbar");
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            int fabBottomMargin = lp.bottomMargin;
            int distanceToScroll = fab.getHeight() + fabBottomMargin;
            float ratio = (float) dependency.getY() / (float) toolbarHeight;
            fab.setTranslationY(-distanceToScroll * ratio);
        }
//        return super.onDependentViewChanged(parent, fab,dependency);
        return true;
    }

//    private float getFabTranslationYForSnackbar(CoordinatorLayout parent,
//                                                FloatingActionButton fab) {
//        float minOffset = 0;
//        final List<View> dependencies = parent.getDependencies(fab);
//        for (int i = 0, z = dependencies.size(); i < z; i++) {
//            final View view = dependencies.get(i);
//            if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(fab, view)) {
//                minOffset = Math.min(minOffset,
//                        ViewCompat.getTranslationY(view) - view.getHeight());
//            }
//        }
//
//        return minOffset;
//    }
}
