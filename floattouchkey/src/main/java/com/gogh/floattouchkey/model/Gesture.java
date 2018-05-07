package com.gogh.floattouchkey.model;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 10/11/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 10/11/2017 do fisrt create. </li>
 */

public interface Gesture {

    int getSingleTapOrder();

    int getDoubleTapOrder();

    int getSwipLeftOrder();

    int getSwipeUpOrder();

    int getSwipeRightOrder();

    int getSwipeDownOrder();
}
