package com.gogh.floattouchkey.preference.seekbar;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gogh.floattouchkey.R;
import com.gogh.floattouchkey.uitls.Logger;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/28/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/28/2017 do fisrt create. </li>
 */

public class BaseSeekBarPreference extends Preference implements SeekBar.OnSeekBarChangeListener {

    protected static int MAX_VALUE = 100;

    private int mProgress = MAX_VALUE;

    private TextView mSummaryText;

    private TextView mProgressText;

    private ImageView mIconImage;

    private boolean mTrackingTouch;

    private OnSeekBarPreferenceChangedListener onSeekBarPrefsChangeListener = null;

    public BaseSeekBarPreference(Context context) {
        super(context);
        init();
    }

    public BaseSeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseSeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setMax(MAX_VALUE);
        setLayoutResource(R.layout.pref_seekbar_layout);
    }

    /**
     * 初始化view，并设置进度条默认值为最小值
     *
     * @param view
     * @author 高晓峰
     * @date 9/28/2017
     * @ChangeLog: <li> 高晓峰 on 9/28/2017 </li>
     */
    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.pref_seekbar_seekbar);
        seekBar.setMax(MAX_VALUE);
        seekBar.setProgress(mProgress);
        seekBar.setEnabled(isEnabled());
        seekBar.setOnSeekBarChangeListener(this);
        // icon
        mIconImage = (ImageView) view.findViewById(R.id.pref_seekbar_icon);
        // summary
        mSummaryText = (TextView) view.findViewById(R.id.pref_seelbar_summary);
        // value
        mProgressText = (TextView) view.findViewById(R.id.pref_seekbar_value);
        onBindView(seekBar);
        mProgressText.setText(String.valueOf(seekBar.getProgress()));
    }

    protected void onBindView(SeekBar seekBar) {

    }

    @Override
    public void setSummary(CharSequence summary) {
        super.setSummary(summary);
        mSummaryText.setText(summary);
    }

    @Override
    public void setIcon(@DrawableRes int iconResId) {
        super.setIcon(iconResId);
        mIconImage.setImageResource(iconResId);
    }

 /*   protected void setIconImage(int iconResId) {
        mIconImage.setImageResource(iconResId);
    }*/

    /**
     * 设置默认的值
     *
     * @param defaultValue
     */
    public void setDefaultProgressValue(int defaultValue) {
        if (getPersistedInt(-1) == -1) {
            setProgress(defaultValue);
        }
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setProgress(restoreValue ? getPersistedInt(mProgress) : (Integer) defaultValue);
    }

    public void setMax(int max) {
        if (max != MAX_VALUE) {
            MAX_VALUE = max;
            notifyChanged();
        }
    }

    private void setProgress(int progress, boolean notifyChanged) {
        if (progress > MAX_VALUE) {
            progress = MAX_VALUE;
        }
        if (progress < 0) {
            progress = 0;
        }
        if (progress != mProgress) {
            mProgress = progress;
            persistInt(progress);
            if (notifyChanged) {
                notifyChanged();
            }
        }
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        setProgress(progress, true);
    }

    public void setOnSeekBarPrefsChangeListener(OnSeekBarPreferenceChangedListener onSeekBarPrefsChangeListener) {
        this.onSeekBarPrefsChangeListener = onSeekBarPrefsChangeListener;
    }

    /**
     * Persist the seekBar's progress value if callChangeListener
     * returns true, otherwise set the seekBar's progress to the stored value
     */
    void syncProgress(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        if (progress != mProgress) {
            if (callChangeListener(progress)) {
                setProgress(progress, false);
            } else {
                seekBar.setProgress(mProgress);
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mProgressText.setText(String.valueOf(seekBar.getProgress()));
        if (onSeekBarPrefsChangeListener != null) {
            onSeekBarPrefsChangeListener.onProgressChanged(getKey(), progress);
        }
        if (seekBar.getProgress() != mProgress) {
            syncProgress(seekBar);
        }
        if (fromUser && !mTrackingTouch) {
            syncProgress(seekBar);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (onSeekBarPrefsChangeListener != null) {
            onSeekBarPrefsChangeListener.onStartTracking(getKey());
        }
        mTrackingTouch = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (onSeekBarPrefsChangeListener != null) {
            onSeekBarPrefsChangeListener.onStopTracking(getKey(), seekBar);
        }
        mTrackingTouch = false;
        Logger.d("onStopTrackingTouch : ", seekBar.getProgress() + " ," + mProgress);
        if (seekBar.getProgress() != mProgress) {
            syncProgress(seekBar);
        }
        notifyHierarchyChanged();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        /*
         * Suppose a client uses this preference type without persisting. We
         * must save the instance state so it is able to, for example, survive
         * orientation changes.
         */

        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }

        // Save the instance state
        final SavedState myState = new SavedState(superState);
        myState.progress = mProgress;
        myState.max = MAX_VALUE;
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!state.getClass().equals(SavedState.class)) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }

        // Restore the instance state
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        mProgress = myState.progress;

        MAX_VALUE = myState.max;
        notifyChanged();
    }

    /**
     * 进度条监听器
     *
     * @author 高晓峰
     * @date 9/28/2017
     * @ChangeLog: <li> 高晓峰 on 9/28/2017 </li>
     */
    public interface OnSeekBarPreferenceChangedListener {

        void onStopTracking(String key, SeekBar seekBar);

        void onStartTracking(String key);

        void onProgressChanged(String key, int progress);

    }

    /**
     * SavedState, a subclass of {@link BaseSavedState}, will store the state
     * of MyPreference, a subclass of Preference.
     * <p>
     * It is important to always call through to super methods.
     */
    private static class SavedState extends BaseSavedState {
        int progress;
        int max;

        public SavedState(Parcel source) {
            super(source);
            // Restore the click counter
            progress = source.readInt();
            max = source.readInt();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(progress);
            dest.writeInt(max);
        }
    }

}