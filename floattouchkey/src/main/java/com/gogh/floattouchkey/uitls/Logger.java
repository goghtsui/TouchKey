package com.gogh.floattouchkey.uitls;

import android.util.Log;

import com.gogh.floattouchkey.BuildConfig;

/**
 * Copyright (c) 2016 All rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 12/21/2016. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 12/21/2016 do fisrt create. </li>
 */
public class Logger {

    /**
     * @author gaoxiaofeng
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG_MODEL) {
            Log.v(tag, msg);
        }
    }

    /**
     * @author gaoxiaofeng
     * @param tag
     * @param msg
     * @param throwable
     */
    public static void v(String tag, String msg, Throwable throwable) {
        if (BuildConfig.DEBUG_MODEL) {
            Log.v(tag, msg, throwable);
        }
    }

    /**
     * 打印程序调试信息
     *
     * @Title: LogUtils
     * @author:陈丽晓
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG_MODEL) {
            Log.d(tag, msg);
        }
    }

    /**
     * @author gaoxiaofeng
     * @param tag
     * @param msg
     * @param throwable
     */
    public static void d(String tag, String msg, Throwable throwable) {
        if (BuildConfig.DEBUG_MODEL) {
            Log.d(tag, msg, throwable);
        }
    }

    /**
     * 打印数据返回结果
     *
     * @Title: LogUtils
     * @author:陈丽晓
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG_MODEL) {
            Log.i(tag, msg);
        }
    }

    /**
     * @author gaoxiaofeng
     * @param tag
     * @param msg
     * @param throwable
     */
    public static void i(String tag, String msg, Throwable throwable) {
        if (BuildConfig.DEBUG_MODEL) {
            Log.i(tag, msg, throwable);
        }
    }

    /**
     * @author gaoxiaofeng
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG_MODEL) {
            Log.w(tag, msg);
        }
    }

    /**
     * @author gaoxiaofeng
     * @param tag
     * @param msg
     * @param throwable
     */
    public static void w(String tag, String msg, Throwable throwable) {
        if (BuildConfig.DEBUG_MODEL) {
            Log.w(tag, msg, throwable);
        }
    }

    /**
     * 打印错误报错信息
     *
     * @Title: LogUtils
     * @author:陈丽晓
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG_MODEL) {
            Log.e(tag, msg);
        }
    }

    /**
     * @author gaoxiaofeng
     * @param tag
     * @param msg
     * @param throwable
     */
    public static void e(String tag, String msg, Throwable throwable) {
        if (BuildConfig.DEBUG_MODEL) {
            Log.e(tag, msg, throwable);
        }
    }

}
