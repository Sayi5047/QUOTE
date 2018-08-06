package com.hustler.quote.ui.Widgets;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hustler.quote.ui.fragments.CarouselItemFragment;
import com.hustler.quote.ui.fragments.MainFragment;
import com.hustler.quote.ui.pojo.OffLineQuotes;

import java.util.ArrayList;

/**
 * Created by Sayi on 04-08-2018.
 */

public class CarouselAdapter extends FragmentPagerAdapter {

    MainFragment segmentOfferFragment;
    private ArrayList<OffLineQuotes> banners = new ArrayList<>();


    public CarouselAdapter(FragmentManager fm, MainFragment segmentOfferFragment, ArrayList<OffLineQuotes> banners) {
        super(fm);
        this.segmentOfferFragment = segmentOfferFragment;
        this.banners = banners;
    }


    @Override
    public int getCount() {
        if (banners.size() <= 0) {
            return 0;
        } else return banners.size();
    }

    @Override
    public Fragment getItem(int position) {

        return CarouselItemFragment.newInstance(banners.get(position));
    }


}
