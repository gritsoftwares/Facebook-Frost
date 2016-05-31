package com.pitchedapps.facebook.frost.customViews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sromku.simple.fb.entities.Comment;
import com.sromku.simple.fb.entities.Event;
import com.sromku.simple.fb.entities.Post;

/**
 * Created by Allan Wang on 2016-05-20.
 */
public class BaseCard<T> extends RecyclerView.ViewHolder {

    public BaseCard(Context c, View v, T i) {
        super(v);

        if (i instanceof Post) {
            new PostCard(c, v, (Post)i);
        } else if (i instanceof Comment) {
            new CommentCard(c, v, (Comment)i);
        } else if (i instanceof Event) {
            new EventCard(c, v, (Event)i);
        }
    }

}
