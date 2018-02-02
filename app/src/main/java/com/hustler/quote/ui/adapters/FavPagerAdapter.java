package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hustler.quote.R;
import com.hustler.quote.ui.fragments.FAV_images_fragment;
import com.hustler.quote.ui.fragments.FAV_quotes_fragment;

/**
 * Created by anvaya5 on 02/02/2018.
 */

public class FavPagerAdapter extends FragmentPagerAdapter {
    Activity activity;

    public FavPagerAdapter(FragmentActivity activity, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment returning_fragment = null;
        switch (position) {
            case 0: {
                returning_fragment = new FAV_quotes_fragment();
            }
            break;
            case 1: {
                returning_fragment = new FAV_images_fragment();
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
                returning_fragment = activity.getString(R.string.Quotes);
            }
            break;
            case 1: {
                returning_fragment = activity.getString(R.string.images);

            }
            break;
        }
        return returning_fragment;
    }
}
