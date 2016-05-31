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

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.adapters.CommentAdapter;
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
    private float y, netOffset = 0.0f, translateOffset = 0.0f, border, screenHeight, animSpeedFactor;
    private Window mWindow;
    private boolean slidingUp = false;
    private DialogFragment mDialog;
//    private View mFullView;

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
        return d;
    }


    public void getViews(View view) {
        mDialog = this;
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
        animSpeedFactor = new FrostPreferences(mContext).getAnimationSpeedFactor() / 10.0f;
        mDialog.setExitTransition(R.anim.slide_out_up);
    }

    public void setCommentList(List<Comment> lC) {
        mCommentList = lC;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mLLM.findLastCompletelyVisibleItemPosition() == (mCommentList.size() - 1)) {
            int action = MotionEventCompat.getActionMasked(event);
            switch (action) {
                case (MotionEvent.ACTION_MOVE):
                    sliding(event);
                    break;
                case (MotionEvent.ACTION_UP):
                    e("NETOFFSET " + netOffset);
                    slidingUp = false;
                    if (netOffset > border) {
                        float newOffset = translateOffset - netOffset;
                        ObjectAnimator.ofFloat(mWindow.getDecorView(), "translationY", translateOffset, newOffset).setDuration((long)(Math.abs(netOffset) * animSpeedFactor)).start();
                        netOffset = 0.0f;
                        translateOffset = newOffset;
                    } else {
                        e("EXIT");
                        getActivity().getSupportFragmentManager().popBackStack();
                        ObjectAnimator exit = ObjectAnimator.ofFloat(mWindow.getDecorView(), "translationY", translateOffset, translateOffset - screenHeight - netOffset).setDuration((long)(Math.abs(screenHeight - netOffset) * animSpeedFactor));
                        exit.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) { }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mDialog.dismiss();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {  }

                            @Override
                            public void onAnimationRepeat(Animator animation) { }
                        });
                        exit.start();
                    }
                    break;
                default:
                    break;
            }
            y = event.getRawY();
            if (netOffset < 0.0f) return true;
//        } else if (slidingUp) {
//            int action = MotionEventCompat.getActionMasked(event);
//            switch (action) {
//                case (MotionEvent.ACTION_UP):
//                    slidingUp = false;
//                    break;
//                case (MotionEvent.ACTION_MOVE):
//                    sliding(event);
//                    break;
//                default:
//                    break;
//            }
//            y = event.getRawY();
//            if (netOffset < 0.0f) return true;
        }
        return false;
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
//        e("diff " + diff + " netOffset " + netOffset);
    }
}
