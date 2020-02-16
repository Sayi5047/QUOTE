package com.hustler.quote.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hustler.quote.R;
import com.hustler.quote.ui.Executors.AppExecutor;
import com.hustler.quote.ui.ORM.AppDatabase;
import com.hustler.quote.ui.activities.WallpapersPagerActivity;
import com.hustler.quote.ui.adapters.WallpaperAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.apiRequestLauncher.Restutility;
import com.hustler.quote.ui.listeners.InfiniteScrolListener;
import com.hustler.quote.ui.pojo.UserWorkImages;
import com.hustler.quote.ui.pojo.unspalsh.FeaturedImagesRespoonseListener;
import com.hustler.quote.ui.pojo.unspalsh.Unsplash_Image;
import com.hustler.quote.ui.utils.InternetUtils;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * Created by Sayi on 18-03-2018.
 */

public class RandomWallpapersFragment extends Fragment {
    private static final int MY_PERMISSION_REQUEST_ = 1001;
    RecyclerView rv;
    RelativeLayout loader;
    UserWorkImages userWorkImages;
    WallpaperAdapter wallAdapter;
    private RelativeLayout dataView;
    InfiniteScrolListener infiniteScrolListener;
    Unsplash_Image[] unsplash_images_loaded;
    SharedPreferences sharedPreferences;
    Unsplash_Image[] unsplash_images;
    AppDatabase appDatabase;
    AppExecutor appExecutor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.curatd_wallpaper_fragment, container, false);
        rv = view.findViewById(R.id.main_rv);
        loader = view.findViewById(R.id.loader);
//        dataView = view.findViewById(R.id.data_views);
        loader.setVisibility(View.GONE);
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
        appDatabase = AppDatabase.getmAppDatabaseInstance(getContext());
        appExecutor = AppExecutor.getInstance();
        //        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
//        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);

//        infiniteScrolListener = new InfiniteScrolListener(gridLayoutManager) {
//            @Override
//            public void onLoadMore(int i, int totalItemCount) {
//                if (IS_CATEGORY_FLAG == true) {
//                    Log.d("LISTENR", String.valueOf(i + "----" + totalItemCount));
//                    load_searched_images(collection_category, i);
//
//                } else {
//
//                    getRandomIMages(i);
//                }
//            }
//        };
//        rv.addOnScrollListener(infiniteScrolListener);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        checkPermission_and_proceed();

        return view;
    }

    private void checkPermission_and_proceed() {
        if (InternetUtils.isConnectedtoNet(Objects.requireNonNull(getActivity()))) {
//            dataView.setVisibility(View.VISIBLE);
            setRecyclerview();
//            setCategoriesRecyclerView();
        } else {
//            dataView.setVisibility(View.GONE);
        }
    }

    public void setRecyclerview() {
//        clearEverything();

        if (sharedPreferences.getInt(Constants.Shared_prefs_images_loaded_times, 0) > 5) {
//            appExecutor.getNetworkExecutor().execute(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
            getRandomIMages(new Random().nextInt(30));
        } else {
            if (sharedPreferences.getBoolean(Constants.Shared_prefs_Images_loaded_for_first_time, false)) {
                new LoadImagestoSharedPrefsTask().execute();

            } else {
                getRandomIMages(1);
            }
        }
//


    }

    private void getRandomIMages(final int pagePosition) {
        loader.setVisibility(View.VISIBLE);


        String request = Constants.UNSPLASH_GET_CURATED_IMAGES + "&page=" + pagePosition;
        new Restutility(getActivity()).getUnsplashFeaturedImages(getActivity(), new FeaturedImagesRespoonseListener() {
            @Override
            public void onSuccess(final Unsplash_Image[] unsplash_images) {
//                IS_CATEGORY_FLAG = false;
//                loader.setVisibility(View.GONE);
//                if (pagePosition > 1) {
//                    if (unsplash_images.length <= 0) {
//                        // TODO: 04-02-2018 handle for nodata in pagination
//                    } else {
//                        if (wallAdapter != null) {
//                            wallAdapter.addItems(unsplash_images);
//                            wallAdapter.notifyDataSetChanged();
//                        }
//                    }
//                } else {
                loader.setVisibility(View.GONE);
                unsplash_images_loaded = unsplash_images;

                new LoadImagestoSharedPrefsTask2().execute();

            }

//            }

            @Override
            public void onError(String error) {
                Log.e("ERROR FROM UNSPLASH", error);
                loader.setVisibility(View.GONE);
                if (isAdded() && getActivity() != null) {
                    Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), getString(R.string.Failed));
                }
            }
        }, request);
    }


    private void setDataToRecyclerVIew(Unsplash_Image[] unsplash_images) {
        rv.setAdapter(null);

        if (unsplash_images.length <= 0) {
//            dataView.setVisibility(View.GONE);
            Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), getString(R.string.Currently_no_wallpaper));
        } else {
//            dataView.setVisibility(View.VISIBLE);
            rv.setAdapter(new WallpaperAdapter(getActivity(), unsplash_images, new WallpaperAdapter.OnWallpaperClickListener() {
                @Override
                public void onWallpaperClicked(int position, ArrayList<Unsplash_Image> m_AL_Images, ImageView itemView) {
                    //                            Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), wallpaper.getUser().getFirst_name());
                    Intent intent = new Intent(getActivity(), WallpapersPagerActivity.class);

                    intent.putExtra(Constants.Pager_position, position);
                    intent.putExtra(Constants.PAGER_LIST_WALL_OBKHECTS, m_AL_Images);
                    intent.putExtra(Constants.is_from_fav, false);
                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), new Pair<View, String>(itemView, getString(R.string.wallpaper_transision_name))).toBundle();

                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slideup, R.anim.slidedown);
                }
            }));
        }

    }

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
    public class LoadImagestoSharedPrefsTask extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Nullable
        @Override
        protected Void doInBackground(String... strings) {
            Type type = new TypeToken<Unsplash_Image[]>() {
            }.getType();
            unsplash_images = new Gson().fromJson(sharedPreferences.getString(Constants.Shared_prefs_loaded_images, null), type);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setDataToRecyclerVIew(unsplash_images);
            int loaded_times = sharedPreferences.getInt(Constants.Shared_prefs_images_loaded_times, 0);
            sharedPreferences.edit().putInt(Constants.Shared_prefs_images_loaded_times, loaded_times + 1).apply();
        }
    }

    public class LoadImagestoSharedPrefsTask2 extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Nullable
        @Override
        protected Void doInBackground(String... strings) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constants.Shared_prefs_Images_loaded_for_first_time, true);
            Gson gson = new Gson();
            String images = gson.toJson(unsplash_images_loaded);
            editor.putString(Constants.Shared_prefs_loaded_images, images);
            editor.putInt(Constants.Shared_prefs_images_loaded_times, 1).apply();
            editor.apply();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setDataToRecyclerVIew(unsplash_images_loaded);
        }
    }

}
