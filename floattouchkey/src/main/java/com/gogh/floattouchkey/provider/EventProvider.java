package com.gogh.floattouchkey.provider;

import android.content.Context;
import android.content.SharedPreferences;

import com.gogh.floattouchkey.R;
import com.gogh.floattouchkey.model.Gesture;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 10/11/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 10/11/2017 do fisrt create. </li>
 */

public class EventProvider implements Gesture {

    private Context context;
    private SharedPreferences mPreferences;

    public static EventProvider get() {
        return SingleHolder.HOLDER;
    }

    public void init(Context context, SharedPreferences preferences) {
        this.context = context;
        this.mPreferences = preferences;
    }

    @Override
    public int getSingleTapOrder() {
        return Integer.valueOf(mPreferences.getString(context.getResources().getString(R.string.pref_gesture_category_single_click_key),
                context.getResources().getString(R.string.pref_gesture_category_single_click_default_value)));
    }

    @Override
    public int getDoubleTapOrder() {
        return Integer.valueOf(mPreferences.getString(context.getResources().getString(R.string.pref_gesture_category_double_click_key),
                context.getResources().getString(R.string.pref_gesture_category_double_click_default_value)));
    }

    @Override
    public int getSwipLeftOrder() {
        return Integer.valueOf(mPreferences.getString(context.getResources().getString(R.string.pref_gesture_category_swipe_left_key),
                context.getResources().getString(R.string.pref_gesture_category_swipe_left_default_value)));
    }

    @Override
    public int getSwipeUpOrder() {
        return Integer.valueOf(mPreferences.getString(context.getResources().getString(R.string.pref_gesture_category_swipe_up_key),
                context.getResources().getString(R.string.pref_gesture_category_swipe_up_default_value)));
    }

    @Override
    public int getSwipeRightOrder() {
        return Integer.valueOf(mPreferences.getString(context.getResources().getString(R.string.pref_gesture_category_swipe_right_key),
                context.getResources().getString(R.string.pref_gesture_category_double_right_default_value)));
    }

    @Override
    public int getSwipeDownOrder() {
        return Integer.valueOf(mPreferences.getString(context.getResources().getString(R.string.pref_gesture_category_swipe_down_key),
                context.getResources().getString(R.string.pref_gesture_category_swipe_down_default_value)));
    }

    private static final class SingleHolder {
        private static final EventProvider HOLDER = new EventProvider();
    }
}
