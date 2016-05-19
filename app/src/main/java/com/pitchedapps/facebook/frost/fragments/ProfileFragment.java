package com.pitchedapps.facebook.frost.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.adapters.EmptyAdapter;
import com.pitchedapps.facebook.frost.adapters.PostAdapter;
import com.pitchedapps.facebook.frost.customViews.AlertDialogWithCircularReveal;
import com.pitchedapps.facebook.frost.exampleFragments.BaseFragment;
import com.pitchedapps.facebook.frost.utils.AnimUtils;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Post;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnPostsListener;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.sromku.simple.fb.utils.Attributes;
import com.sromku.simple.fb.utils.PictureAttributes;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

public class ProfileFragment extends BaseFragment {

    private final static String EXAMPLE = "Profile";

    private TextView mHeader;
    private ImageView mCover, mProfile, mbEmail, mbGender, mbLocation, mbBirthday;
    private Drawable iMale, iFemale;
    private SwipeRefreshLayout mRefresh;
    private RecyclerView mRV;
    private Context mContext;
    private View[] viewList;
    private boolean firstRun = true, firstRun2 = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mContext = getActivity();
        getViews(view);
        getData();


//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
//                null, getResources().getDimensionPixelSize(R.dimen.dividers_height), false, true));
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(EXAMPLE);
    }


    private void getViews(View view) {
        mCover = (ImageView) view.findViewById(R.id.profile_cover);
        mbEmail = (ImageView) view.findViewById(R.id.profile_buttons_email);
        mbGender = (ImageView) view.findViewById(R.id.profile_buttons_gender);
        mbLocation = (ImageView) view.findViewById(R.id.profile_buttons_location);
        mbBirthday = (ImageView) view.findViewById(R.id.profile_buttons_birthday);
        mProfile = (ImageView) view.findViewById(R.id.profile_photo);
        mHeader = (TextView) view.findViewById(R.id.profile_header);
        mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.profile_refresh);
        mRV = (RecyclerView) view.findViewById(R.id.profile_rv);
        LinearLayoutManager mLLM = new LinearLayoutManager(mContext);
        mLLM.setOrientation(LinearLayoutManager.VERTICAL);
        mRV.setLayoutManager(mLLM);
        mRV.setAdapter(new EmptyAdapter()); //Set empty adapter so error does not occur
        mRV.setFocusable(false); //Do not jump to mRV when the animation starts
        if (firstRun) {
            viewList = new View[]{mbEmail, mbGender, mbBirthday, mbLocation};
            AnimUtils.setVisibility(viewList, AnimUtils.V.INVISIBLE);
        }

        if (firstRun2) {
            mRV.setVisibility(View.INVISIBLE);
        }

        int buttonColor = ContextCompat.getColor(mContext, R.color.profile_buttons);

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
        mbGender.setImageDrawable(iMaleFemale);
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

        //Refresh
        mRefresh.setEnabled(true);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    private void getData() {
        PictureAttributes pictureAttributes = Attributes.createPictureAttributes();
        pictureAttributes.setHeight(500);
        pictureAttributes.setWidth(500);
        pictureAttributes.setType(PictureAttributes.PictureType.SQUARE);
        Profile.Properties properties = new Profile.Properties.Builder()
                .add(Profile.Properties.PICTURE, pictureAttributes)
                .add(Profile.Properties.AGE_RANGE)
                .add(Profile.Properties.BIRTHDAY)
                .add(Profile.Properties.BIO)
                .add(Profile.Properties.COVER)
//                .add(Profile.Properties.CURRENCY)
//                .add(Profile.Properties.DEVICES)
                .add(Profile.Properties.EDUCATION)
                .add(Profile.Properties.EMAIL)
                .add(Profile.Properties.FAVORITE_TEAMS)
                .add(Profile.Properties.FAVORITE_ATHLETES)
                .add(Profile.Properties.FIRST_NAME)
                .add(Profile.Properties.GENDER)
                .add(Profile.Properties.HOMETOWN)
                .add(Profile.Properties.INTERESTED_IN)
                .add(Profile.Properties.ID)
                .add(Profile.Properties.INSTALLED)
                .add(Profile.Properties.LANGUAGE)
                .add(Profile.Properties.LOCALE)
                .add(Profile.Properties.LOCATION)
                .add(Profile.Properties.LAST_NAME)
                .add(Profile.Properties.LINK)
                .add(Profile.Properties.MIDDLE_NAME)
                .add(Profile.Properties.NAME)
                .add(Profile.Properties.POLITICAL)
                .add(Profile.Properties.QUOTES)
                .add(Profile.Properties.RELATIONSHIP_STATUS)
                .add(Profile.Properties.RELIGION)
                .add(Profile.Properties.TIMEZONE)
                .add(Profile.Properties.UPDATED_TIME)
                .add(Profile.Properties.VERIFIED)
                .add(Profile.Properties.WEBSITE)
                .add(Profile.Properties.WORK)
                .build();

        SimpleFacebook.getInstance().getProfile(properties, new OnProfileListener() {

            @Override
            public void onThinking() {
                mRefresh.setRefreshing(true);
            }

            @Override
            public void onException(Throwable throwable) {
                mRefresh.setRefreshing(false);
                Utils.showSimpleSnackbar(mContext, mRefresh, throwable.getMessage());
            }

            @Override
            public void onFail(String reason) {
                mRefresh.setRefreshing(false);
                Utils.showSimpleSnackbar(mContext, mRefresh, reason);
            }

            @Override
            public void onComplete(Profile response) {
                mRefresh.setRefreshing(false);
                updateProfileContent(response);
            }
        });

        SimpleFacebook.getInstance().getPosts(Post.PostType.POSTS, new OnPostsListener() {

            @Override
            public void onThinking() {
                mRefresh.setRefreshing(true);
            }

            @Override
            public void onException(Throwable throwable) {
                mRefresh.setRefreshing(false);
                Utils.showSimpleSnackbar(mContext, mRefresh, throwable.getMessage());
            }

            @Override
            public void onFail(String reason) {
                mRefresh.setRefreshing(false);
                Utils.showSimpleSnackbar(mContext, mRefresh, reason);
            }

            @Override
            public void onComplete(List<Post> response) {
                mRefresh.setRefreshing(false);
                updatePostContent(response);
            }
        });
    }

    private void updateProfileContent(final Profile response) {

//        printProfileData(response);
//                e(response.getAgeRange().getMax() + "-" + response.getAgeRange().getMin());
        if (!response.getCover().toString().equals("null")) { //TODO learn more about cover
            Glide.with(mContext)
                    .load(response.getCover().toString())
                    .centerCrop()
                    .into(mCover);
        }

        if (response.getPicture() != null) {
            Glide.with(mContext)
                    .load(response.getPicture())
                    .centerCrop()
                    .into(mProfile);
        }

        if (response.getGender().equalsIgnoreCase("male")) {
            mbGender.setImageDrawable(iMale);
        } else if (response.getGender().equalsIgnoreCase("female")) {
            mbGender.setImageDrawable(iFemale);
        }

        mbEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (response.getEmail() != null) {
                    Utils.sendEmailFromFrost(mContext, response.getEmail());
                } else {
                    Utils.showSimpleSnackbar(mContext, mRefresh, "No email found");
                }
            }
        });

        mbGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogWithCircularReveal d = new AlertDialogWithCircularReveal(mContext, R.layout.overlay_dialog);
                d.setDuration(2000);
                d.setRippleStart(Utils.getLocation(mbGender));
                TextView t = (TextView) d.getChildView(R.id.overlay_dialog_title);
                t.setText("About " + response.getFirstName());

                StringBuilder content = new StringBuilder();

                try {
                    for (Field f : response.getClass().getDeclaredFields()) {
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
                            content.append("\n\u2022" + f.getName().substring(1) + " " + f.get(response));
                        }
                    }
                } catch (IllegalAccessException e) {
                    //do nothing
                }

                TextView t2 = (TextView) d.getChildView(R.id.overlay_dialog_content);
                t2.setText(content.toString());
                d.showDialog();
            }
        });


        mbBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogWithCircularReveal d = new AlertDialogWithCircularReveal(mContext, R.layout.overlay_dialog);
                d.setDuration(2000);
                d.setRippleStart(Utils.getLocation(mbBirthday));
                TextView t = (TextView) d.getChildView(R.id.overlay_dialog_title);
                t.setText("About " + response.getFirstName());

                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                Date date = null;
                try {
                    date = formatter.parse(response.getBirthday());//catch exception
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
                t2.setText("Birthday: " + response.getBirthday() + "\nCurrently " + days + " days old.");
                d.showDialog();
            }
        });

        if (firstRun) {
            AnimUtils.sequentialFadeIn(mContext, viewList, 300);
            firstRun = false;
        }

        String header = response.getFirstName() + " " + response.getLastName();
        if (header.equals("null null")) {
            mHeader.setText("Unnamed");
        } else {
            mHeader.setText(header);
        }

    }

    private void updatePostContent(List<Post> response) {

        PostAdapter mAdapter = new PostAdapter(mContext, response);
        mRV.setAdapter(mAdapter);
        if (firstRun2) {
//            AnimUtils.fadeIn(mContext, mRV, 0, 1000);
            AnimUtils.circleReveal(mRV, 0, 0, Utils.getScreenDiagonal(mContext), Utils.getScreenDiagonal(mContext));
            firstRun2 = false;
        }
//        e("DI " + response.get(0).getMessage() + " " + response.get(0).getId() + " " + response.get(0).getObjectId());

//        String mAllPages = "";
//        // make the result more readable.
////        mAllPages += "<u>\u25B7\u25B7\u25B7 (paging) #" + getPageNum() + " \u25C1\u25C1\u25C1</u><br>";
//        mAllPages += com.sromku.simple.fb.utils.Utils.join(response.iterator(), "<br>", new com.sromku.simple.fb.utils.Utils.Process<Post>() {
//            @Override
//            public String process(Post post) {
//                e("Post " + post.getId() + " " + post.getPicture() + " " + post.getType() + " " + post.getProperties() + " " + post.getLink());
//                return "\u25CF " + (post.getMessage() == null || "null".equalsIgnoreCase(post.getMessage()) ? post.getId() : post.getMessage()) + " \u25CF";
//            }
//        });
//        mAllPages += "<br>";
//        mResult.setText(Html.fromHtml(mAllPages));
//        mResult.setTextColor(0xff000000);

        // check if more pages exist
//        if (hasNext()) {
//            enableLoadMore(getCursor());
//        } else {
//            disableLoadMore();
//        }
    }

    private void printProfileData(Profile response) {
        try {
            for (Field f : response.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (Modifier.isStatic(f.getModifiers())) {
                    continue;
                }
                e(f.getName() + " " + f.get(response));
            }
        } catch (IllegalAccessException e) {
            //do nothing
        }
    }

    private void getSinglePost(String id) {
        SimpleFacebook.getInstance().getPosts(id, new OnPostsListener() {

            @Override
            public void onException(Throwable throwable) {
                Utils.showSimpleSnackbar(mContext, mRefresh, throwable.getMessage());
//                mRefresh.setRefreshing(false);
//                mResult.setText(throwable.getMessage());
            }

            @Override
            public void onFail(String reason) {
                Utils.showSimpleSnackbar(mContext, mRefresh, reason);
//                mResult.setText(reason);
            }

            @Override
            public void onComplete(List<Post> response) {
//                mRefresh.setRefreshing(false);
                AlertDialogWithCircularReveal p = new AlertDialogWithCircularReveal(mContext, R.layout.overlay_dialog);
//                e("P " + response.get(0).getProperties());
                StringBuilder s = new StringBuilder();
                Post post = response.get(0);
                String[] ss = {post.getProperties().toString(), post.getLink(), post.getMessage(), post.getObjectId()};
                for (String sss : ss) {
                    s.append("\n" + sss);
                }
                ((TextView) p.getChildView(R.id.overlay_dialog_content)).setText(s.toString());
                p.showDialog();
//                updatePostContent(response);
            }
        });
    }
}
