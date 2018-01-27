package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.View;

import com.hustler.quote.ui.fragments.PaperFragment;
import com.hustler.quote.ui.pojo.unspalsh.Unsplash_Image;

/**
 * Created by Sayi on 27-01-2018.
 */

public class WallpaperSliderAdapter extends FragmentStatePagerAdapter {
    Unsplash_Image[] images;
    Activity activity;

    public WallpaperSliderAdapter(FragmentManager fragmentManager, Unsplash_Image[] images, Activity activity) {
        super(fragmentManager);
        this.images = images;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        Log.i("POSITION", String.valueOf(images.length));

        return 10;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("POSITION--", String.valueOf(position));
        PaperFragment paperFragment;
        switch (position) {
            case 0: {
                paperFragment = getWallpaperFragment(position, images[position]);
            }
            break;

            case 1: {
                paperFragment = getWallpaperFragment(position, images[position]);
            }
            break;
            case 2: {
                paperFragment = getWallpaperFragment(position, images[position]);
            }
            break;

            case 3: {
                paperFragment = getWallpaperFragment(position, images[position]);
            }
            break;

            case 4: {
                paperFragment = getWallpaperFragment(position, images[position]);
            }
            break;

            case 5: {
                paperFragment = getWallpaperFragment(position, images[position]);
            }
            break;

            case 6: {
                paperFragment = getWallpaperFragment(position, images[position]);
            }
            break;

            case 7: {
                paperFragment = getWallpaperFragment(position, images[position]);
            }
            break;
            default: {
                paperFragment = getWallpaperFragment(position, images[position]);

            }
        }
        return paperFragment;

    }

    public static PaperFragment getWallpaperFragment(int position, Unsplash_Image image) {
        Log.i("RETURNED POSITIOn", String.valueOf(position));

        return PaperFragment.newInstance(image.getUrls().getFull());
    }





}
