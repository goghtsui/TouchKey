package com.gogh.floattouchkey.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.gogh.floattouchkey.observable.SettingsObservable;
import com.gogh.floattouchkey.provider.SettingsProvider;
import com.gogh.floattouchkey.receiver.AdminLockReceiver;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/26/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/26/2017 do fisrt create. </li>
 */

public class AdminLockPreference extends BaseSwitchPreference {

    public AdminLockPreference(Context context) {
        super(context);
    }

    public AdminLockPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdminLockPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AdminLockPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initStatus() {
        setCheckStatus();
    }

    @Override
    protected boolean onPreferenceClicked(Preference preference, Object newValue) {
        SettingsObservable.get().onChanged(SettingsProvider.CODE_DEVICEADMIN);
        return super.onPreferenceClicked(preference, newValue);
    }

    @Override
    protected void onFragmentResume() {
        setCheckStatus();
    }

    @Override
    protected void onFragmentResult(int requestCode) {
        if (requestCode == SettingsProvider.CODE_DEVICEADMIN) {
            setCheckStatus();
        }
    }

    @Override
    protected void setCheckStatus() {
        if (AdminLockReceiver.isActivated(getContext().getApplicationContext())) {
            setChecked(true);
        } else {
            setChecked(false);
        }
    }
}