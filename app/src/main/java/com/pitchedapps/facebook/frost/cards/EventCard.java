package com.pitchedapps.facebook.frost.cards;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.pitchedapps.facebook.frost.MainActivity;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;
import com.pitchedapps.facebook.frost.utils.ViewUtils;
import com.sromku.simple.fb.entities.Event;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

/**
 * Created by Allan Wang on 2016-05-20.
 */
public class EventCard extends RecyclerView.ViewHolder {

    private Event sEvent;
    private Context mContext;
    private FrostPreferences fPrefs;
    private ViewUtils vu;

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

        fPrefs = new FrostPreferences(mContext);
        int textColor = fPrefs.getTextColor();
        vu = new ViewUtils(mContext, itemView);

        String title = sEvent.getName();
        String time = sEvent.getStartTime() == null ? "NA" : sEvent.getStartTime().toString();
        String location = sEvent.getPlace() == null ? "NA" : sEvent.getPlace().getName();
        String pictureURL = sEvent.getPictureURL();
        StringBuilder interestedMaybe = new StringBuilder();
        if (sEvent.getAttendingCount() != null) {
            interestedMaybe.append(String.format(s(R.string.x_going), sEvent.getAttendingCount()));
        }
        if (sEvent.getInterestedCount() != null) {
            if (interestedMaybe.length() != 0) {
                interestedMaybe.append("     ");
            }
            interestedMaybe.append(String.format(s(R.string.x_interested), sEvent.getInterestedCount()));
        }
        if (sEvent.getMaybeCount() != null) {
            if (interestedMaybe.length() != 0) {
                interestedMaybe.append("\n");
            }
            interestedMaybe.append(String.format(s(R.string.x_maybe), sEvent.getMaybeCount()));
        }

        Drawable iAttending = new IconicsDrawable(mContext)
                .icon(GoogleMaterial.Icon.gmd_event_available)
                .color(textColor)
                .sizeDp(24);

        Drawable iBusy = new IconicsDrawable(mContext)
                .icon(GoogleMaterial.Icon.gmd_event_busy)
                .color(textColor)
                .sizeDp(24);

        Drawable iInterested = new IconicsDrawable(mContext)
                .icon(GoogleMaterial.Icon.gmd_star)
                .color(textColor)
                .sizeDp(24);

        Drawable iUnsure = new IconicsDrawable(mContext)
                .icon(GoogleMaterial.Icon.gmd_event_note)
                .color(textColor)
                .sizeDp(24);

        Drawable iError = new IconicsDrawable(mContext)
                .icon(GoogleMaterial.Icon.gmd_error_outline)
                .color(textColor)
                .sizeDp(24);

        ImageView iRSVP = (ImageView) itemView.findViewById(R.id.item_event_icon_rsvp);
        if (sEvent.getRsvpStatus() != null) {
            switch (sEvent.getRsvpStatus()) {
                case "unsure":
                    iRSVP.setImageDrawable(iUnsure);
                    break;
                case "attending":
                    iRSVP.setImageDrawable(iAttending);
                    break;
                default:
                    iRSVP.setImageDrawable(iError);
                    e("New RSVP Key: " + sEvent.getRsvpStatus());
                    break;
            }
        }

        ImageView iLocation = (ImageView) itemView.findViewById(R.id.item_event_icon_location);
        iLocation.setImageDrawable(new IconicsDrawable(mContext)
                .icon(GoogleMaterial.Icon.gmd_location_on)
                .color(textColor)
                .sizeDp(24));

//        RelativeLayout card = (RelativeLayout) itemView.findViewById(R.id.item_event_container);

//        if (fPrefs.isDark()) {
//            card.setBackgroundColor(new ColorUtils(mContext).getTintedBackground(0.1f));
//        } else {
//            card.setBackgroundColor(fPrefs.getBackgroundColor());
//        }

        CardView card = (CardView) itemView.findViewById(R.id.item_event_card);
        if (fPrefs.isDark()) {
            card.setBackgroundColor(fPrefs.getBackgroundColorTint1());
        } else {
            card.setBackgroundColor(fPrefs.getBackgroundColor());
        }
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleEventData();
            }
        });

        ImageView mPhoto = (ImageView) itemView.findViewById(R.id.item_event_photo);
        if (pictureURL != null) {
            Glide.with(mContext)
                    .load(pictureURL)
                    .fitCenter()
                    .into(mPhoto);
        } else {
            mPhoto.setVisibility(View.GONE);
        }

        vu.textView(R.id.item_event_title, title);
        vu.textView(R.id.item_event_start_time, time);
        vu.textView(R.id.item_event_location, location);
        TextView stats = vu.textView(R.id.item_event_interested_maybe, interestedMaybe.toString());
        stats.setAlpha(0.7f);

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
    */

    private String s(int id) {
        return mContext.getResources().getString(id);
    }

    private void singleEventData() {

        ((MainActivity)mContext).loadEvent(sEvent);

//        AlertDialogWithCircularReveal p = new AlertDialogWithCircularReveal(mContext, R.layout.overlay_dialog);
////        StringBuilder s = new StringBuilder();
////        String[] ss = {sEvent.getId(), sEvent.getType(), sEvent.getStatusType(), sEvent.getPicture(), "Event C " + sEvent.getEventCount() + " " + JsonUtils.toJson(sEvent.getEvents())};
////        for (String sss : ss) {
////            s.append("\n").append(sss);
////        }
//
//
//        TextView tDialog = (TextView) p.getChildView(R.id.overlay_dialog_content);
//        tDialog.setText(FacebookUtils.printResponseData(sEvent));
//        tDialog.setTextColor(fPrefs.getTextColor());
//        p.showDialog();
    }

    /*
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
