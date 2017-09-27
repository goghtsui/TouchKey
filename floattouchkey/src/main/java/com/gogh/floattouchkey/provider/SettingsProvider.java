package com.gogh.floattouchkey.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.gogh.floattouchkey.R;
import com.gogh.floattouchkey.model.Switcher;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/22/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/22/2017 do fisrt create. </li>
 */

public class SettingsProvider implements Switcher {

    public static final int CODE_ERROR = -1;
    public static final int CODE_CHECK_RESET = -2;
    public static final int CODE_OVERLAY = 100;
    public static final int CODE_ACCESSIBILITY = 101;
    public static final int CODE_DEVICEADMIN = 102;
    public static final int CODE_LOCKPOINT = 103;
    public static final int CODE_AUTOEDGE = 104;
    public static final int CODE_IME_REPOSITION= 105;

    private View view;
    private Context context;
    private SharedPreferences mPreferences;

    public static SettingsProvider get() {
        return SingleHolder.HOLDER;
    }

    public void init(Context context, View view, SharedPreferences preferences) {
        this.view = view;
        this.context = context;
        this.mPreferences = preferences;
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public boolean isLockPosition() {
        return mPreferences.getBoolean(context.getResources().getString(R.string.pref_root_category_settings_position_locker), false);
    }

    @Override
    public boolean isAutoEdge() {
        return mPreferences.getBoolean(context.getResources().getString(R.string.pref_root_category_settings_edge_key), false);
    }

    @Override
    public boolean hasSound() {
        return mPreferences.getBoolean(context.getResources().getString(R.string.pref_root_category_settings_sound_key), false);
    }

    @Override
    public boolean hasVibrate() {
        return mPreferences.getBoolean(context.getResources().getString(R.string.pref_root_category_settings_vibrate_key), false);
    }

    @Override
    public boolean isRepositionInIme() {
        return mPreferences.getBoolean(context.getResources().getString(R.string.pref_root_category_settings_ime_key), false);
    }

    @Override
    public boolean isHiddenInIme() {
        return mPreferences.getBoolean(context.getResources().getString(R.string.pref_root_category_settings_ime_hidden_key), false);
    }

    private static final class SingleHolder {
        private static final SettingsProvider HOLDER = new SettingsProvider();
    }

}
