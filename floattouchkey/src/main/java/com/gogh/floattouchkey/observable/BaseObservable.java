package com.gogh.floattouchkey.observable;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/22/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/22/2017 do fisrt create. </li>
 */

public interface BaseObservable<T> {
    /**
     * 当状态发生改变
     *
     * @param key 选项的唯一标识
     * @author 高晓峰
     * @date 9/22/2017
     * @ChangeLog: <li> 高晓峰 on 9/22/2017 </li>
     */
    void onChanged(T key);
}
