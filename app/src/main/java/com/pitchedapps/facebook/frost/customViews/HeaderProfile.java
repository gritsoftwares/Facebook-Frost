package com.pitchedapps.facebook.frost.customViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.sromku.simple.fb.entities.Profile;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Allan Wang on 2016-05-20.
 */
public class HeaderProfile extends RecyclerView.ViewHolder {

    private TextView mHeader;
    private ImageView mCover, mProfile, mbEmail, mbGender, mbLocation, mbBirthday;
    private Drawable iMale, iFemale;
    private Context mContext;
    private Profile sProfile;
    private FrostPreferences fPrefs;

    public HeaderProfile(Context c, View view, Profile p) {
        super(view);

        mContext = c;
        sProfile = p;
        fPrefs = new FrostPreferences(mContext);

        mCover = (ImageView) view.findViewById(R.id.header_profile_cover);
        mbEmail = (ImageView) view.findViewById(R.id.header_profile_buttons_email);
        mbGender = (ImageView) view.findViewById(R.id.header_profile_buttons_gender);
        mbLocation = (ImageView) view.findViewById(R.id.header_profile_buttons_location);
        mbBirthday = (ImageView) view.findViewById(R.id.header_profile_buttons_birthday);
        mProfile = (ImageView) view.findViewById(R.id.header_profile_photo);
        mHeader = (TextView) view.findViewById(R.id.header_profile_header);

        mHeader.setTextColor(fPrefs.getTextColor());
        int buttonColor = fPrefs.getTextColor();

        //Buttons
        Drawable iEmail = new IconicsDrawable(mContext)
                .icon(MaterialDesignIconic.Icon.gmi_email)
                .color(buttonColor)
                .sizeDp(24);
        mbEmail.setImageDrawable(iEmail);
        iMale = new IconicsDrawable(mContext)
                .icon(MaterialDesignIconic.Icon.gmi_male_alt)
                .color(buttonColor)
                .sizeDp(24);
        iFemale = new IconicsDrawable(mContext)
                .icon(MaterialDesignIconic.Icon.gmi_female)
                .color(buttonColor)
                .sizeDp(24);
        Drawable iMaleFemale = new IconicsDrawable(mContext)
                .icon(MaterialDesignIconic.Icon.gmi_male_female)
                .color(buttonColor)
                .sizeDp(24);
        Drawable iLocation = new IconicsDrawable(mContext)
                .icon(MaterialDesignIconic.Icon.gmi_globe)
                .color(buttonColor)
                .sizeDp(24);
        mbLocation.setImageDrawable(iLocation);
        Drawable iBirthday = new IconicsDrawable(mContext)
                .icon(MaterialDesignIconic.Icon.gmi_cake)
                .color(buttonColor)
                .sizeDp(24);
        mbBirthday.setImageDrawable(iBirthday);

        if (sProfile.getCover() != null) { //TODO learn more about cover
            Glide.with(mContext)
                    .load(sProfile.getCover().toString())
                    .centerCrop()
                    .into(mCover);
        }

        if (sProfile.getPicture() != null) {
            Glide.with(mContext)
                    .load(sProfile.getPicture())
                    .centerCrop()
                    .into(mProfile);
        }

        if (sProfile.getGender() == null) {
            mbGender.setImageDrawable(iMaleFemale);
        } else {
            if (sProfile.getGender().equalsIgnoreCase("male")) {
                mbGender.setImageDrawable(iMale);
            } else if (sProfile.getGender().equalsIgnoreCase("female")) {
                mbGender.setImageDrawable(iFemale);
            } else {
                mbGender.setImageDrawable(iMaleFemale);
            }
        }

        mbEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sProfile.getEmail() != null) {
                    Utils.sendEmailFromFrost(mContext, sProfile.getEmail());
                } else {
                    Utils.showSimpleSnackbar(mContext, (View) mHeader.getParent().getParent(), mContext.getResources().getString(R.string.no_email_found));
                }
            }
        });

        mbGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogWithCircularReveal d = new AlertDialogWithCircularReveal(mContext, R.layout.overlay_dialog);
                d.setRippleStart(Utils.getLocation(mbGender));
                TextView t = (TextView) d.getChildView(R.id.overlay_dialog_title);
                t.setText(String.format(mContext.getResources().getString(R.string.about_user), sProfile.getFirstName()));
                t.setTextColor(fPrefs.getTextColor());

                StringBuilder content = new StringBuilder();

                try {
                    for (Field f : sProfile.getClass().getDeclaredFields()) {
                        f.setAccessible(true);
                        if (Modifier.isStatic(f.getModifiers())) {
                            continue;
                        }
                        if (f.getName().equalsIgnoreCase("mBio") ||
                                f.getName().equalsIgnoreCase("mGender") ||
                                f.getName().equalsIgnoreCase("mEducation") ||
                                f.getName().equalsIgnoreCase("mFavoriteAthletes") ||
                                f.getName().equalsIgnoreCase("mFavoriteTeams") ||
                                f.getName().equalsIgnoreCase("mLanguages") ||
                                f.getName().equalsIgnoreCase("mRelationshipStatus") ||
                                f.getName().equalsIgnoreCase("mWebsite")) {
                            content.append("\n\u2022").append(f.getName().substring(1)).append(" ").append(f.get(sProfile));
                        }
                    }
                } catch (IllegalAccessException e) {
                    //do nothing
                }

                TextView t2 = (TextView) d.getChildView(R.id.overlay_dialog_content);
                t2.setText(content.toString());
                t2.setTextColor(fPrefs.getTextColor());
                d.showDialog();
            }
        });


        mbBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogWithCircularReveal d = new AlertDialogWithCircularReveal(mContext, R.layout.overlay_dialog);
                d.setRippleStart(Utils.getLocation(mbBirthday));
                TextView t = (TextView) d.getChildView(R.id.overlay_dialog_title);
                t.setText(String.format(mContext.getResources().getString(R.string.about_user), sProfile.getFirstName()));
                t.setTextColor(fPrefs.getTextColor());

                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                Date date = null;
                try {
                    date = formatter.parse(sProfile.getBirthday());//catch exception
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                Calendar thatDay = Calendar.getInstance();
                thatDay.setTime(date);

                Calendar today = Calendar.getInstance();
                long diff = today.getTimeInMillis() - thatDay.getTimeInMillis();
                long days = diff / (24 * 60 * 60 * 1000);

                TextView t2 = (TextView) d.getChildView(R.id.overlay_dialog_content);
                t2.setText(String.format(mContext.getResources().getString(R.string.birthday_currently), sProfile.getBirthday(), days));
                t2.setTextColor(fPrefs.getTextColor());

                d.showDialog();
            }
        });

        String header = sProfile.getFirstName() + " " + sProfile.getLastName();
        if (header.equals("null null")) {
            mHeader.setText(R.string.unnamed);
        } else {
            mHeader.setText(header);
        }

//            AnimUtils.fadeIn(mContext, itemView, pos * 200, 5000);
    }

}
