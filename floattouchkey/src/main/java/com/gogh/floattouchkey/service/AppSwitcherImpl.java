package com.gogh.floattouchkey.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;

import com.chrisplus.rootmanager.RootManager;
import com.gogh.floattouchkey.provider.ImePackageProvider;
import com.gogh.floattouchkey.provider.LauncherPackageProvider;
import com.gogh.floattouchkey.task.ThreadExecutor;
import com.gogh.floattouchkey.uitls.Logger;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 10/10/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 10/10/2017 do fisrt create. </li>
 */

class AppSwitcherImpl implements AppSwitcher {

    private static final String TAG = "AppSwitcherImpl";
    private long clickTimeMillions = 0;
    private static final int DELAY = 300;
    String mCurrentPkg;
    private PackageManager packageManager;
    private String mPreviousPkg;

    AppSwitcherImpl(TouchAccessibilityService service) {
        this.packageManager = service.getPackageManager();
        service.setOnAccessibilityEventListener(new TouchAccessibilityService.AccessibilityEventListener() {
            @Override
            public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
                if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                    String pkgName = accessibilityEvent.getPackageName() != null ? accessibilityEvent.getPackageName().toString() : "";
                    String clz = accessibilityEvent.getClassName() != null ? accessibilityEvent.getClassName().toString() : "";
                    Logger.i(TAG, String.format("AppSwitcherImpl: onAccessibilityEvent: %s, %s", pkgName, clz));
                    if (ignore(pkgName, clz)){
                        return;
                    }
                    mPreviousPkg = mCurrentPkg;
                    mCurrentPkg = pkgName;
                }
            }
        });
    }

    // FIXME: 10/10/2017 launcher过滤条件
    private boolean ignore(String pkg, String clz) {
        return
                TextUtils.isEmpty(pkg)
                        || pkg.equals(mCurrentPkg)
                        || ImePackageProvider.isIME(pkg)
                        || LauncherPackageProvider.isLauncherApp(pkg)
                        || pkg.equals("android")
                        || pkg.contains("incall")
                        || pkg.contains("andorid.systemui");
    }

    @Override
    public boolean switchApp(TouchAccessibilityService service) {
        if (TextUtils.isEmpty(mPreviousPkg)){
            return false;
        }

        Intent pkgIntent = packageManager.getLaunchIntentForPackage(mPreviousPkg);
        if (pkgIntent != null) {
            pkgIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            service.startActivity(pkgIntent);
            return true;
        }

        return false;
    }

    @Override
    public synchronized boolean recentTask(TouchAccessibilityService service) {
        long currentTimeMillis = System.currentTimeMillis();
        if((currentTimeMillis - clickTimeMillions) > DELAY){
            clickTimeMillions = currentTimeMillis;
            return service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
        }
        return service.performGlobalAction(-1);
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
