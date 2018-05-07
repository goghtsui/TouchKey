package com.gogh.floattouchkey.provider;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;

import com.chrisplus.rootmanager.RootManager;
import com.gogh.floattouchkey.R;

import static android.content.Context.DEVICE_POLICY_SERVICE;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/26/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/26/2017 do fisrt create. </li>
 */

public class SensorProvider {

    private Context context;

    private Vibrator mVibrator;
    private SoundPool mSoundPool;
    private int mStartSound;

    private DevicePolicyManager mDevicePolicyManager;

    public static SensorProvider get() {
        return SingleHolder.HOLDER;
    }

    public void init(Context context) {
        this.context = context;
        mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(DEVICE_POLICY_SERVICE);
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mSoundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .build())
                .build();
        mStartSound = mSoundPool.load(context, R.raw.sound, 1);
    }

    private static final class SingleHolder {
        private static final SensorProvider HOLDER = new SensorProvider();
    }

    public DevicePolicyManager getDevicePolicyManager() {
        return mDevicePolicyManager;
    }

    public void vibrate() {
        mVibrator.vibrate(30);
    }

    public void sound() {
        mSoundPool.play(mStartSound, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    public void lockScreen() {
        if (SettingsProvider.get().isRoot()) {
            ScreenLockerRoot.lock();
        } else if (SettingsProvider.get().isActivateAdmin()) {
            mDevicePolicyManager.lockNow();
        } else {
            Snackbar.make(SettingsProvider.get().getView(), context.getResources().getString(R.string.toast_touch_key_activate_tips),
                    Snackbar.LENGTH_LONG).show();
        }
    }

    public static class ScreenLockerRoot {
        static void lock() {
            RootManager.getInstance().runCommand("input keyevent 26");
        }
    }

}
