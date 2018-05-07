package com.gogh.floattouchkey.preference.list;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.gogh.floattouchkey.common.GlobalActionExt;
import com.gogh.floattouchkey.observable.EventHandleObservable;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 10/11/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 10/11/2017 do fisrt create. </li>
 */

public class SwipToLeftPreference extends BaseGesturePreference {

    private static final String TAG = "SingleClickPreference";

    public SwipToLeftPreference(Context context) {
        super(context);
        setSelectIndex(SWIP_LEFT_INDEX);
    }

    public SwipToLeftPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSelectIndex(SWIP_LEFT_INDEX);
    }

    public SwipToLeftPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSelectIndex(SWIP_LEFT_INDEX);
    }

    public SwipToLeftPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setSelectIndex(SWIP_LEFT_INDEX);
    }

    @Override
    protected void setSelectIndex(int selectIndex) {
        super.setSelectIndex(selectIndex);
    }

    @Override
    protected void onPreferenceSelected(Preference preference, Object newValue) {
        setValue(String.valueOf(newValue));
        setSummary(getEntry());
        EventHandleObservable.get().onChanged(GlobalActionExt.GLOBAL_ACTION_SWIPE_LEFT);
    }

    @Override
    protected void updateSummary() {
        setSummary(getEntries()[Integer.valueOf(getPersistedString(String.valueOf(SWIP_LEFT_INDEX)))]);
    }
}
