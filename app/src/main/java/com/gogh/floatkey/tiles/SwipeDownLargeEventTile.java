package com.gogh.floatkey.tiles;

import android.content.Context;
import android.widget.RelativeLayout;

import com.gogh.floatkey.R;
import com.gogh.floatkey.provider.SettingsProvider;

import org.newstand.logger.Logger;

import java.util.List;

import dev.nick.tiles.tile.DropDownTileView;
import dev.nick.tiles.tile.QuickTile;

/**
 * Created by guohao4 on 2017/8/2.
 * Email: Tornaco@163.com
 */

public class SwipeDownLargeEventTile extends QuickTile {

    private List<String> descList;

    private boolean firstHook = true;

    public SwipeDownLargeEventTile(Context context) {
        super(context);

        descList = EventRes.getDescriptions(context.getResources());

        this.titleRes = R.string.title_swipe_down;
        this.iconRes = R.drawable.ic_keyboard_arrow_down_black_24dp;

        final int event = SettingsProvider.get().getInt(SettingsProvider.Key.SWIPE_DOWN_LARGE_ACTION);
        this.summaryRes = EventRes.getDescriptionRes(event);

        this.tileView = new DropDownTileView(context) {

            @Override
            protected void onBindActionView(RelativeLayout container) {
                super.onBindActionView(container);
                setSelectedItem(EventRes.getIndex(event), true);
            }

            @Override
            protected List<String> onCreateDropDownList() {
                return descList;
            }

            @Override
            protected void onItemSelected(int position) {
                super.onItemSelected(position);

                if (firstHook) {
                    firstHook = false;
                    return;
                }

                Logger.i("onItemSelected:" + position);

                int event = EventRes.getEvent(position);
                SettingsProvider.get().putInt(SettingsProvider.Key.SWIPE_DOWN_LARGE_ACTION, event);

                getTileView().getSummaryTextView().setText(descList.get(position));
            }
        };
    }
}
