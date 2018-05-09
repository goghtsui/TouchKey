package com.gogh.floattouchkey.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

import com.gogh.floattouchkey.common.GlobalActionExt;
import com.gogh.floattouchkey.observable.EventObservabale;
import com.gogh.floattouchkey.provider.ImePackageProvider;
import com.gogh.floattouchkey.provider.SensorProvider;
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

    private AccessibilityEventListener accessibilityEventListener;

    public static TouchAccessibilityService getService() {
        return SERVICE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SERVICE = this;
        EventObservabale.get().addObserver(this);
        EventHandleService.get().initEvent(this);
        AppSwitcherCompat.get().init(this);
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
        if (accessibilityEventListener != null) {
            accessibilityEventListener.onAccessibilityEvent(accessibilityEvent);
        }
        if (SettingsProvider.get().isRepositionInIme()) {
            String pkgName = accessibilityEvent.getPackageName() != null ? accessibilityEvent.getPackageName().toString() : "";
            String clz = accessibilityEvent.getClassName() != null ? accessibilityEvent.getClassName().toString() : "";
            Logger.i(TAG, String.format("onAccessibilityEvent: %s, %s", pkgName, clz));
            switch (accessibilityEvent.getEventType()) {
                case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                    if (ImePackageProvider.isIME(pkgName)) {
                        // 输入法窗口打开，移动按钮
                        Logger.i(TAG, String.format("onAccessibilityEvent in IME: %s", pkgName));
                        if (SettingsProvider.get().isRepositionInIme()) {
                            FloatTouchView.get().repositionInIme();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
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
        Logger.d(TAG, "onServiceConnected onServiceConnected");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isConnected = false;
        Logger.d(TAG, "onServiceConnected onUnbind");
        return super.onUnbind(intent);
    }

    public boolean isConnected() {
        Logger.d(TAG, "onServiceConnected isConnected " + isConnected);
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
            case GlobalActionExt.GLOBAL_ACTION_HOME:
                performGlobalAction(GLOBAL_ACTION_HOME);
                break;
            case GlobalActionExt.GLOBAL_ACTION_LOCK_SCREEN:
                SensorProvider.get().lockScreen();
                break;
            case GlobalActionExt.GLOBAL_ACTION_LONG_PRESSED:
                SensorProvider.get().vibrate();
                break;
            default:
                EventHandleService.get().run(event);
                break;
        }
    }

    public void setOnAccessibilityEventListener(AccessibilityEventListener accessibilityEventListener) {
        this.accessibilityEventListener = accessibilityEventListener;
    }

    interface AccessibilityEventListener {
        void onAccessibilityEvent(AccessibilityEvent accessibilityEvent);
    }

}
