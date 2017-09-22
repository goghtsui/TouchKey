package com.gogh.floattouchkey.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/22/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/22/2017 do fisrt create. </li>
 */

public class ActivityResultObserver implements Observer {

    private OnActivityResult onActivityResult;

    public static ActivityResultObserver get() {
        return SingleHolder.HOLDER;
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        if (onActivityResult != null) {
            int code = Integer.valueOf(String.valueOf(arg));
            onActivityResult.onActivityResult(code);
        }
    }

    public void subscribe(OnActivityResult onActivityResult) {
        this.onActivityResult = onActivityResult;
    }

    public interface OnActivityResult {
        void onActivityResult(int code);
    }

    private static final class SingleHolder {
        private static final ActivityResultObserver HOLDER = new ActivityResultObserver();
    }

}
