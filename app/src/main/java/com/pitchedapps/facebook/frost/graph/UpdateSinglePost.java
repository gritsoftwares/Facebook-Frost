package com.pitchedapps.facebook.frost.graph;

import com.pitchedapps.facebook.frost.customViews.PostCard;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Post;
import com.sromku.simple.fb.listeners.OnSinglePostListener;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

/**
 * Created by Allan Wang on 2016-05-30.
 */
public class UpdateSinglePost {

    private String mID;
    private PostCard mPostCard;

    public UpdateSinglePost(PostCard pc) {
        mPostCard = pc;
        mID = mPostCard.getPostID();
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
                mPostCard.updateLikeCommentCount(response.getLikeCount(), null);
                mPostCard.getActionButtons().updateHasLiked(response.hasLiked());
            }
        });
    }

}
