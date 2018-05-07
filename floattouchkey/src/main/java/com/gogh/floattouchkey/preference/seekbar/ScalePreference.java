package com.gogh.floattouchkey.preference.seekbar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.gogh.floattouchkey.R;
import com.gogh.floattouchkey.widget.FloatTouchView;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/28/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/28/2017 do fisrt create. </li>
 */

public class ScalePreference extends BaseSeekBarPreference implements BaseSeekBarPreference.OnSeekBarPreferenceChangedListener {

    public ScalePreference(Context context) {
        super(context);
        setOnSeekBarPrefsChangeListener(this);
    }

    public ScalePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnSeekBarPrefsChangeListener(this);
    }

    public ScalePreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnSeekBarPrefsChangeListener(this);
    }

    @Override
    protected void onBindView(SeekBar seekBar) {
        setSummary(getContext().getResources().getString(R.string.pref_root_category_style_scale_title));
        setIcon(R.drawable.ic_pref_scale);
        setProgress(MAX_VALUE / 2);
    }

    @Override
    public void onStopTracking(String key, SeekBar seekBar) {
        float alphaRate = (seekBar.getProgress() * 1.0f) / (MAX_VALUE / 2 * 1.0f);
        FloatTouchView.get().reSize(alphaRate);
    }

    @Override
    public void onStartTracking(String key) {
        FloatTouchView.get().restoreAlpha();
        FloatTouchView.get().initSize();
    }

    @Override
    public void onProgressChanged(String key, int progress) {
    }

}
