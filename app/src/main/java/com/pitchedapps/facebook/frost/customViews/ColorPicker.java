package com.pitchedapps.facebook.frost.customViews;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pavelsikun.vintagechroma.ChromaDialog;
import com.pavelsikun.vintagechroma.IndicatorMode;
import com.pavelsikun.vintagechroma.OnColorSelectedListener;
import com.pavelsikun.vintagechroma.colormode.ColorMode;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.SettingsActivity;
import com.pitchedapps.facebook.frost.utils.ColorUtils;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Allan Wang on 2016-05-22.
 */
public class ColorPicker extends LinearLayout {

    private TextView mText;
    private CircleImageView mColor;
    private FrostPreferences fPrefs;
    private Context mContext;
    private FragmentManager fManager;
    private String mTextString, mKey;
    private Activity mActivity;

    public ColorPicker(Context c) {
        super(c);
        mContext = c;
    }

    public ColorPicker(Context c, AttributeSet attrs) {
        super(c, attrs);
        mContext = c;
    }

    public ColorPicker(Context c, AttributeSet attrs, int defStyle) {
        super(c, attrs, defStyle);
        mContext = c;
    }

    public void initialize(FragmentManager f, Activity a) {
        fManager = f;
        mActivity = a;
        initializeViews();
    }

    public void setText(String s) {
        mTextString = s;
    }

    private void initializeViews() {
        fPrefs = new FrostPreferences(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.color_picker, this);

        mText = (TextView) findViewById(R.id.color_picker_text);
        mColor = (CircleImageView) findViewById(R.id.color_picker_color);
        mText.setTextColor(fPrefs.getTextColor());

    }

    public void setPrefKey(String s) {
        mKey = s;
        final int color = fPrefs.getInt(s);
        if (mTextString == null) mTextString = s;
        mText.setText(mTextString);
        int borderColor = fPrefs.isDark() ? 0x80ffffff : 0x80000000;
        mColor.setBorderColor(borderColor);
        mColor.setBorderWidth(5);
        mColor.setImageDrawable(new ColorDrawable(color));
        mColor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChromaDialog.Builder()
                        .initialColor(color)
                        .colorMode((mKey.equals(FrostPreferences.TEXT_COLOR)) ? ColorMode.RGB : ColorMode.ARGB)
                        .indicatorMode(IndicatorMode.DECIMAL) //HEX or DECIMAL;
                        .onColorSelected(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int newColor) {
                                switch (mKey) {
                                    case FrostPreferences.BACKGROUND_COLOR_CP:
                                        fPrefs.setBackgroundColorCP(newColor);
                                        break;
                                    case FrostPreferences.TEXT_COLOR_CP:
                                        fPrefs.setTextColorCP(newColor);
                                        break;
                                    default:
                                        fPrefs.setInt(mKey, newColor);
                                        break;
                                }
                                if (color != newColor)
                                    ((SettingsActivity) mActivity).colorChanged();

                            }
                        })
                        .create()
                        .show(fManager, "dialog_" + mKey);
            }
        });
    }

}
