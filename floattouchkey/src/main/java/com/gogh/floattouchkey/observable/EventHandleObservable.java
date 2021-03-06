package com.gogh.floattouchkey.observable;

import java.util.Observable;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 10/11/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 10/11/2017 do fisrt create. </li>
 */

public class EventHandleObservable extends Observable implements BaseObservable<Integer> {

    public static EventHandleObservable get() {
        return SingleHolder.HOLDER;
    }

    /**
     * 当状态发生改变
     *
     * @param key 选项的唯一标识
     * @author 高晓峰
     * @date 9/22/2017
     * @ChangeLog: <li> 高晓峰 on 9/22/2017 </li>
     */
    @Override
    public void onChanged(Integer key) {
        this.setChanged();
        this.notifyObservers(key);
    }

    private static final class SingleHolder {
        private static final EventHandleObservable HOLDER = new EventHandleObservable();
    }


}
