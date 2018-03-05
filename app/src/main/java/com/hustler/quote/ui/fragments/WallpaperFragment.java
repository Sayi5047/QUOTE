package com.hustler.quote.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hustler.quote.R;
import com.hustler.quote.ui.activities.WallpapersPagerActivity;
import com.hustler.quote.ui.adapters.CollectionsWallpaperAdapter;
import com.hustler.quote.ui.adapters.ImageCategoryAdapter;
import com.hustler.quote.ui.adapters.WallpaperAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.apiRequestLauncher.Restutility;
import com.hustler.quote.ui.listeners.InfiniteScrolListener;
import com.hustler.quote.ui.pojo.UnsplashImages_Collection_Response;
import com.hustler.quote.ui.pojo.Unsplash_Image_collection_response_listener;
import com.hustler.quote.ui.pojo.UserWorkImages;
import com.hustler.quote.ui.pojo.unspalsh.ImagesFromUnsplashResponse;
import com.hustler.quote.ui.pojo.unspalsh.Unsplash_Image;
import com.hustler.quote.ui.utils.InternetUtils;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sayi on 26-01-2018.
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
public class WallpaperFragment extends android.support.v4.app.Fragment {


    private static final int MY_PERMISSION_REQUEST_ = 1001;
    RecyclerView rv, catgories;
    TextView credit;
    ProgressBar loader;
    UserWorkImages userWorkImages;
    private RelativeLayout dataView;
    InfiniteScrolListener infiniteScrolListener;
    WallpaperAdapter wallAdapter;
    CollectionsWallpaperAdapter wallAdapter2;
    boolean IS_CATEGORY_FLAG = false;
    String collection_category = "girls";
    Unsplash_Image[] unsplash_images_loaded;
    SharedPreferences sharedPreferences;
    Unsplash_Image[] unsplash_images;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_wallpaper_fragment, container, false);
        rv = view.findViewById(R.id.main_rv);
        catgories = view.findViewById(R.id.catgories);
        loader = view.findViewById(R.id.loader);
        dataView = view.findViewById(R.id.data_views);
        credit = view.findViewById(R.id.crdit);
        loader.setVisibility(View.GONE);
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
//        });
        catgories.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
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
        rv.setLayoutManager(gridLayoutManager);
        credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://unsplash.com?utm_source=Quotzy&utm_medium=referral"));
                startActivity(intent);
            }
        });
        return view;
    }

    private void checkPermission_and_proceed() {
        if (InternetUtils.isConnectedtoNet(getActivity()) == true) {
            dataView.setVisibility(View.VISIBLE);
            setRecyclerview();
            setCategoriesRecyclerView();
        } else {
            dataView.setVisibility(View.GONE);
        }
    }

    private void setCategoriesRecyclerView() {
        getCategories();
    }

    private void getCategories() {

        catgories.setAdapter(new ImageCategoryAdapter(getActivity(), new ImageCategoryAdapter.OnImageClickLitner() {
            @Override
            public void onCategoryClicked(int position, String category) {
                collection_category = category;
//                clearEverything();

                load_searched_images(category, 1);


            }
        }));
    }

    public void load_searched_images(String category, final int pagePosition) {
        loader.setVisibility(View.VISIBLE);
        final String request = Constants.API_GET_Collections_FROM_UNSPLASH + Constants.QUERY + category + Constants.PER_PAGE + Constants.PAGE_NUMBER + pagePosition;
        new Restutility(getActivity()).getUnsplash_Collections_Images(getActivity(), new Unsplash_Image_collection_response_listener() {
            @Override
            public void onSuccess(final UnsplashImages_Collection_Response response) {
                IS_CATEGORY_FLAG = true;

//                if (pagePosition > 1) {
//                    if (response.getResults().length <= 0) {
//                        // TODO: 04-02-2018 handle for nodata in pagination
//                    } else {
//                        if (wallAdapter2 != null) {
//                            wallAdapter2.addItems(response.getResults());
//                            wallAdapter2.notifyDataSetChanged();
//                        }
//                    }
//                } else if (pagePosition == 1) {
                loader.setVisibility(View.GONE);
                rv.setAdapter(null);
                rv.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
//                    wallAdapter2.removeItems();

                Log.i("VALUE FROM UNSPLASH", String.valueOf(response.getResults().length));
                if (response.getResults().length <= 0) {
                    dataView.setVisibility(View.GONE);
                    Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), getString(R.string.Currently_no_wallpaper));
                } else {
                    dataView.setVisibility(View.VISIBLE);
                    rv.setAdapter(wallAdapter2 = new CollectionsWallpaperAdapter(getActivity(), response.getResults(), new CollectionsWallpaperAdapter.OnWallpaperClickListener() {
                        @Override
                        public void onWallpaperClicked(int position, ArrayList<Unsplash_Image> m_AL_Images) {
//                            Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), wallpaper.getUser().getFirst_name());
                            Intent intent = new Intent(getActivity(), WallpapersPagerActivity.class);

                            intent.putExtra(Constants.Pager_position, position);
                            intent.putExtra(Constants.PAGER_LIST_WALL_OBKHECTS, m_AL_Images);
                            intent.putExtra(Constants.is_from_fav, false);

                            startActivity(intent);
                        }
                    }));
                }
            }
//            }

            @Override
            public void onError(String error) {
                Log.e("ERROR FROM UNSPLASH", error);
                loader.setVisibility(View.GONE);

                dataView.setVisibility(View.GONE);
                Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), getString(R.string.Failed));
            }
        }, request);

    }


    public void setRecyclerview() {
//        clearEverything();

        if (sharedPreferences.getInt(Constants.Shared_prefs_images_loaded_times, 0) > 5) {
            getRandomIMages(new Random().nextInt(30));
        } else {
            if (sharedPreferences.getBoolean(Constants.Shared_prefs_Images_loaded_for_first_time, false) == true) {
                new LoadImagestoSharedPrefsTask().execute();

            } else {
                getRandomIMages(1);
            }
        }
//


    }

    private void getRandomIMages(final int pagePosition) {
        loader.setVisibility(View.VISIBLE);


        String request = Constants.API_GET_IMAGES_FROM_UNSPLASH + "&page=" + pagePosition;
        new Restutility(getActivity()).getUnsplashRandomImages(getActivity(), new ImagesFromUnsplashResponse() {
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
                unsplash_images_loaded = unsplash_images;

                new LoadImagestoSharedPrefsTask2().execute();

            }

//            }

            @Override
            public void onError(String error) {
                Log.e("ERROR FROM UNSPLASH", error);
                loader.setVisibility(View.GONE);

                dataView.setVisibility(View.GONE);
                Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), getString(R.string.Failed));
            }
        }, request);
    }

    private void setDataToRecyclerVIew(Unsplash_Image[] unsplash_images) {
        rv.setAdapter(null);

        if (unsplash_images.length <= 0) {
            dataView.setVisibility(View.GONE);
            Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), getString(R.string.Currently_no_wallpaper));
        } else {
            dataView.setVisibility(View.VISIBLE);
            rv.setAdapter(new WallpaperAdapter(getActivity(), unsplash_images, new WallpaperAdapter.OnWallpaperClickListener() {
                @Override
                public void onWallpaperClicked(int position, ArrayList<Unsplash_Image> m_AL_Images) {
                    //                            Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), wallpaper.getUser().getFirst_name());
                    Intent intent = new Intent(getActivity(), WallpapersPagerActivity.class);

                    intent.putExtra(Constants.Pager_position, position);
                    intent.putExtra(Constants.PAGER_LIST_WALL_OBKHECTS, m_AL_Images);
                    intent.putExtra(Constants.is_from_fav, false);

                    startActivity(intent);
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

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param strings The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */

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
public class LoadImagestoSharedPrefsTask2 extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param strings The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */

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

//    public void clearEverything() {
//        rv.setAdapter(null);
//// 2. Notify the adapter of the update
//
//        try {
//            wallAdapter2.notifyDataSetChanged();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            wallAdapter.notifyDataSetChanged();
//
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//
//
//        // or notifyItemRangeRemoved
//// 3. Reset endless scroll listener when performing a new search
////        infiniteScrolListener.resetState();
//        infiniteScrolListener.resetState();
//    }
}
