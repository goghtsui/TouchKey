package com.gogh.floatkey.tiles;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.widget.Toast;

import com.gogh.floatkey.R;
import com.gogh.floatkey.provider.SettingsProvider;

import dev.nick.tiles.tile.EditTextTileView;
import dev.nick.tiles.tile.QuickTile;

/**
 * Created by guohao4 on 2017/8/2.
 * Email: Tornaco@163.com
 */

public class TapDelayTile extends QuickTile {
    public TapDelayTile(final Context context) {
        super(context);
        this.titleRes = R.string.title_tap_delay;
        this.iconRes = R.drawable.ic_timer_black_24dp;
        this.summary = String.valueOf(SettingsProvider.get().getInt(SettingsProvider.Key.TAP_DELAY));

        this.tileView = new EditTextTileView(context) {
            @Override
            protected int getInputType() {
                return InputType.TYPE_CLASS_NUMBER;
            }

            @Override
            protected CharSequence getHint() {
                return String.valueOf(SettingsProvider.get().getInt(SettingsProvider.Key.TAP_DELAY));
            }

            @Override
            protected CharSequence getDialogTitle() {
                return context.getString(R.string.title_tap_delay);
            }

            protected CharSequence getPositiveButton() {
                return context.getString(android.R.string.ok);
            }

            protected CharSequence getNegativeButton() {
                return context.getString(android.R.string.cancel);
            }

            @Override
            protected void onPositiveButtonClick() {
                super.onPositiveButtonClick();
                String text = getEditText().getText().toString().trim();
                try {
                    int rate = Integer.parseInt(text);

                    if (rate < 0 || rate > 9999) {
                        Toast.makeText(context, ">=0 && <=9999 ~.~", Toast.LENGTH_LONG).show();
                        return;
                    }

                    SettingsProvider.get().putInt(SettingsProvider.Key.TAP_DELAY, rate);
                } catch (Throwable e) {
                    Toast.makeText(context, Log.getStackTraceString(e), Toast.LENGTH_LONG).show();
                }

                getSummaryTextView().setText(String.valueOf(SettingsProvider.get().getInt(SettingsProvider.Key.TAP_DELAY)));
            }
        };
    }
}
