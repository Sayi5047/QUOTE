package com.hustler.quote.ui.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.hustler.quote.ui.Executors.AppExecutor;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.customviews.ScalePagerTransformer;
import com.hustler.quote.ui.utils.InternetUtils;

import java.util.ArrayList;
import java.util.Objects;

import com.hustler.quote.R;

import com.hustler.quote.ui.Services.DownloadImageJobService;
import com.hustler.quote.ui.adapters.WallpaperAdapter;
import com.hustler.quote.ui.adapters.WallpaperSliderAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Restutility;
import com.hustler.quote.ui.database.ImagesDbHelper;
import com.hustler.quote.ui.pojo.unspalsh.FeaturedImagesRespoonseListener;
import com.hustler.quote.ui.pojo.unspalsh.Unsplash_Image;
import com.hustler.quote.ui.utils.TextUtils;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

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
public class WallpapersPagerActivity extends BaseActivity implements View.OnClickListener {

    ScalePagerTransformer pageTransformer;
    int position;
    ArrayList<Unsplash_Image> unsplash_images;
    WallpaperSliderAdapter adapter;
    private FloatingActionButton fabSetLike;
    private FloatingActionButton fabSetWall;
    private FloatingActionButton fabDownload;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabShare;
    ImageView profile_image;
    TextView profile_name, profile_desc;
    Window window;
    boolean isfromFav = false;
    Driver driver;
    FirebaseJobDispatcher firebaseJobDispatcher;
    boolean is_image_liked;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpaper_viewer_activity);

        getIntentData();
        findViews();

    }


    private void getIntentData() {
        position = getIntent().getIntExtra(Constants.Pager_position, 1);
        unsplash_images = (ArrayList<Unsplash_Image>) getIntent().getSerializableExtra(Constants.PAGER_LIST_WALL_OBKHECTS);
        isfromFav = getIntent().getBooleanExtra(Constants.is_from_fav, false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slideup, R.anim.slidedown);

    }

    private void findViews() {
        ViewPager imageViewer = findViewById(R.id.image_viewer);
        LinearLayout buttonsLayout = findViewById(R.id.buttons_layout);
        fabSetLike = findViewById(R.id.fab_set_like);
        fabSetWall = findViewById(R.id.fab_set_wall);
        fabDownload = findViewById(R.id.fab_download);
        fabEdit = findViewById(R.id.fab_edit);
        fabShare = findViewById(R.id.fab_share);
        profile_image = findViewById(R.id.profile_image);
        profile_name = findViewById(R.id.profile_name);
        profile_desc = findViewById(R.id.image_desciption);

        fabSetLike.setOnClickListener(this);
        fabSetWall.setOnClickListener(this);
        fabDownload.setOnClickListener(this);
        fabEdit.setOnClickListener(this);
        fabShare.setOnClickListener(this);
        pageTransformer = new ScalePagerTransformer(R.id.wallpaper_image);
        pageTransformer.setBorder(16);
        imageViewer.setPageTransformer(false, pageTransformer);
        imageViewer.setAdapter(new WallpaperSliderAdapter(getSupportFragmentManager(), unsplash_images, WallpapersPagerActivity.this));
        imageViewer.setCurrentItem(position, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            profile_image.setClipToOutline(true);
            fabShare.setClipToOutline(true);

        }
        profile_name.setText(unsplash_images.get(position).getUser().getFirst_name());
        profile_desc.setText(unsplash_images.get(position).getUser().getPortfolio_url());
//        Glide.with(WallpapersPagerActivity.this).load(unsplash_images.get(position).getUser().getProfile_image().getMedium()).centerCrop().into(profile_image);
        imageViewer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                WallpapersPagerActivity.this.position = position;
                if (!isfromFav) {
                    if (new ImagesDbHelper(WallpapersPagerActivity.this).check_Fav_Image_Exists(unsplash_images.get(position).getId())) {
                        fabSetLike.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp));
                        is_image_liked = true;
                    } else {
                        fabSetLike.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));
                        is_image_liked = false;

                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (isfromFav) {
            fabShare.setVisibility(View.GONE);
            fabSetLike.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp));

        } else {
            if (new ImagesDbHelper(this).check_Fav_Image_Exists(unsplash_images.get(position).getId())) {
                fabSetLike.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp));
                is_image_liked = true;

            } else {
                fabSetLike.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));
                is_image_liked = false;

            }
            fabShare.setVisibility(View.VISIBLE);

        }

    }


    @Override
    public void onClick(View v) {
        if (v == fabSetLike) {
            if (isfromFav || (is_image_liked)) {
                fabSetLike.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));
                new ImagesDbHelper(WallpapersPagerActivity.this).removeFav(unsplash_images.get(position));
            } else {
                new ImagesDbHelper(WallpapersPagerActivity.this).addFav(unsplash_images.get(position));
                fabSetLike.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp));
            }
        } else if (v == fabSetWall) {
            if (InternetUtils.isConnectedtoNet(WallpapersPagerActivity.this)) {
                AppExecutor.getInstance().getNetworkExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        setWallPaper();
                    }
                });
                Toast_Snack_Dialog_Utils.show_ShortToast(WallpapersPagerActivity.this, getString(R.string.downloading_and_setting));

            } else {
                Toast_Snack_Dialog_Utils.show_ShortToast(WallpapersPagerActivity.this, getString(R.string.need_internet_hd));
            }

        } else if (v == fabDownload) {
            if (InternetUtils.isConnectedtoNet(WallpapersPagerActivity.this)) {
                AppExecutor.getInstance().getNetworkExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        downloadImage();
                    }
                });
                Toast_Snack_Dialog_Utils.show_ShortToast(WallpapersPagerActivity.this, getString(R.string.image_will_be_downloaded_to_sdCard));

            } else {
                Toast_Snack_Dialog_Utils.show_ShortToast(WallpapersPagerActivity.this, getString(R.string.need_internet_hd));
            }
        } else if (v == fabEdit) {
            Intent intent = new Intent(WallpapersPagerActivity.this, EditorActivity.class);
            intent.putExtra(Constants.INTENT_IS_FROM_EDIT_KEY, 2);
            intent.putExtra(Constants.INTENT_UNSPLASH_IMAGE_FOR_EDIOTR_KEY, unsplash_images.get(position).getUrls().getRegular());
            startActivity(intent);
        } else if (v == fabShare) {
            // Handle clicks for fabShare

            showUserDetails(unsplash_images.get(position));
        }
    }

    private void showUserDetails(@NonNull final Unsplash_Image image) {
        final Dialog dialog = new Dialog(WallpapersPagerActivity.this, R.style.EditTextDialog_non_floater);
        dialog.setContentView(R.layout.show_photographer_dialog_layout);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.EditTextDialog_non_floater;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
        }
        dialog.setCancelable(true);

        RelativeLayout rootView;
        ImageView userCover;
        ImageView userImage;
        TextView name;
        TextView bio;
        TextView location;
        TextView twitterName;
        TextView portfolioUrl;
        final RecyclerView userPics;
        FloatingActionButton close;

        Button visit_profile;
        rootView = dialog.findViewById(R.id.root);
        userCover = dialog.findViewById(R.id.user_cover);
        userImage = dialog.findViewById(R.id.user_image);
        name = dialog.findViewById(R.id.name);
        bio = dialog.findViewById(R.id.bio);
        location = dialog.findViewById(R.id.location);
        twitterName = dialog.findViewById(R.id.twitter_name);
        portfolioUrl = dialog.findViewById(R.id.portfolio_url);
        userPics = dialog.findViewById(R.id.user_pics);
        close = dialog.findViewById(R.id.close);
        visit_profile = dialog.findViewById(R.id.visit_profile);
        dialog.show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            userImage.setClipToOutline(true);
        }
        TextUtils.findText_and_applyTypeface(rootView, WallpapersPagerActivity.this);
        TextUtils.findText_and_applyamim_slideup(rootView, WallpapersPagerActivity.this);

        Glide.with(WallpapersPagerActivity.this).load(image.getUser().getProfile_image().getLarge()).centerCrop().into(userImage);
        Glide.with(WallpapersPagerActivity.this).load(image.getUrls().getRegular()).centerCrop().into(userCover);

        String nameD = image.getUser().getName() == null ? "NA" : image.getUser().getName();
        String bioD = image.getUser().getBio() == null ? "NA" : image.getUser().getBio();
        String twitterD = image.getUser().getTwitter_username() == null ? "NA" : image.getUser().getTwitter_username();
        String locationD = image.getUser().getLocation() == null ? "NA" : image.getUser().getLocation();
        String portfolioD = image.getUser().getPortfolio_url() == null ? "NA" : image.getUser().getPortfolio_url();

        name.setText(nameD);
        bio.setText(bioD);
        location.setText(locationD);
        twitterName.setText(twitterD);
        portfolioUrl.setText(portfolioD);

        portfolioUrl.setClickable(true);
        portfolioUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(image.getUser().getPortfolio_url()));
                startActivity(i);
            }
        });


        userPics.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        if (image.getUser().getLinks().getPortfolio() == null || image.getUser().getLinks().getPortfolio().length() <= 0) {

        } else {
            new Restutility(WallpapersPagerActivity.this).getUnsplashUserImages(WallpapersPagerActivity.this, new FeaturedImagesRespoonseListener() {
                @Override
                public void onSuccess(@NonNull final Unsplash_Image[] unsplash_images) {
                    userPics.setAdapter(new WallpaperAdapter(WallpapersPagerActivity.this, unsplash_images, new WallpaperAdapter.OnWallpaperClickListener() {
                        @Override
                        public void onWallpaperClicked(int position, ArrayList<Unsplash_Image> images, ImageView itemView) {
                            Intent intent = new Intent(WallpapersPagerActivity.this, WallpapersPagerActivity.class);

                            intent.putExtra(Constants.Pager_position, position);
                            intent.putExtra(Constants.PAGER_LIST_WALL_OBKHECTS, images);
                            intent.putExtra(Constants.is_from_fav, false);

                            startActivity(intent);
                        }
                    }));
                }

                @Override
                public void onError(String error) {

                }
            }, image.getUser().getLinks().getPhotos() + "/?client_id=" + Constants.UNSPLASH_CLIENT_ID);
        }
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(@NonNull DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
                    dialog.dismiss();
                    return true;
                } else {
                    return false;
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        visit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://unsplash.com/" + image.getUser().getUsername() + "?utm_source=Quotzy&utm_medium=referral\""));
                startActivity(intent);
            }
        });
    }

    private void downloadImage() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ImageUrl_to_download, unsplash_images.get(position).getLinks().getDownload());
        bundle.putString(Constants.Image_Name_to_save_key, unsplash_images.get(position).getId());
        bundle.putBoolean(Constants.is_to_setWallpaper_fromActivity, false);
        if (null == driver) {
            driver = new GooglePlayDriver(this);

        }
        if (null == firebaseJobDispatcher) {
            firebaseJobDispatcher = new FirebaseJobDispatcher(driver);

        }
        firebaseJobDispatcher.cancel(Constants.DONWLOADIMAGE_IMAGE_JOB_TAG);
        Job downloadJob = firebaseJobDispatcher.
                newJobBuilder().
                setService(DownloadImageJobService.class)
                .setRecurring(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setExtras(bundle)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setTag(Constants.DONWLOADIMAGE_IMAGE_JOB_TAG)
                .build();
        firebaseJobDispatcher.mustSchedule(downloadJob);

    }

    public void setWallPaper() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ImageUrl_to_download, unsplash_images.get(position).getLinks().getDownload());
        bundle.putString(Constants.Image_Name_to_save_key, unsplash_images.get(position).getId());
        bundle.putBoolean(Constants.is_to_setWallpaper_fromActivity, true);
        if (null == driver) {
            driver = new GooglePlayDriver(this);
        }
        if (null == firebaseJobDispatcher) {
            firebaseJobDispatcher = new FirebaseJobDispatcher(driver);

        }
        firebaseJobDispatcher.cancel(Constants.SETWALLPAPER_IMAGE_TAG);
        Job downloadJob = firebaseJobDispatcher
                .newJobBuilder()
                .setService(DownloadImageJobService.class)
                .setRecurring(false)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.NOW)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setExtras(bundle)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setTag(Constants.SETWALLPAPER_IMAGE_TAG)
                .build();
        firebaseJobDispatcher.mustSchedule(downloadJob);
    }

}
