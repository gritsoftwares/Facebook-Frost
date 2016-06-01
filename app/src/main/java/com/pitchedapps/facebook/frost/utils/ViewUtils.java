package com.pitchedapps.facebook.frost.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.pitchedapps.facebook.frost.enums.V;

/**
 * Created by Allan Wang on 2016-05-31.
 */
public class ViewUtils {

    private int textColor;
    private View parent;

    public ViewUtils initTextView(int c, View p) {
        textColor = c;
        parent = p;
        return this;
    }

    public TextView tv(int id, String text) {
        TextView mTextView = (TextView) parent.findViewById(id);
        mTextView.setTextColor(textColor);
        if (text != null) {
            mTextView.setText(text);
        }
        return mTextView;
    }

    public Button b(int id, String text) {
        Button mButton = (Button) parent.findViewById(id);
        mButton.setTextColor(textColor);
        if (text != null) {
            mButton.setText(text);
        }
        return mButton;
    }

}
