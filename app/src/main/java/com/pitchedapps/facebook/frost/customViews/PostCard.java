package com.pitchedapps.facebook.frost.customViews;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.utils.ColorUtils;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;
import com.pitchedapps.facebook.frost.utils.Retrieve;
import com.sromku.simple.fb.entities.Post;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

/**
 * Created by Allan Wang on 2016-05-20.
 */
public class PostCard extends RecyclerView.ViewHolder {

    private Post sPost;
    private Context mContext;
    private FrostPreferences fPrefs;
    private ActionButtons mAB;

    public PostCard(Context c, View itemView, Post p) {
        super(itemView);

        mContext = c;
        sPost = p;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singlePostData();
            }
        });

        String message = sPost.getMessage();
        String story = sPost.getStory();

        CardView card = (CardView) itemView.findViewById(R.id.item_post_card);
        fPrefs = new FrostPreferences(mContext);

        if (fPrefs.isDark()) {
            card.setBackgroundColor(new ColorUtils(mContext).getTintedBackground(0.1f));
        } else {
            card.setBackgroundColor(fPrefs.getBackgroundColor());
        }

        TextView tText = (TextView) itemView.findViewById(R.id.item_post_text);
        tText.setText(message != null ? message : story);
        tText.setTextColor(fPrefs.getTextColor());

        TextView tFrom = (TextView) itemView.findViewById(R.id.item_post_from);
        tFrom.setText(sPost.getFrom().getName());
        tFrom.setTextColor(fPrefs.getTextColor());

        mAB = (ActionButtons) itemView.findViewById(R.id.item_post_action_buttons);
        mAB.initialize(sPost);

        if (sPost.getFrom() != null) {
            ImageView avatar = (ImageView) itemView.findViewById(R.id.item_post_avatar);
            Retrieve.setProfilePhoto(mContext, avatar, sPost.getFrom().getId());
        }

        switch (sPost.getType()) {

            case "photo":
                ImageView photo = (ImageView) itemView.findViewById(R.id.item_post_picture);
                photo.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(sPost.getFullPicture())
//                        .dontTransform() //load full image
                        .into(photo);
                break;
            case "link":
                break;
            case "status":
                break;

            default:
                e("New post type: " + sPost.getType());
                break;
        }


//            AnimUtils.fadeIn(mContext, itemView, pos * 200, 5000);
    }

    private void singlePostData() {
        AlertDialogWithCircularReveal p = new AlertDialogWithCircularReveal(mContext, R.layout.overlay_dialog);
        StringBuilder s = new StringBuilder();
        String[] ss = {sPost.getId(), sPost.getType(), sPost.getStatusType(), sPost.getPicture(), "LIKEC " + sPost.getLikeCount()};
        for (String sss : ss) {
            s.append("\n").append(sss);
        }

        TextView tDialog = (TextView) p.getChildView(R.id.overlay_dialog_content);
        tDialog.setText(s.toString());
        tDialog.setTextColor(fPrefs.getTextColor());
        p.showDialog();
    }
}
