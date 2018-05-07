package com.gogh.floattouchkey.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 10/10/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 10/10/2017 do fisrt create. </li>
 */

public class ThreadExecutor {

    private static ExecutorService SERVICE = Executors.newCachedThreadPool();

    public static void execute(Runnable runnable) {
        SERVICE.execute(runnable);
    }

    public static ExecutorService getservice() {
        return SERVICE;
    }
}
