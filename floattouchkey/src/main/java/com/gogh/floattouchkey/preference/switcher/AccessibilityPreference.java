package com.gogh.floattouchkey.preference.switcher;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.gogh.floattouchkey.observable.SettingsObservable;
import com.gogh.floattouchkey.provider.SettingsProvider;
import com.gogh.floattouchkey.service.TouchAccessibilityService;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/22/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/22/2017 do fisrt create. </li>
 */

public class AccessibilityPreference extends BaseSwitchPreference {

    public AccessibilityPreference(Context context) {
        super(context);
    }

    public AccessibilityPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AccessibilityPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AccessibilityPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initStatus() {
        setCheckStatus();
    }

    @Override
    protected boolean onPreferenceClicked(Preference preference, Object newValue) {
        SettingsObservable.get().onChanged(SettingsProvider.CODE_ACCESSIBILITY);
        return super.onPreferenceClicked(preference, newValue);
    }

    @Override
    protected void onFragmentResume() {
        setCheckStatus();
    }

    @Override
    protected void onFragmentResult(int requestCode) {
        if (requestCode == SettingsProvider.CODE_ACCESSIBILITY) {
            if (TouchAccessibilityService.getService().isConnected()) {
                setChecked(true);
            } else {
                setChecked(false);
            }
        }
    }

    @Override
    protected void setCheckStatus() {
        if (TouchAccessibilityService.getService().isConnected()) {
            setChecked(true);
        } else {
            setChecked(false);
        }
    }

}
