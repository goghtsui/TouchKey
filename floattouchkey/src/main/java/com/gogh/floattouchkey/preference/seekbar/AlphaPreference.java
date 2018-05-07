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

public class AlphaPreference extends BaseSeekBarPreference implements BaseSeekBarPreference.OnSeekBarPreferenceChangedListener {

    public AlphaPreference(Context context) {
        super(context);
        setOnSeekBarPrefsChangeListener(this);
    }

    public AlphaPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnSeekBarPrefsChangeListener(this);
    }

    public AlphaPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnSeekBarPrefsChangeListener(this);
    }

    @Override
    protected void onBindView(SeekBar seekBar) {
        setSummary(getContext().getResources().getString(R.string.pref_root_category_style_alpha_title));
        setIcon(R.drawable.ic_pref_tranparent);
    }

    @Override
    public void onStopTracking(String key, SeekBar seekBar) {
    }

    @Override
    public void onStartTracking(String key) {
        FloatTouchView.get().restoreAlpha();
    }

    @Override
    public void onProgressChanged(String key, int progress) {
        float alphaRate = (progress * 1.0f) / (MAX_VALUE * 1.0f);
        FloatTouchView.get().resetAlphaBySetting(alphaRate);
    }

}
