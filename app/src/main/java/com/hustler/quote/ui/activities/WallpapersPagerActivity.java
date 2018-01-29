package com.hustler.quote.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hustler.quote.R;
import com.hustler.quote.ui.Services.DownloadImageService;
import com.hustler.quote.ui.adapters.WallpaperSliderAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.customviews.WallpaperPageTransformer;
import com.hustler.quote.ui.pojo.unspalsh.Unsplash_Image;
import com.hustler.quote.ui.utils.InternetUtils;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

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
    ImageView profile_image;
    TextView profile_name, profile_desc;
    Window window;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpaper_viewer_activity);
        window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
        }
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
        profile_image = (ImageView) findViewById(R.id.profile_image);
        profile_name = (TextView) findViewById(R.id.profile_name);
        profile_desc = (TextView) findViewById(R.id.image_desciption);

        fabSetLike.setOnClickListener(this);
        fabSetWall.setOnClickListener(this);
        fabDownload.setOnClickListener(this);
        fabEdit.setOnClickListener(this);
        fabShare.setOnClickListener(this);
        pageTransformer = new WallpaperPageTransformer(R.id.wallpaper_image);
        pageTransformer.setBorder(16);
        imageViewer.setPageTransformer(false, pageTransformer);
        imageViewer.setAdapter(new WallpaperSliderAdapter(getSupportFragmentManager(), unsplash_images, WallpapersPagerActivity.this));
        imageViewer.setCurrentItem(position, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            profile_image.setClipToOutline(true);
            fabShare.setClipToOutline(true);

        }
        profile_name.setText(unsplash_images[position].getUser().getFirst_name());
        profile_desc.setText(unsplash_images[position].getUser().getPortfolio_url());
        Glide.with(WallpapersPagerActivity.this).load(unsplash_images[position].getUser().getProfile_image().getMedium()).centerCrop().into(profile_image);
        imageViewer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                WallpapersPagerActivity.this.position = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v == fabSetLike) {
            // Handle clicks for fabSetLike
        } else if (v == fabSetWall) {
            if (InternetUtils.isConnectedtoNet(WallpapersPagerActivity.this) == true) {
                setWallPaer();
            } else {
                Toast_Snack_Dialog_Utils.show_ShortToast(WallpapersPagerActivity.this, getString(R.string.need_internet_hd));
            }

        } else if (v == fabDownload) {
            if (InternetUtils.isConnectedtoNet(WallpapersPagerActivity.this) == true) {
                downloadImage();
            } else {
                Toast_Snack_Dialog_Utils.show_ShortToast(WallpapersPagerActivity.this, getString(R.string.need_internet_hd));
            }
        } else if (v == fabEdit) {
            Intent intent = new Intent(WallpapersPagerActivity.this, EditorActivity.class);
            intent.putExtra(Constants.INTENT_IS_FROM_EDIT_KEY, 2);
            intent.putExtra(Constants.INTENT_UNSPLASH_IMAGE_FOR_EDIOTR_KEY, unsplash_images[position].getUrls().getRegular());
            startActivity(intent);
        } else if (v == fabShare) {
            // Handle clicks for fabShare
        }
    }

    private void downloadImage() {
        Intent intent = new Intent(WallpapersPagerActivity.this, DownloadImageService.class);
        intent.putExtra(Constants.ImageUrl_to_download, unsplash_images[position].getUrls().getRaw());
        intent.putExtra(Constants.Image_Name_to_save_key, unsplash_images[position].getId());
        intent.putExtra(Constants.is_to_setWallpaper_fromActivity, false);
        startService(intent);
    }

    public void setWallPaer() {
        Intent intent = new Intent(WallpapersPagerActivity.this, DownloadImageService.class);
        intent.putExtra(Constants.ImageUrl_to_download, unsplash_images[position].getUrls().getRaw());
        intent.putExtra(Constants.Image_Name_to_save_key, unsplash_images[position].getId());
        intent.putExtra(Constants.is_to_setWallpaper_fromActivity, true);
        startService(intent);
//        Intent intent = new Intent(WallpaperManager.
//                getInstance(getApplicationContext()).
//                getCropAndSetWallpaperIntent());
//
//        startActivity(intent);
    }


}
