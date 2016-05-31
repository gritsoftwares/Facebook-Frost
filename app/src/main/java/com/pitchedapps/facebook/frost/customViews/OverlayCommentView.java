package com.pitchedapps.facebook.frost.customViews;

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
import android.widget.LinearLayout;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.adapters.CommentAdapter;
import com.pitchedapps.facebook.frost.utils.ColorUtils;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.sromku.simple.fb.entities.Comment;

import java.util.List;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

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
            mRV.setAdapter(new CommentAdapter(mContext, mCommentList));
            mRV.setOnTouchListener(this);
        } else {
            mRV.setVisibility(View.GONE);
        }
        mWindow = getDialog().getWindow();
        mWindow.setGravity(Gravity.TOP);
        screenHeight = Utils.getScreenSize(mContext).y;
        border = screenHeight * -0.3f;
        absoluteBorder = border * 2;
        animSpeedFactor = new FrostPreferences(mContext).getAnimationSpeedFactor() / 6.0f;
        view.findViewById(R.id.comment_layout).setBackgroundColor(new ColorUtils(mContext).getTintedBackground(0.1f));

        ReplyBox mReply = (ReplyBox) view.findViewById(R.id.comment_reply_box);
        mReply.initialize();
//        mReply.setOnTouchListener(this);

        mReply.getEditText().setOnTouchListener(this);
        mReply.getPostButton().setOnTouchListener(this);
    }

    public void setCommentList(List<Comment> lC) {
        mCommentList = lC;
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
        ObjectAnimator toOrig = ObjectAnimator.ofFloat(mWindow.getDecorView(), "translationY", 0, newOffset).setDuration((long) (Math.max(Math.abs(netOffset), minAnimDuration) * animSpeedFactor));
        toOrig.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mWindow.getDecorView().offsetTopAndBottom((int) newOffset);
                ObjectAnimator.ofFloat(mWindow.getDecorView(), "translationY", newOffset, 0).setDuration((long) (Math.abs(netOffset) * animSpeedFactor)).setDuration(0).start();
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
        final DialogFragment mDialogFragment = this;
        exiting = true;
        ObjectAnimator exit = ObjectAnimator.ofFloat(mWindow.getDecorView(), "translationY", 0, -screenHeight - netOffset).setDuration((long) (Math.max(Math.abs(screenHeight - netOffset), minAnimDuration) * animSpeedFactor));
        exit.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mDialogFragment.dismiss();
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
        if (!slidingUp) {
            if (diff < 0.0f) {
                slidingUp = true;
                netOffset += diff;
                if (netOffset <= 0.0f) {
                    mWindow.getDecorView().offsetTopAndBottom((int) diff);
                }
            }
        } else {
            netOffset += diff;
            if (netOffset <= 0.0f) {
                mWindow.getDecorView().offsetTopAndBottom((int) diff);
            }
        }
        if (netOffset < absoluteBorder) {
            translateExit();
        }
    }
}
