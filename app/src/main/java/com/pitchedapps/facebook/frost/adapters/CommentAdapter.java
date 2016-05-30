package com.pitchedapps.facebook.frost.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.customViews.CommentCard;
import com.pitchedapps.facebook.frost.customViews.HeaderProfile;
import com.pitchedapps.facebook.frost.customViews.PostCard;
import com.pitchedapps.facebook.frost.enums.PostHeader;
import com.sromku.simple.fb.actions.Cursor;
import com.sromku.simple.fb.entities.Comment;
import com.sromku.simple.fb.entities.Post;
import com.sromku.simple.fb.entities.Profile;

import java.util.List;

/**
 * Created by Allan Wang on 2016-05-19.
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Comment> mComments;
    private Cursor mCursor;
    private boolean loading = false;


    public CommentAdapter(Context context, List<Comment> comments) {
        mContext = context;
        mComments = comments;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View singleComment = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.item_comment_card,
                viewGroup, false);
        return new CommentCard(mContext, singleComment, mComments.get(i));
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
        return mComments.size();
    }

    /*
     * Work around to have different item types; header may contain a negative value
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void addComments(List<Comment> comments) {
        mComments.addAll(comments);
        loading = false;
    }

    public Comment getPost(int position) {
        return mComments.get(position);
    }

}
