package com.gogh.floattouchkey.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gogh.floattouchkey.R;
import com.gogh.floattouchkey.observable.ActivityResultObservable;
import com.gogh.floattouchkey.observer.SettingsObserver;
import com.gogh.floattouchkey.provider.SettingsProvider;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/21/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/21/2017 do fisrt create. </li>
 */

public class SettingsFragment extends BaseFragment implements SettingsObserver.OnPreferenceClickListener {

    private static final String TAG = "SettingsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings_root_category);
       /* ((OverlayPreference) findPreference(getResources().getString(R.string.pref_root_category_switcher_overlay_key))).setOnPreferenceChangedEvent(new OverlayPreference.OnPreferenceChangedEvent() {
            @Override
            public void onChanged() {
                //启动Activity让用户授权
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                startActivityForResult(intent, 100);
            }
        });*/
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(getActivity().getApplicationContext())) {
                ((SwitchPreference) findPreference(getResources().getString(R.string.pref_root_category_switcher_overlay_key))).setChecked(true);
            } else {
                ((SwitchPreference) findPreference(getResources().getString(R.string.pref_root_category_switcher_overlay_key))).setChecked(false);
            }
        }*/
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SettingsObserver.get().observe(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
       /* if (key.equals(getResources().getString(R.string.pref_root_category_switcher_overlay_key))) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //启动Activity让用户授权
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                startActivityForResult(intent, 100);
            }
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, requestCode + ", " + resultCode);
        ActivityResultObservable.get().onChanged(requestCode);
    }

    @Override
    public void onOverlayClicked() {
        // 启动Activity让用户授权
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
        startActivityForResult(intent, SettingsProvider.CODE_OVERLAY);
    }

    @Override
    public void onAccessibilityClicked() {
        Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
        startActivityForResult(intent, SettingsProvider.CODE_ACCESSIBILITY);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SettingsObserver.get().cancelClickListener();
    }
}
