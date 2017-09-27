package com.gogh.floattouchkey.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.gogh.floattouchkey.fragment.fragment;

/**
 * Created by XiaoFeng on 9/25/2017.
 */

public class GestureActivity extends BaseAppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, new fragment())
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }
}
