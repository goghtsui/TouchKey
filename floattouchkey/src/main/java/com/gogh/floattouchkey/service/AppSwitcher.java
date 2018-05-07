package com.gogh.floattouchkey.service;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 10/10/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 10/10/2017 do fisrt create. </li>
 */

public interface AppSwitcher {
    boolean switchApp(TouchAccessibilityService service);
    boolean recentTask(TouchAccessibilityService service);
    void killCurrent();
}
