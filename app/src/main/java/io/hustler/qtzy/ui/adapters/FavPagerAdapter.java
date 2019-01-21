package io.hustler.qtzy.ui.adapters;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.fragments.FAV_images_fragment;
import io.hustler.qtzy.ui.fragments.FAV_quotes_fragment;

/**
 * Created by anvaya5 on 02/02/2018.
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
