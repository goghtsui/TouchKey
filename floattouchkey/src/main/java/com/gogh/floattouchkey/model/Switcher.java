package com.gogh.floattouchkey.model;

import android.view.View;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/26/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/26/2017 do fisrt create. </li>
 */

public interface Switcher {

    View getView();

    boolean isLockPosition();

    boolean isAutoEdge();

    boolean hasSound();

    boolean hasVibrate();

    boolean isRepositionInIme();

    boolean isHiddenInIme();
}
