package com.hustler.quote.ui.fragments;

import android.app.Dialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdView;
import com.hustler.quote.ui.Executors.AppExecutor;
import com.hustler.quote.ui.activities.WallpapersPagerActivity;
import com.hustler.quote.ui.adapters.CollectionsWallpaperAdapter;
import com.hustler.quote.ui.adapters.ImageCategoryAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.apiRequestLauncher.Restutility;
import com.hustler.quote.ui.utils.AdUtils;
import com.hustler.quote.ui.utils.TextUtils;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import com.hustler.quote.R;

import com.hustler.quote.ui.ViewModels.CollectionViewModel;
import com.hustler.quote.ui.pojo.unspalsh.FeaturedImagesRespoonseListener;
import com.hustler.quote.ui.pojo.unspalsh.ResGetCollectionsDto;
import com.hustler.quote.ui.pojo.unspalsh.Unsplash_Image;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

/**
 * Created by Sayi on 18-03-2018.
 */

public class Categoris_wallpaper_fragment extends Fragment {
    RecyclerView catgories;
    CollectionsWallpaperAdapter wallAdapter2;
    long collection_category = 0;
    boolean IS_CATEGORY_FLAG = false;
    AppExecutor appExecutor;
    CollectionViewModel collectionViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wallcategories_layout, container, false);
        catgories = view.findViewById(R.id.catgories);
        catgories.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, false));
        appExecutor = AppExecutor.getInstance();
        collectionViewModel = new CollectionViewModel(getActivity().getApplication());
        getCategories();
        return view;
    }

    private void getCategories() {

        final LiveData<ArrayList<ResGetCollectionsDto>> resGetCollectionsDto = collectionViewModel.getResGetCollections();
        resGetCollectionsDto.observe(Objects.requireNonNull(getActivity()), resGetCollectionsDtos -> {


            if (null != resGetCollectionsDto) {
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> catgories.setAdapter(new ImageCategoryAdapter(getActivity(), resGetCollectionsDtos, (position, id) -> {
                    collection_category = id;
                    buildDialog_and_search(collection_category, appExecutor);
                })));
            } else {

                Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), "Categories are null");
            }
        });


    }


    private void buildDialog_and_search(long collection_category, AppExecutor appExecutor) {

        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()), R.style.EditTextDialog_non_floater_2);
        dialog.setContentView(R.layout.search_chooser_layout2);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.EditTextDialog_non_floater_2;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        }


        final RelativeLayout root;
        ImageView close;
        final RecyclerView result_rv;
        final ProgressBar loader;
        // AdView // AdView;


        close = dialog.findViewById(R.id.search_btn);
        root = dialog.findViewById(R.id.root);
        result_rv = dialog.findViewById(R.id.result_rv);
        loader = dialog.findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);

        // AdView = dialog.findViewById(R.id.adView);
        dialog.findViewById(R.id.search_term);
        // AdUtils.loadBannerAd(adView, getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            result_rv.setClipToOutline(true);
        }

        load_searched_images(collection_category, new Random().nextInt(5), result_rv, loader, dialog, appExecutor);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result_rv.setAdapter(null);
                TextUtils.findText_and_applyamim_slidedown(root, getActivity());

                dialog.dismiss();

            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(@NonNull DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
                    TextUtils.findText_and_applyamim_slidedown(root, getActivity());

                    dialog.dismiss();
                    return true;
                } else {
                    return false;
                }
            }
        });
        dialog.show();


    }

    public void load_searched_images(final long collectionId, final int pagePosition,
                                     @NonNull final RecyclerView rv, @NonNull final ProgressBar loader, @NonNull final Dialog dialog, final AppExecutor appExecutor) {

        appExecutor.getNetworkExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final String request = Constants.UNSPLASH_GET_COLLECTIONS_PHOTOS.replace(":id", String.valueOf(collectionId));
                new Restutility(getActivity()).getUnsplashFeaturedImages(Objects.requireNonNull(getActivity()), new FeaturedImagesRespoonseListener() {
                    @Override
                    public void onSuccess(@NonNull final Unsplash_Image[] response) {
                        IS_CATEGORY_FLAG = true;

                        appExecutor.getMainThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                loader.setVisibility(View.GONE);
                                rv.setLayoutManager(new LinearLayoutManager(dialog.getContext(), VERTICAL, false));

                                Log.i("VALUE FROM UNSPLASH", String.valueOf(response.length));
                                if (response.length <= 0) {
                                    Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), getString(R.string.Currently_no_wallpaper));
                                } else {
                                    rv.setAdapter(wallAdapter2 = new CollectionsWallpaperAdapter(getActivity(), response, new CollectionsWallpaperAdapter.OnWallpaperClickListener() {
                                        @Override
                                        public void onWallpaperClicked(int position, ArrayList<Unsplash_Image> m_AL_Images) {
                                            Intent intent = new Intent(getActivity(), WallpapersPagerActivity.class);

                                            intent.putExtra(Constants.Pager_position, position);
                                            intent.putExtra(Constants.PAGER_LIST_WALL_OBKHECTS, m_AL_Images);
                                            intent.putExtra(Constants.is_from_fav, false);

                                            startActivity(intent);
                                        }
                                    }));
                                }
                            }
                        });

                    }

                    @Override
                    public void onError(final String error) {
                        appExecutor.getMainThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("ERROR FROM UNSPLASH", error);
                                loader.setVisibility(View.GONE);

//                                Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), getString(R.string.Failed));
                                dialog.dismiss();

                            }
                        });

                    }
                }, request);

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
