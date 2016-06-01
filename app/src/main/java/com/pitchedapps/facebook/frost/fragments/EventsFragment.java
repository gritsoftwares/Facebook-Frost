package com.pitchedapps.facebook.frost.fragments;

import android.os.Bundle;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.enums.FBURL;
import com.pitchedapps.facebook.frost.enums.FrostFragment;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Event;
import com.sromku.simple.fb.listeners.OnEventsListener;

import java.util.List;

public class EventsFragment extends BaseFragment<Event> {

    private static final String EVENT_QUERY =
//            "id,start_time,end_time,description,name,rsvp_status";
            "id,start_time,end_time,description,name,place,rsvp_status,picture.type(large){url},attending_count,cover,declined_count,maybe_count,owner,can_guests_invite,guest_list_enabled,interested_count";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(FrostFragment.EVENTS);
    }

    @Override
    public void getData() {
//        SimpleFacebook.getInstance().getEvents(Event.EventDecision.ALL, new OnEventsListener() {
        SimpleFacebook.getInstance().getEvents(Event.EventDecision.ALL, EVENT_QUERY, 25, new OnEventsListener() {

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
            public void onComplete(List<Event> response) {
                mRefresh.setRefreshing(false);
                mCursor = getCursor();
                updateContent(response);
            }
        });
    }


//    @Override
//    public void setAdapter() {
//        mAdapter = new BaseAdapter<Post>(mContext, mResponse, mCursor, mSingleLayoutID).initialize(PostHeader.PROFILE, mProfile);
//    }

}
