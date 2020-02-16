package com.hustler.quote.ui.adapters;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hustler.quote.ui.fragments.QuotesFragment;
import com.hustler.quote.ui.fragments.UserWorkFragment;
import com.hustler.quote.ui.fragments.WallpaperFragment;

import com.hustler.quote.R;

import com.hustler.quote.ui.fragments.FAV_quotes_fragment;

public class MainPagerAdapter extends FragmentPagerAdapter {
    private Context context;

    public MainPagerAdapter(Context mContext, FragmentManager fm) {
        super(fm);
        context = mContext;
    }

    @Nullable
    @Override
    public Fragment getItem(int position) {
        Fragment returning_fragment = null;
        switch (position) {
            case 0: {
                returning_fragment = new QuotesFragment();

            }
            break;
            case 1: {
                returning_fragment = new WallpaperFragment();
            }
            break;
            case 2: {
                returning_fragment = new FAV_quotes_fragment();
            }
            break;
//            case 3: {
//                returning_fragment = new FavouritesHolderFragment();
//            }
//            break;
            case 3: {
                returning_fragment = new UserWorkFragment();
            }
            break;

        }
        return returning_fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence fagemnt_Name = null;
        switch (position) {
            case 0: {
                fagemnt_Name = context.getString(R.string.Quotes);
            }
            break;
            case 1: {
                fagemnt_Name = context.getString(R.string.wallpapers);
            }
            break;
            case 2: {
                fagemnt_Name = context.getString(R.string.collections);
            }
            break;
//            case 3: {
//                fagemnt_Name = context.getString(R.string.Fav);
//            }
//            break;
            case 3: {
                fagemnt_Name = context.getString(R.string.Work);
            }
            break;

        }
        return fagemnt_Name;
    }


}
