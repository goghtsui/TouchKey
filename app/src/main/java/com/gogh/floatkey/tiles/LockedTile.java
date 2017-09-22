package com.gogh.floatkey.tiles;

import android.content.Context;
import android.widget.RelativeLayout;

import com.gogh.floatkey.R;
import com.gogh.floatkey.provider.SettingsProvider;

import dev.nick.tiles.tile.QuickTile;
import dev.nick.tiles.tile.SwitchTileView;

/**
 * Created by guohao4 on 2017/8/2.
 * Email: Tornaco@163.com
 */

public class LockedTile extends QuickTile {

    public LockedTile(final Context context) {
        super(context);

        this.titleRes = R.string.title_lock_position;
        this.iconRes = R.drawable.ic_screen_lock_portrait_black_24dp;
        this.tileView = new SwitchTileView(context) {
            @Override
            protected void onBindActionView(RelativeLayout container) {
                super.onBindActionView(container);
                setChecked(SettingsProvider.get().getBoolean(SettingsProvider.Key.LOCKED));
            }

            @Override
            protected void onCheckChanged(boolean checked) {
                super.onCheckChanged(checked);
                SettingsProvider.get().putBoolean(SettingsProvider.Key.LOCKED, checked);
            }
        };
    }
}
