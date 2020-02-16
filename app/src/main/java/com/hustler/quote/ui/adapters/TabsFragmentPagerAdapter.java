package com.hustler.quote.ui.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.util.SparseArray;

import com.hustler.quote.ui.fragments.QuotesFragment;
import com.hustler.quote.ui.fragments.UserFavuritesFragment;
import com.hustler.quote.ui.fragments.UserWorkFragment;
import com.hustler.quote.ui.fragments.WallpaperFragment;

import com.hustler.quote.R;

import com.hustler.quote.ui.fragments.CategoriesFragment;

/**
 * Created by Sayi on 29-10-2017.
 */
/*   Copyright [2018] [Sayi Manoj Sugavasi]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.*/
public class TabsFragmentPagerAdapter extends FragmentPagerAdapter {
    Context context;
    @NonNull
    SparseArray<Fragment> registeredFragments =new SparseArray<>();

    public TabsFragmentPagerAdapter(Context mContext, FragmentManager fm) {
        super(fm);
        context = mContext;
    }

    @Nullable
    @Override
    public Fragment getItem(int position) {
        Fragment returning_fragment = null;
        switch (position) {
            case 0: {
                returning_fragment = new CategoriesFragment();

            }
            break;
            case 1: {
                returning_fragment = new QuotesFragment();
            }
            break;
            case 2: {
                returning_fragment = new WallpaperFragment();
            }
            break;
            case 3: {
                returning_fragment = new UserFavuritesFragment();
            }
            break;
            case 4: {
                returning_fragment = new UserWorkFragment();
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
                fagemnt_Name = context.getString(R.string.Wallpaper);
            }
            break;
            case 3: {
                fagemnt_Name = context.getString(R.string.Fav);
            }
            break;
            case 4: {
                fagemnt_Name = context.getString(R.string.Work);
            }
            break;

        }
        return fagemnt_Name;
    }


}
