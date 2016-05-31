package com.pitchedapps.facebook.frost.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.customViews.CommentCard;
import com.pitchedapps.facebook.frost.customViews.EventCard;
import com.sromku.simple.fb.actions.Cursor;
import com.sromku.simple.fb.entities.Comment;
import com.sromku.simple.fb.entities.Event;

import java.util.List;

/**
 * Created by Allan Wang on 2016-05-19.
 */
public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Event> mEvents;
    private Cursor mCursor;
    private boolean loading = false;


    public EventAdapter(Context context, List<Event> events, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        mEvents = events;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View singleEvent = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.item_event_card,
                viewGroup, false);
        return new EventCard(mContext, singleEvent, mEvents.get(i));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (mCursor.hasNext() && position == (getItemCount()-1) && !loading) {
//            loading = true;
//            mCursor.next();
//        }
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    /*
     * Work around to have different item types; header may contain a negative value
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void addEvents(List<Event> events) {
        mEvents.addAll(events);
        loading = false;
    }

    public Event getPost(int position) {
        return mEvents.get(position);
    }

}
