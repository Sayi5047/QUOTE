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
