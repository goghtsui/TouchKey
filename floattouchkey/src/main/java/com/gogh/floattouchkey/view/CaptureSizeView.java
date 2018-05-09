package com.gogh.floattouchkey.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gogh.floattouchkey.R;
import com.gogh.floattouchkey.entity.GraphicPath;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 5/9/2018. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 5/9/2018 do fisrt create. </li>
 */
public class CaptureSizeView extends View {

    /**
     * 手指轨迹画笔
     */
    private Paint markPaint;

    /**
     * 半透背景画笔
     */
    private Paint unMarkPaint;

    /**
     * 是否抬起手指
     */
    private boolean isUp;

    /**
     * 记录手指路径的实体类
     */
    private GraphicPath mGraphicPath;

    /**
     * 是否是移动状态
     */
    private boolean isMoveMode;

    /**
     * 手指按下时的X坐标
     */
    private int downX;

    /**
     * 手指按下时的Y坐标
     */
    private int downY;

    /**
     * 手指开始移动时的x坐标
     */
    private int startX;

    /**
     * 手指开始移动时的Y坐标
     */
    private int startY;

    /**
     * 手指移动结束时的x坐标
     */
    private int endX;

    /**
     * 手指移动结束时的Y坐标
     */
    private int endY;
    private OnCaptureListener onCaptureListener;

    public CaptureSizeView(Context context) {
        super(context);
        init();
        initPaint();
    }

    public CaptureSizeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        initPaint();
    }

    public CaptureSizeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initPaint();
    }

    private void init() {
        mGraphicPath = new GraphicPath();
    }

    private void initPaint() {
        unMarkPaint = new Paint();
        unMarkPaint.setColor(getResources().getColor(R.color.colorHalfBlack));
        unMarkPaint.setAntiAlias(true);
        //
        markPaint = new Paint();
        markPaint.setColor(Color.WHITE);
        markPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        // PorterDuff.Mode.CLEAR 的作用就在于将选取的区域背景色去除掉
        markPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        markPaint.setStrokeWidth(3.0f);
        markPaint.setAntiAlias(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isUp = false;
                downX = x;
                downY = y;
                isMoveMode = false;
                startX = (int) event.getX();
                startY = (int) event.getY();
                endX = startX;
                endY = startY;
                mGraphicPath.clear();
                mGraphicPath.addPath(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                mGraphicPath.addPath(x, y);
                break;
            case MotionEvent.ACTION_UP:
                isUp = true;
                mGraphicPath.addPath(x, y);
                break;
            case MotionEvent.ACTION_CANCEL:
                isUp = true;
                break;
            default:
                break;
        }
        postInvalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        // 把半透的背景填充
        canvas.drawRect(0, 0, width, height, unMarkPaint);
        // 抬起手指，选取完成，连接头尾（封闭空间将透明化）
        if (isUp) {
            Path path = new Path();
            if (mGraphicPath.size() > 1) {
                path.moveTo(mGraphicPath.pathX.get(0), mGraphicPath.pathY.get(0));
                for (int i = 1; i < mGraphicPath.size(); i++) {
                    path.lineTo(mGraphicPath.pathX.get(i), mGraphicPath.pathY.get(i));
                }
            } else {
                return;
            }
            canvas.drawPath(path, markPaint);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(onCaptureListener != null){
                        onCaptureListener.onComplete();
                    }
                }
            }, 1000);
        } else {
            // 手指还在移动中
            if (mGraphicPath.size() > 1) {
                for (int i = 1; i < mGraphicPath.size(); i++) {
                    canvas.drawLine(mGraphicPath.pathX.get(i - 1), mGraphicPath.pathY.get(i - 1), mGraphicPath.pathX.get(i), mGraphicPath.pathY.get(i), markPaint);
                }
            }
        }
    }

    public GraphicPath getGraphicPath() {
        return mGraphicPath;
    }

    public interface OnCaptureListener {
        void onComplete();
    }

    public void setOnCaptureListener(OnCaptureListener onCaptureListener){
        this.onCaptureListener = onCaptureListener;
    }
}
