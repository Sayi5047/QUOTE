package com.hustler.quote.ui.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.hustler.quote.R;
import com.hustler.quote.ui.adapters.WallpaperSliderAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.customviews.WallpaperPageTransformer;
import com.hustler.quote.ui.pojo.unspalsh.Unsplash_Image;

/**
 * Created by Sayi on 27-01-2018.
 */

public class WallpapersPagerActivity extends BaseActivity implements View.OnClickListener {

    WallpaperPageTransformer pageTransformer;
    int position;
    Unsplash_Image[] unsplash_images;
    WallpaperSliderAdapter adapter;
    private ViewPager imageViewer;
    private LinearLayout buttonsLayout;
    private FloatingActionButton fabSetLike;
    private FloatingActionButton fabSetWall;
    private FloatingActionButton fabDownload;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabShare;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpaper_viewer_activity);
        getIntentData();
        findViews();
    }

    private void getIntentData() {
        position = getIntent().getIntExtra(Constants.Pager_position, 1);
        unsplash_images = (Unsplash_Image[]) getIntent().getSerializableExtra(Constants.PAGER_LIST_WALL_OBKHECTS);

    }

    private void findViews() {
        imageViewer = (ViewPager) findViewById(R.id.image_viewer);
        buttonsLayout = (LinearLayout) findViewById(R.id.buttons_layout);
        fabSetLike = (FloatingActionButton) findViewById(R.id.fab_set_like);
        fabSetWall = (FloatingActionButton) findViewById(R.id.fab_set_wall);
        fabDownload = (FloatingActionButton) findViewById(R.id.fab_download);
        fabEdit = (FloatingActionButton) findViewById(R.id.fab_edit);
        fabShare = (FloatingActionButton) findViewById(R.id.fab_share);

        fabSetLike.setOnClickListener(this);
        fabSetWall.setOnClickListener(this);
        fabDownload.setOnClickListener(this);
        fabEdit.setOnClickListener(this);
        fabShare.setOnClickListener(this);
        pageTransformer = new WallpaperPageTransformer(R.id.wallpaper_image);
        pageTransformer.setBorder(16);
        imageViewer.setPageTransformer(false, pageTransformer);
        imageViewer.setAdapter(new WallpaperSliderAdapter(getSupportFragmentManager(), unsplash_images, WallpapersPagerActivity.this));
        imageViewer.setCurrentItem(position);
    }

    @Override
    public void onClick(View v) {
        if (v == fabSetLike) {
            // Handle clicks for fabSetLike
        } else if (v == fabSetWall) {
            // Handle clicks for fabSetWall
        } else if (v == fabDownload) {
            // Handle clicks for fabDownload
        } else if (v == fabEdit) {
            // Handle clicks for fabEdit
        } else if (v == fabShare) {
            // Handle clicks for fabShare
        }
    }
}
