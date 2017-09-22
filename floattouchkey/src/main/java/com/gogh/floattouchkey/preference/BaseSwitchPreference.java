package com.gogh.floattouchkey.preference;

import android.content.Context;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.gogh.floattouchkey.observer.ActivityResultObserver;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/22/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/22/2017 do fisrt create. </li>
 */

public class BaseSwitchPreference extends SwitchPreference implements ActivityResultObserver.OnActivityResult,
        Preference.OnPreferenceChangeListener {

    public BaseSwitchPreference(Context context) {
        super(context);
        setOnPreferenceChangeListener(this);
        ActivityResultObserver.get().subscribe(this);
    }

    public BaseSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOnPreferenceChangeListener(this);
        ActivityResultObserver.get().subscribe(this);
    }

    public BaseSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnPreferenceChangeListener(this);
        ActivityResultObserver.get().subscribe(this);
    }

    public BaseSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnPreferenceChangeListener(this);
        ActivityResultObserver.get().subscribe(this);
    }

    @Override
    protected void onBindView(View view) {
        // 必须要调用onBindView，因为我们只需要在后面添加相应的方法
        super.onBindView(view);
        // 如果是Switch，当enabled为false，通过调用自定义的setEnabledStateOnViews使其enabled不受影响
        if (!isEnabled()) {
            setEnabledStateOnViews(view, isEnabled());
        }
    }

    private void setEnabledStateOnViews(View v, boolean enabled) {
        v.setEnabled(enabled);

        if (v instanceof ViewGroup) {
            final ViewGroup vg = (ViewGroup) v;
            for (int i = vg.getChildCount() - 1; i >= 0; i--) {
                //判断是否为Switch控件
                if (vg.getChildAt(i) instanceof Switch) {
                    setEnabledStateOnViews(vg.getChildAt(i), !enabled);
                } else {
                    setEnabledStateOnViews(vg.getChildAt(i), enabled);
                }
            }
        }
    }

    /**
     * Called when a Preference has been changed by the user. This is
     * called before the state of the Preference is about to be updated and
     * before the state is persisted.
     *
     * @param preference The changed Preference.
     * @param newValue   The new value of the Preference.
     * @return True to update the state of the Preference with the new value.
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return onPreferenceChanged(preference, newValue);
    }

    protected boolean onPreferenceChanged(Preference preference, Object newValue) {
        return false;
    }

    @Override
    public void onActivityResult(int code) {
        onFragmentResult(code);
    }

    protected void onFragmentResult(int code) {
        // need to impl
    }

}
