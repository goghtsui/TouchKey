package com.gogh.floattouchkey.preference.list;

import android.content.Context;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.AttributeSet;

import com.gogh.floattouchkey.observable.GestureObservable;

import java.util.Observable;
import java.util.Observer;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 10/11/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 10/11/2017 do fisrt create. </li>
 */

public class BaseGesturePreference extends ListPreference implements Preference.OnPreferenceChangeListener, Observer {

    protected static final String EMPTY = "empty";
    protected static final int DOUBLE_CLICK_INDEX = 2;
    protected static final int SWIP_LEFT_INDEX = 11;
    protected static final int SWIP_UP_INDEX = 5;
    protected static final int SWIP_RIGHT_INDEX = 6;
    protected static final int SWIP_DOWN_INDEX = 4;
    protected static int SINGLE_CLICK_INDEX = 1;

    public BaseGesturePreference(Context context) {
        super(context);
        GestureObservable.get().addObserver(this);
        setOnPreferenceChangeListener(this);
    }

    public BaseGesturePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        GestureObservable.get().addObserver(this);
        setOnPreferenceChangeListener(this);
    }

    public BaseGesturePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        GestureObservable.get().addObserver(this);
        setOnPreferenceChangeListener(this);
    }

    public BaseGesturePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        GestureObservable.get().addObserver(this);
        setOnPreferenceChangeListener(this);
    }

    protected void setSelectIndex(int selectIndex) {
        setValueIndex(selectIndex);
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
        onPreferenceSelected(preference, newValue);
        return true;
    }

    protected void onPreferenceSelected(Preference preference, Object newValue) {
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        updateSummary();
    }

    protected void updateSummary() {
        // need to imp
    }

}
