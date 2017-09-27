package com.gogh.floattouchkey.widget;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.gogh.floattouchkey.R;
import com.gogh.floattouchkey.observable.EventObservabale;
import com.gogh.floattouchkey.observable.SettingsObservable;
import com.gogh.floattouchkey.provider.SensorProvider;
import com.gogh.floattouchkey.provider.SettingsProvider;
import com.gogh.floattouchkey.service.TouchAccessibilityService;
import com.gogh.floattouchkey.uitls.Logger;

import java.util.Observable;
import java.util.Observer;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/26/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/26/2017 do fisrt create. </li>
 */

public class FloatTouchView implements Observer {

    private static final String TAG = "FloatTouchView";

    private static final float IME_WINDOW_SIZE_SC = 2.7785f;

    private int mDensity;

    private int mIMEHeight;

    private int mScreenHeight;

    private ImageView mFloatView;

    private Rect mRect = new Rect();

    private boolean isImeHidden = false;

    private boolean isImeReposition = false;

    private int[] oldPosition = new int[]{0, 152};

    //创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager;

    private WindowManager.LayoutParams wmParams;

    private FloatTouchView() {
    }

    public static FloatTouchView get() {
        return SingleHolder.HOLDER;
    }

    public void removeView() {
        if (mWindowManager != null && mFloatView != null) {
            SettingsObservable.get().deleteObserver(this);
            mWindowManager.removeView(mFloatView);
        }
    }

    public void display(Context context) {
        synchronized (this) {
            if (mWindowManager == null) {
                SettingsObservable.get().addObserver(this);
                initSize(context);
                //通过getApplication获取的是WindowManagerImpl.CompatModeWrapper
                mWindowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                initParams();
                createFloatView(context);
                // 添加mFloatLayout
                mWindowManager.addView(mFloatView, wmParams);
            }
        }
    }

    private void initSize(Context context) {
        mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
        mDensity = (int) context.getResources().getDisplayMetrics().density;
        mIMEHeight = (int) ((float) mScreenHeight / IME_WINDOW_SIZE_SC) + 48 * mDensity; //Offset;
        Logger.i(TAG, String.format("Screen height:%s, ime height:%s", mScreenHeight, mIMEHeight));
    }

    private void initParams() {
        wmParams = new WindowManager.LayoutParams();
        //设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 152;
        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    private void createFloatView(final Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //获取浮动窗口视图所在布局
        mFloatView = (ImageView) inflater.inflate(R.layout.float_ball_layout, null);
        mFloatView.setAlpha(1.0f);
        mFloatView.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        mFloatView.getWindowVisibleDisplayFrame(mRect);

        //设置监听浮动窗口的触摸移动
        mFloatView.setOnTouchListener(new View.OnTouchListener() {

            boolean isClick;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (SettingsProvider.get().isLockPosition()) {
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isClick = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isClick = true;
                        // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                        wmParams.x = (int) event.getRawX()
                                - mFloatView.getMeasuredWidth() / 2;
                        // 减25为状态栏的高度
                        wmParams.y = (int) event.getRawY()
                                - mFloatView.getMeasuredHeight() / 2 - 75;
                        // 刷新
                        mWindowManager.updateViewLayout(mFloatView, wmParams);
                        return true;
                    case MotionEvent.ACTION_UP:
                        oldPosition[0] = wmParams.x;
                        oldPosition[1] = wmParams.y;
                        resetPosition();
                        return isClick;// 此处返回false则属于移动事件，返回true则释放事件，可以出发点击否。
                    default:
                        break;
                }
                return false;
            }
        });

        mFloatView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TouchAccessibilityService.getService().isConnected()) {
                    if (SettingsProvider.get().hasVibrate()) {
                        SensorProvider.get().vibrate();
                    }
                    if (SettingsProvider.get().hasSound()) {
                        SensorProvider.get().sound();
                    }
                    EventObservabale.get().handleEvent(AccessibilityService.GLOBAL_ACTION_BACK);
                } else {
                    Snackbar.make(SettingsProvider.get().getView(), context.getResources().getString(R.string.toast_touch_key_tips),
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    public void resetPosition() {
        if (SettingsProvider.get().isAutoEdge()) {
            if (wmParams.x < (mRect.width() - mFloatView.getWidth()) / 2) {
                wmParams.x = 0;
            } else {
                wmParams.x = mRect.width();
            }
            if (wmParams.y < mRect.top) {
                wmParams.y = mRect.top;
            }

            mWindowManager.updateViewLayout(mFloatView, wmParams);
        }
    }

    public void resetAlpha() {
        if (isImeHidden) {
            mFloatView.setAlpha(1.0f);
            isImeHidden = false;
        } else {
            mFloatView.setAlpha(0f);
            isImeHidden = true;
        }
    }


    public void restoreXYOnImeHidden() {
        if (isImeReposition) {
            Logger.i(TAG, String.format("restoreXYOnImeHidden to: %s, %s", oldPosition[0], oldPosition[1]));
            restorePoint();
            isImeReposition = false;
        }
    }

    /**
     * 在输入法的情况下重置位置
     *
     * @author 高晓峰
     * @date 9/27/2017
     * @ChangeLog: <li> 高晓峰 on 9/27/2017 </li>
     */
    public void repositionInIme() {
        if (mScreenHeight - wmParams.y <= mIMEHeight) {
            oldPosition[0] = wmParams.x;
            oldPosition[1] = wmParams.y;
            Logger.i(TAG, "Reposition within IME");
            wmParams.y -= (mIMEHeight - (mScreenHeight - wmParams.y) + mDensity * 48 * 2 /*Nav and candidate*/);
            mWindowManager.updateViewLayout(mFloatView, wmParams);
            isImeReposition = true;
        }
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        // 更新按钮状态及UI
        int code = Integer.valueOf(String.valueOf(arg));
        if (code == SettingsProvider.CODE_AUTOEDGE) {
            if (SettingsProvider.get().isAutoEdge()) {
                resetPosition();
            } else {
                restorePoint();
            }
        }
    }

    private void restorePoint() {
        wmParams.x = oldPosition[0];
        wmParams.y = oldPosition[1];
        mWindowManager.updateViewLayout(mFloatView, wmParams);
    }

    private static final class SingleHolder {
        private static final FloatTouchView HOLDER = new FloatTouchView();
    }

}
