package com.pitchedapps.facebook.frost.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.pitchedapps.facebook.frost.R;

/**
 * Created by 7681 on 2016-05-16.
 */
public class DemoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        int position = FragmentPagerItem.getPosition(getArguments());
        if (position == 3) {
            return inflater.inflate(R.layout.fragment_demo_2, container, false);
        }
        return inflater.inflate(R.layout.fragment_demo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int position = FragmentPagerItem.getPosition(getArguments());
        TextView title = (TextView) view.findViewById(R.id.item_title);
        title.setText(String.valueOf(position));
    }

}
