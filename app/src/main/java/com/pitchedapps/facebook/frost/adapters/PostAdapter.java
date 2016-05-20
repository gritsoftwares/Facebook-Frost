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
import com.sromku.simple.fb.entities.Post;

import java.util.List;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

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

        public PostCard(View itemView, int i) {
            super(itemView);
            view = itemView;
//            itemView.setVisibility(View.INVISIBLE);

            String description;

            final Post sPost = mPosts.get(i);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    singlePostData(sPost);
                }
            });

            String message = sPost.getMessage();
            String story = sPost.getStory();
            ((TextView) itemView.findViewById(R.id.item_post_text)).setText(message != null ? message : story);

            ((TextView) itemView.findViewById(R.id.item_post_from)).setText(sPost.getFrom().getName());



            ImageView avatar = (ImageView) itemView.findViewById(R.id.item_post_avatar);
            e("FROM " + i + sPost.getFrom());
//            if (sPost.getFrom() != null) { //TODO learn more about cover
//                Glide.with(mContext)
//                        .load(response.getCover().toString())
//                        .centerCrop()
//                        .into(mCover);
//            }

            switch (mPosts.get(i).getType()) {

                case "photo":
                    ImageView photo = (ImageView) itemView.findViewById(R.id.item_post_picture);
                    photo.setVisibility(View.VISIBLE);
                    Glide.with(mContext)
                            .load(sPost.getPicture())
                            .centerCrop()
                            .into(photo);
                    break;
                case "link":
                    break;
                case "status":
                    break;

                default:
                    e("New post type: " + mPosts.get(i).getType());
                    break;
            }


//            AnimUtils.fadeIn(mContext, itemView, pos * 200, 5000);
        }
    }

    private void singlePostData(Post post) {
        AlertDialogWithCircularReveal p = new AlertDialogWithCircularReveal(mContext, R.layout.overlay_dialog);
//                e("P " + response.get(0).getProperties());
        StringBuilder s = new StringBuilder();
        if (post != null) {
            String[] ss = {post.getId(), post.getType(), post.getStatusType(), post.getPicture()};
            for (String sss : ss) {
                s.append("\n" + sss);
            }
        } else {
            s.append("POST IS NULL");
        }
        ((TextView) p.getChildView(R.id.overlay_dialog_content)).setText(s.toString());
        p.showDialog();
        try {
            e("Actions: " + post.getActions());
        } catch (Exception e) {
            e("Error: " + e);
        }
    }

}
