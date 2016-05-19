package com.pitchedapps.facebook.frost.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.customViews.AlertDialogWithCircularReveal;
import com.pitchedapps.facebook.frost.utils.AnimUtils;
import com.sromku.simple.fb.entities.Post;

import java.util.List;

/**
 * Created by Allan Wang on 2016-05-19.
 */
public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private View view;
    private List<Post> mPosts;

    public PostAdapter(Context context, List<Post> posts) {
        mContext = context;
        mPosts = posts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View singlePost = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.item_post_card,
                viewGroup, false);
        return new PostCard(singlePost, i);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class PostCard extends RecyclerView.ViewHolder {

        CardView mCardView;
        TextView cardTitle, cardDesc;
        ImageView cardIcon;

        public PostCard(View itemView, int i) {
            super(itemView);
            view = itemView;
//            itemView.setVisibility(View.INVISIBLE);

            String description;

            final int pos = i;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    singlePostData(mPosts.get(pos));
                }
            });

            cardTitle = (TextView) itemView.findViewById(R.id.item_post_text);
            cardTitle.setText(mPosts.get(pos).getMessage());
//            AnimUtils.fadeIn(mContext, itemView, pos * 200, 5000);
        }
    }

    private void singlePostData(Post post) {
        AlertDialogWithCircularReveal p = new AlertDialogWithCircularReveal(mContext, R.layout.overlay_dialog);
//                e("P " + response.get(0).getProperties());
        StringBuilder s = new StringBuilder();
        String[] ss = {post.getLink(), post.getMessage(), post.getId(), post.getType()};
        for (String sss : ss) {
            if (sss == null) continue;
            s.append("\n" + sss);
        }
        ((TextView) p.getChildView(R.id.overlay_dialog_content)).setText(s.toString());
        p.showDialog();
    }

}
