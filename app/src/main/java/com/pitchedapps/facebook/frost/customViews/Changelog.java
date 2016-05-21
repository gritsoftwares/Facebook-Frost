package com.pitchedapps.facebook.frost.customViews;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.adapters.ChangelogAdapter;
import com.pitchedapps.facebook.frost.utils.AnimUtils;
import com.pitchedapps.facebook.frost.utils.Utils;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public class Changelog {

    private Context mContext;
    private AlertDialog mDialog;
    private View mView;

    public Changelog(Context c) {
        mContext = c;
        mView = View.inflate(mContext, R.layout.changelog_dialog, null);
        ListView mList = (ListView) mView.findViewById(R.id.changelog_list);
        LinearLayout mLL = (LinearLayout) mView.findViewById(R.id.changelog_dialog);

        mList.setAdapter(new ChangelogAdapter(mContext, R.array.fullchangelog));
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(mView);
        mDialog = builder.create();

        //Hacky way to be able to close the dialog on outside touch
        mLL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDialog.cancel();
                return true;
            }
        });
    }

    public void show() {
        mDialog.show();
    }

    public void showWithCircularReveal(final int x, final int y) {
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
//        lp.dimAmount = 0.2f; // Dim level. 0.0 - no dim, 1.0 - completely opaque

        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                AnimUtils.circleReveal(mView, x, y, Utils.getScreenDiagonal(mContext), Utils.getScreenDiagonal(mContext));
            }
        });
        mDialog.show();
    }
}
