package com.gogh.floattouchkey.uitls;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 5/9/2018. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 5/9/2018 do fisrt create. </li>
 */
public class Screen {

    public static int getDensity(Context context){
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display displayMetrics = null;
        if (windowManager != null) {
            displayMetrics = windowManager.getDefaultDisplay();
        }
        if (displayMetrics != null) {
            displayMetrics.getMetrics(localDisplayMetrics);
        }
        return localDisplayMetrics.densityDpi;
    }

    public static int getWidth(Context context) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display displayMetrics = null;
        if (windowManager != null) {
            displayMetrics = windowManager.getDefaultDisplay();
        }
        if (displayMetrics != null) {
            displayMetrics.getMetrics(localDisplayMetrics);
        }
        return localDisplayMetrics.widthPixels;
    }


    public static int getHeight(Context context) {
        Point point = new Point();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display displayMetrics = null;
        if (windowManager != null) {
            displayMetrics = windowManager.getDefaultDisplay();
        }
        if (displayMetrics != null) {
            displayMetrics.getSize(point);
        }
        return point.y + getNavigationBarHeight(context);
    }

    public static int getNavigationBarHeight(Context context) {
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

    private static boolean isNavigationBarShow(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display displayMetrics = null;
            if (windowManager != null) {
                displayMetrics = windowManager.getDefaultDisplay();
                Point size = new Point();
                Point realSize = new Point();
                displayMetrics.getSize(size);
                displayMetrics.getRealSize(realSize);
                return realSize.y != size.y;
            }
        } else {
            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !menu && !back;
        }
        return false;
    }

}
