package com.hustler.quotzy.ui.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentPagerAdapter;

import com.hustler.quotzy.R;
import com.hustler.quotzy.ui.fragments.widget_layout_config_fragment;

/**
 * Created by anvaya5 on 24/01/2018.
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
public class QuoteWidgetConfigAdapter extends FragmentPagerAdapter {
    Context context;

    public QuoteWidgetConfigAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment returning_fragment = null;
        switch (position) {
            case 0: {
                returning_fragment = new widget_layout_config_fragment();
            }
            break;
//            case 1: {
//                returning_fragment = new Look_and_feel_config_fragment();
//            }
//            break;
//            case 2: {
//                returning_fragment = new Advanced_config_fragment();
//            }
//            break;
        }
        return returning_fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence fagemnt_Name = null;
        switch (position) {
            case 0: {
                fagemnt_Name = context.getString(R.string.Layout);
            } break;


        }
        return fagemnt_Name;
    }
}
