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

public class SoundTile extends QuickTile {

    public SoundTile(Context context) {
        super(context);

        this.titleRes = R.string.title_sound;
        this.iconRes = R.drawable.ic_volume_up_black_24dp;
        this.tileView = new SwitchTileView(context) {
            @Override
            protected void onBindActionView(RelativeLayout container) {
                super.onBindActionView(container);
                setChecked(SettingsProvider.get().getBoolean(SettingsProvider.Key.SOUND));
            }

            @Override
            protected void onCheckChanged(boolean checked) {
                super.onCheckChanged(checked);
                SettingsProvider.get().putBoolean(SettingsProvider.Key.SOUND, checked);
            }
        };
    }
}
