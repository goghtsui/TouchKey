package com.gogh.floattouchkey.ui.capture;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.gogh.floattouchkey.R;
import com.gogh.floattouchkey.common.CaptureManager;
import com.gogh.floattouchkey.common.ScreenCapture;
import com.gogh.floattouchkey.ui.BaseAppCompatActivity;
import com.gogh.floattouchkey.uitls.Logger;
import com.gogh.floattouchkey.uitls.Screen;
import com.gogh.floattouchkey.view.CaptureSizeView;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 5/9/2018. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 5/9/2018 do fisrt create. </li>
 */
public class CaptureActivity extends BaseAppCompatActivity {

    private static final String TAG = "CaptureActivity";

    private CaptureSizeView mCaptureSizeView;
    private LinearLayout mTypeContainer;

    private int REQUEST_MEDIA_PROJECTION = 1;
    private MediaProjectionManager mMediaProjectionManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //5.0 之后才允许使用屏幕截图
            this.finish();
            return;
        }
        setContentView(R.layout.activity_capture_layout);
        Screen.get().init(this);
        initData();
        initView();
    }

    private void initView(){
        mCaptureSizeView = (CaptureSizeView) findViewById(R.id.activity_capture_markview);
        mCaptureSizeView.setOnCaptureListener(new CaptureSizeView.OnCaptureListener() {
            @Override
            public void onComplete() {
                mCaptureSizeView.setVisibility(View.GONE);
                startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
            }
        });
        mTypeContainer = (LinearLayout) findViewById(R.id.activity_capture_type_container);
        mTypeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTypeContainer.setVisibility(View.GONE);
            }
        });
    }

    private void initData(){
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    private void startScreenCapture(Intent intent, int resultCode) {
        try {
            new ScreenCapture(this ,intent, resultCode, null, mCaptureSizeView.getGraphicPath()).toCapture();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d(TAG, "onActivityResult");
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode == RESULT_OK && data != null) {
                Logger.i(TAG, "user agree the application to capture screen");
                CaptureManager.graphicPath = mCaptureSizeView.getGraphicPath();
                CaptureManager.setUpMediaProjection(CaptureActivity.this, data);
                CaptureManager.initScreenSize();
                CaptureManager.createImageReader();
                CaptureManager.beginScreenShot(CaptureActivity.this, data);
            }
        }
    }

}
