package com.gogh.floattouchkey.preference.switcher;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.gogh.floattouchkey.observable.SettingsObservable;
import com.gogh.floattouchkey.provider.SettingsProvider;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/29/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/29/2017 do fisrt create. </li>
 */

public class BreathPreference extends BaseSwitchPreference {

    public BreathPreference(Context context) {
        super(context);
    }

    public BreathPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public BreathPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BreathPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean onPreferenceClicked(Preference preference, Object newValue) {
        setChecked(!isChecked());
        SettingsObservable.get().onChanged(SettingsProvider.CODE_BREATH);
        return super.onPreferenceClicked(preference, newValue);
    }
}
