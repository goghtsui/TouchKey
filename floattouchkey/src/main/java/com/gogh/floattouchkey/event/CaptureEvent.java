package com.gogh.floattouchkey.event;

import android.content.Intent;

import com.gogh.floattouchkey.TouchApplication;
import com.gogh.floattouchkey.event.base.AbstractEvent;
import com.gogh.floattouchkey.ui.capture.CaptureActivity;
import com.gogh.floattouchkey.uitls.Logger;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 10/11/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 10/11/2017 do fisrt create. </li>
 */

public class CaptureEvent extends AbstractEvent {

    @Override
    protected void execute() {
        Logger.d("CaptureEvent", "CaptureEvent run");
        TouchApplication.get().startActivity(new Intent(TouchApplication.get(), CaptureActivity.class));
    }

}
