package com.pitchedapps.facebook.frost.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pitchedapps.facebook.frost.customViews.PostCard;
import com.sromku.simple.fb.actions.Cursor;

import java.util.List;

/**
 * Created by Allan Wang on 2016-05-19.
 */
public class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<T> mObjects;
    private Cursor mCursor;
    private int mLayoutID;
    private boolean loading = false;


    public BaseAdapter(Context context, List<T> objects, Cursor cursor, int layoutID) {
        mContext = context;
        mObjects = objects;
        mCursor = cursor;
        mLayoutID = layoutID;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(mLayoutID,
                viewGroup, false);
        return new PostCard(mContext, view, mObjects.get(i));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mCursor.hasNext() && position == (getItemCount()-1) && !loading) {
            loading = true;
            mCursor.next();
        }
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    /*
     * Work around to have different item types; header may contain a negative value
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void addObjects(List<T> response) {
        mObjects.addAll(response);
        loading = false;
    }

    public T getObject(int position) {
        return mObjects.get(position);
    }

}
