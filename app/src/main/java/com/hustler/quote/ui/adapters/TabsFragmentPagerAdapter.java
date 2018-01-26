package com.hustler.quote.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hustler.quote.R;
import com.hustler.quote.ui.activities.MainFragment;
import com.hustler.quote.ui.fragments.CategoriesFragment;
import com.hustler.quote.ui.fragments.UserFavuritesFragment;
import com.hustler.quote.ui.fragments.UserWorkFragment;
import com.hustler.quote.ui.fragments.WallpaperFragment;

/**
 * Created by Sayi on 29-10-2017.
 */

public class TabsFragmentPagerAdapter extends FragmentPagerAdapter {
    Context context;

    public TabsFragmentPagerAdapter(Context mContext, FragmentManager fm) {
        super(fm);
        context = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment returning_fragment = null;
        switch (position) {
            case 0: {
                returning_fragment = new CategoriesFragment();
            }
            break;
            case 1: {
                returning_fragment = new MainFragment();
            }
            break;
            case 2: {
                returning_fragment = new UserFavuritesFragment();
            }
            break;
            case 3: {
                returning_fragment = new UserWorkFragment();
            }
            break;
            case 4: {
                returning_fragment = new WallpaperFragment();
            }
            break;
        }
        return returning_fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence fagemnt_Name = null;
        switch (position) {
            case 0: {
                fagemnt_Name = context.getString(R.string.categories);
            }
            break;
            case 1: {
                fagemnt_Name = context.getString(R.string.Quotes);
            }
            break;
            case 2: {
                fagemnt_Name = context.getString(R.string.Fav);
            }
            break;
            case 3: {
                fagemnt_Name = context.getString(R.string.Work);
            }
            break;
            case 4: {
                fagemnt_Name = context.getString(R.string.Wallpaper);
            }
            break;
        }
        return fagemnt_Name;
    }


}
