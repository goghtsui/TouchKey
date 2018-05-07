package com.gogh.floattouchkey.event;

import com.gogh.floattouchkey.service.AppSwitcherCompat;
import com.gogh.floattouchkey.service.TouchAccessibilityService;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 10/11/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 10/11/2017 do fisrt create. </li>
 */

public class SwitchAppEvent implements Event {
    @Override
    public void run() {
        AppSwitcherCompat.get().switchApp(TouchAccessibilityService.getService());
    }
}
