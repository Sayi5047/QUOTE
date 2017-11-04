package com.hustler.quote.ui.activities;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.adapters.TabsFragmentPagerAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.superclasses.App;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener ,ViewPager.OnPageChangeListener{
    private AppBarLayout appBar;
    private TextView headerName;
    private FloatingActionButton floatingActionButton;
    private ViewPager mainPager;
    private TabLayout tab_layout;
    Animator anim;
    int cx,cy;
    float finalRadius;
    int[]colors;

    private TabsFragmentPagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_main);
        findViews();
        cx=appBar.getWidth()/2;
        cy=appBar.getHeight()/2;
        finalRadius=(float) StrictMath.hypot(cx,cy);
        colors= new int[]{getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.textColor)};

    }


    private void findViews() {
        appBar = (AppBarLayout)findViewById( R.id.app_bar );
        headerName = (TextView)findViewById( R.id.header_name );
        headerName.setTypeface(App.getZingCursive(this, Constants.FONT_Sans_Bold));
        floatingActionButton = (FloatingActionButton)findViewById( R.id.floatingActionButton );
        mainPager = (ViewPager)findViewById( R.id.main_pager );
        tab_layout = (TabLayout) findViewById( R.id.tab_layout );
        tab_layout.setupWithViewPager(mainPager);
        mainPager.setAdapter(new TabsFragmentPagerAdapter(this,getSupportFragmentManager()));
        mainPager.setCurrentItem(0);
        mainPager.setOnPageChangeListener(this);
        floatingActionButton.setOnClickListener( this );
    }


    @Override
    public void onClick(View v) {
        if ( v == floatingActionButton ) {
            // Handle clicks for floatingActionButton
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        appBar.setBackgroundColor(getHEaderColor(position,positionOffset));
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

    private int getHEaderColor(int position, float positionOffset) {
        if(position== colors.length-1){
            return colors[position];
        }else {
            int start=colors[position];
            int end=colors[position+1];
            int color=(int) new ArgbEvaluator().evaluate(positionOffset,start,end);
            return color;
        }
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
