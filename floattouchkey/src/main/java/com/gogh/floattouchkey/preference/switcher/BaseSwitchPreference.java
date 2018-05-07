package com.gogh.floattouchkey.preference.switcher;

import android.content.Context;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.gogh.floattouchkey.observable.ActivityResultObservable;
import com.gogh.floattouchkey.provider.SettingsProvider;

import java.util.Observable;
import java.util.Observer;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/22/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/22/2017 do fisrt create. </li>
 */

public class BaseSwitchPreference extends SwitchPreference implements Preference.OnPreferenceChangeListener, Observer {

    public BaseSwitchPreference(Context context) {
        super(context);
//        initStatus();
        setOnPreferenceChangeListener(this);
        ActivityResultObservable.get().addObserver(this);
    }

    public BaseSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
//        initStatus();
        setOnPreferenceChangeListener(this);
        ActivityResultObservable.get().addObserver(this);
    }

    public BaseSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        initStatus();
        setOnPreferenceChangeListener(this);
        ActivityResultObservable.get().addObserver(this);
    }

    public BaseSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
//        initStatus();
        setOnPreferenceChangeListener(this);
        ActivityResultObservable.get().addObserver(this);
    }

    /**
     * 初始化设置的状态
     *
     * @param
     * @ChangeLog: <li> 高晓峰 on 9/26/2017 </li>
     * @author 高晓峰
     * @date 9/26/2017
     */
    protected void initStatus() {

    }

    @Override
    protected void onBindView(View view) {
        // 必须要调用onBindView，因为我们只需要在后面添加相应的方法
        super.onBindView(view);
        // 如果是Switch，当enabled为false，通过调用自定义的setEnabledStateOnViews使其enabled不受影响
        if (!isEnabled()) {
            setEnabledStateOnViews(view, isEnabled());
        }
        initStatus();
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
        return onPreferenceClicked(preference, newValue);
    }

    /**
     * 点击事件
     *
     * @param preference 设置项
     * @param newValue   新值
     * @return
     */
    protected boolean onPreferenceClicked(Preference preference, Object newValue) {
        return true;
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
        int code = Integer.valueOf(String.valueOf(arg));
        if (code == SettingsProvider.CODE_CHECK_RESET) {
            onFragmentResume();
        } else {
            onFragmentResult(code);
        }
    }

    /**
     * 在设置之后回到当前页面刷新
     */
    protected void onFragmentResume() {
        // need to imp
    }

    /**
     * 在设置之后回到当前页面的回调
     *
     * @param requestCode 对应的设置项
     */
    protected void onFragmentResult(int requestCode) {
        // need to imp
    }

    /**
     * 重置设置的状态
     *
     * @param
     * @ChangeLog: <li> 高晓峰 on 9/26/2017 </li>
     * @author 高晓峰
     * @date 9/26/2017
     */
    protected void setCheckStatus() {
        // need to imp
    }
}
