package com.pitchedapps.facebook.frost.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pitchedapps.facebook.frost.MainActivity;
import com.pitchedapps.facebook.frost.R;
import com.pitchedapps.facebook.frost.utils.FrostPreferences;
import com.pitchedapps.facebook.frost.utils.Utils;
import com.pitchedapps.facebook.frost.utils.ViewUtils;

/**
 * Created by Allan Wang on 2016-05-30.
 */
public class CreatePostDialog extends DialogFragment {

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_create_post, container);
        mContext = getActivity();
        getViews(view);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), R.style.Frost_DialogFragment_AlertDialog);
    }

    private void getViews(View view) {
        ViewUtils vu = new ViewUtils(mContext, view);
        vu.setDialogBG(R.id.dialog_create_container);
        EditText mEditText = vu.editText(R.id.dialog_create_edittext);
        Button mButton = vu.buttonWithEditTextDependency(R.id.dialog_create_post, mEditText);
//        ((EditText) view.findViewById(R.id.dialog_create_edittext)).setTextColor(textColor);
//        Button cancel = (Button) view.findViewById(R.id.dialog_logout_cancel);
//        Button confirm = (Button) view.findViewById(R.id.dialog_logout_confirm);
//
//        cancel.setTextColor(textColor);
//        confirm.setTextColor(textColor);
//
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//        confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((MainActivity)mContext).logout(Utils.getLocation(v));
//                dismiss();
//            }
//        });
    }


}
