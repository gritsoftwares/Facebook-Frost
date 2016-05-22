package com.pitchedapps.facebook.frost.exampleFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pitchedapps.facebook.frost.R;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Feed;
import com.sromku.simple.fb.listeners.OnPublishListener;

public class PublishFeedDialogFragment extends BaseFragment {

    private final static String EXAMPLE = "Publish feed - dialog";

    private TextView mResult;
    private Button mButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(EXAMPLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_example_action, container, false);
        mResult = (TextView) view.findViewById(R.id.result);
        view.findViewById(R.id.load_more).setVisibility(View.GONE);
        mButton = (Button) view.findViewById(R.id.button);
        mButton.setText(EXAMPLE);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Feed feed = new Feed.Builder()
                        .setMessage("Clone it out...")
                        .setName("Simple Facebook SDK for Android")
                        .setCaption("Code less, do the same.")
                        .setDescription("Login, publish feeds and stories, invite friends and more....")
                        .setPicture("https://raw.githubusercontent.com/wiki/sromku/android-simple-facebook/images/android_facebook_sdk_logo.png")
                        .setLink("https://github.com/sromku/android-simple-facebook")
                        .build();

                SimpleFacebook.getInstance().publish(feed, true, new OnPublishListener() {

                    @Override
                    public void onException(Throwable throwable) {
                        mResult.setText(throwable.getMessage());
                    }

                    @Override
                    public void onFail(String reason) {
                        mResult.setText(reason);
                    }

                    @Override
                    public void onComplete(String response) {
                        mResult.setText(response);
                    }
                });

            }
        });
        return view;
    }

}
