package com.gogh.floattouchkey.observable;

import java.util.Observable;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 10/11/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 10/11/2017 do fisrt create. </li>
 */

public class GestureObservable extends Observable {

    public static GestureObservable get() {
        return SingleHolder.HOLDER;
    }

    public void onChanged() {
        this.setChanged();
        this.notifyObservers();
    }

    private static final class SingleHolder {
        private static final GestureObservable HOLDER = new GestureObservable();
    }
}
