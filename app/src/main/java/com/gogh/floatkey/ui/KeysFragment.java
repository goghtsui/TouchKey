package com.gogh.floatkey.ui;

import com.gogh.floatkey.R;
import com.gogh.floatkey.tiles.DoubleTapEventTile;
import com.gogh.floatkey.tiles.LargeSlopTile;
import com.gogh.floatkey.tiles.LongPressDelayTile;
import com.gogh.floatkey.tiles.PanicDetectionTile;
import com.gogh.floatkey.tiles.ScreenToLEventTile;
import com.gogh.floatkey.tiles.ScreenToPEventTile;
import com.gogh.floatkey.tiles.SingleTapEventTile;
import com.gogh.floatkey.tiles.SwipeDownEventTile;
import com.gogh.floatkey.tiles.SwipeDownLargeEventTile;
import com.gogh.floatkey.tiles.SwipeLeftEventTile;
import com.gogh.floatkey.tiles.SwipeLeftLargeEventTile;
import com.gogh.floatkey.tiles.SwipeRightEventTile;
import com.gogh.floatkey.tiles.SwipeRightLargeEventTile;
import com.gogh.floatkey.tiles.SwipeUpEventTile;
import com.gogh.floatkey.tiles.SwipeUpLargeEventTile;
import com.gogh.floatkey.tiles.TapDelayTile;

import java.util.List;

import dev.nick.tiles.tile.Category;
import dev.nick.tiles.tile.DashboardFragment;

public class KeysFragment extends DashboardFragment {
    @Override
    protected void onCreateDashCategories(List<Category> categories) {
        super.onCreateDashCategories(categories);

        Category key = new Category();
        key.titleRes = R.string.category_key;
        key.addTile(new SingleTapEventTile(getContext()));
        key.addTile(new DoubleTapEventTile(getContext()));
        key.addTile(new TapDelayTile(getContext()));
        key.addTile(new LongPressDelayTile(getContext()));
        key.addTile(new PanicDetectionTile(getContext()));

        Category ges = new Category();
        ges.titleRes = R.string.category_gesture;
        ges.addTile(new SwipeLeftEventTile(getContext()));
        ges.addTile(new SwipeRightEventTile(getContext()));
        ges.addTile(new SwipeUpEventTile(getContext()));
        ges.addTile(new SwipeDownEventTile(getContext()));

        Category gesLarge = new Category();
        gesLarge.titleRes = R.string.category_gesture_large;
        gesLarge.addTile(new LargeSlopTile(getContext()));
        Category space = new Category();
        space.addTile(new SwipeLeftLargeEventTile(getContext()));
        space.addTile(new SwipeRightLargeEventTile(getContext()));
        space.addTile(new SwipeUpLargeEventTile(getContext()));
        space.addTile(new SwipeDownLargeEventTile(getContext()));

        Category screen = new Category();
        screen.titleRes = R.string.category_screen;
        screen.addTile(new ScreenToLEventTile(getContext()));
        screen.addTile(new ScreenToPEventTile(getContext()));

        Category others = new Category();
        others.titleRes = R.string.category_others;

        categories.add(key);
        categories.add(screen);

        categories.add(ges);
        categories.add(gesLarge);
        categories.add(space);
    }
}
