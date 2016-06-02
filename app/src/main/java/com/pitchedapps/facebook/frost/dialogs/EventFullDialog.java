package com.pitchedapps.facebook.frost.dialogs;

import android.os.Bundle;

import com.sromku.simple.fb.entities.Event;

/**
 * Created by Allan Wang on 2016-05-30.
 */
public class EventFullDialog extends BaseFullDialogWithImageCollapse {

    private Event mEvent;

    public void initialize(Event e) {
        mEvent = e;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarImageURL(mEvent.getCoverURL());
    }


}
