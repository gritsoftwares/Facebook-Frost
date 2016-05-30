package com.pitchedapps.facebook.frost.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.utils.ColorUtils;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Post;
import com.sromku.simple.fb.listeners.OnDeleteListener;
import com.sromku.simple.fb.listeners.OnPostListener;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

/**
 * Created by Allan Wang on 2016-05-22.
 */
public class ActionButtons extends LinearLayout {


    private Context mContext;
    private Post mPost;
    private TextView tLike, tComment, tShare;
    private LinearLayout lLike, lComment, lShare;
    private int cNormal, cDisabled;
    private boolean postLiked = false;

    public ActionButtons(Context c) {
        super(c);
        mContext = c;
    }

    public ActionButtons(Context c, AttributeSet attrs) {
        super(c, attrs);
        mContext = c;
    }

    public ActionButtons(Context c, AttributeSet attrs, int defStyle) {
        super(c, attrs, defStyle);
        mContext = c;
    }

    public void initialize(Post p) {
        mPost = p;
//        postLiked = mPost.getIsHidden();
        initializeViews();
        addListeners();
    }

    private void initializeViews() {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.action_buttons, this);

        cNormal = new FrostPreferences(mContext).getTextColor();
        cDisabled = new ColorUtils(mContext).getDisabledTextColor();
        tLike = (TextView) findViewById(R.id.action_button_like_text);
        tComment = (TextView) findViewById(R.id.action_button_comment_text);
        tShare = (TextView) findViewById(R.id.action_button_share_text);

        lLike = (LinearLayout) findViewById(R.id.action_button_like);
        lComment = (LinearLayout) findViewById(R.id.action_button_comment);
        lShare = (LinearLayout) findViewById(R.id.action_button_share);

        tLike.setTextColor(cNormal);
        tComment.setTextColor(cNormal);
        tShare.setTextColor(cNormal);

    }

    private void addListeners() {
        lLike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleFacebook mSimpleFacebook = SimpleFacebook.getInstance();
                e("liked? " + postLiked);
                if (postLiked) {
                    mSimpleFacebook.deleteRequest(mPost.getId() + "/likes", new OnDeleteListener() {
                        @Override
                        public void onComplete(Void response) {
                            super.onComplete(response);
                            tLike.setTextColor(cDisabled);
                        }

                        @Override
                        public void onFail(String reason) {
                            super.onFail(reason);
                            Utils.showSimpleSnackbar(mContext, (View) getParent(), getResources().getString(R.string.like_fail));
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            super.onException(throwable);
                            Utils.showSimpleSnackbar(mContext, (View) getParent(), getResources().getString(R.string.like_fail));
                        }
                    });
                } else {
                    mSimpleFacebook.postRequest(mPost.getId() + "/likes", new OnPostListener() {
                        @Override
                        public void onComplete(Void response) {
                            super.onComplete(response);
                            tLike.setTextColor(cNormal);
                        }

                        @Override
                        public void onFail(String reason) {
                            super.onFail(reason);
                            Utils.showSimpleSnackbar(mContext, (View) getParent(), getResources().getString(R.string.like_fail));
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            super.onException(throwable);
                            Utils.showSimpleSnackbar(mContext, (View) getParent(), getResources().getString(R.string.like_fail));
                        }
                    });
                }
                e("liked??? " + postLiked);

            }
        });
    }


}
