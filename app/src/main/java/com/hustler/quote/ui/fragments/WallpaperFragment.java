package com.hustler.quote.ui.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hustler.quote.R;
import com.hustler.quote.ui.adapters.UserWorkAdapter;
import com.hustler.quote.ui.pojo.UserWorkImages;
import com.hustler.quote.ui.utils.FileUtils;
import com.hustler.quote.ui.utils.InternetUtils;
import com.hustler.quote.ui.utils.PermissionUtils;
import com.hustler.quote.ui.utils.TextUtils;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Sayi on 26-01-2018.
 */

public class WallpaperFragment extends android.support.v4.app.Fragment {


    private static final int MY_PERMISSION_REQUEST_ = 1001;
    RecyclerView rv;
    ProgressBar loader;
    UserWorkAdapter adapter;
    UserWorkImages userWorkImages;
    private LinearLayout dataView;
    private RelativeLayout quoteOfDayLl;
    private LinearLayout noPermissionView;
    private TextView imageText;
    private TextView message;
    UserWorkAdapter userWorkAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_wallpaper_fragment, container, false);
        rv = (RecyclerView) view.findViewById(R.id.main_rv);
        loader = (ProgressBar) view.findViewById(R.id.loader);
        dataView = (LinearLayout) view.findViewById(R.id.data_view);
        loader.setVisibility(View.GONE);
        return view;
    }

    private void checkPermission_and_proceed() {
        if (InternetUtils.isConnectedtoNet(getActivity())==true) {
            dataView.setVisibility(View.VISIBLE);
            setRecyclerview();
        } else {
            dataView.setVisibility(View.GONE);
        }
    }


    private void setRecyclerview() {
        userWorkImages = FileUtils.getImagesFromSdCard(getActivity());
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


        rv.setAdapter(userWorkAdapter);
        rv.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slideup));
    }











}
