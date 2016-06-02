package com.pitchedapps.facebook.frost.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.adapters.ChangelogAdapter;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;

/**
 * Created by Allan Wang on 2016-05-21.
 */
public class Changelog extends DialogFragment{

    private Context mContext;
    private int x = 0, y = 0;
    private LinearLayout mLinear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.changelog_dialog, container);
        mContext = getActivity();
        getViews(view);
        return view;
    }

    private void getViews(View view) {
//        FrostPreferences fPrefs = new FrostPreferences(mContext);

        ListView mList = (ListView) view.findViewById(R.id.changelog_list);
        mLinear = (LinearLayout) view.findViewById(R.id.changelog_dialog);
        mLinear.setBackgroundColor(new FrostPreferences(mContext).getDialogBackgroundColor());
        mList.setAdapter(new ChangelogAdapter(mContext, R.array.fullchangelog));
    }

//    public void setRippleStartPoint(int xx, int yy) {
//        x = xx;
//        y = yy;
//    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog d = new Dialog(getActivity());
//        d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        d.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialog) {
//                AnimUtils.circleReveal(mContext, mLinear, x, y, Utils.getScreenDiagonal(mContext));
//            }
//        });
//        return d;
//    }
}
