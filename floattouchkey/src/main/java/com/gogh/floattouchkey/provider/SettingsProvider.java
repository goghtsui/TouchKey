package com.gogh.floattouchkey.provider;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/22/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/22/2017 do fisrt create. </li>
 */

public class SettingsProvider {

    public static final int CODE_OVERLAY = 0x00011000;
    public static final int CODE_ACCESSIBILITY = 0x00011001;

    private static final String NAME = "touch_preference";

    private Context context;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    public static SettingsProvider get() {
        return SingleHolder.HOLDER;
    }

    public void init(Context context) {
        this.context = context;
        mPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    private static final class SingleHolder {
        private static final SettingsProvider HOLDER = new SettingsProvider();
    }


}
