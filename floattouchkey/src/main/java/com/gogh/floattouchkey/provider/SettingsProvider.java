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
    public static final int CODE_AUTOALPHA = 106;
    public static final int CODE_ROTATION = 107;
    public static final int CODE_BREATH = 108;
    public static final int CODE_WINDOW_TYPE = 109;

    private View view;
    private Context context;
    private SharedPreferences mPreferences;

    public static SettingsProvider get() {
        return SingleHolder.HOLDER;
    }

    public void init(Context context, SharedPreferences preferences) {
        this.context = context;
        this.mPreferences = preferences;
        EventProvider.get().init(context, preferences);
    }

    public void setView(View view) {
        this.view = view;
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public boolean isActivateAdmin() {
        return mPreferences.getBoolean(context.getResources().getString(R.string.pref_root_category_lockscreen_key), false);
    }

    @Override
    public boolean isRoot() {
        return mPreferences.getBoolean(context.getResources().getString(R.string.pref_root_category_lockscreen_root_key), false);
    }

    @Override
    public void setRootStatus(boolean isRoot) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.pref_root_category_lockscreen_root_key), isRoot);
        editor.commit();
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

    @Override
    public int getAlpha() {
        return mPreferences.getInt(context.getResources().getString(R.string.pref_root_category_style_alpha_key), 100);
    }

    @Override
    public boolean isRotate() {
        return mPreferences.getBoolean(context.getResources().getString(R.string.pref_root_category_style_rotate_key), false);
    }

    @Override
    public boolean isBreath() {
        return mPreferences.getBoolean(context.getResources().getString(R.string.pref_root_category_style_breath_key), false);
    }

    @Override
    public boolean isAutoAlpha() {
        return mPreferences.getBoolean(context.getResources().getString(R.string.pref_root_category_style_auto_alpha_key), false);
    }

    private static final class SingleHolder {
        private static final SettingsProvider HOLDER = new SettingsProvider();
    }

}
