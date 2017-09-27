package com.gogh.floattouchkey.provider;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Vibrator;

import com.gogh.floattouchkey.R;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/26/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/26/2017 do fisrt create. </li>
 */

public class SensorProvider {

    private Vibrator mVibrator;
    private SoundPool mSoundPool;
    private int mStartSound;

    public static SensorProvider get() {
        return SingleHolder.HOLDER;
    }

    public void init(Context context) {
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


    public void vibrate(){
        mVibrator.vibrate(30);
    }

    public void sound(){
        mSoundPool.play(mStartSound, 1.0f, 1.0f, 0, 0, 1.0f);
    }


}
