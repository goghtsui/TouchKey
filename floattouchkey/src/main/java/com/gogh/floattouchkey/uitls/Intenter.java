package com.gogh.floattouchkey.uitls;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.gogh.floattouchkey.R;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/29/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/29/2017 do fisrt create. </li>
 */

public class Intenter {

    /**
     * 使用系统发送分享数据
     *
     * @param context 上下文
     * @param text    要分享的文本
     */
    public static void share(@NonNull Context context, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.action_share_to));
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.action_share_to)));
    }

}
