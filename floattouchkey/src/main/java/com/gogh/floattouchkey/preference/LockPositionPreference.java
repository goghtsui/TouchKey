package com.gogh.floattouchkey.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.gogh.floattouchkey.observable.SettingsObservable;
import com.gogh.floattouchkey.provider.SettingsProvider;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/27/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/27/2017 do fisrt create. </li>
 */

public class LockPositionPreference extends BaseSwitchPreference {

    public LockPositionPreference(Context context) {
        super(context);
    }

    public LockPositionPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public LockPositionPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LockPositionPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean onPreferenceClicked(Preference preference, Object newValue) {
        setChecked(!isChecked());
        SettingsObservable.get().onChanged(SettingsProvider.CODE_LOCKPOINT);
        return super.onPreferenceClicked(preference, newValue);
    }
}
