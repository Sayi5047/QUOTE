package com.hustler.quote.ui.fragments.v1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.hustler.quote.R;
import com.hustler.quote.ui.activities.LoginActivity;
import com.hustler.quote.ui.activities.MainActivity;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.customviews.PagerTransformer;
import com.hustler.quote.ui.customviews.ParallaxViewsPagerTransformer;
import com.hustler.quote.ui.customviews.ScalePagerTransformer;
import com.hustler.quote.ui.utils.ColorUtils;
import com.hustler.quote.ui.utils.TextUtils;

import java.util.Timer;

public class IntroFragment extends Fragment implements View.OnClickListener {
    private LinearLayout rootView;
    private TextView textViewHead;
    private ImageView icon;
    private ViewPager mPager;
    private Button mLogin;
    private Button mSignup;
    ParallaxViewsPagerTransformer parallaxViewsPagerTransformer;
    int[] colors;
    int[] colors2;
    public static int currentcolor = Color.WHITE;
    public static int currentcolor2 = Color.WHITE;
    SharedPreferences sharedPreferences;
    Button continueButton;
    private float rotation;

    int current_page = 0;
    int totalNumberOfpagesOriginal = 25;
    int MULTIPLIER = 25;
    int numberInOneSlides = totalNumberOfpagesOriginal * MULTIPLIER;
    Timer pagerTimer;

    public static IntroFragment getInstance() {
        return new IntroFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intro_fragment_layout, container, false);
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        rootView = view.findViewById(R.id.root);
        textViewHead = view.findViewById(R.id.textView_head);
        icon = view.findViewById(R.id.icon);
        mPager = view.findViewById(R.id.m_pager);
        mLogin = view.findViewById(R.id.mLogin);
        mSignup = view.findViewById(R.id.mSignup);
        continueButton = view.findViewById(R.id.continueBtn);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        TextUtils.findText_and_applyTypeface(rootView, getActivity());
        TextUtils.findText_and_applyamim_slideup(rootView, getActivity());

        mLogin.setOnClickListener(this);
        mSignup.setOnClickListener(this);

        colors = new int[]{ContextCompat.getColor(getActivity(), R.color.light_blue_400), ContextCompat.getColor(getActivity(), R.color.green_300), ContextCompat.getColor(getActivity(), R.color.orange_300), ContextCompat.getColor(getActivity(), R.color.pink_400), ContextCompat.getColor(getActivity(), R.color.purple_400),};
        colors2 = new int[]{ContextCompat.getColor(getActivity(), android.R.color.white), ContextCompat.getColor(getActivity(), R.color.orange_300), ContextCompat.getColor(getActivity(), R.color.pink_400), ContextCompat.getColor(getActivity(), R.color.purple_400), ContextCompat.getColor(getActivity(), R.color.light_blue_400),};

        mPager.setAdapter(new landerAdapter(getActivity().getSupportFragmentManager(), getActivity()));
        parallaxViewsPagerTransformer = new ParallaxViewsPagerTransformer(R.id.card, R.id.on_board_image, R.id.on_board_title, R.id.on_board_image_circle, R.id.on_board_descriptiom);
        ScalePagerTransformer scalePagerTransformer = new ScalePagerTransformer(R.id.icon);
        PagerTransformer transformer = new PagerTransformer(R.id.root_container);

        mPager.setPageTransformer(false, parallaxViewsPagerTransformer);
        mPager.setCurrentItem(0);
        MULTIPLIER = totalNumberOfpagesOriginal = 4;
        getActivity().getWindow().setStatusBarColor(colors[0]);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentcolor = ColorUtils.getHEaderColor(colors, position, positionOffset, getActivity());
                currentcolor2 = ColorUtils.getHEaderColor(colors2, position, positionOffset, getActivity());
                getActivity().getWindow().setStatusBarColor(currentcolor);
//                rotation = positionOffset;
                if (position <= 2) {
                    mPager.getChildAt(position).setBackgroundColor(currentcolor);
                }
//                bt_next.setBackgroundColor(currentcolor);
//                skip.setBackgroundColor(currentcolor);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(Constants.COLOUR_KEY, currentcolor2);
                editor.putInt(Constants.PREVIOUS_COLOR, currentcolor);
                editor.apply();

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 4) {
                    continueButton.setVisibility(View.VISIBLE);

                } else {
                    continueButton.setVisibility(View.GONE);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Constants.IS_USER_SAW_INRODUCTION, true);
                editor.commit();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("IS_LOGIN", true);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mLogin) {
            // Handle clicks for mLogin
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("IS_LOGIN", true);
            startActivity(intent);
        } else if (v == mSignup) {
            // Handle clicks for mSignup
            Intent intent = new Intent(getActivity(), LoginActivity.class);

            intent.putExtra("IS_LOGIN", false);
            startActivity(intent);

        }
    }


    private class landerAdapter extends FragmentStatePagerAdapter {
        FragmentManager fragmentManager;
        Context context;
        String[] titles;
        @NonNull
        int[] images2 = {R.mipmap.ic_launcher, R.drawable.ic_collection, R.drawable.ic_canvas, R.drawable.ic_font_size, R.drawable.ic_infinite};
        int[] images = {R.drawable.ic_welcome_quotzy, R.drawable.ic_welcome_2, R.drawable.ic_welcome_3, R.drawable.ic_welcome_4, R.drawable.ic_welcome_5};

        String[] descriptions;

        landerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
            titles = context.getResources().getStringArray(R.array.lander_titles);
            descriptions = context.getResources().getStringArray(R.array.lander_descriptions);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return getOnBoardFragment(position);
        }

        @NonNull
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
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View view = inflater.inflate(R.layout.onboard_layout, container, false);
            if (imageresource != 0) {
                ((ImageView) view.findViewById(R.id.on_board_image)).setImageResource(imageresource);
                ((ImageView) view.findViewById(R.id.bg_image)).setImageResource(imageresource);
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
//            TextUtils.findText_and_applycolor(linearLayout, getActivity(), null);
//            animationDrawable = (AnimationDrawable) linearLayout.getBackground();
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

            sharedPreferences.registerOnSharedPreferenceChangeListener(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();

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
        public void onSharedPreferenceChanged(@NonNull SharedPreferences sharedPreferences, @NonNull String key) {
            if (key.equals(Constants.COLOUR_KEY)) {
                imageView.getDrawable().setColorFilter(sharedPreferences.getInt(Constants.COLOUR_KEY, Color.WHITE), PorterDuff.Mode.SRC_IN);
                image.getBackground().setColorFilter(sharedPreferences.getInt(Constants.PREVIOUS_COLOR, Color.WHITE), PorterDuff.Mode.SRC_IN);
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
