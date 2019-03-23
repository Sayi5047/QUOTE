package io.hustler.qtzy.ui.fragments.v1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;

import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.Widgets.PagerTransformer;
import io.hustler.qtzy.ui.activities.LoginActivity;
import io.hustler.qtzy.ui.apiRequestLauncher.Constants;
import io.hustler.qtzy.ui.apiRequestLauncher.Restutility;
import io.hustler.qtzy.ui.customviews.MyViewPager;
import io.hustler.qtzy.ui.customviews.ParrallaxPageTransformer;
import io.hustler.qtzy.ui.pojo.UnsplashImages_Collection_Response;
import io.hustler.qtzy.ui.pojo.Unsplash_Image_collection_response_listener;
import io.hustler.qtzy.ui.utils.ColorUtils;
import io.hustler.qtzy.ui.utils.TextUtils;
import io.hustler.qtzy.ui.utils.Toast_Snack_Dialog_Utils;

public class SplashFragment extends Fragment implements View.OnClickListener {
    private LinearLayout rootView;
    private TextView textViewHead;
    private ImageView icon;
    private ViewPager mPager;
    private Button mLogin;
    private Button mSignup;
    ParrallaxPageTransformer parrallaxPageTransformer;
    int[] colors;
    int[] colors2;
    public static int currentcolor = Color.WHITE;
    public static int currentcolor2 = Color.WHITE;
    SharedPreferences sharedPreferences;
    private float rotation;

    int current_page = 0;
    int totalNumberOfpagesOriginal = 25;
    int MULTIPLIER = 25;
    int numberInOneSlides = totalNumberOfpagesOriginal * MULTIPLIER;
    Timer pagerTimer;

    public static SplashFragment getInstance() {
        return new SplashFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.v1_splash_fragment_layout, container, false);
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
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        TextUtils.findText_and_applyTypeface(rootView, getActivity());
        TextUtils.findText_and_applyamim_slideup(rootView, getActivity());

        mLogin.setOnClickListener(this);
        mSignup.setOnClickListener(this);

        colors = new int[]{ContextCompat.getColor(getActivity(), R.color.light_blue_400), ContextCompat.getColor(getActivity(), R.color.green_300), ContextCompat.getColor(getActivity(), R.color.orange_300), ContextCompat.getColor(getActivity(), R.color.pink_400), ContextCompat.getColor(getActivity(), R.color.purple_400),};
        colors2 = new int[]{ContextCompat.getColor(getActivity(), android.R.color.white), ContextCompat.getColor(getActivity(), R.color.orange_300), ContextCompat.getColor(getActivity(), R.color.pink_400), ContextCompat.getColor(getActivity(), R.color.purple_400), ContextCompat.getColor(getActivity(), R.color.light_blue_400),};

        mPager.setAdapter(new landerAdapter(getActivity().getSupportFragmentManager(), getActivity()));
        parrallaxPageTransformer = new ParrallaxPageTransformer(R.id.card, R.id.on_board_image, R.id.on_board_title, R.id.on_board_image_circle, R.id.on_board_descriptiom);
        PagerTransformer transformer = new PagerTransformer(R.id.root_container);

        mPager.setPageTransformer(false, transformer);
        mPager.setCurrentItem(0);
        mPager.setOffscreenPageLimit(2);
        MULTIPLIER = totalNumberOfpagesOriginal = 5;
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentcolor = ColorUtils.getHEaderColor(colors, position, positionOffset, getActivity());
                currentcolor2 = ColorUtils.getHEaderColor(colors2, position, positionOffset, getActivity());
//                rotation = positionOffset;
//                ((View)mPager.getChildAt(position)).setBackgroundColor(currentcolor);
//                bt_next.setBackgroundColor(currentcolor);
//                skip.setBackgroundColor(currentcolor);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(Constants.COLOUR_KEY, currentcolor2);
                editor.putInt(Constants.PREVIOUS_COLOR, currentcolor);
                editor.apply();

            }

            @Override
            public void onPageSelected(int position) {
//                if (position == 4) {
//                    bt_next.setVisibility(View.GONE);
//                    bt_launch.setVisibility(View.VISIBLE);
//                    skip.setText(getString(R.string.Previous));
//
//                } else {
//                    bt_launch.setVisibility(View.GONE);
//                    bt_next.setVisibility(View.VISIBLE);
//                    skip.setText(getString(R.string.Skip));
//
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
//            Intent intent = new Intent(getActivity(), LoginActivity.class);
//
//            intent.putExtra("IS_LOGIN", false);
//            startActivity(intent);
            new Restutility(getActivity()).testAuth(getActivity(), new Unsplash_Image_collection_response_listener() {
                @Override
                public void onSuccess(UnsplashImages_Collection_Response response) {
                    Toast_Snack_Dialog_Utils.show_LongToast(getActivity(), response.toString());
                }

                @Override
                public void onError(String error) {
                    Toast_Snack_Dialog_Utils.show_LongToast(getActivity(), error.toString());

                }
            }, "");

        }
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
//            TextUtils.findText_and_applycolor(linearLayout, getActivity(), null);
            animationDrawable = (AnimationDrawable) linearLayout.getBackground();
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
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
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
