package com.pitchedapps.facebook.frost.graph;

import com.pitchedapps.facebook.frost.adapters.PostAdapter;
import com.pitchedapps.facebook.frost.customViews.PostCard;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Post;
import com.sromku.simple.fb.listeners.OnSinglePostListener;

/**
 * Created by Allan Wang on 2016-05-30.
 */
public class UpdateSinglePost {

    private Post newPost;
    private PostAdapter mPostAdapter;
    private int mPosition;
    private String mID;
    private PostCard mPostCard;

    public UpdateSinglePost(PostCard pc) {
        mPostCard = pc;
        mID = mPostCard.getPostID();
        mPostAdapter = mPostCard.getPostAdapter();
        mPosition = mPostCard.getPostPosition();
        newPost = mPostAdapter.getPost(mPosition);
    }

    public void updateLikes() {
        SimpleFacebook mSimpleFacebook = SimpleFacebook.getInstance();
        mSimpleFacebook.getSinglePost(mID, "likes.summary(true)", new OnSinglePostListener() {

            @Override
            public void onException(Throwable throwable) {
                mPostCard.getActionButtons().showLikeError();
            }

            @Override
            public void onFail(String reason) {
                mPostCard.getActionButtons().showLikeError();
            }

            @Override
            public void onComplete(Post response) {
                newPost.setLiked(response.hasLiked());
                newPost.setCanLike(response.canLike());
                newPost.setLikeCount(response.getLikeCount());

                mPostAdapter.notifyItemChanged(mPosition + 1);
            }
        });
    }

}
