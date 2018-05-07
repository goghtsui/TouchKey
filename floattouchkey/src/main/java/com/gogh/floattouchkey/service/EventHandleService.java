package com.gogh.floattouchkey.service;

import android.content.Context;

import com.gogh.floattouchkey.common.GlobalActionExt;
import com.gogh.floattouchkey.event.AppShortCut;
import com.gogh.floattouchkey.event.AppShortCutPanel;
import com.gogh.floattouchkey.event.BackEvent;
import com.gogh.floattouchkey.event.CaptureEvent;
import com.gogh.floattouchkey.event.EmptyEvent;
import com.gogh.floattouchkey.event.Event;
import com.gogh.floattouchkey.event.HomeEvent;
import com.gogh.floattouchkey.event.KillAppEvent;
import com.gogh.floattouchkey.event.LockEvent;
import com.gogh.floattouchkey.event.NotificationEvent;
import com.gogh.floattouchkey.event.RecentTaskEvent;
import com.gogh.floattouchkey.event.ShortCutPanel;
import com.gogh.floattouchkey.event.SwitchAppEvent;
import com.gogh.floattouchkey.observable.EventHandleObservable;
import com.gogh.floattouchkey.provider.EventProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 10/11/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 10/11/2017 do fisrt create. </li>
 */

public class EventHandleService implements Observer {

    public int mSingleTap = 1;
    public int mDoubleTap = 2;
    public int mSwipToLeft = 11;
    public int mSwipToUp = 5;
    public int mSwipToRight = 6;
    public int mSwipToDown = 4;

    private Context context;

    private List<Event> events = new ArrayList<>();

    public static EventHandleService get() {
        return SingleHolder.HOLDER;
    }

    public void initEvent(Context context) {
        this.context = context;
        EventHandleObservable.get().addObserver(this);
        events.add(new EmptyEvent());
        events.add(new BackEvent());
        events.add(new HomeEvent());
        events.add(new LockEvent());
        events.add(new NotificationEvent());
        events.add(new RecentTaskEvent());
        events.add(new SwitchAppEvent());
        events.add(new AppShortCut());
        events.add(new CaptureEvent());
        events.add(new ShortCutPanel());
        events.add(new AppShortCutPanel());
        events.add(new KillAppEvent());
        initUserEvent();
    }

    private void initUserEvent() {
        mSingleTap = EventProvider.get().getSingleTapOrder();
        mDoubleTap = EventProvider.get().getDoubleTapOrder();
        mSwipToLeft = EventProvider.get().getSwipLeftOrder();
        mSwipToUp = EventProvider.get().getSwipeUpOrder();
        mSwipToRight = EventProvider.get().getSwipeRightOrder();
        mSwipToDown = EventProvider.get().getSwipeDownOrder();
    }

    public void run(int code) {
        switch (code) {
            case GlobalActionExt.GLOBAL_ACTION_SINGLE_CLICK:
                events.get(mSingleTap).run();
                break;
            case GlobalActionExt.GLOBAL_ACTION_DOUBLE_CLICK:
                events.get(mDoubleTap).run();
                break;
            case GlobalActionExt.GLOBAL_ACTION_SWIPE_LEFT:
                events.get(mSwipToLeft).run();
                break;
            case GlobalActionExt.GLOBAL_ACTION_SWIPE_UP:
                events.get(mSwipToUp).run();
                break;
            case GlobalActionExt.GLOBAL_ACTION_SWIPE_RIGHT:
                events.get(mSwipToRight).run();
                break;
            case GlobalActionExt.GLOBAL_ACTION_SWIPE_DOWN:
                events.get(mSwipToDown).run();
                break;
            default:
                break;
        }
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
        int code = Integer.valueOf(String.valueOf(arg));
        switch (code) {
            case GlobalActionExt.GLOBAL_ACTION_SINGLE_CLICK:
                mSingleTap = EventProvider.get().getSingleTapOrder();
                break;
            case GlobalActionExt.GLOBAL_ACTION_DOUBLE_CLICK:
                mDoubleTap = EventProvider.get().getDoubleTapOrder();
                break;
            case GlobalActionExt.GLOBAL_ACTION_SWIPE_LEFT:
                mSwipToLeft = EventProvider.get().getSwipLeftOrder();
                break;
            case GlobalActionExt.GLOBAL_ACTION_SWIPE_UP:
                mSwipToUp = EventProvider.get().getSwipeUpOrder();
                break;
            case GlobalActionExt.GLOBAL_ACTION_SWIPE_RIGHT:
                mSwipToRight = EventProvider.get().getSwipeRightOrder();
                break;
            case GlobalActionExt.GLOBAL_ACTION_SWIPE_DOWN:
                mSwipToDown = EventProvider.get().getSwipeDownOrder();
                break;
            default:
                break;
        }
    }

    @Override
    public String toString() {
        return "EventHandleService{" +
                "mSingleTap=" + mSingleTap +
                ", mDoubleTap=" + mDoubleTap +
                ", mSwipToLeft=" + mSwipToLeft +
                ", mSwipToUp=" + mSwipToUp +
                ", mSwipToRight=" + mSwipToRight +
                ", mSwipToDown=" + mSwipToDown +
                ", events=" + events +
                '}';
    }

    private static final class SingleHolder {
        private static final EventHandleService HOLDER = new EventHandleService();
    }

}
