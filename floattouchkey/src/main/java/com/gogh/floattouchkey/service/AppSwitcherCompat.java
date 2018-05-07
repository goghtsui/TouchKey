package com.gogh.floattouchkey.service;

import android.os.Build;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 10/10/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 10/10/2017 do fisrt create. </li>
 */

public class AppSwitcherCompat implements AppSwitcher {

    private AppSwitcher mImpl;

    public static AppSwitcherCompat get() {
        return SingleHolder.HOLDER;
    }

    private static final class SingleHolder {
        private static final AppSwitcherCompat HOLDER = new AppSwitcherCompat();
    }

    public void init(final TouchAccessibilityService service) {
        getDelegate(service);
    }

    private void getDelegate(TouchAccessibilityService service) {
        this.mImpl = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                ? new AppSwitcherImplV24(service)
                : new AppSwitcherImpl(service);
    }

    @Override
    public boolean switchApp(TouchAccessibilityService service) {
        return mImpl.switchApp(service);
    }

    @Override
    public boolean recentTask(TouchAccessibilityService service) {
        return mImpl.recentTask(service);
    }

    @Override
    public void killCurrent() {
        mImpl.killCurrent();
    }
}
