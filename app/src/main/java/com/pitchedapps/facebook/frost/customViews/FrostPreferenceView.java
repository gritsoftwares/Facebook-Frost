package com.pitchedapps.facebook.frost.customViews;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;

import java.util.Random;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

/**
 * Created by Allan Wang on 2016-05-22.
 */
public class FrostPreferenceView extends FrameLayout {

    private Random rnd;
    private FragmentManager fManager;
    private Context mContext;
    private Activity mActivity;
    private FrostPreferences fPrefs;

    public FrostPreferenceView(Context c) {
        super(c);
        mContext = c;
    }

    public FrostPreferenceView(Context c, AttributeSet attrs) {
        super(c, attrs);
        mContext = c;
    }

    public FrostPreferenceView(Context c, AttributeSet attrs, int defStyle) {
        super(c, attrs, defStyle);
        mContext = c;
    }

    public void initialize(FragmentManager f, Activity a) {
        fManager = f;
        mActivity = a;
        initializeViews();
    }

    private void initializeViews() {
        rnd = new Random();
        fPrefs = new FrostPreferences(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.preferences_frost, this);

        ((ScrollView) findViewById(R.id.preferences_frame)).setBackgroundColor(fPrefs.getBackgroundColor());
        TextView t = (TextView) findViewById(R.id.preferences_theme_picker);
        t.setText("Added tv\nTnnnnnn\nTESTETSET\nTESFSEFSEFSEFES\nTTESESSE\nTTEEEE");
        t.setTextColor(fPrefs.getTextColor());
//        t.setBackgroundColor(randomColor());

        ColorPicker cText = (ColorPicker) findViewById(R.id.preferences_text_color);
        cText.initialize(fManager, mActivity);
        cText.setPrefKey(FrostPreferences.TEXT_COLOR);

        ColorPicker cBG = (ColorPicker) findViewById(R.id.preferences_background_color);
        cBG.initialize(fManager, mActivity);
        cBG.setPrefKey(FrostPreferences.BACKGROUND_COLOR);
    }

    private int randomColor() {
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
