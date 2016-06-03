package com.pitchedapps.facebook.frost.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.cards.BaseCard;
import com.pitchedapps.facebook.frost.customViews.HeaderProfile;
import com.pitchedapps.facebook.frost.enums.PostHeader;
import com.sromku.simple.fb.actions.Cursor;
import com.sromku.simple.fb.entities.Profile;

import java.util.List;

/**
 * Created by Allan Wang on 2016-05-19.
 */
public class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private boolean mHasHeader = false;
    private int mHeaderType;
    private Object mHeaderContents;
    private Context mContext;
    private List<T> mItems;
    private Cursor mCursor;
    private int mLayoutID;
    private boolean loading = false, reverse = false;

    public BaseAdapter(Context context, List<T> items, int layoutID) {
        mContext = context;
        mItems = items;
        mLayoutID = layoutID;
    }

    public BaseAdapter(Context context, List<T> items, Cursor cursor, int layoutID) {
        mContext = context;
        mItems = items;
        mCursor = cursor;
        mLayoutID = layoutID;
    }

    public BaseAdapter<T> addHeader(PostHeader newHeader, Object headerContents) {
        mHasHeader = true;
        mHeaderType = newHeader.getType();
        mHeaderContents = headerContents;
        return this;
    }

    public BaseAdapter<T> reverse() {
        reverse = true;
        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (mHasHeader) {
            switch (i) {
                case -1:
                    final View profileHeader = LayoutInflater.from(
                            viewGroup.getContext()).inflate(R.layout.header_profile,
                            viewGroup, false);
                    return new HeaderProfile(mContext, profileHeader, (Profile) mHeaderContents);
                default:
                    break;
            }
        }
        final View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(mLayoutID,
                viewGroup, false);
        if (reverse) {
            return new BaseCard<T>(mContext, view, mItems.get(getItemCount() - 1 - i));
        }
        return new BaseCard<T>(mContext, view, mItems.get(i));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mCursor != null) {
            if (mCursor.hasNext() && position == (getItemCount() - 1) && !loading) {
                loading = true;
                mCursor.next();
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mHasHeader) return mItems.size() + 1;
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (!mHasHeader) return position;

        if (position == 0)
            return mHeaderType; //custom header; change position value of first card
        return position - 1; //Start of actual post list
    }

    public void addItems(List<T> response) {
        if (reverse) {
            mItems.addAll(0, response);
        } else {
            mItems.addAll(response);
        }
        loading = false;
    }

    public void addItems(T response) {
        if (reverse) {
            mItems.add(0, response);
        } else {
            mItems.add(response);
        }
        loading = false;
    }

    public void switchItems(List<T> response) {
        mItems = response;
    }

    public T getItem(int position) {
        return mItems.get(position);
    }

}
