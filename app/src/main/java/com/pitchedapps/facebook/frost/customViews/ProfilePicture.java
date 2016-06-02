package com.pitchedapps.facebook.frost.customViews;

/**
 * Created by Allan Wang on 2016-06-02.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.utils.FacebookUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * https://gist.github.com/tylerchesley/5d15d859be4f3ce31213
 *
 * http://stackoverflow.com/a/17788095
 */
public class ProfilePicture extends FrameLayout {

    public ProfilePicture(Context context) {
        super(context);
        init(context);
    }

    public ProfilePicture(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProfilePicture(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.profile_picture, this);

        CircleImageView mProfilePicture = (CircleImageView) findViewById(R.id.profile_picture_image);
        Glide.with(context)
                .load(FacebookUtils.getProfileURL())
                .centerCrop()
                .into(mProfilePicture);
    }


}