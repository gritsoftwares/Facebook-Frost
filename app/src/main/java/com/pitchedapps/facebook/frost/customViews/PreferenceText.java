package com.pitchedapps.facebook.frost.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Allan Wang on 2016-05-27.
 */
public class PreferenceText extends LinearLayout {

    private Context mContext;
    private TextView titleText;
    private Button valueText;
    private String mTitle, mKey;

    public PreferenceText(Context context) {
        super(context);
        mContext = context;
    }

    public PreferenceText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public PreferenceText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public PreferenceText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
    }

    public void initialize(String title) {
        int textColor = new FrostPreferences(mContext).getTextColor();

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.preferences_text, this);

        titleText = (TextView) findViewById(R.id.preferences_text_title);
        valueText = (Button) findViewById(R.id.preferences_text_value);
        titleText.setTextColor(textColor);
        valueText.setTextColor(textColor);
        titleText.setText(title);
        valueText.setBackground(null);
    }

    public Button getValueTextView() {
        return valueText;
    }


}
