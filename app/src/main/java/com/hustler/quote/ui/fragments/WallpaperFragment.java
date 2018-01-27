package com.hustler.quote.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.activities.WallpapersPagerActivity;
import com.hustler.quote.ui.adapters.UserWorkAdapter;
import com.hustler.quote.ui.adapters.WallpaperAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.apiRequestLauncher.Restutility;
import com.hustler.quote.ui.pojo.UserWorkImages;
import com.hustler.quote.ui.pojo.unspalsh.ImagesFromUnsplashResponse;
import com.hustler.quote.ui.pojo.unspalsh.Unsplash_Image;
import com.hustler.quote.ui.utils.FileUtils;
import com.hustler.quote.ui.utils.InternetUtils;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

/**
 * Created by Sayi on 26-01-2018.
 */

public class WallpaperFragment extends android.support.v4.app.Fragment {


    private static final int MY_PERMISSION_REQUEST_ = 1001;
    RecyclerView rv;
    ProgressBar loader;
    UserWorkImages userWorkImages;
    private LinearLayout dataView;
    WallpaperAdapter wallpaperAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_wallpaper_fragment, container, false);
        rv = (RecyclerView) view.findViewById(R.id.main_rv);
        loader = (ProgressBar) view.findViewById(R.id.loader);
        dataView = (LinearLayout) view.findViewById(R.id.data_view);
        loader.setVisibility(View.GONE);
        checkPermission_and_proceed();
        return view;
    }

    private void checkPermission_and_proceed() {
        if (InternetUtils.isConnectedtoNet(getActivity()) == true) {
            dataView.setVisibility(View.VISIBLE);
            setRecyclerview();
        } else {
            dataView.setVisibility(View.GONE);
        }
    }


    private void setRecyclerview() {
        userWorkImages = FileUtils.getImagesFromSdCard(getActivity());
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        getRandomIMages();

    }

    private void getRandomIMages() {
        loader.setVisibility(View.VISIBLE);

        String request = Constants.API_GET_IMAGES_FROM_UNSPLASH;
        new Restutility(getActivity()).getUnsplashRandomImages(getActivity(), new ImagesFromUnsplashResponse() {
            @Override
            public void onSuccess(final Unsplash_Image[] unsplash_images) {
                loader.setVisibility(View.GONE);
                Log.i("VALUE FROM UNSPLASH", String.valueOf(unsplash_images.length));
                if (unsplash_images.length <= 0) {
                    dataView.setVisibility(View.GONE);
                    Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), getString(R.string.Currently_no_wallpaper));
                } else {
                    dataView.setVisibility(View.VISIBLE);
                    wallpaperAdapter = new WallpaperAdapter(getActivity(), unsplash_images, new WallpaperAdapter.OnWallpaperClickListener() {
                        @Override
                        public void onWallpaperClicked(int position, Unsplash_Image wallpaper) {
                            Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), wallpaper.getUser().getFirst_name());
                            Intent intent=new Intent(getActivity(), WallpapersPagerActivity.class);

                            intent.putExtra(Constants.Pager_position,position);
                            intent.putExtra(Constants.PAGER_LIST_WALL_OBKHECTS,unsplash_images);
                            startActivity(intent);
                        }
                    });
                    rv.setAdapter(wallpaperAdapter);
                }

            }

            @Override
            public void onError(String error) {
                Log.e("ERROR FROM UNSPLASH", error);
                loader.setVisibility(View.GONE);

                dataView.setVisibility(View.GONE);
                Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), getString(R.string.Failed));
            }
        }, request);
    }


}
