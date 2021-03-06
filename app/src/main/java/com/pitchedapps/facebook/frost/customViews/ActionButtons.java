package com.pitchedapps.facebook.frost.customViews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.pitchedapps.facebook.frost.MainActivity;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.cards.PostCard;
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
    private PostCard mPostCard;
    private TextView tLike, tComment, tShare;
    private LinearLayout lLike, lComment, lShare;
    private ImageView iLike, iComment, iShare;
    private int cNormal, cDisabled, cEmphasis;
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

    public void initialize(PostCard pc) {
        mPostCard = pc;
        mPost = mPostCard.getPost();
        postLiked = mPost.hasLiked();
        initializeViews();
        addListeners();
    }

    private void initializeViews() {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.action_buttons, this);

        FrostPreferences fPrefs = new FrostPreferences(mContext);

        cNormal = fPrefs.getTextColor();
        cDisabled = new ColorUtils(mContext).getDisabledTextColor();
        cEmphasis = fPrefs.getAccentColor();

        tLike = (TextView) findViewById(R.id.action_button_like_text);
        tComment = (TextView) findViewById(R.id.action_button_comment_text);
        tShare = (TextView) findViewById(R.id.action_button_share_text);
        tLike.setTextColor(cNormal);
        tComment.setTextColor(cNormal);
        tShare.setTextColor(cNormal);

        Drawable dLike = new IconicsDrawable(mContext)
                .icon(GoogleMaterial.Icon.gmd_thumb_up)
                .color(cNormal)
                .sizeDp(16);

        Drawable dComment = new IconicsDrawable(mContext)
                .icon(MaterialDesignIconic.Icon.gmi_comment)
                .color(cNormal)
                .sizeDp(16);

        Drawable dShare = new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_share)
                .color(cNormal)
                .sizeDp(16);

        iLike = (ImageView) findViewById(R.id.action_button_like_image);
        iComment = (ImageView) findViewById(R.id.action_button_comment_image);
        iShare = (ImageView) findViewById(R.id.action_button_share_image);
        iLike.setImageDrawable(dLike);
        iComment.setImageDrawable(dComment);
        iShare.setImageDrawable(dShare);

        updateHasLiked(postLiked);

        lLike = (LinearLayout) findViewById(R.id.action_button_like);
        lComment = (LinearLayout) findViewById(R.id.action_button_comment);
        lShare = (LinearLayout) findViewById(R.id.action_button_share);

    }

    public void updateHasLiked(boolean b) {
        postLiked = b;
        if (b) {
            iLike.setColorFilter(cEmphasis);
        } else {
            iLike.setColorFilter(cNormal);
        }
    }

    public void showLikeError() {
        Utils.showSimpleSnackbar(mContext, (View) getParent(), getResources().getString(R.string.like_fail));
    }

    private void addListeners() {
        lLike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleFacebook mSimpleFacebook = SimpleFacebook.getInstance();
                if (postLiked) {
                    mSimpleFacebook.deleteRequest(mPost.getId() + "/likes", new OnDeleteListener() {
                        @Override
                        public void onComplete(Void response) {
                            super.onComplete(response);
                            mPostCard.reload();
                        }

                        @Override
                        public void onFail(String reason) {
                            super.onFail(reason);
                            showLikeError();
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            super.onException(throwable);
                            showLikeError();
                        }
                    });
                } else {
                    mSimpleFacebook.postRequest(mPost.getId() + "/likes", new OnPostListener() {
                        @Override
                        public void onComplete(Void response) {
                            super.onComplete(response);
                            mPostCard.reload();
                        }

                        @Override
                        public void onFail(String reason) {
                            super.onFail(reason);
                            showLikeError();
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            super.onException(throwable);
                            showLikeError();
                        }
                    });
                }
            }
        });

        lComment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof MainActivity) {
                    MainActivity mActivty = (MainActivity)mContext;
                    mActivty.loadComments(mPost.getComments(), mPost.getId());
                } else {
                    e("mCONTEXT NOT INSTANCE");
                }
            }
        });

        lShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
