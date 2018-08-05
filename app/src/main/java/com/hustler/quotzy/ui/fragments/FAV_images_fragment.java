package com.hustler.quotzy.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hustler.quotzy.R;
import com.hustler.quotzy.ui.activities.WallpapersPagerActivity;
import com.hustler.quotzy.ui.adapters.FavWallpaperAdapter;
import com.hustler.quotzy.ui.apiRequestLauncher.Constants;
import com.hustler.quotzy.ui.database.ImagesDbHelper;
import com.hustler.quotzy.ui.pojo.unspalsh.Unsplash_Image;
import com.hustler.quotzy.ui.utils.Toast_Snack_Dialog_Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sayi Manoj Sugavasi on 02/02/2018.
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
public class FAV_images_fragment extends android.support.v4.app.Fragment {
    ImageView iv_no_fav;
    RecyclerView rv_imag_no_fv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fav_images_fragment_layout, container, false);

        iv_no_fav = view.findViewById(R.id.iv_no_fav);
        rv_imag_no_fv = view.findViewById(R.id.rv_imag_no_fv);
        rv_imag_no_fv.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        setAdapter();
        return view;
    }

    private void setAdapter() {
        final List<Unsplash_Image>[] imageLists = new List[]{new LinkedList()};
        new Thread(new Runnable() {
            @Override
            public void run() {
                imageLists[0] = new ImagesDbHelper(getActivity()).getAllFav();
            }
        }).run();

        rv_imag_no_fv.setAdapter(new FavWallpaperAdapter(getActivity(), imageLists[0], new FavWallpaperAdapter.OnWallpaperClickListener() {
            @Override
            public void onWallpaperClicked(int position, Unsplash_Image wallpaper) {
                ArrayList<Unsplash_Image> images1=new ArrayList<Unsplash_Image>();
                for (int i = 0; i < imageLists[0].size(); i++) {
                    images1.add(imageLists[0].get(i));
                }
                Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), wallpaper.getId());
                Intent intent = new Intent(getActivity(), WallpapersPagerActivity.class);

                intent.putExtra(Constants.Pager_position, position);
                intent.putExtra(Constants.is_from_fav, true);
                intent.putExtra(Constants.PAGER_LIST_WALL_OBKHECTS, images1);
                startActivity(intent);
            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdapter();

    }
}
