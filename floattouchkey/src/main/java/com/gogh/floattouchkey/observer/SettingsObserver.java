package com.gogh.floattouchkey.observer;

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

public class SettingsObserver implements Observer {

    private OnPreferenceClickListener onClickObserverListener;

    public static SettingsObserver get() {
        return SingleHolder.HOLDER;
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
        if (onClickObserverListener != null) {
            int code = Integer.valueOf(String.valueOf(arg));
            switch (code) {
                case SettingsProvider.CODE_OVERLAY:
                    onClickObserverListener.onOverlayClicked();
                    break;
                case SettingsProvider.CODE_ACCESSIBILITY:
                    onClickObserverListener.onAccessibilityClicked();
                    break;
                case SettingsProvider.CODE_DEVICEADMIN:
                    onClickObserverListener.onDeviceAdminClicked();
                    break;
                default:
                    break;
            }
        }
    }

    public void observe(OnPreferenceClickListener onClickObserverListener) {
        this.onClickObserverListener = onClickObserverListener;
    }

    public void cancelClickListener() {
        onClickObserverListener = null;
    }

    public interface OnPreferenceClickListener {
        void onOverlayClicked();

        void onAccessibilityClicked();

        void onDeviceAdminClicked();
    }

    private static final class SingleHolder {
        private static final SettingsObserver HOLDER = new SettingsObserver();
    }

}
