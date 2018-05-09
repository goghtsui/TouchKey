package com.gogh.floattouchkey.event.base;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: 按键响应类，过滤短连续按键，间隔300毫秒</p>
 * <p> Created by <b>高晓峰</b> on 5/9/2018. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 5/9/2018 do fisrt create. </li>
 */
public abstract class AbstractEvent implements Event {

    private long clickTimeMillions = 0;
    private static final int DELAY = 300;

    @Override
    public void run() {
        long currentTimeMillis = System.currentTimeMillis();
        if((currentTimeMillis - clickTimeMillions) > DELAY) {
            clickTimeMillions = currentTimeMillis;
            execute();
        }
    }

    protected abstract void execute();

}
