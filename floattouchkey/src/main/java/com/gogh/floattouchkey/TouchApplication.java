package com.gogh.floattouchkey;

import android.app.Application;
import android.content.Intent;

import com.gogh.floattouchkey.observable.SettingsObservable;
import com.gogh.floattouchkey.observer.SettingsObserver;
import com.gogh.floattouchkey.provider.ImePackageProvider;
import com.gogh.floattouchkey.provider.SensorProvider;
import com.gogh.floattouchkey.service.TouchAccessibilityService;
import com.gogh.floattouchkey.uitls.FileUtil;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.IOException;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/22/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/22/2017 do fisrt create. </li>
 */

public class TouchApplication extends Application {

    private static TouchApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        SensorProvider.get().init(this);
        SettingsObservable.get().addObserver(SettingsObserver.get());
        ImePackageProvider.initAsync(this);
        startService(new Intent(this, TouchAccessibilityService.class));
//        AdSdk.initialize(this, appId);

        try {
            FileUtil.copyFile(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TessBaseAPI getTessAPI(){
        TessBaseAPI tessApi = new TessBaseAPI();
        tessApi.init(FileUtil.getTessbasePath(this), FileUtil.DEFAULT_LANGUAGE);
        //设置识别模式
        tessApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO_OSD);
        return tessApi;
    }

    public static TouchApplication get(){
        return INSTANCE;
    }

}
