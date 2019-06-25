package io.hustler.qtzy.ui.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.activities.QuotesFragment;
import io.hustler.qtzy.ui.fragments.Categoris_wallpaper_fragment;
import io.hustler.qtzy.ui.fragments.UserWorkFragment;
import io.hustler.qtzy.ui.fragments.WallpaperFragment;

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
                returning_fragment = new Categoris_wallpaper_fragment();
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
