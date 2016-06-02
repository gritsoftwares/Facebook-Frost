package com.pitchedapps.facebook.frost.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Allan Wang on 2016-05-31.
 */
public class ViewUtils {

    private int textColor, disabledTextColor, backgroundColor, dialogBackgroundColor;
    private View parent;

    private ColorStateList mColorStateList;
    private int[][] states = new int[][] {
            new int[] { android.R.attr.state_enabled},
            new int[] {-android.R.attr.state_enabled}
    };

    public ViewUtils(Context c, View p) {
        parent = p;
        FrostPreferences fPrefs = new FrostPreferences(c);
        textColor = fPrefs.getTextColor();
        disabledTextColor = ColorUtils.getTransparentColor(0.5f, textColor);
        backgroundColor = fPrefs.getBackgroundColor();
        dialogBackgroundColor = fPrefs.getDialogBackgroundColor();

        int[] colorStateList = new int[] {
                textColor,
                disabledTextColor
        };

        mColorStateList = new ColorStateList(states, colorStateList);
    }

    public TextView textView(int id, String text) {
        TextView mTextView = (TextView) parent.findViewById(id);
        mTextView.setTextColor(textColor);
        if (text != null) {
            mTextView.setText(text);
        }
        return mTextView;
    }

    public View setBG(int id) {
        View v = parent.findViewById(id);
        v.setBackgroundColor(backgroundColor);
        return v;
    }

    public View setDialogBG(int id) {
        View v = parent.findViewById(id);
        v.setBackgroundColor(dialogBackgroundColor);
        return v;
    }

    public Button button(int id) {
        Button mButton = (Button) parent.findViewById(id);
        mButton.setTextColor(mColorStateList);
        return mButton;
    }

    public Button button(int id, String text) {
        Button mButton = button(id);
        mButton.setTextColor(textColor);
        if (text != null) {
            mButton.setText(text);
        }
        return mButton;
    }



    public EditText editText(int id) {
        EditText mEditText = (EditText) parent.findViewById(id);
        mEditText.getBackground().setColorFilter(textColor, PorterDuff.Mode.SRC_IN);
        mEditText.setTextColor(mColorStateList);
        mEditText.setHintTextColor(disabledTextColor);
        return mEditText;
    }

    public Button buttonWithEditTextDependency(int id, EditText mEditText) {
        final Button mButton = button(id);
        mButton.setEnabled(false);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mButton.setEnabled(true);
                } else {
                    mButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return mButton;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getDialogBackgroundColor() {
        return dialogBackgroundColor;
    }
}
