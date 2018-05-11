package com.gogh.floattouchkey.common;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: 截屏时用户的选择的截屏类型</p>
 * <p> Created by <b>高晓峰</b> on 5/11/2018. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 5/11/2018 do fisrt create. </li>
 */
public class Event {
    public static final int CAPTURE_NONE = 100;

    /**
     * 自由截屏
     */
    public static final int CAPTURE_FREE = 101;

    /**
     * 全屏截屏
     */
    public static final int CAPTURE_FULL = 102;

    /**
     * 矩形截屏
     */
    public static final int CAPTURE_RECTANGLE = 103;
}
