package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.hustler.quote.ui.fragments.PaperFragment;
import com.hustler.quote.ui.pojo.unspalsh.Unsplash_Image;

import java.util.ArrayList;

/**
 * Created by Sayi on 27-01-2018.
 */

public class WallpaperSliderAdapter extends FragmentStatePagerAdapter {
    ArrayList<Unsplash_Image> images;
    Activity activity;

    public WallpaperSliderAdapter(FragmentManager fragmentManager, ArrayList<Unsplash_Image> images, Activity activity) {
        super(fragmentManager);
        this.images = images;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        Log.i("POSITION", String.valueOf(images.size()));

        return images.size();
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("POSITION--", String.valueOf(position));
        PaperFragment paperFragment;

        paperFragment = getWallpaperFragment(position, images.get(position));


        return paperFragment;

    }

    public static PaperFragment getWallpaperFragment(int position, Unsplash_Image image) {
        Log.i("RETURNED POSITIOn", String.valueOf(position));

        return PaperFragment.newInstance(image);
    }


}
