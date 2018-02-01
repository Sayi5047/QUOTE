package com.hustler.quote.ui.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.hustler.quote.R;
import com.hustler.quote.ui.Services.DownloadImageService;
import com.hustler.quote.ui.adapters.WallpaperAdapter;
import com.hustler.quote.ui.adapters.WallpaperSliderAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.apiRequestLauncher.Restutility;
import com.hustler.quote.ui.customviews.WallpaperPageTransformer;
import com.hustler.quote.ui.database.ImagesDbHelper;
import com.hustler.quote.ui.pojo.unspalsh.ImagesFromUnsplashResponse;
import com.hustler.quote.ui.pojo.unspalsh.Unsplash_Image;
import com.hustler.quote.ui.utils.InternetUtils;
import com.hustler.quote.ui.utils.TextUtils;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

import static com.hustler.quote.ui.apiRequestLauncher.Constants.UNSPLASH_CLIENT_ID;

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
            new ImagesDbHelper(WallpapersPagerActivity.this).addFav(unsplash_images[position]);
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

            showUserDetails(unsplash_images[position]);
        }
    }

    private void showUserDetails(final Unsplash_Image image) {
        final Dialog dialog = new Dialog(WallpapersPagerActivity.this, R.style.EditTextDialog_non_floater);
        dialog.setContentView(R.layout.show_photographer_dialog_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog_non_floater;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
        }
        dialog.setCancelable(true);
        LinearLayout root;

        RelativeLayout rootView;
        RelativeLayout header;
        ImageView userCover;
        ImageView userImage;
        RelativeLayout details;
        TextView name;
        TextView bio;
        TextView location;
        TextView twitterName;
        TextView portfolioUrl;
        final RecyclerView userPics;
        FloatingActionButton close;

        Button visit_profile;
        rootView = (RelativeLayout) dialog.findViewById(R.id.root);
        header = (RelativeLayout) dialog.findViewById(R.id.header);
        userCover = (ImageView) dialog.findViewById(R.id.user_cover);
        userImage = (ImageView) dialog.findViewById(R.id.user_image);
        details = (RelativeLayout) dialog.findViewById(R.id.details);
        name = (TextView) dialog.findViewById(R.id.name);
        bio = (TextView) dialog.findViewById(R.id.bio);
        location = (TextView) dialog.findViewById(R.id.location);
        twitterName = (TextView) dialog.findViewById(R.id.twitter_name);
        portfolioUrl = (TextView) dialog.findViewById(R.id.portfolio_url);
        userPics = (RecyclerView) dialog.findViewById(R.id.user_pics);
        close = (FloatingActionButton) dialog.findViewById(R.id.close);
        visit_profile = (Button) dialog.findViewById(R.id.visit_profile);
        dialog.show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            userImage.setClipToOutline(true);
        }
        TextUtils.findText_and_applyTypeface(rootView, WallpapersPagerActivity.this);
        TextUtils.findText_and_applyamim_slideup(rootView, WallpapersPagerActivity.this);

        Glide.with(WallpapersPagerActivity.this).load(image.getUser().getProfile_image().getLarge()).crossFade().centerCrop().into(userImage);
        Glide.with(WallpapersPagerActivity.this).load(image.getUrls().getRegular()).crossFade().centerCrop().into(userCover);

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
            new Restutility(WallpapersPagerActivity.this).getUnsplashUSERImages(WallpapersPagerActivity.this, new ImagesFromUnsplashResponse() {
                @Override
                public void onSuccess(final Unsplash_Image[] unsplash_images) {
                    userPics.setAdapter(new WallpaperAdapter(WallpapersPagerActivity.this, unsplash_images, new WallpaperAdapter.OnWallpaperClickListener() {
                        @Override
                        public void onWallpaperClicked(int position, Unsplash_Image wallpaper) {
                            Intent intent = new Intent(WallpapersPagerActivity.this, WallpapersPagerActivity.class);

                            intent.putExtra(Constants.Pager_position, position);
                            intent.putExtra(Constants.PAGER_LIST_WALL_OBKHECTS, unsplash_images);
                            startActivity(intent);
                        }
                    }));
                }

                @Override
                public void onError(String error) {

                }
            }, image.getUser().getLinks().getPhotos() + "/?client_id=" + UNSPLASH_CLIENT_ID);
        }
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_BACK || keyCode == event.KEYCODE_HOME) {
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
