package com.gogh.floattouchkey.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gogh.floattouchkey.provider.SettingsProvider;
import com.gogh.floattouchkey.receiver.IMEPackageReceiver;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/27/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/27/2017 do fisrt create. </li>
 */

public class BaseAppCompatActivity extends AppCompatActivity {

    private IMEPackageReceiver imePackageReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsProvider.get().setView(getWindow().getDecorView());
        imePackageReceiver = new IMEPackageReceiver();
        imePackageReceiver.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imePackageReceiver.unRegister(this);
    }
}
