package com.gogh.floattouchkey.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.gogh.floattouchkey.observable.SettingsObservable;
import com.gogh.floattouchkey.provider.SettingsProvider;

import java.util.Observable;
import java.util.Observer;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/27/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/27/2017 do fisrt create. </li>
 */

public class AutoEdgePreference extends BaseSwitchPreference implements Observer {

    private static final String TAG = "AutoEdgePreference";

    public AutoEdgePreference(Context context) {
        super(context);
        SettingsObservable.get().addObserver(this);
    }

    public AutoEdgePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        SettingsObservable.get().addObserver(this);
    }

    public AutoEdgePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SettingsObservable.get().addObserver(this);
    }

    public AutoEdgePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        SettingsObservable.get().addObserver(this);
    }

    @Override
    protected boolean onPreferenceClicked(Preference preference, Object newValue) {
        setChecked(!isChecked());
        SettingsObservable.get().onChanged(SettingsProvider.CODE_AUTOEDGE);
        return super.onPreferenceClicked(preference, newValue);
    }

    @Override
    protected void initStatus() {
        super.initStatus();
        setEnabled(!SettingsProvider.get().isLockPosition());
    }

    @Override
    public void update(Observable o, Object arg) {
        int code = Integer.valueOf(String.valueOf(arg));
        if (code == SettingsProvider.CODE_LOCKPOINT) {
            setEnabled(!SettingsProvider.get().isLockPosition());
        }
    }
}
