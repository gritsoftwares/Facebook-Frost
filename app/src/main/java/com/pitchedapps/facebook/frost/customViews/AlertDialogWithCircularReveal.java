package com.pitchedapps.facebook.frost.customViews;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.utils.AnimUtils;
import com.pitchedapps.facebook.frost.utils.Utils;

/**
 * Created by Allan Wang on 2016-05-19.
 */
public class AlertDialogWithCircularReveal {

    private AlertDialog mDialog;
    private Context mContext;
    private View mView;
    private int startX = 0, startY = 0;
    private double duration = 1000;

    //v should be View.inflate(c, v, null);
    public AlertDialogWithCircularReveal(Context c, int layoutID) {
        mContext = c;
        mView = View.inflate(mContext, layoutID, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setView(mView);

        mDialog = builder.create();
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.dimAmount=0.2f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
    }

    public void showDialog() {
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                revealShow();
            }
        });
        mDialog.show();
    }

    public View getChildView(int id) {
        return mView.findViewById(id);
    }

    public void setRippleStart(Point p) {
        startX = p.x;
        startY = p.y;
    }

    public void setRippleStart(int x, int y) {
        startX = x;
        startY = y;
    }

    public void setDuration(double d) {
        duration = d;
    }

    public AlertDialog getDialog() {
        return mDialog;
    }

    private void revealShow() {
        int w = mView.getWidth();
        int h = mView.getHeight();

        Point size = Utils.getScreenSize(mContext);
        int width = size.x;
        int height = size.y;

        //Make coordinates reflect position from alertDialog
        int x = startX - width / 2 + w / 2;
        int y = startY - height / 2 + h / 2;

        double maxRadius = Math.sqrt(w * w + h * h);

        AnimUtils.circleReveal(mView, x, y, maxRadius, duration);
    }
}
