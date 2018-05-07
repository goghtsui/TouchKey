package com.gogh.floattouchkey;

import android.app.Application;
import android.content.Intent;

import com.gogh.floattouchkey.observable.SettingsObservable;
import com.gogh.floattouchkey.observer.SettingsObserver;
import com.gogh.floattouchkey.provider.ImePackageProvider;
import com.gogh.floattouchkey.provider.SensorProvider;
import com.gogh.floattouchkey.service.TouchAccessibilityService;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/22/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/22/2017 do fisrt create. </li>
 */

public class TouchApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, TouchAccessibilityService.class));
        SensorProvider.get().init(this);
        SettingsObservable.get().addObserver(SettingsObserver.get());
        ImePackageProvider.initAsync(this);
//        AdSdk.initialize(this, appId);
    }

}
