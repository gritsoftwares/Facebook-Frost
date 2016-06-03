package com.pitchedapps.facebook.frost.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

/**
 * Created by Allan Wang on 2016-05-31.
 */
public class ViewUtils {

    private int textColor, disabledTextColor, backgroundColor, dialogBackgroundColor;
    private View parent;
    private Context mContext;

    private ColorStateList mColorStateList;
    private int[][] states = new int[][] {
            new int[] { android.R.attr.state_enabled},
            new int[] {-android.R.attr.state_enabled}
    };

    public ViewUtils(Context c, View p) {
        parent = p;
        mContext = c;
        FrostPreferences fPrefs = new FrostPreferences(mContext);
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

    public ImageView imageView(int id, IIcon icon, int size) {
        ImageView mImageView = (ImageView) parent.findViewById(id);
        mImageView.setImageDrawable(new IconicsDrawable(mContext)
                .icon(icon)
                .color(textColor)
                .sizeDp(size));
        return mImageView;
    }

    public ImageView imageView(int id, Drawable d) {
        ImageView mImageView = (ImageView) parent.findViewById(id);
        mImageView.setImageDrawable(d);
        return mImageView;
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
