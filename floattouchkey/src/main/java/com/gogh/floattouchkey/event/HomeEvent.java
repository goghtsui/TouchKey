package com.gogh.floattouchkey.event;

import android.accessibilityservice.AccessibilityService;

import com.gogh.floattouchkey.event.base.Event;
import com.gogh.floattouchkey.service.TouchAccessibilityService;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 10/11/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 10/11/2017 do fisrt create. </li>
 */

public class HomeEvent implements Event {
    @Override
    public void run() {
        TouchAccessibilityService.getService().performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
    }
}
