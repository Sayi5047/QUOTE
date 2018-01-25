package com.hustler.quote.ui.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentPagerAdapter;

import com.hustler.quote.R;
import com.hustler.quote.ui.activities.MainFragment;
import com.hustler.quote.ui.fragments.Advanced_config_fragment;
import com.hustler.quote.ui.fragments.CategoriesFragment;
import com.hustler.quote.ui.fragments.Look_and_feel_config_fragment;
import com.hustler.quote.ui.fragments.UserFavuritesFragment;
import com.hustler.quote.ui.fragments.UserWorkFragment;
import com.hustler.quote.ui.fragments.widget_layout_config_fragment;

/**
 * Created by anvaya5 on 24/01/2018.
 */

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
