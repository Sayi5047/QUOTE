package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hustler.quote.R;
import com.hustler.quote.ui.fragments.AppFontsFragment;
import com.hustler.quote.ui.fragments.CustomFontsFragment;
import com.hustler.quote.ui.fragments.SymbolFontsFragment;

/**
 * Created by anvaya5 on 15/12/2017.
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
public class Font_pager_adapter extends FragmentPagerAdapter {
    Activity activity;

    public Font_pager_adapter(FragmentManager fm, Activity activity) {
        super(fm);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        DialogFragment return_fragment = null;
        switch (position) {
            case 1: {
                return_fragment = new AppFontsFragment();
            }break;case 2: {
                return_fragment = new CustomFontsFragment();
            }break;case 3: {
                return_fragment = new CustomFontsFragment();
            }break;
        }
        return return_fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String return_fragment_name = null;
        switch (position) {
            case 1: {
                return_fragment_name = activity.getString(R.string.local_fonts);
            }
            break;
            case 2: {
                return_fragment_name = activity.getString(R.string.symbol_fonts);
            }
            break;
            case 3: {
                return_fragment_name = activity.getString(R.string.downloaded_fonts);
            }
            break;
        }
        return return_fragment_name;
    }
}
