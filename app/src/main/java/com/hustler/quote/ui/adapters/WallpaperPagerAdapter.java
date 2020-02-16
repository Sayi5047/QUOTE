package com.hustler.quote.ui.adapters;

import android.app.Activity;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hustler.quote.ui.fragments.RandomWallpapersFragment;

import com.hustler.quote.R;
import com.hustler.quote.ui.fragments.Categoris_wallpaper_fragment;

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
