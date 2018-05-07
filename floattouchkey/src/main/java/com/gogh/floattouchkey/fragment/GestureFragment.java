package com.gogh.floattouchkey.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.View;

import com.gogh.floattouchkey.R;
import com.gogh.floattouchkey.observable.GestureObservable;

/**
 * Created by XiaoFeng on 9/25/2017.
 */

public class GestureFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings_gesture_category);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GestureObservable.get().onChanged();
    }

}
