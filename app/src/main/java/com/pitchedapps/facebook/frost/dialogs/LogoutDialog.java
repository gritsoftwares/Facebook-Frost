package com.pitchedapps.facebook.frost.dialogs;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
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
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.TextView;

import com.pitchedapps.facebook.frost.MainActivity;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.adapters.BaseAdapter;
import com.pitchedapps.facebook.frost.customViews.ReplyBox;
import com.pitchedapps.facebook.frost.utils.ColorUtils;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.pitchedapps.facebook.frost.utils.ViewUtils;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Comment;
import com.sromku.simple.fb.entities.Post;
import com.sromku.simple.fb.listeners.OnLogoutListener;
import com.sromku.simple.fb.listeners.OnSinglePostListener;

import java.util.List;

import static com.pitchedapps.facebook.frost.utils.Utils.e;

/**
 * Created by Allan Wang on 2016-05-30.
 */
public class LogoutDialog extends DialogFragment {

    private Context mContext;
    private Window mWindow;
    private int textColor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_logout, container);
        mContext = getActivity();
        getViews(view);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), R.style.Frost_DialogFragment_AlertDialog);
    }

    public void getViews(View view) {
        FrostPreferences fPrefs = new FrostPreferences(mContext);
        textColor = fPrefs.getTextColor();
        view.findViewById(R.id.dialog_logout_container).setBackgroundColor(new ColorUtils(mContext).getTintedBackground(0.2f));
        ((TextView) view.findViewById(R.id.dialog_logout_text)).setTextColor(textColor);
        Button cancel = (Button) view.findViewById(R.id.dialog_logout_cancel);
        Button confirm = (Button) view.findViewById(R.id.dialog_logout_confirm);

        cancel.setTextColor(textColor);
        confirm.setTextColor(textColor);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)mContext).logout(Utils.getLocation(v));
                dismiss();
            }
        });


    }


}
