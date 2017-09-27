package com.gogh.floattouchkey.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/21/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/21/2017 do fisrt create. </li>
 */

public class BaseFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    protected static final int STATUS_ONCREATE = 0x00001001;
    protected static final int STATUS_ONRESULT = 0x00001010;
    protected static final int STATUS_ONPAUSE = 0x00001011;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        onPreferenceChanged(sharedPreferences, key);
    }

    protected void onPreferenceChanged(SharedPreferences preferences, String key) {

    }

}
