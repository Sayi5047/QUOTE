package com.hustler.quote.ui.activities;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.adapters.TabsFragmentPagerAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.superclasses.App;
import com.hustler.quote.ui.utils.AnimUtils;
import com.hustler.quote.ui.utils.TextUtils;

import org.w3c.dom.Text;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private AppBarLayout appBar;
    private TextView headerName;
    private Button floatingActionButton;
    private ViewPager mainPager;
    private TabLayout tab_layout;
    Window window;
    Animator anim;
    CoordinatorLayout rootView;
    int cx, cy;
    float finalRadius;
    int[] colors;
    int currentcolor;

    private TabsFragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_main);
        findViews();
        cx = appBar.getWidth() / 2;
        cy = appBar.getHeight() / 2;
        finalRadius = (float) StrictMath.hypot(cx, cy);
        window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        colors = new int[]{getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.textColor)};

    }


    private void findViews() {
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        headerName = (TextView) findViewById(R.id.header_name);
        headerName.setTypeface(App.getZingCursive(this, Constants.FONT_Sans_Bold));
        floatingActionButton = (Button) findViewById(R.id.floatingActionButton);
        mainPager = (ViewPager) findViewById(R.id.main_pager);
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        rootView = (CoordinatorLayout) findViewById(R.id.root);
        tab_layout.setupWithViewPager(mainPager);
        mainPager.setAdapter(new TabsFragmentPagerAdapter(this, getSupportFragmentManager()));
        mainPager.setCurrentItem(0);
        mainPager.setOnPageChangeListener(this);
        floatingActionButton.setOnClickListener(this);

    }


    @Override
    public void onClick(final View v) {
//        DIALOG FOR EDITORS
        if (v == floatingActionButton) {
            Button quote, meme, close;
            TextView quote_tv, meme_tv, close_tv,select_tv;


            // Handle clicks for floatingActionButton
            final Dialog dialog = new Dialog(this, R.style.MyAlertDialog);
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

            TextUtils.setFont(HomeActivity.this,quote_tv,Constants.FONT_Sans_Bold);
            TextUtils.setFont(HomeActivity.this,meme_tv,Constants.FONT_Sans_Bold);
            TextUtils.setFont(HomeActivity.this,close_tv,Constants.FONT_Sans_Bold);
            TextUtils.setFont(HomeActivity.this,select_tv,Constants.FONT_Sans_Bold);

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    AnimUtils.revealCircular(floatingActionButton, dialog, view, true);
                }
            });

            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == event.KEYCODE_BACK) {
                        AnimUtils.revealCircular(floatingActionButton, dialog, view, false);
                        return true;
                    }
                    return false;
                }
            });


            quote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 02-12-2017 implement to go quotes editor
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
                    AnimUtils.revealCircular(floatingActionButton, dialog, view, false);

                }
            });
            view.setBackgroundColor(getResources().getColor(android.R.color.white));
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            dialog.show();
        }
    }


    /*VIEW PAGER METHODS*/
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        appBar.setBackgroundColor(getHEaderColor(position, positionOffset));
        mainPager.setBackgroundColor(getHEaderColor(position, positionOffset));
        tab_layout.setBackgroundColor(getHEaderColor(position, positionOffset));
        floatingActionButton.setBackgroundColor(getHEaderColor(position, positionOffset));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getHEaderColor(position, positionOffset));
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

    private int getHEaderColor(int position, float positionOffset) {
        if (position == colors.length - 1) {
            return colors[position];
        } else {
            int start = colors[position];
            int end = colors[position + 1];
            int color = (int) new ArgbEvaluator().evaluate(positionOffset, start, end);
            return color;
        }
    }

}
