package io.hustler.qtzy.ui.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.activities.WallpapersPagerActivity;
import io.hustler.qtzy.ui.adapters.CollectionsWallpaperAdapter;
import io.hustler.qtzy.ui.adapters.ImageCategoryAdapter;
import io.hustler.qtzy.ui.apiRequestLauncher.Constants;
import io.hustler.qtzy.ui.apiRequestLauncher.Restutility;
import io.hustler.qtzy.ui.pojo.UnsplashImages_Collection_Response;
import io.hustler.qtzy.ui.pojo.Unsplash_Image_collection_response_listener;
import io.hustler.qtzy.ui.pojo.unspalsh.Unsplash_Image;
import io.hustler.qtzy.ui.utils.AdUtils;
import io.hustler.qtzy.ui.utils.TextUtils;
import io.hustler.qtzy.ui.utils.Toast_Snack_Dialog_Utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sayi on 18-03-2018.
 */

public class Categoris_wallpaper_fragment extends android.support.v4.app.Fragment {
    RecyclerView catgories;
    CollectionsWallpaperAdapter wallAdapter2;
    String collection_category = "girls";
    boolean IS_CATEGORY_FLAG = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wallcategories_layout, container, false);
        catgories = view.findViewById(R.id.catgories);
        catgories.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        getCategories();
        return view;
    }

    private void getCategories() {

        catgories.setAdapter(new ImageCategoryAdapter(getActivity(), new ImageCategoryAdapter.OnImageClickLitner() {
            @Override
            public void onCategoryClicked(int position, String category) {
                collection_category = category;
//                clearEverything();

                // TODO: 18-03-2018 implement waht to do here
//                load_searched_images(category, 1);
                buildDialog_and_search(collection_category);


            }
        }));
    }


    private void buildDialog_and_search(String collection_category) {

        final Dialog dialog = new Dialog(getActivity(), R.style.EditTextDialog_non_floater_2);
        dialog.setContentView(R.layout.search_chooser_layout2);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog_non_floater_2;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(andro));
        dialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        }


        final RelativeLayout root;
        final ImageView search;
        ImageView close;
        final RecyclerView result_rv;
        final LinearLayout search_header;
        final ProgressBar loader;
        final String[] selected_type = new String[1];
        AdView adView;
        final TextView search_term;


        close = dialog.findViewById(R.id.search_btn);
        root = dialog.findViewById(R.id.root);
        result_rv = dialog.findViewById(R.id.result_rv);
        loader = dialog.findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);

        adView = dialog.findViewById(R.id.adView);
        search_term = dialog.findViewById(R.id.search_term);
        AdUtils.loadBannerAd(adView, getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            result_rv.setClipToOutline(true);
        }

        load_searched_images(collection_category, new Random().nextInt(5), result_rv, loader, dialog);
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
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
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

    public void load_searched_images(String category, final int pagePosition, final RecyclerView rv, final ProgressBar loader, final Dialog dialog) {
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
                rv.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
//                    wallAdapter2.removeItems();

                Log.i("VALUE FROM UNSPLASH", String.valueOf(response.getResults().length));
                if (response.getResults().length <= 0) {
                    Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), getString(R.string.Currently_no_wallpaper));
                } else {
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

                loader.setVisibility(View.GONE);
                Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), getString(R.string.Failed));
                dialog.dismiss();

            }
        }, request);

    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
