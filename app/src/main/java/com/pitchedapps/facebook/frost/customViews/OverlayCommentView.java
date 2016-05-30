package com.pitchedapps.facebook.frost.customViews;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.adapters.CommentAdapter;
import com.pitchedapps.facebook.frost.adapters.EmptyAdapter;
import com.sromku.simple.fb.entities.Comment;

import java.util.List;

/**
 * Created by Allan Wang on 2016-05-30.
 */
public class OverlayCommentView extends DialogFragment {

    private Context mContext;
    private LinearLayoutManager mLLM;
    private RecyclerView mRV;
    private List<Comment> mCommentList;
//    private View mFullView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comment_view_popup, container);
        mContext = getActivity();
        getViews(view);
        return view;
    }

    public void getViews(View view) {
        mRV = (RecyclerView) view.findViewById(R.id.comment_rv);
        mLLM = new LinearLayoutManager(mContext);
        mLLM.setOrientation(LinearLayoutManager.VERTICAL);
        mRV.setLayoutManager(mLLM);
        mRV.setAdapter(new CommentAdapter(mContext, mCommentList));
//        mRV.setAdapter(new EmptyAdapter()); //Set empty adapter so error does not occur
    }

    public void setCommentList(List<Comment> lC) {
//        mContext = c;
        mCommentList = lC;
//        getViews();
//        mRV.setAdapter(new CommentAdapter(mContext, mCommentList));
    }
}
