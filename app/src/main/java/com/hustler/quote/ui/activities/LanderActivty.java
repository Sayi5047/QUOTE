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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.customviews.ParrallaxPageTransformer;
import com.hustler.quote.ui.utils.ColorUtils;
import com.hustler.quote.ui.utils.TextUtils;

/**
 * Created by Sayi on 03-01-2018.
 */

public class LanderActivty extends BaseActivity {
    ViewPager viewPager;
    RelativeLayout relativeLayout;
    AnimationDrawable animationDrawable;
    Button button;
    ParrallaxPageTransformer parrallaxPageTransformer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lander_activity_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        relativeLayout = (RelativeLayout) findViewById(R.id.root_layout);

        button = (Button) findViewById(R.id.bt_launch);

        viewPager.setAdapter(new landerAdapter(getSupportFragmentManager(), LanderActivty.this));

        parrallaxPageTransformer = new ParrallaxPageTransformer(R.id.card, R.id.on_board_image, R.id.on_board_title,R.id.on_board_image_circle, R.id.on_board_descriptiom);
        parrallaxPageTransformer.setBorder(2);
//        parrallaxPageTransformer.setSpeed(0.2f);

        viewPager.setPageTransformer(false, parrallaxPageTransformer);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                viewPager.setBackgroundColor(ColorUtils.getHEaderColor(position, positionOffset,LanderActivty.this));
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3) {
//                    button.setVisibility(View.VISIBLE);
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
            onBoardFragment.imageresource = images[position];
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
        RelativeLayout image;
        AnimationDrawable animationDrawable;
        RelativeLayout linearLayout;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
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
            image = (RelativeLayout) view.findViewById(R.id.card);
            linearLayout = (RelativeLayout) view.findViewById(R.id.linear_bg_layout);
            TextUtils.findText_and_applyTypeface(linearLayout, getActivity());
            animationDrawable = (AnimationDrawable) linearLayout.getBackground();
//            animationDrawable.setEnterFadeDuration(2000);
//            animationDrawable.setExitFadeDuration(2000);

//            image.post(new Runnable() {
//                @Override
//                public void run() {
//                    Matrix matrix = new Matrix();
//                    matrix.reset();
//
//                    float wv = image.getWidth();
//                    float hv = image.getHeight();
//
//                    float wi = image.getDrawable().getIntrinsicWidth();
//                    float hi = image.getDrawable().getIntrinsicHeight();
//
//                    float width = wv;
//                    float height = hv;
//
//                    if (wi / wv > hi / hv) {
//                        matrix.setScale(hv / hi, hv / hi);
//                        width = wi * hv / hi;
//                    } else {
//                        matrix.setScale(wv / wi, wv / wi);
//                        height = hi * wv / wi;
//                    }
//
//                    matrix.preTranslate((wv - width) / 2, (hv - height) / 2);
//                    image.setScaleType(ImageView.ScaleType.MATRIX);
//                    image.setImageMatrix(matrix);
//                }
//            });
            return view;
        }
    }
}
