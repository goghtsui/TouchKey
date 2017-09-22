package com.gogh.floattouchkey.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.widget.Toast;

import com.gogh.floattouchkey.observable.SettingsObservable;
import com.gogh.floattouchkey.provider.SettingsProvider;

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
    protected boolean onPreferenceChanged(Preference preference, Object newValue) {
        SettingsObservable.get().onChanged(SettingsProvider.CODE_ACCESSIBILITY);
        return super.onPreferenceChanged(preference, newValue);
    }

    @Override
    protected void onFragmentResult(int code) {
        super.onFragmentResult(code);
        Toast.makeText(getContext(), "result", Toast.LENGTH_SHORT).show();
    }
}
