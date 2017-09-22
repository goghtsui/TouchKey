package com.gogh.floattouchkey.preference;

import android.content.Context;
import android.os.Build;
import android.preference.Preference;
import android.provider.Settings;
import android.util.AttributeSet;
import android.widget.Toast;

import com.gogh.floattouchkey.R;
import com.gogh.floattouchkey.observable.SettingsObservable;
import com.gogh.floattouchkey.provider.SettingsProvider;

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
    protected boolean onPreferenceChanged(Preference preference, Object newValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setEnabled(true);
            SettingsObservable.get().onChanged(SettingsProvider.CODE_OVERLAY);
        } else {
            setChecked(true);
            setSummary(getContext().getResources().getString(R.string.pref_root_category_switcher_overlay_summary_on));
            setEnabled(false);
        }
        return super.onPreferenceChanged(preference, newValue);
    }

    @Override
    protected void onFragmentResult(int code) {
        super.onFragmentResult(code);
        if (code == SettingsProvider.CODE_OVERLAY) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(getContext())) {
                    Toast.makeText(getContext(), "权限已授予", Toast.LENGTH_SHORT).show();
                    setChecked(true);
                } else {
                    Toast.makeText(getContext(), "权限被拒绝", Toast.LENGTH_SHORT).show();
                    setChecked(false);
                }
            }
        }
    }

}
