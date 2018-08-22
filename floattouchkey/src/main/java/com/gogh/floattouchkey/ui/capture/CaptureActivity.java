package com.gogh.floattouchkey.ui.capture;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gogh.floattouchkey.R;
import com.gogh.floattouchkey.common.Event;
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
    private static final int REQUEST_MEDIA_PROJECTION = 1;
    private CaptureSizeView mCaptureSizeView;
    private LinearLayout mTypeContainer;
    private TextView mFreeCapture;
    private TextView mFullCapture;
    private TextView mRectangleCapture;
    private ImageView mOkButton;
    private int mMinWidth;
    private int mEventType = Event.CAPTURE_NONE;
    private MediaProjectionManager mMediaProjectionManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_layout);
        Screen.get().init(this);
        initData();
        initView();
    }

    private void initView() {
        mMinWidth = getResources().getDimensionPixelOffset(R.dimen.capture_selector_min_width);
        mCaptureSizeView = (CaptureSizeView) findViewById(R.id.activity_capture_markview);
        mTypeContainer = (LinearLayout) findViewById(R.id.activity_capture_type_container);
        mFreeCapture = (TextView) findViewById(R.id.activity_capture_freecapture);
        mFullCapture = (TextView) findViewById(R.id.activity_capture_fullcapture);
        mRectangleCapture = (TextView) findViewById(R.id.activity_capture_rectanglecapture);
        mOkButton = (ImageView) findViewById(R.id.activity_capture_select_ok);

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOkButton.setVisibility(View.GONE);
                mCaptureSizeView.setVisibility(View.GONE);
                startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
            }
        });

        mCaptureSizeView.setOnCaptureListener(new CaptureSizeView.OnCaptureListener() {
            @Override
            public void onStart() {
                mOkButton.setVisibility(View.GONE);
            }

            @Override
            public void onComplete() {
                Logger.d(TAG, "capture onComplete");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Rect mRect = new Rect(mCaptureSizeView.getGraphicPath().getLeft(), mCaptureSizeView.getGraphicPath().getTop(),
                                mCaptureSizeView.getGraphicPath().getRight(), mCaptureSizeView.getGraphicPath().getBottom());

                        int width = Math.abs(mRect.left - mRect.right);
                        if(width > 2){
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.topMargin = mRect.bottom + 10;
                            params.leftMargin = mRect.right;
                            mOkButton.setLayoutParams(params);
                            mOkButton.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

        mFreeCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTypeContainer.setVisibility(View.GONE);
                mEventType = Event.CAPTURE_FREE;
            }
        });
        mRectangleCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTypeContainer.setVisibility(View.GONE);
                mEventType = Event.CAPTURE_RECTANGLE;
            }
        });
        mFullCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCaptureSizeView.setVisibility(View.GONE);
                mTypeContainer.setVisibility(View.GONE);
                mEventType = Event.CAPTURE_FULL;
                startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
            }
        });
    }

    private void initData() {
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d(TAG, "onActivityResult");
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode == RESULT_OK && data != null) {
                Logger.i(TAG, "user agree the application to capture screen");
                ScreenCapture.get().init(this, mCaptureSizeView.getGraphicPath(), data, mEventType);
            } else {
                this.finish();
            }
        }
    }

}
