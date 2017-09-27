package com.gogh.floattouchkey.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

import com.gogh.floattouchkey.observable.EventObservabale;
import com.gogh.floattouchkey.provider.ImePackageProvider;
import com.gogh.floattouchkey.provider.SettingsProvider;
import com.gogh.floattouchkey.uitls.Logger;
import com.gogh.floattouchkey.widget.FloatTouchView;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by XiaoFeng on 9/25/2017.
 */

public class TouchAccessibilityService extends AccessibilityService implements Observer {

    private static final String TAG = "TouchAccessibilityServi";
    private static TouchAccessibilityService SERVICE;
    private boolean isConnected = false;
    private long mTime = 0;
    private int mType = Integer.MIN_VALUE;

    public static TouchAccessibilityService getService() {
        return SERVICE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SERVICE = this;
        EventObservabale.get().addObserver(this);
    }

    /**
     * Callback for {@link AccessibilityEvent}s.
     *
     * @param accessibilityEvent The new event. This event is owned by the caller and cannot be used after
     *                           this method returns. Services wishing to use the event after this method returns should
     *                           make a copy.
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Logger.d(TAG, "onAccessibilityEvent" + accessibilityEvent.getEventType());
        if (SettingsProvider.get().isRepositionInIme() || SettingsProvider.get().isHiddenInIme()) {
            switch (accessibilityEvent.getEventType()) {
                case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                    String pkgName = accessibilityEvent.getPackageName() != null ? accessibilityEvent.getPackageName().toString() : "";
                    String clz = accessibilityEvent.getClassName() != null ? accessibilityEvent.getClassName().toString() : "";
                    Logger.i(TAG, String.format("onAccessibilityEvent: %s, %s", pkgName, clz));
                    if (ImePackageProvider.isIME(pkgName)) {
                        mTime = System.currentTimeMillis();
                        Logger.i(TAG, String.format("onAccessibilityEvent in IME: %s", pkgName));
                        if (SettingsProvider.get().isHiddenInIme()) {
                            mType = Integer.MAX_VALUE;
                            FloatTouchView.get().resetAlpha();
                        } else if (SettingsProvider.get().isRepositionInIme()) {
                            FloatTouchView.get().repositionInIme();
                        }
                    }
                    break;
                case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                    Logger.i(TAG, "onAccessibilityEvent out of IME reset status.");
                    if ((System.currentTimeMillis() - mTime) > 300 && mTime != 0) {
                        if (SettingsProvider.get().isHiddenInIme() && mType == Integer.MAX_VALUE) {
                            FloatTouchView.get().resetAlpha();
                            mType = Integer.MIN_VALUE;
                        } else if (SettingsProvider.get().isRepositionInIme()) {
                            FloatTouchView.get().restoreXYOnImeHidden();
                        }
                    }
                    break;
                default:
                    break;
            }
        }

       /* if (SettingsProvider.get().isRepositionInIme() || SettingsProvider.get().isHiddenInIme()) {
            switch (accessibilityEvent.getEventType()) {
                case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                    String pkgName = accessibilityEvent.getPackageName() != null ? accessibilityEvent.getPackageName().toString() : "";
                    String clz = accessibilityEvent.getClassName() != null ? accessibilityEvent.getClassName().toString() : "";
                    Logger.i(TAG, String.format("onAccessibilityEvent: %s, %s", pkgName, clz));
                    if (ImePackageProvider.isIME(pkgName)) {
                        mTime = System.currentTimeMillis();
                        mType = TYPE_RESET_POSITION;
                        Logger.i(TAG, String.format("onAccessibilityEvent in IME: %s", pkgName));
                        FloatTouchView.get().repositionInIme();
                    } else if (SettingsProvider.get().isHiddenInIme()) {
                        Logger.i(TAG, "onAccessibilityEvent in IME hide it.");
                        mTime = System.currentTimeMillis();
                        mType = TYPE_RESET_ALPHA;
                        FloatTouchView.get().resetAlpha();
                    }
                    break;
                case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                    if (mTime != 0 && (System.currentTimeMillis() - mTime) > 300) {
                        switch (mType) {
                            case TYPE_RESET_POSITION:
                                Logger.i(TAG, "onAccessibilityEvent out of IME restore position.");
                                FloatTouchView.get().restoreXYOnImeHidden();
                                break;
                            case TYPE_RESET_ALPHA:
                                Logger.i(TAG, "onAccessibilityEvent out of IME reset alpha.");
                                FloatTouchView.get().resetAlpha();
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                default:
                    break;
            }
        }*/
    }

    /**
     * Callback for interrupting the accessibility feedback.
     */
    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        isConnected = true;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isConnected = false;
        return super.onUnbind(intent);
    }

    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
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
        int event = Integer.valueOf(String.valueOf(arg));
        switch (event) {
            case GLOBAL_ACTION_BACK:
                performGlobalAction(GLOBAL_ACTION_BACK);
                break;
            case GLOBAL_ACTION_HOME:
                performGlobalAction(GLOBAL_ACTION_HOME);
                break;
            default:
                break;
        }
    }

}
