package com.pitchedapps.facebook.frost.customViews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.utils.Retrieve;
import com.sromku.simple.fb.entities.Post;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

/**
 * Created by Allan Wang on 2016-05-20.
 */
public class PostCard extends RecyclerView.ViewHolder {

    public PostCard(final Context mContext, final View itemView, final Post sPost) {
        super(itemView);
//            itemView.setVisibility(View.INVISIBLE);

        String description;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singlePostData(mContext, sPost);
            }
        });

        String message = sPost.getMessage();
        String story = sPost.getStory();
        ((TextView) itemView.findViewById(R.id.item_post_text)).setText(message != null ? message : story);

        ((TextView) itemView.findViewById(R.id.item_post_from)).setText(sPost.getFrom().getName());


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
                        .centerCrop()
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

    private void singlePostData(Context mContext, Post post) {
        AlertDialogWithCircularReveal p = new AlertDialogWithCircularReveal(mContext, R.layout.overlay_dialog);
//                e("P " + response.get(0).getProperties());
        StringBuilder s = new StringBuilder();
        String[] ss = {post.getId(), post.getType(), post.getStatusType(), post.getPicture()};
        for (String sss : ss) {
            s.append("\n" + sss);
        }

        ((TextView) p.getChildView(R.id.overlay_dialog_content)).setText(s.toString());
        p.showDialog();
    }
}
