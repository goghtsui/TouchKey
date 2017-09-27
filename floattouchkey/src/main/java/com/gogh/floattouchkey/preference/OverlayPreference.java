package com.gogh.floattouchkey.preference;

import android.content.Context;
import android.os.Build;
import android.preference.Preference;
import android.provider.Settings;
import android.util.AttributeSet;

import com.gogh.floattouchkey.R;
import com.gogh.floattouchkey.observable.SettingsObservable;
import com.gogh.floattouchkey.provider.SettingsProvider;
import com.gogh.floattouchkey.widget.FloatTouchView;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/22/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/22/2017 do fisrt create. </li>
 */

public class OverlayPreference extends BaseSwitchPreference {

    private static final String TAG = "OverlayPreference";

    public OverlayPreference(Context context) {
        super(context);
    }

    public OverlayPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public OverlayPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public OverlayPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initStatus() {
        setCheckStatus();
    }

    @Override
    protected boolean onPreferenceClicked(Preference preference, Object newValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setEnabled(true);
            SettingsObservable.get().onChanged(SettingsProvider.CODE_OVERLAY);
        } else {
            setChecked(true);
            setSummary(getContext().getResources().getString(R.string.pref_root_category_switcher_overlay_summary_on));
            setEnabled(false);
        }
        return super.onPreferenceClicked(preference, newValue);
    }

    @Override
    protected void onFragmentResume() {
        setCheckStatus();
    }

    @Override
    protected void onFragmentResult(int requestCode) {
        if (requestCode == SettingsProvider.CODE_OVERLAY) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(getContext())) {
                    setChecked(true);
                    FloatTouchView.get().display(getContext());
                } else {
                    setChecked(false);
                    FloatTouchView.get().removeView();
                }
            }
        }
    }

    @Override
    protected void setCheckStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(getContext())) {
                setChecked(true);
                FloatTouchView.get().display(getContext());
            } else {
                setChecked(false);
                FloatTouchView.get().removeView();
            }
        } else {
            FloatTouchView.get().display(getContext());
            setEnabled(false);
            setChecked(true);
        }
    }

}
