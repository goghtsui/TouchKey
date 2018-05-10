package com.gogh.floattouchkey.uitls;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 5/9/2018. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 5/9/2018 do fisrt create. </li>
 */
public class Screen {

    private int widthPixels = 0;
    private int heightPixels = 0;
    private int densityDpi = 0;

    public static Screen get() {
        return SingleHolder.HOLDER;
    }

    private int getNavigationBarHeight(Context context) {
        if (!isNavigationBarShow(context)) {
            return 0;
        }
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    private boolean isNavigationBarShow(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display displayMetrics;
        if (windowManager != null) {
            displayMetrics = windowManager.getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            displayMetrics.getSize(size);
            displayMetrics.getRealSize(realSize);
            return realSize.y != size.y;
        }
        return false;
    }

    private int getHeightPixels(Context context, Display displayMetrics){
        Point point = new Point();
        if (displayMetrics != null) {
            displayMetrics.getSize(point);
        }
        return point.y + getNavigationBarHeight(context);
    }

    public void init(Context context) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display displayMetrics = null;
        if (windowManager != null) {
            displayMetrics = windowManager.getDefaultDisplay();
        }
        if (displayMetrics != null) {
            displayMetrics.getMetrics(localDisplayMetrics);
        }
        widthPixels = localDisplayMetrics.widthPixels;
        heightPixels = getHeightPixels(context, displayMetrics);
        densityDpi = localDisplayMetrics.densityDpi;
    }

    public int getDensity() {
        return densityDpi;
    }

    public int getHeight() {
        return heightPixels;
    }

    public int getWidth() {
        return widthPixels;
    }

    private static final class SingleHolder {
        private static final Screen HOLDER = new Screen();
    }

}
