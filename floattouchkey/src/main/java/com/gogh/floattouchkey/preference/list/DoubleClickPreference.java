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

public class DoubleClickPreference extends BaseGesturePreference {

    private static final String TAG = "SingleClickPreference";

    public DoubleClickPreference(Context context) {
        super(context);
        setSelectIndex(DOUBLE_CLICK_INDEX);
    }

    public DoubleClickPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSelectIndex(DOUBLE_CLICK_INDEX);
    }

    public DoubleClickPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSelectIndex(DOUBLE_CLICK_INDEX);
    }

    public DoubleClickPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setSelectIndex(DOUBLE_CLICK_INDEX);
    }

    @Override
    protected void setSelectIndex(int selectIndex) {
        super.setSelectIndex(selectIndex);
    }

    @Override
    protected void onPreferenceSelected(Preference preference, Object newValue) {
        setValue(String.valueOf(newValue));
        setSummary(getEntry());
        EventHandleObservable.get().onChanged(GlobalActionExt.GLOBAL_ACTION_DOUBLE_CLICK);
    }

    @Override
    protected void updateSummary() {
        setSummary(getEntries()[Integer.valueOf(getPersistedString(String.valueOf(DOUBLE_CLICK_INDEX)))]);
    }

}
