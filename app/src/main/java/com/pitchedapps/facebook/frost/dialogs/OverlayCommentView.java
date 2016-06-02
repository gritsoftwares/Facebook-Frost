package com.pitchedapps.facebook.frost.dialogs;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.adapters.BaseAdapter;
import com.pitchedapps.facebook.frost.customViews.ReplyBox;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Comment;
import com.sromku.simple.fb.entities.Post;
import com.sromku.simple.fb.listeners.OnSinglePostListener;

import java.util.List;

/**
 * Created by Allan Wang on 2016-05-30.
 */
public class OverlayCommentView extends DialogFragment implements View.OnTouchListener {

    private Context mContext;
    private LinearLayoutManager mLLM;
    private RecyclerView mRV;
    private List<Comment> mCommentList;
    private float y, netOffset = 0.0f, border, absoluteBorder, screenHeight, animSpeedFactor, minAnimDuration = 500.0f;
    private Window mWindow;
    private boolean slidingUp = false, exiting = false;
    private FrostPreferences fPrefs;
    private String mPostID;
    private ReplyBox mReply;
    private BaseAdapter<Comment> mAdapter;
    private static final float scrollRatio = 0.5f;

    private static final String COMMENT_QUERY =
            "comments.summary(true).limit(25).order(reverse_chronological){attachment,message,can_comment,can_like,comment_count,created_time,from,like_count}";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comment_view_popup, container);
        mContext = getActivity();
        getViews(view);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = new Dialog(getActivity(), R.style.Frost_DialogFragment);
        d.getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
        d.getWindow().setWindowAnimations(R.style.SlideUpAnimations);
        d.getWindow().getDecorView().setOnTouchListener(this);
        return d;
    }

    public void getViews(View view) {
        fPrefs = new FrostPreferences(mContext);
        mRV = (RecyclerView) view.findViewById(R.id.comment_rv);
        if (mCommentList != null) {
            mLLM = new LinearLayoutManager(mContext);
            mLLM.setOrientation(LinearLayoutManager.VERTICAL);
            mRV.setLayoutManager(mLLM);
            mAdapter = new BaseAdapter<>(mContext, mCommentList, R.layout.item_comment_card).reverse();
            mRV.setAdapter(mAdapter);
//            mRV.setAdapter(new CommentAdapter(mContext, mCommentList));
            mRV.setOnTouchListener(this);
        } else {
            mRV.setVisibility(View.GONE);
        }
        mWindow = getDialog().getWindow();
        mWindow.setGravity(Gravity.TOP);
        screenHeight = Utils.getScreenSize(mContext).y;
        border = screenHeight * -0.3f * scrollRatio;
        absoluteBorder = border * 2;
        animSpeedFactor = fPrefs.getAnimationSpeedFactor() / 6.0f;
        view.findViewById(R.id.comment_layout).setBackgroundColor(fPrefs.getDialogBackgroundColor());

        mReply = (ReplyBox) view.findViewById(R.id.comment_reply_box);
        mReply.initialize(mPostID, this);
//        mReply.setOnTouchListener(this);

        mReply.getEditText().setOnTouchListener(this);
        mReply.getPostButton().setOnTouchListener(this);
    }

    public void initialize(List<Comment> lC, String postID) {
        mCommentList = lC;
        mPostID = postID;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (exiting) return true;
        if (!(v instanceof RecyclerView) || (mLLM.findLastCompletelyVisibleItemPosition() == (mCommentList.size() - 1))) {
//            e("TOUCH " + v);
            int action = MotionEventCompat.getActionMasked(event);
            switch (action) {
                case (MotionEvent.ACTION_MOVE):
                    sliding(event);
                    break;
                case (MotionEvent.ACTION_UP):
//                    e("NETOFFSET " + netOffset);
                    slidingUp = false;
                    if (netOffset > border) {
                        translateToOriginal();
                    } else {
                        translateExit();
                    }
                    break;
                default:
                    break;
            }
            y = event.getRawY();
            if (netOffset < 0.0f) return true;
        }
        return false;
    }

    private void translateToOriginal() {
        final float newOffset = -netOffset;
        ObjectAnimator toOrig = ObjectAnimator.ofFloat(mWindow.getDecorView(), "translationY", 0, newOffset).setDuration((long) (Math.max(Math.abs(netOffset), minAnimDuration) * 2 * animSpeedFactor));
        toOrig.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mWindow.getDecorView().offsetTopAndBottom((int) newOffset);
                ObjectAnimator.ofFloat(mWindow.getDecorView(), "translationY", newOffset, 0).setDuration(0).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        toOrig.start();
        netOffset = 0.0f;
    }

    private void translateExit() {
        exiting = true;
        ObjectAnimator exit = ObjectAnimator.ofFloat(mWindow.getDecorView(), "translationY", 0, -screenHeight - netOffset).setDuration((long) (Math.max(Math.abs(screenHeight - netOffset), minAnimDuration) * animSpeedFactor));
        exit.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        exit.start();
    }

    private void sliding(MotionEvent event) {
        float diff = event.getRawY() - y;
        diff *= scrollRatio;
        if (!slidingUp) {
            if (diff < 0.0f) {
                slidingUp = true;
                netOffset += diff;
                if (netOffset <= 0.0f) {
                    mWindow.getDecorView().offsetTopAndBottom((int) (diff));
                }
            }
        } else {
            netOffset += diff;
            if (netOffset <= 0.0f) {
                mWindow.getDecorView().offsetTopAndBottom((int) (diff));
            }
        }
        if (netOffset < absoluteBorder) {
            translateExit();
        }
    }

    public void addNewComment(Comment comment) {
        mAdapter.addItems(comment);
        reloadAfterPost();
    }

    public void reloadAfterPost() {
        SimpleFacebook.getInstance().getSinglePost(mPostID, COMMENT_QUERY, new OnSinglePostListener() {

            @Override
            public void onException(Throwable throwable) {
                showCommentLoadError();
            }

            @Override
            public void onFail(String reason) {
                showCommentLoadError();
            }

            @Override
            public void onComplete(Post response) {
                mAdapter.switchItems(response.getComments());
                mAdapter.notifyDataSetChanged();
                mRV.smoothScrollToPosition(mAdapter.getItemCount());

//                mReply.getEditText().getText().clear();
//                mReply.getEditText().setEnabled(true);
//                mAdapter.addItems();
//                mRV.setAdapter(new BaseAdapter<>(mContext, response, R.layout.item_comment_card));
//                mRV.scrollToPosition(response.size() - 1);
            }
        });
    }

    private void showCommentLoadError() {
        Utils.showSimpleSnackbar(mContext, mRV, getResources().getString(R.string.comment_reload_fail));
    }

    public BaseAdapter<Comment> getAdapter() {
        return mAdapter;
    }
}
