package com.pitchedapps.facebook.frost.customViews;

/**
 * Created by Allan Wang on 2016-05-20.
 */

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.dialogs.OverlayCommentView;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.pitchedapps.facebook.frost.utils.ViewUtils;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Comment;
import com.sromku.simple.fb.listeners.OnPublishListener;

public class ReplyBox extends RelativeLayout {

    private Context mContext;
    private EditText mEditText;
    private Button mPostButton;
    private String mPostID;
    private OverlayCommentView mOCV;

    public ReplyBox(Context context) {
        super(context);
        mContext = context;
    }

    public ReplyBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public ReplyBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    public void initialize(String postID, OverlayCommentView OCV) {
        mPostID = postID;
        mOCV = OCV;
        initializeViews();
    }

    private void initializeViews() {
        ViewUtils vu = new ViewUtils(mContext, this);
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_reply, this);

        View mDivider = findViewById(R.id.item_reply_divider);
        mDivider.setBackgroundColor(vu.getTextColor());
        mDivider.setAlpha(0.5f);

        mEditText = vu.editText(R.id.item_reply_editText);
        mPostButton = vu.buttonWithEditTextDependency(R.id.item_reply_post, mEditText);
//        mPostButton.setBackgroundColor(fPrefs.getBackgroundColor());
        mPostButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                mEditText.setEnabled(false);

                String text = mEditText.getText().toString();
                final Comment comment = new Comment.Builder()
                        .setMessage(text)
                        .build();

                SimpleFacebook.getInstance().publish(mPostID, comment, new OnPublishListener() {

                    @Override
                    public void onException(Throwable throwable) {
                        showCommentError();
                    }

                    @Override
                    public void onFail(String reason) {
                        showCommentError();
                    }

//                    @Override
//                    public void onThinking() {
//                        showDialog();
//                    }

                    @Override
                    public void onComplete(String response) {
                        mEditText.setEnabled(true);
                        mEditText.getText().clear();
                        mOCV.addNewComment(comment);

                    }
                });
            }
        });

    }

    private void showCommentError() {
        mEditText.setEnabled(true);
        Utils.showSimpleSnackbar(mContext, (View) getParent(), getResources().getString(R.string.comment_fail));
    }

    public EditText getEditText() {
        return mEditText;
    }

    public Button getPostButton() {
        return mPostButton;
    }

}