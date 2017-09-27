package com.gogh.floattouchkey.fragment;

import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.gogh.floattouchkey.R;
import com.gogh.floattouchkey.observable.ActivityResultObservable;
import com.gogh.floattouchkey.observer.SettingsObserver;
import com.gogh.floattouchkey.provider.SettingsProvider;
import com.gogh.floattouchkey.receiver.AdminLockReceiver;
import com.gogh.floattouchkey.uitls.Logger;

/**
 * Copyright (c) 2017 All Rights reserved by gaoxiaofeng
 * <p> Description: </p>
 * <p> Created by <b>高晓峰</b> on 9/21/2017. </p>
 * <p> ChangeLog: </p>
 * <li> 高晓峰 on 9/21/2017 do fisrt create. </li>
 */

public class SettingsFragment extends BaseFragment implements SettingsObserver.OnPreferenceClickListener {

    private static final String TAG = "SettingsFragment";

    private int mStatus = STATUS_ONCREATE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings_root_category);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SettingsObserver.get().observe(this);
        mStatus = STATUS_ONCREATE;
        SettingsProvider.get().init(getActivity(), getView(), getPreferenceManager().getSharedPreferences());
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d(TAG, "onResume");
        if (mStatus == STATUS_ONPAUSE) {
            Logger.d(TAG, "onResume reset check status.");
            ActivityResultObservable.get().onChanged(SettingsProvider.CODE_CHECK_RESET);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mStatus = STATUS_ONRESULT;
        Log.d(TAG, "onActivityResult [ " + requestCode + ", " + resultCode + " ]");
        if (resultCode == SettingsProvider.CODE_ERROR) {
            switch (requestCode) {
                case SettingsProvider.CODE_DEVICEADMIN:
                    Snackbar.make(getView(), getActivity().getResources().getString(R.string.pref_root_category_lockscreen_tips),
                            Snackbar.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        } else {
            ActivityResultObservable.get().onChanged(requestCode);
        }
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
    public void onDeviceAdminClicked() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, AdminLockReceiver.name(getActivity()));
        startActivityForResult(intent, SettingsProvider.CODE_DEVICEADMIN);
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.d(TAG, "onPause");
        mStatus = STATUS_ONPAUSE;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SettingsObserver.get().cancelClickListener();
    }
}
