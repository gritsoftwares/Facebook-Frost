package com.pitchedapps.facebook.frost.fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pitchedapps.facebook.frost.exampleFragments.BaseFragment;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.actions.Cursor;
import com.sromku.simple.fb.entities.Post;
import com.pitchedapps.facebook.frost.R;
import com.sromku.simple.fb.listeners.OnPostsListener;
import com.sromku.simple.fb.utils.Utils;

import java.util.List;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

public class GetPostsFragment extends BaseFragment {

    private final static String EXAMPLE = "Get posts";

    private TextView mResult;
    private Button mGetButton;
    private TextView mMore;
    private String mAllPages = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_example_action, container, false);
        mResult = (TextView) view.findViewById(R.id.result);
        mMore = (TextView) view.findViewById(R.id.load_more);
        mMore.setPaintFlags(mMore.getPaint().getFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mGetButton = (Button) view.findViewById(R.id.button);
        mGetButton.setText(EXAMPLE);
        mGetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAllPages = "";
                mResult.setText(mAllPages);

                SimpleFacebook.getInstance().getPosts(Post.PostType.POSTS, new OnPostsListener() {

                    @Override
                    public void onThinking() {
                        showDialog();
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        hideDialog();
                        mResult.setText(throwable.getMessage());
                    }

                    @Override
                    public void onFail(String reason) {
                        hideDialog();
                        mResult.setText(reason);
                    }

                    @Override
                    public void onComplete(List<Post> response) {
                        hideDialog();
                        // make the result more readable.
                        mAllPages += "<u>\u25B7\u25B7\u25B7 (paging) #" + getPageNum() + " \u25C1\u25C1\u25C1</u><br>";
                        mAllPages += Utils.join(response.iterator(), "<br>", new Utils.Process<Post>() {
                            @Override
                            public String process(Post post) {
                                e("Post " + post);
                                return "\u25CF " + (post.getMessage() == null || "null".equalsIgnoreCase(post.getMessage()) ? post.getId() : post.getMessage()) + " \u25CF";
                            }
                        });
                        mAllPages += "<br>";
                        mResult.setText(Html.fromHtml(mAllPages));
                        mResult.setTextColor(0xff000000);

                        // check if more pages exist
                        if (hasNext()) {
                            enableLoadMore(getCursor());
                        } else {
                            disableLoadMore();
                        }
                    }
                });
            }
        });
        return view;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        getActivity().setTitle(EXAMPLE);
//    }

    private void enableLoadMore(final Cursor<List<Post>> cursor) {
        mMore.setVisibility(View.VISIBLE);
        mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mAllPages += "<br>";
                cursor.next();
            }
        });
    }

    private void disableLoadMore() {
        mMore.setOnClickListener(null);
        mMore.setVisibility(View.INVISIBLE);
    }
}
