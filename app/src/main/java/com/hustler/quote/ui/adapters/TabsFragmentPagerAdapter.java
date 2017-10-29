package com.hustler.quote.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hustler.quote.R;
import com.hustler.quote.ui.activities.MainFragment;

/**
 * Created by Sayi on 29-10-2017.
 */

public class TabsFragmentPagerAdapter extends FragmentPagerAdapter {
    Context context;
    public TabsFragmentPagerAdapter(Context mContext,FragmentManager fm) {
        super(fm);
        context=mContext;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment returning_fragment = null;
        switch (position){
            case 0:{
               returning_fragment= new MainFragment();
            }break; case 1:{
               returning_fragment= new MainFragment();
            }break; case 2:{
               returning_fragment= new MainFragment();
            }break;
        }
        return returning_fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence fagemnt_Name = null;
        switch (position){
            case 0:{
                fagemnt_Name= context.getString(R.string.Quotes);
            }break; case 1:{
                fagemnt_Name= context.getString(R.string.Facts);
            }break; case 2:{
                fagemnt_Name= context.getString(R.string.Poems);
            }break;
        }
        return fagemnt_Name;
    }


}
