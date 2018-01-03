package com.hustler.quote.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hustler.quote.R;

/**
 * Created by Sayi on 03-01-2018.
 */

public class LanderActivty extends BaseActivity {
    ViewPager viewPager;
    RelativeLayout relativeLayout;
    AnimationDrawable animationDrawable;
    Button button;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lander_activity_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        relativeLayout = (RelativeLayout) findViewById(R.id.root_layout);
        animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        button = (Button) findViewById(R.id.bt_launch);
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        viewPager.setAdapter(new landerAdapter(getSupportFragmentManager(), LanderActivty.this));

        viewPager.setPageTransformer(true, new ZoomoutPageTransformer());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3) {
                    button.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LanderActivty.this, HomeActivity.class));

            }
        });
    }


    private class ZoomoutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

//                // Fade the page relative to its size.
//                view.setAlpha(MIN_ALPHA +
//                        (scaleFactor - MIN_SCALE) /
//                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }

    }

    private class landerAdapter extends FragmentStatePagerAdapter {
        android.support.v4.app.FragmentManager fragmentManager;
        Context context;
        String[] titles;
        int[] images = {R.drawable.ic_archive_black_24dp, R.drawable.ic_share_accent_24dp, R.drawable.ic_mode_edit_black_24dp, R.drawable.ic_archive_black_24dp};

        String[] descriptions;

        public landerAdapter(android.support.v4.app.FragmentManager fm, Context context) {
            super(fm);
            this.fragmentManager = fragmentManager;
            this.context = context;
            titles = context.getResources().getStringArray(R.array.Text_features);
            descriptions = context.getResources().getStringArray(R.array.Background_features);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int position) {
            return getOnBoardFragment(position);
        }

        private Fragment getOnBoardFragment(int position) {
            OnBoardFragment onBoardFragment = new OnBoardFragment();
            onBoardFragment.title = titles[position];
//            onBoardFragment.imageresource = images[position];
            onBoardFragment.desciption = descriptions[position];
            return onBoardFragment;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }

    public static class OnBoardFragment extends Fragment {
        String title;
        String desciption;
        int imageresource;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View view = inflater.inflate(R.layout.onboard_layout, container, false);
            if (imageresource != 0) {
                ((ImageView) view.findViewById(R.id.on_board_image)).setImageResource(imageresource);
            }
            if (title != null && !title.equals("")) {
                ((TextView) view.findViewById(R.id.on_board_title)).setText(title);
            }
            if (desciption != null && !desciption.equals("")) {
                ((TextView) view.findViewById(R.id.on_board_descriptiom)).setText(desciption);
            }

            return view;
        }
    }
}
