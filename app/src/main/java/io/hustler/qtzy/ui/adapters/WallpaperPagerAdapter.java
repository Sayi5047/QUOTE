package io.hustler.qtzy.ui.adapters;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.fragments.Categoris_wallpaper_fragment;
import io.hustler.qtzy.ui.fragments.RandomWallpapersFragment;

/**
 * Created by Sayi on 18-03-2018.
 */

public class WallpaperPagerAdapter extends FragmentPagerAdapter {
    Activity activity;

    public WallpaperPagerAdapter(FragmentActivity activity, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Nullable
    @Override
    public Fragment getItem(int position) {
        Fragment returning_fragment = null;
        switch (position) {
            case 0: {
                returning_fragment = new RandomWallpapersFragment();
            }
            break;
            case 1: {
                returning_fragment = new Categoris_wallpaper_fragment();
            }
            break;
        }
        return returning_fragment;

    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String returning_fragment = null;
        switch (position) {
            case 0: {
                returning_fragment = activity.getString(R.string.wallpapers);
            }
            break;
            case 1: {
                returning_fragment = activity.getString(R.string.categories);

            }
            break;
        }
        return returning_fragment;
    }
}
