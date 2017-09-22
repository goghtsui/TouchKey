package com.gogh.floatkey.tiles;

import android.content.Context;
import android.view.View;

import com.gogh.floatkey.R;
import com.gogh.floatkey.provider.SettingsProvider;
import com.gogh.floatkey.ui.ContainerHostActivity;
import com.gogh.floatkey.ui.ContainerHostActivityNoRecents;
import com.gogh.floatkey.ui.PayListBrowserFragment;

import dev.nick.tiles.tile.QuickTile;
import dev.nick.tiles.tile.QuickTileView;

/**
 * Created by Tornaco on 2017/7/28.
 * Licensed with Apache.
 */

public class PayListTile extends QuickTile {

    public PayListTile(final Context context) {
        super(context);

        this.iconRes = R.drawable.ic_shopping_cart_white_24dp;
        this.titleRes = R.string.title_pay_list;

        this.tileView = new QuickTileView(context, this) {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                boolean noRecent = SettingsProvider.get().getBoolean(SettingsProvider.Key.NO_RECENTS);
                if (noRecent) {
                    context.startActivity(ContainerHostActivityNoRecents.getIntent(context, PayListBrowserFragment.class));
                } else {
                    context.startActivity(ContainerHostActivity.getIntent(context, PayListBrowserFragment.class));
                }
            }
        };


    }
}
