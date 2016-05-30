package com.sromku.simple.fb.actions;

import android.util.Log;

import com.facebook.GraphResponse;
import com.google.gson.reflect.TypeToken;
import com.sromku.simple.fb.SessionManager;
import com.sromku.simple.fb.entities.Post;
import com.sromku.simple.fb.entities.Post.PostType;
import com.sromku.simple.fb.utils.JsonUtils;
import com.sromku.simple.fb.utils.Utils;

import java.util.List;

public class GetSinglePostAction extends GetAction<Post> {

    public GetSinglePostAction(SessionManager sessionManager) {
        super(sessionManager);
        setEdge(null);
    }

//    public void setPostType(PostType postType) {
//        mPostType = postType;
//    }

    @Override
    protected String getGraphPath() {
        return getTarget();
    }

    @Override
    protected Post processResponse(GraphResponse response) {
        return Utils.convert(response, new TypeToken<Post>() {
        }.getType());
    }

}
