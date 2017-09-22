package com.gogh.floatkey.ui;

import com.gogh.floatkey.R;
import com.gogh.floatkey.provider.SettingsProvider;
import com.gogh.floatkey.tiles.AdTile;
import com.gogh.floatkey.tiles.AlphaTile;
import com.gogh.floatkey.tiles.BlurTile;
import com.gogh.floatkey.tiles.CropToCircleTile;
import com.gogh.floatkey.tiles.EdgeTile;
import com.gogh.floatkey.tiles.HeartbeatTile;
import com.gogh.floatkey.tiles.IMETile;
import com.gogh.floatkey.tiles.ImageTile;
import com.gogh.floatkey.tiles.KeysTile;
import com.gogh.floatkey.tiles.LockScreenPermTile;
import com.gogh.floatkey.tiles.LockedTile;
import com.gogh.floatkey.tiles.NSwitchAppTile;
import com.gogh.floatkey.tiles.NoRecentsTile;
import com.gogh.floatkey.tiles.RestoreImeHiddenTile;
import com.gogh.floatkey.tiles.RotateTile;
import com.gogh.floatkey.tiles.SizeTile2;
import com.gogh.floatkey.tiles.SoundTile;
import com.gogh.floatkey.tiles.TapFeedbackTile;
import com.gogh.floatkey.tiles.ToggleSwitchTile;
import com.gogh.floatkey.tiles.VibrateTile;

import java.util.List;

import dev.nick.tiles.tile.Category;
import dev.nick.tiles.tile.DashboardFragment;

public class Dashboard extends DashboardFragment {
    private AdTile mAdTile;

    @Override
    protected void onCreateDashCategories(List<Category> categories) {
        super.onCreateDashCategories(categories);
        Category def = new Category();
        def.titleRes = R.string.category_status;
        def.addTile(new ToggleSwitchTile(getActivity()));
        def.addTile(new LockScreenPermTile(getActivity()));

        Category settings = new Category();
        settings.titleRes = R.string.category_settings;
        settings.addTile(new EdgeTile(getContext()));
        settings.addTile(new SoundTile(getContext()));
        settings.addTile(new VibrateTile(getContext()));
        settings.addTile(new IMETile(getContext()));
        settings.addTile(new RestoreImeHiddenTile(getContext()));
        settings.addTile(new LockedTile(getContext()));

        Category ad = new Category();
        ad.titleRes = R.string.title_ad_area;
        mAdTile = new AdTile(getContext());
        ad.addTile(mAdTile);

        Category view = new Category();
        view.titleRes = R.string.category_view;
        view.addTile(new SizeTile2(getContext()));
        view.addTile(new TapFeedbackTile(getContext()));
        view.addTile(new HeartbeatTile(getContext()));
        view.addTile(new RotateTile(getContext()));
        view.addTile(new AlphaTile(getContext()));

        Category image = new Category();
        image.titleRes = R.string.category_image;

        image.addTile(new ImageTile(getActivity()));
        image.addTile(new CropToCircleTile(getActivity()));
        image.addTile(new BlurTile(getActivity()));

        Category key = new Category();
        key.titleRes = R.string.category_key;
        key.addTile(new KeysTile(getContext()));

        Category dev = new Category();
        dev.titleRes = R.string.summary_exp;
        dev.addTile(new NoRecentsTile(getActivity()));
        dev.addTile(new NSwitchAppTile(getActivity()));

        categories.add(def);
        if (SettingsProvider.get().getBoolean(SettingsProvider.Key.FORCE_SHOW_AD) ||
                !SettingsProvider.get().shouldSkipAd()) {
            categories.add(ad);
        }
        categories.add(settings);
        categories.add(key);
        categories.add(view);
        categories.add(image);
        categories.add(dev);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mAdTile.recycle();
        } catch (Throwable ignored) {

        }
    }
}
