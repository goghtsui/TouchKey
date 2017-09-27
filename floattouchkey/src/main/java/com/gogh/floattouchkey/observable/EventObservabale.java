package com.gogh.floattouchkey.observable;

import java.util.Observable;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/26/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/26/2017 do fisrt create. </li>
 */

public class EventObservabale extends Observable {

    public static EventObservabale get() {
        return SingleHolder.HOLDER;
    }

    public void handleEvent(int event) {
        setChanged();
        this.notifyObservers(event);
    }

    private static final class SingleHolder {
        private static final EventObservabale HOLDER = new EventObservabale();
    }

}
