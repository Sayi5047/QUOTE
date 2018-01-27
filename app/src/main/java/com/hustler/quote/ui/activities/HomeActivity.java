package com.hustler.quote.ui.activities;

import android.animation.Animator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.hustler.quote.R;
import com.hustler.quote.ui.adapters.TabsFragmentPagerAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.superclasses.App;
import com.hustler.quote.ui.utils.ColorUtils;
import com.hustler.quote.ui.utils.TextUtils;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private AppBarLayout appBar;
    private TextView headerName;
    private FloatingActionButton floatingActionButton;
    private ViewPager mainPager;
    private TabLayout tab_layout;
    Window window;
    Animator anim;
    CoordinatorLayout rootView;
    int cx, cy;
    float finalRadius;
    int currentcolor;
    int[] colors;


    private AdView mAdView;
    private TabsFragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_main);
        MobileAds.initialize(HomeActivity.this, Constants.ADS_APP_ID);

        findViews();
        cx = appBar.getWidth() / 2;
        cy = appBar.getHeight() / 2;
        finalRadius = (float) StrictMath.hypot(cx, cy);
        window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        colors = new int[]{
                ContextCompat.getColor(HomeActivity.this, R.color.pink_400),
                ContextCompat.getColor(HomeActivity.this, R.color.colorAccent),
                ContextCompat.getColor(HomeActivity.this, R.color.green_300),
                ContextCompat.getColor(HomeActivity.this, R.color.orange_300),
                ContextCompat.getColor(HomeActivity.this, R.color.textColor)};

    }

    @Override
    protected void onStart() {
        super.onStart();
        setAnimation(appBar);
        setAnimation(floatingActionButton);
        setAnimation(mainPager);
        setAnimation(tab_layout);
    }

    private void findViews() {
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        headerName = (TextView) findViewById(R.id.header_name);
        headerName.setTypeface(App.applyFont(this, Constants.FONT_Google_sans_regular));
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        mainPager = (ViewPager) findViewById(R.id.main_pager);
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        rootView = (CoordinatorLayout) findViewById(R.id.root);
        tab_layout.setupWithViewPager(mainPager);
        mAdView = (AdView) findViewById(R.id.adView);
        loadAds();
        mainPager.setAdapter(new TabsFragmentPagerAdapter(this, getSupportFragmentManager()));
        mainPager.setCurrentItem(1);
        mainPager.setOnPageChangeListener(this);
        floatingActionButton.setOnClickListener(this);


    }

    private void loadAds() {
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("A5B1E467FD401973F9F69AD2CCC13C30").build();
        mAdView.loadAd(adRequest);

    }

    private void setAnimation(View view) {
        view.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup));
    }


    @Override
    public void onClick(final View v) {
//        DIALOG FOR EDITORS
        if (v == floatingActionButton) {
            Button quote, meme, close;
            TextView quote_tv, meme_tv, close_tv, select_tv;


            // Handle clicks for floatingActionButton
            final Dialog dialog = new Dialog(this, R.style.EditTextDialog_scaled);
            final View view = View.inflate(this, R.layout.edit_chooser, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(view);

            quote = (Button) dialog.findViewById(R.id.Quotes);
            meme = (Button) dialog.findViewById(R.id.meme);
            close = (Button) dialog.findViewById(R.id.close);

            quote_tv = (TextView) dialog.findViewById(R.id.Quotes_tv);
            meme_tv = (TextView) dialog.findViewById(R.id.meme_tv);
            close_tv = (TextView) dialog.findViewById(R.id.close_tv);
            select_tv = (TextView) dialog.findViewById(R.id.select_tv);

            TextUtils.setFont(HomeActivity.this, quote_tv, Constants.FONT_Google_sans_regular);
            TextUtils.setFont(HomeActivity.this, meme_tv, Constants.FONT_Google_sans_regular);
            TextUtils.setFont(HomeActivity.this, close_tv, Constants.FONT_Google_sans_regular);
            TextUtils.setFont(HomeActivity.this, select_tv, Constants.FONT_Google_sans_regular);

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
//                    AnimUtils.revealCircular(floatingActionButton, dialog, view, true);
                }
            });

            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == event.KEYCODE_BACK) {
                        dialog.dismiss();
//                        AnimUtils.revealCircular(floatingActionButton, dialog, view, false);
                        return true;
                    }
                    return false;
                }
            });


            quote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 02-12-2017 implement to go quotes editor
                    dialog.dismiss();
                    Intent intent = new Intent(HomeActivity.this, EditorActivity.class);
                    intent.putExtra(Constants.INTENT_IS_FROM_EDIT_KEY, false);
                    startActivity(intent);
                }
            });

            meme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 02-12-2017 implement to go to meme Editor
                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
//                    AnimUtils.revealCircular(floatingActionButton, dialog, view, false);

                }
            });
            view.setBackgroundColor(getResources().getColor(android.R.color.white));
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog_scaled;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialog.getWindow().setStatusBarColor(ContextCompat.getColor(HomeActivity.this, android.R.color.white));
            }
            dialog.show();
        }
    }


    /*VIEW PAGER METHODS*/
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        appBar.setBackgroundColor(ColorUtils.getHEaderColor(colors, position, positionOffset, HomeActivity.this));
        mainPager.setBackgroundColor(ColorUtils.getHEaderColor(colors, position, positionOffset, HomeActivity.this));
        tab_layout.setBackgroundColor(ColorUtils.getHEaderColor(colors, position, positionOffset, HomeActivity.this));
//        floatingActionButton.setBackgroundColor((getHEaderColor(position, positionOffset)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ColorUtils.getHEaderColor(colors, position, positionOffset, HomeActivity.this));
        }


//        switch (position){
//            case 0:{
//                tab_layout.setBackgroundColor(this.getResources().getColor(R.color.colorPrimaryDark));
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    anim = ViewAnimationUtils.createCircularReveal(tab_layout, cx, cy, 0, finalRadius);
//
//                }
//                appBar.setVisibility(View.VISIBLE);
//
//                anim.start();
//
//            }break;case 1:{
//                tab_layout.setBackgroundColor(this.getResources().getColor(R.color.colorAccent));
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    anim = ViewAnimationUtils.createCircularReveal(tab_layout, cx, cy, 0, finalRadius);
//                }
//                appBar.setVisibility(View.VISIBLE);
//
//                anim.start();
//
//            }break;case 2:{
//                tab_layout.setBackgroundColor(this.getResources().getColor(R.color.textColor));
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    anim = ViewAnimationUtils.createCircularReveal(tab_layout, cx, cy, 0, finalRadius);
//                }
//                appBar.setVisibility(View.VISIBLE);
//
//                anim.start();
//
//
//            }break;
//        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onBackPressed() {
        Toast_Snack_Dialog_Utils.createDialog(HomeActivity.this, getString(R.string.warning), getString(R.string.are_you_sure_close), getString(R.string.cancel), getString(R.string.close), new Toast_Snack_Dialog_Utils.Alertdialoglistener() {
            @Override
            public void onPositiveselection() {
                finishAffinity();
                System.exit(0);
            }

            @Override
            public void onNegativeSelection() {
                return;
            }
        });
//        super.onBackPressed();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            this.finishAndRemoveTask();
//        }
//        else {
//            this.finishAffinity();
//        }
//        System.exit(0);
    }
}
