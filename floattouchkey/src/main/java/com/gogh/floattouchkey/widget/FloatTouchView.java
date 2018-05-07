package com.gogh.floattouchkey.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.gogh.floattouchkey.R;
import com.gogh.floattouchkey.common.GlobalActionExt;
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

    private static final int DOUBLE_CLICK_DELAY = 200;
    private static final int MIN_MOVE_DISTANCE = 100;

    /**
     * 消息标识符：自动半透消息
     */
    private static final int MSG_AUTOALPHA = 200;
    private static final int MSG_DRAG_MODE = 201;

    /**
     * 输入法窗口屏占比系数
     */
    private static final float IME_WINDOW_SIZE_SC = 2.7785f;

    private Context context;

    /**
     * 像素密度
     */
    private int mDensity;

    /**
     * 输入法窗口高度
     */
    private int mIMEHeight;

    /**
     * 屏幕高度
     */
    private int mScreenHeight;

    /**
     * 是否是可见状态
     */
    private boolean isDetachView = false;

    /**
     * 默认透明度值
     */
    private float mAlphaValue = 1.0f;

    /**
     * 是否展示了悬浮按钮
     */
    private volatile boolean isShowing = false;

    /**
     * 悬浮按钮
     */
    private ImageView mFloatView;

    /**
     * 悬浮按钮的矩形区域
     */
    private Rect mRect = new Rect();

    /**
     * 悬浮按钮的位置坐标
     */
    private int[] oldPosition = new int[]{0, 152};

    /**
     * 悬浮按钮的默认宽
     */
    private int defaultWidth = -1;

    private boolean isDraggingMode = false;

    /**
     * 呼吸动画
     */
    private ObjectAnimator breathAnimator;

    /**
     * 旋转动画
     */
    private ObjectAnimator rotateAnimator;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUTOALPHA:
                    ViewCompat.animate(mFloatView).alpha(0.3f).setDuration(1000).start();
                    break;
                case MSG_DRAG_MODE:
                    isDraggingMode = true;
                    EventObservabale.get().handleEvent(GlobalActionExt.GLOBAL_ACTION_LONG_PRESSED);
                    break;
                case GlobalActionExt.GLOBAL_ACTION_SINGLE_CLICK:
                    handleEvent(GlobalActionExt.GLOBAL_ACTION_SINGLE_CLICK);
                    break;
                case GlobalActionExt.GLOBAL_ACTION_DOUBLE_CLICK:
                    handleEvent(GlobalActionExt.GLOBAL_ACTION_DOUBLE_CLICK);
                    break;
                case GlobalActionExt.GLOBAL_ACTION_SWIPE_LEFT:
                    handleEvent(GlobalActionExt.GLOBAL_ACTION_SWIPE_LEFT);
                    break;
                case GlobalActionExt.GLOBAL_ACTION_SWIPE_UP:
                    handleEvent(GlobalActionExt.GLOBAL_ACTION_SWIPE_UP);
                    break;
                case GlobalActionExt.GLOBAL_ACTION_SWIPE_RIGHT:
                    handleEvent(GlobalActionExt.GLOBAL_ACTION_SWIPE_RIGHT);
                    break;
                case GlobalActionExt.GLOBAL_ACTION_SWIPE_DOWN:
                    handleEvent(GlobalActionExt.GLOBAL_ACTION_SWIPE_DOWN);
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * 悬浮窗口管理器
     */
    private WindowManager mWindowManager;
    /**
     * 悬浮窗口参数配置
     */
    private WindowManager.LayoutParams wmParams;

    /**
     * 获取本类实例
     *
     * @return
     */
    public static FloatTouchView get() {
        return SingleHolder.HOLDER;
    }

    private FloatTouchView() {
    }

    private void handleEvent(int msgType) {
        if (TouchAccessibilityService.getService().isConnected()) {
            switch (msgType) {
                case GlobalActionExt.GLOBAL_ACTION_SINGLE_CLICK:
                    feedback();
                    EventObservabale.get().handleEvent(GlobalActionExt.GLOBAL_ACTION_SINGLE_CLICK);
                    break;
                case GlobalActionExt.GLOBAL_ACTION_DOUBLE_CLICK:
                    feedback();
                    EventObservabale.get().handleEvent(GlobalActionExt.GLOBAL_ACTION_DOUBLE_CLICK);
                    break;
                case GlobalActionExt.GLOBAL_ACTION_SWIPE_LEFT:
                    feedback();
                    EventObservabale.get().handleEvent(GlobalActionExt.GLOBAL_ACTION_SWIPE_LEFT);
                    break;
                case GlobalActionExt.GLOBAL_ACTION_SWIPE_UP:
                    feedback();
                    EventObservabale.get().handleEvent(GlobalActionExt.GLOBAL_ACTION_SWIPE_UP);
                    break;
                case GlobalActionExt.GLOBAL_ACTION_SWIPE_RIGHT:
                    feedback();
                    EventObservabale.get().handleEvent(GlobalActionExt.GLOBAL_ACTION_SWIPE_RIGHT);
                    break;
                case GlobalActionExt.GLOBAL_ACTION_SWIPE_DOWN:
                    feedback();
                    EventObservabale.get().handleEvent(GlobalActionExt.GLOBAL_ACTION_SWIPE_DOWN);
                    break;
                default:
                    break;
            }
        } else {
            Snackbar.make(SettingsProvider.get().getView(), context.getResources().getString(R.string.toast_touch_key_tips),
                    Snackbar.LENGTH_LONG).show();
        }
        autoAlpha();
    }

    private void feedback(){
        if (SettingsProvider.get().hasVibrate()) {
            SensorProvider.get().vibrate();
        }
        if (SettingsProvider.get().hasSound()) {
            SensorProvider.get().sound();
        }
    }

    /**
     * 移除悬浮窗口
     *
     * @author 高晓峰
     * @date 10/9/2017
     * @ChangeLog: <li> 高晓峰 on 10/9/2017 </li>
     */
    public void removeView() {
        if (isShowing) {
            isShowing = false;
            if (mWindowManager != null && mFloatView != null) {
                SettingsObservable.get().deleteObserver(this);
                mWindowManager.removeView(mFloatView);
            }
        }
    }

    /**
     * 显示悬浮窗口
     *
     * @param context
     * @author 高晓峰
     * @date 10/9/2017
     * @ChangeLog: <li> 高晓峰 on 10/9/2017 </li>
     */
    public void display(Context context) {
        this.context = context;
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
                isShowing = true;
                autoAlpha();
            }
        }
    }

    /**
     * 初始化屏幕相关参数值
     *
     * @author 高晓峰
     * @date 10/9/2017
     * @ChangeLog: <li> 高晓峰 on 10/9/2017 </li>
     */
    private void initSize(Context context) {
        mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
        mDensity = (int) context.getResources().getDisplayMetrics().density;
        mIMEHeight = (int) ((float) mScreenHeight / IME_WINDOW_SIZE_SC) + 50 * mDensity; //Offset;
        Logger.i(TAG, String.format("Screen density:%s, ime height:%s", mDensity, mIMEHeight));
    }

    /**
     * 初始化悬浮窗口参数值
     *
     * @author 高晓峰
     * @date 10/9/2017
     * @ChangeLog: <li> 高晓峰 on 10/9/2017 </li>
     */
    private void initParams() {
        wmParams = new WindowManager.LayoutParams();
        //设置window type
        setWindowType(false);
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

    /**
     * 创建悬浮窗口
     *
     * @param context
     * @author 高晓峰
     * @date 10/9/2017
     * @ChangeLog: <li> 高晓峰 on 10/9/2017 </li>
     */
    private void createFloatView(final Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // 获取窗口布局
        mFloatView = (ImageView) inflater.inflate(R.layout.float_ball_layout, null);

        mFloatView.setAlpha(mAlphaValue);
        mFloatView.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        mFloatView.getWindowVisibleDisplayFrame(mRect);

        // 设置窗口的触摸移动
        mFloatView.setOnTouchListener(new View.OnTouchListener() {

            boolean isClickEvent;
            float startX = 0;
            float startY = 0;
            float endX = 0;
            float endY = 0;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mFloatView.setAlpha(mAlphaValue);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        // 默认为点击事件
                        endX = startX;
                        endY = startY;
                        isClickEvent = false;
                        mHandler.sendEmptyMessageDelayed(MSG_DRAG_MODE, 1500);// 计时开启，制定时间后才会触发移动事件
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isClickEvent = true;
                        if (isCanMove()) {// 移动模式
                            // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                            wmParams.x = (int) event.getRawX()
                                    - mFloatView.getMeasuredWidth() / 2;
                            // 减25为状态栏的高度
                            wmParams.y = (int) event.getRawY()
                                    - mFloatView.getMeasuredHeight() / 2 - 75;
                            // 刷新
                            mWindowManager.updateViewLayout(mFloatView, wmParams);
                            return true;
                        } else {// 按键模式
                            mHandler.removeMessages(MSG_DRAG_MODE);
                            // 重置抬起手机坐标
                            endX = event.getX();
                            endY = event.getY();
                        }
                    case MotionEvent.ACTION_UP:
                        if (isCanMove()) {// 移动模式
                            oldPosition[0] = wmParams.x;
                            oldPosition[1] = wmParams.y;
                            resetPosition();
                            autoAlpha();
                        } else {// 按键模式
                            if ((startX == endX || Math.abs(endX - startX) < MIN_MOVE_DISTANCE)
                                    && (startY == endY || Math.abs(endY - startY) < MIN_MOVE_DISTANCE)) {
                                isClickEvent = false;
                            } else {
                                if (Math.abs(endX - startX) > Math.abs(endY - startY)) {// x方向移动距离大
                                    if (startX > endX && Math.abs(endX - startX) >= MIN_MOVE_DISTANCE) {// 向左
                                        mHandler.sendEmptyMessage(GlobalActionExt.GLOBAL_ACTION_SWIPE_LEFT);
                                    } else {// 向右
                                        mHandler.sendEmptyMessage(GlobalActionExt.GLOBAL_ACTION_SWIPE_RIGHT);
                                    }
                                } else {// y方向移动距离大
                                    if (startY > endY && Math.abs(endY - startY) >= MIN_MOVE_DISTANCE) {// 向上
                                        mHandler.sendEmptyMessage(GlobalActionExt.GLOBAL_ACTION_SWIPE_UP);
                                    } else {// 向下
                                        mHandler.sendEmptyMessage(GlobalActionExt.GLOBAL_ACTION_SWIPE_DOWN);
                                    }
                                }
                            }
                        }

                        // 重置按键标识状态
                        mHandler.removeMessages(MSG_DRAG_MODE);
                        isDraggingMode = false;
                        return isClickEvent;// 返回false则属于移动事件，返回true则属于点击事件
                    default:
                        break;
                }
                return false;
            }
        });

        mFloatView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mHandler.hasMessages(GlobalActionExt.GLOBAL_ACTION_SINGLE_CLICK)) {// 双击
                    mHandler.removeMessages(GlobalActionExt.GLOBAL_ACTION_SINGLE_CLICK);
                    mHandler.sendEmptyMessage(GlobalActionExt.GLOBAL_ACTION_DOUBLE_CLICK);
                } else {// 单击
                    mHandler.sendEmptyMessageDelayed(GlobalActionExt.GLOBAL_ACTION_SINGLE_CLICK, DOUBLE_CLICK_DELAY);
                }
            }
        });

    }

    private boolean isCanMove() {
        return isDraggingMode && !SettingsProvider.get().isLockPosition();
    }

    public void setWindowType(boolean ifNeedUpdate) {
        if (ifNeedUpdate) {
            mWindowManager.removeView(mFloatView);
        }
        if (SettingsProvider.get().isHiddenInIme()) {
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        if (ifNeedUpdate) {
            mWindowManager.addView(mFloatView, wmParams);
        }
    }

    /**
     * 在未操作达3秒之后自动设置为0.3的透明度
     *
     * @author 高晓峰
     * @date 9/29/2017
     * @ChangeLog: <li> 高晓峰 on 9/29/2017 </li>
     */
    private void autoAlpha() {
        if (SettingsProvider.get().isAutoAlpha()) {
            if (mHandler.hasMessages(MSG_AUTOALPHA)) {
                mHandler.removeMessages(MSG_AUTOALPHA);
            }
            mHandler.sendEmptyMessageDelayed(MSG_AUTOALPHA, 3000);
        } else {
            if (mHandler.hasMessages(MSG_AUTOALPHA)) {
                mHandler.removeMessages(MSG_AUTOALPHA);
            }
            restoreAlpha();
        }
    }

    /**
     * 记录下默认宽高，只初始化一次
     *
     * @author 高晓峰
     * @date 9/29/2017
     * @ChangeLog: <li> 高晓峰 on 9/29/2017 </li>
     */
    public void initSize() {
        if (defaultWidth == -1) {
            defaultWidth = mFloatView.getWidth();
        }
    }

    /**
     * 根据用户的设置，重新计算按钮的大小
     *
     * @param rate 缩放倍数
     * @author 高晓峰
     * @date 9/29/2017
     * @ChangeLog: <li> 高晓峰 on 9/29/2017 </li>
     */
    public void reSize(final float rate) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                int result = (int) (defaultWidth * rate);
                if (result <= 0) {
                    result = 0;
                    Snackbar.make(SettingsProvider.get().getView(), context.getResources().getString(R.string.toast_touch_key_scale_tips),
                            Snackbar.LENGTH_SHORT).show();
                }
                wmParams.width = result;
                wmParams.height = result;
                mWindowManager.updateViewLayout(mFloatView, wmParams);
            }
        });
    }

    /**
     * 根据用户的设置，重新计算按钮的位置
     *
     * @author 高晓峰
     * @date 9/29/2017
     * @ChangeLog: <li> 高晓峰 on 9/29/2017 </li>
     */
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
        mFloatView.getWindowVisibleDisplayFrame(mRect);
    }

    /**
     * 恢复默认的透明度
     *
     * @author 高晓峰
     * @date 9/29/2017
     * @ChangeLog: <li> 高晓峰 on 9/29/2017 </li>
     */
    public void restoreAlpha() {
        mFloatView.setAlpha(mAlphaValue);
    }

    /**
     * 根据用户的设置，重新设置按钮的透明度
     *
     * @author 高晓峰
     * @date 9/29/2017
     * @ChangeLog: <li> 高晓峰 on 9/29/2017 </li>
     */
    public void resetAlphaBySetting(float alpha) {
        Logger.d(TAG, "current alpha " + mAlphaValue + ", target alpha " + alpha);
        mAlphaValue = alpha;
        mFloatView.setAlpha(alpha);
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
            wmParams.y -= (mIMEHeight - (mScreenHeight - wmParams.y) + mDensity * 48 * 2 /*Nav and candidate*/);
            mWindowManager.updateViewLayout(mFloatView, wmParams);
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
        Logger.d(TAG, "update ui code: " + code);
        switch (code) {
            case SettingsProvider.CODE_WINDOW_TYPE:
                setWindowType(true);
                break;
            case SettingsProvider.CODE_AUTOEDGE:
                if (SettingsProvider.get().isAutoEdge()) {
                    resetPosition();
                } else {
                    restorePoint();
                }
                break;
            case SettingsProvider.CODE_AUTOALPHA:
                autoAlpha();
                break;
            case SettingsProvider.CODE_BREATH:
                breathAnimation();
                break;
            case SettingsProvider.CODE_ROTATION:
                rotationByValue();
                break;
            default:
                break;
        }
    }

    private void rotationByValue() {
        if (mWindowManager != null && mFloatView != null && SettingsProvider.get().isRotate()) {
            if (rotateAnimator == null) {
                rotateAnimator = ObjectAnimator.ofFloat(mFloatView, "rotation", 0.0f, 359.0f);
                rotateAnimator.setDuration(3500);
                rotateAnimator.setInterpolator(new LinearInterpolator());
                rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
                rotateAnimator.setRepeatMode(ValueAnimator.RESTART);
            }

            rotateAnimator.start();
        } else {
            if (rotateAnimator != null) {
                rotateAnimator.end();
            }
        }
    }

    /**
     * 恢复上一次按钮位置
     *
     * @author 高晓峰
     * @date 9/29/2017
     * @ChangeLog: <li> 高晓峰 on 9/29/2017 </li>
     */
    private void restorePoint() {
        wmParams.x = oldPosition[0];
        wmParams.y = oldPosition[1];
        mWindowManager.updateViewLayout(mFloatView, wmParams);
        mFloatView.getWindowVisibleDisplayFrame(mRect);
    }

    public void setDetachStatus(boolean isDetachView) {
        this.isDetachView = isDetachView;
        //        breathAnimation();
    }


    private synchronized void breathAnimation() {
        if (mWindowManager != null && mFloatView != null && SettingsProvider.get().isBreath()) {
            Logger.d(TAG, "startBreathAnimation");
           /* if (isDetachView) {
                if (breathAnimator != null) {
                    Logger.d(TAG, "stopBreathAnimation cancel");
                    breathAnimator.cancel();
                }
                return;
            }*/
            if (breathAnimator == null) {
                breathAnimator = ObjectAnimator.ofFloat(mFloatView, "alpha", 1.0f, 0.3f);
                breathAnimator.setDuration(1500);
                breathAnimator.setInterpolator(new DecelerateInterpolator());
                breathAnimator.setRepeatMode(ValueAnimator.REVERSE);
                breathAnimator.setRepeatCount(ValueAnimator.INFINITE);
               /* breathAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        super.onAnimationRepeat(animation);
                        if (isDetachView) {
                            breathAnimator.cancel();
                        }
                    }
                });*/
            }
            breathAnimator.start();
        } else {
            Logger.d(TAG, "stopBreathAnimation");
            if (breathAnimator != null) {
                Logger.d(TAG, "stopBreathAnimation cancel");
                breathAnimator.cancel();
            }
            if (mFloatView != null) {
                mFloatView.setAlpha(mAlphaValue);
            }
        }
    }

    private static final class SingleHolder {
        private static final FloatTouchView HOLDER = new FloatTouchView();
    }

}
