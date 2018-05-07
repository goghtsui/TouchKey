package com.gogh.floattouchkey.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.gogh.floattouchkey.R;
import com.gogh.floattouchkey.uitls.Intenter;

/**
 * Copyright (c) 2016 All rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 1/18/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 1/18/2017 do fisrt create. </li>
 */
public class ShareActivity extends BaseAppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new TextView(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intenter.share(this, getResources().getString(R.string.settting_prefrences_about_author_summary));
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.onBackPressed();
    }
}
