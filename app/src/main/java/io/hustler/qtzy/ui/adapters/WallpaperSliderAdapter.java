package io.hustler.qtzy.ui.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import io.hustler.qtzy.ui.fragments.PaperFragment;
import io.hustler.qtzy.ui.pojo.unspalsh.Unsplash_Image;

import java.util.ArrayList;

/**
 * Created by Sayi on 27-01-2018.
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
