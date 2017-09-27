package com.gogh.floattouchkey.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.Keep;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/22/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/22/2017 do fisrt create. </li>
 */
@Keep
public class AdminLockReceiver extends DeviceAdminReceiver {

    public static ComponentName name(Context context) {
        return new ComponentName(context, AdminLockReceiver.class);
    }

    public static boolean isActivated(Context context) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = name(context);
        return devicePolicyManager.isAdminActive(componentName);
    }
}
