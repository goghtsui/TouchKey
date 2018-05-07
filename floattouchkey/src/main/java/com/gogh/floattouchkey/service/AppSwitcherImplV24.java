package com.gogh.floattouchkey.service;

import android.accessibilityservice.AccessibilityService;

import com.chrisplus.rootmanager.RootManager;
import com.gogh.floattouchkey.task.ThreadExecutor;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 10/10/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 10/10/2017 do fisrt create. </li>
 */

class AppSwitcherImplV24 extends AppSwitcherImpl {
    private static final String TAG = "AppSwitcherImplV24";

    AppSwitcherImplV24(TouchAccessibilityService service) {
        super(service);
    }

    @Override
    public synchronized boolean switchApp(TouchAccessibilityService service) {
        return service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS)
                && service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
    }

    @Override
    public void killCurrent() {
        ThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                RootManager.getInstance().killProcessByName(mCurrentPkg);
            }
        });
    }
}
