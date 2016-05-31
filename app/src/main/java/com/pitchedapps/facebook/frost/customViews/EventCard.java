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
import com.sromku.simple.fb.entities.Event;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

/**
 * Created by Allan Wang on 2016-05-20.
 */
public class EventCard extends RecyclerView.ViewHolder {

    private Event sEvent;
    private Context mContext;
    private FrostPreferences fPrefs;

    public EventCard(Context c, View itemView, Event event) {
        super(itemView);

        mContext = c;
        sEvent = event;

//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                singlePostData();
//            }
//        });

        String title = sEvent.getName();
        String description = sEvent.getDescription();
        String location = sEvent.getLocation();
        String pictureURL = sEvent.getPicture();

//        RelativeLayout card = (RelativeLayout) itemView.findViewById(R.id.item_event_container);
        fPrefs = new FrostPreferences(mContext);
        int textColor = fPrefs.getTextColor();
//        if (fPrefs.isDark()) {
//            card.setBackgroundColor(new ColorUtils(mContext).getTintedBackground(0.1f));
//        } else {
//            card.setBackgroundColor(fPrefs.getBackgroundColor());
//        }

        CardView card = (CardView) itemView.findViewById(R.id.item_event_card);
        if (fPrefs.isDark()) {
            card.setBackgroundColor(new ColorUtils(mContext).getTintedBackground(0.1f));
        } else {
            card.setBackgroundColor(fPrefs.getBackgroundColor());
        }

        ImageView mPhoto = (ImageView) itemView.findViewById(R.id.item_event_photo);
        if (pictureURL != null) {
            Glide.with(mContext)
                    .load(pictureURL)
                    .centerCrop()
                    .into(mPhoto);
        } else {
            mPhoto.setVisibility(View.GONE);
        }

        TextView mTitle = (TextView) itemView.findViewById(R.id.item_event_title);
        mTitle.setTextColor(textColor);
        mTitle.setText(title);

        TextView mDesc = (TextView) itemView.findViewById(R.id.item_event_desc);
        mDesc.setTextColor(textColor);
        if (description != null) {
            if (description.length() > 200) {
                description = description.substring(0, 200) + "\u2026";;
            }
            mDesc.setText(description);
        }

        TextView mLocation = (TextView) itemView.findViewById(R.id.item_event_location);
        mLocation.setTextColor(textColor);
        if (location != null) {
            mLocation.setText(location);
        }
    }

    /*
    public void reload() {
        new UpdateSinglePost(this).updateLikes();
    }

    public String getPostID() {
        return sPost.getId();
    }

    public ActionButtons getActionButtons() {
        return mAB;
    }

    private void singlePostData() {
        AlertDialogWithCircularReveal p = new AlertDialogWithCircularReveal(mContext, R.layout.overlay_dialog);
        StringBuilder s = new StringBuilder();
        String[] ss = {sPost.getId(), sPost.getType(), sPost.getStatusType(), sPost.getPicture(), "Event C " + sPost.getEventCount() + " " + JsonUtils.toJson(sPost.getEvents())};
        for (String sss : ss) {
            s.append("\n").append(sss);
        }


        TextView tDialog = (TextView) p.getChildView(R.id.overlay_dialog_content);
        tDialog.setText(s.toString());
        tDialog.setTextColor(fPrefs.getTextColor());
        p.showDialog();
    }

    public Post getPost() {
        return sPost;
    }

    public void updateLikeEventCount(Integer likeCount, Integer eventCount) {
        if (likeCount != null) {
            if (likeCount != 0) {
                tLikeCount.setVisibility(View.VISIBLE);
                StringBuilder sLikeCount = new StringBuilder();
                sLikeCount.append(likeCount);
                if (likeCount == 1) {
                    sLikeCount.append(" Like");
                } else {
                    sLikeCount.append(" Likes");
                }
                tLikeCount.setText(sLikeCount);
            } else {
                tLikeCount.setVisibility(View.GONE);
            }
        }

        if (eventCount != null) {
            if (eventCount != 0) {
                tEventCount.setVisibility(View.VISIBLE);
                StringBuilder sEventCount = new StringBuilder();
                sEventCount.append(eventCount);
                if (eventCount == 1) {
                    sEventCount.append(" Event");
                } else {
                    sEventCount.append(" Events");
                }
                tEventCount.setText(sEventCount);
            } else {
                tEventCount.setVisibility(View.GONE);
            }
        }
    }

    */

}
