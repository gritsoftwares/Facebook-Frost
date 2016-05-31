package com.pitchedapps.facebook.frost.customViews;

/**
 * Created by Allan Wang on 2016-05-20.
 */

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.utils.ColorUtils;
import com.pitchedapps.facebook.frost.utils.FacebookUtils;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;

public class ReplyBox extends RelativeLayout {

    private Context mContext;
    private FrostPreferences fPrefs;
    private EditText mEditText;
    private Button mPostButton;

    public ReplyBox(Context context) {
        super(context);
        mContext = context;
    }

    public ReplyBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public ReplyBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    public void initialize() {
        initializeViews();
    }

    private void initializeViews() {
        fPrefs = new FrostPreferences(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_reply, this);

        Glide.with(mContext)
                .load(FacebookUtils.getProfileURL())
                .fitCenter()
                .into((ImageView) findViewById(R.id.item_reply_avatar));

        View mDivider = findViewById(R.id.item_reply_divider);
        mDivider.setBackgroundColor(fPrefs.getTextColor());
        mDivider.setAlpha(0.5f);

        mPostButton = (Button) findViewById(R.id.item_reply_post);
        mPostButton.setBackgroundColor(fPrefs.getBackgroundColor());

        mEditText = (EditText) findViewById(R.id.item_reply_editText);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mPostButton.setEnabled(true);
                } else {
                    mPostButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public EditText getEditText() {
        return mEditText;
    }

    public Button getPostButton() {
        return mPostButton;
    }

}