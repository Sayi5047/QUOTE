package com.hustler.quotzy.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hustler.quotzy.R;
import com.hustler.quotzy.ui.apiRequestLauncher.Constants;
import com.hustler.quotzy.ui.customviews.ParrallaxPageTransformer;
import com.hustler.quotzy.ui.utils.ColorUtils;
import com.hustler.quotzy.ui.utils.TextUtils;

/**
 * Created by Sayi on 03-01-2018.
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
public class OnBoardingActivity extends BaseActivity {
    ViewPager viewPager;
    RelativeLayout relativeLayout;
    AnimationDrawable animationDrawable;
    RelativeLayout relativeLayout1;
    ParrallaxPageTransformer parrallaxPageTransformer;
    Button skip, bt_next, bt_launch;
    int[] colors;
    int[] colors2;
    public static int currentcolor = Color.WHITE;
    public static int currentcolor2 = Color.WHITE;
    SharedPreferences sharedPreferences;
    public static float rotation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lander_activity_layout);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
//        }

        viewPager = findViewById(R.id.view_pager);
        relativeLayout = findViewById(R.id.root_layout);
        colors = new int[]{ContextCompat.getColor(OnBoardingActivity.this, R.color.light_blue_400), ContextCompat.getColor(OnBoardingActivity.this, R.color.green_300), ContextCompat.getColor(OnBoardingActivity.this, R.color.orange_300), ContextCompat.getColor(OnBoardingActivity.this, R.color.pink_400), ContextCompat.getColor(OnBoardingActivity.this, R.color.purple_400),};
        colors2 = new int[]{ContextCompat.getColor(OnBoardingActivity.this, android.R.color.white), ContextCompat.getColor(OnBoardingActivity.this, R.color.orange_300), ContextCompat.getColor(OnBoardingActivity.this, R.color.pink_400), ContextCompat.getColor(OnBoardingActivity.this, R.color.purple_400), ContextCompat.getColor(OnBoardingActivity.this, R.color.light_blue_400),

        };
        relativeLayout1 = findViewById(R.id.bt_launch_layout);
        skip = findViewById(R.id.bt_skip);
        bt_next = findViewById(R.id.bt_next);
        bt_launch = findViewById(R.id.bt_launch_button);
        TextUtils.findText_and_applyTypeface(relativeLayout1, OnBoardingActivity.this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        viewPager.setAdapter(new landerAdapter(getSupportFragmentManager(), OnBoardingActivity.this));
//relativeLayout1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.geometry_2));
        parrallaxPageTransformer = new ParrallaxPageTransformer(R.id.card, R.id.on_board_image, R.id.on_board_title, R.id.on_board_image_circle, R.id.on_board_descriptiom);
//        parrallaxPageTransformer.setBorder(2);
//        parrallaxPageTransformer.setSpeed(0.2f);

        viewPager.setPageTransformer(false, parrallaxPageTransformer);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentcolor = ColorUtils.getHEaderColor(colors, position, positionOffset, OnBoardingActivity.this);
                currentcolor2 = ColorUtils.getHEaderColor(colors2, position, positionOffset, OnBoardingActivity.this);
                rotation = positionOffset;
                viewPager.setBackgroundColor(currentcolor);
//                bt_next.setBackgroundColor(currentcolor);
//                skip.setBackgroundColor(currentcolor);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(Constants.COLOUR_KEY, currentcolor2);
                editor.apply();

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 4) {
                    bt_next.setVisibility(View.GONE);
                    bt_launch.setVisibility(View.VISIBLE);
                    skip.setText(getString(R.string.Previous));

                } else {
                    bt_launch.setVisibility(View.GONE);
                    bt_next.setVisibility(View.VISIBLE);
                    skip.setText(getString(R.string.Skip));

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = viewPager.getCurrentItem();
                viewPager.setCurrentItem(currentItem + 1, true);

            }
        });
        bt_launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSharedPreferences();
                Intent intent = new Intent(OnBoardingActivity.this, MainActivity.class);
                startActivity(new Intent(OnBoardingActivity.this, MainActivity.class));

            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == 4) {
                    viewPager.setCurrentItem(0, true);
                } else {
                    updateSharedPreferences();
                    startActivity(new Intent(OnBoardingActivity.this, MainActivity.class));

                }

            }
        });
    }

    private void updateSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.IS_USER_SAW_INRODUCTION, true);
        editor.apply();
    }


    private class landerAdapter extends FragmentStatePagerAdapter {
        android.support.v4.app.FragmentManager fragmentManager;
        Context context;
        String[] titles;
        int[] images = {R.mipmap.ic_launcher, R.drawable.ic_collection, R.drawable.ic_canvas, R.drawable.ic_font_size, R.drawable.ic_infinite};

        String[] descriptions;

        public landerAdapter(android.support.v4.app.FragmentManager fm, Context context) {
            super(fm);
            this.fragmentManager = fragmentManager;
            this.context = context;
            titles = context.getResources().getStringArray(R.array.lander_titles);
            descriptions = context.getResources().getStringArray(R.array.lander_descriptions);
        }

        @Override
        public int getCount() {
            return 5;
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

    public static class OnBoardFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        String title;
        String desciption;
        int imageresource;
        RelativeLayout image;
        AnimationDrawable animationDrawable;
        RelativeLayout linearLayout;
        public ImageView imageView;
        SharedPreferences sharedPreferences;

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
            image = view.findViewById(R.id.card);
            imageView = view.findViewById(R.id.on_board_image_circle);
            linearLayout = view.findViewById(R.id.linear_bg_layout);
            TextUtils.findText_and_applyTypeface(linearLayout, getActivity());
            animationDrawable = (AnimationDrawable) linearLayout.getBackground();
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);
//            imageView.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scaleup));
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

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(Constants.COLOUR_KEY)) {
                imageView.getDrawable().setColorFilter(sharedPreferences.getInt(Constants.COLOUR_KEY, Color.WHITE), PorterDuff.Mode.SRC_IN);
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        }

        @Override
        public void onStop() {
            super.onStop();
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);

        }
    }
}
