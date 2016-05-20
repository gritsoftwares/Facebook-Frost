package com.pitchedapps.facebook.frost.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.customViews.AlertDialogWithCircularReveal;
import com.pitchedapps.facebook.frost.customViews.HeaderProfile;
import com.pitchedapps.facebook.frost.customViews.PostCard;
import com.pitchedapps.facebook.frost.utils.Retrieve;
import com.sromku.simple.fb.entities.Post;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.utils.Attributes;
import com.sromku.simple.fb.utils.PictureAttributes;

import java.util.List;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

/**
 * Created by Allan Wang on 2016-05-19.
 */
public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Post> mPosts;
    private boolean customHeader = false;
    private PostType headerType;
    private Object headerContents;

    public enum PostType {

        PROFILE("profile", -1);

        private String typeName;
        private int typeInt;

        PostType(String s, int i) {
            typeName = s;
            typeInt = i;
        }

        public String getTypeName() {
            return typeName;
        }

        public int getType() {
            return typeInt;
        }
    }


    public PostAdapter(Context context, List<Post> posts) {
        mContext = context;
        mPosts = posts;
    }

    public PostAdapter(Context context, List<Post> posts, PostType newHeader, Object headerContents) {
        mContext = context;
        mPosts = posts;
        customHeader = true;
        headerType = newHeader;
        this.headerContents = headerContents;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        switch (i) {
            case -1: //Profile
                final View profileHeader = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.header_profile,
                        viewGroup, false);
                return new HeaderProfile(mContext, profileHeader, (Profile) headerContents);
            default: //just a normal post
                final View singlePost = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.item_post_card,
                        viewGroup, false);
                return new PostCard(mContext, singlePost, mPosts.get(i));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (!customHeader) return mPosts.size();
        return mPosts.size() + 1;
    }

    /*
     * Work around to have different item types; header may contain a negative value
     */
    @Override
    public int getItemViewType(int position) {
        if (!customHeader) return position; //no custom header; continue as usual
        if (position == 0) return headerType.getType(); //custom header; change position value of first card
        return position - 1; //Start of actual post list
    }

}
