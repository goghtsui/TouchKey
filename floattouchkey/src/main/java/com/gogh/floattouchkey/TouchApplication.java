package com.gogh.floattouchkey;

import android.app.Application;

import com.gogh.floattouchkey.observable.ActivityResultObservable;
import com.gogh.floattouchkey.observable.SettingsObservable;
import com.gogh.floattouchkey.observer.ActivityResultObserver;
import com.gogh.floattouchkey.observer.SettingsObserver;

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
        SettingsObservable.get().addObserver(SettingsObserver.get());
        ActivityResultObservable.get().addObserver(ActivityResultObserver.get());
    }

}
