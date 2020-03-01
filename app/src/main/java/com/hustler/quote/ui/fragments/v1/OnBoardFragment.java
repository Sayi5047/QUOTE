package com.hustler.quote.ui.fragments.v1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import com.hustler.quote.ui.customviews.ParallaxViewsPagerTransformer;
import com.hustler.quote.ui.utils.ColorUtils;
import com.hustler.quote.ui.utils.TextUtils;

import java.util.Objects;

public class OnBoardFragment extends Fragment implements View.OnClickListener {
    private ViewPager mPager;
    private LinearLayout rootView;

    private Button mLogin;
    private Button mSignUp;
    private int[] colors;
    private int[] colors2;
    private static int currentColor = Color.WHITE;
    private static int currentColor2 = Color.WHITE;
    private SharedPreferences sharedPreferences;
    private Button continueButton;

    public OnBoardFragment() {
    }

    public static com.hustler.quote.ui.fragments.v1.OnBoardFragment getInstance() {
        return new com.hustler.quote.ui.fragments.v1.OnBoardFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intro_fragment_layout, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        findViews(view);
        setUpPager();
        setupFonts(rootView);
        setOnClickListeners();
        return view;
    }

    private void findViews(View view) {
        rootView = view.findViewById(R.id.root);
        mPager = view.findViewById(R.id.m_pager);
        mLogin = view.findViewById(R.id.mLogin);
        mSignUp = view.findViewById(R.id.mSignup);
        continueButton = view.findViewById(R.id.continueBtn);
    }

    private void setupFonts(LinearLayout rootView) {
        TextUtils.findText_and_applyTypeface(rootView, Objects.requireNonNull(getActivity()));
        TextUtils.findText_and_applyamim_slideup(rootView, getActivity());
    }

    private void setOnClickListeners() {
        mLogin.setOnClickListener(this);
        mSignUp.setOnClickListener(this);
        continueButton.setOnClickListener(view1 -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constants.IS_USER_SAW_INRODUCTION, true);
            editor.apply();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("IS_LOGIN", true);
            startActivity(intent);
        });
    }

    private void setUpPager() {
        colors = new int[]{ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.light_blue_400), ContextCompat.getColor(getActivity(), R.color.green_300), ContextCompat.getColor(getActivity(), R.color.orange_300), ContextCompat.getColor(getActivity(), R.color.pink_400), ContextCompat.getColor(getActivity(), R.color.purple_400),};
        colors2 = new int[]{ContextCompat.getColor(getActivity(), android.R.color.white), ContextCompat.getColor(getActivity(), R.color.orange_300), ContextCompat.getColor(getActivity(), R.color.pink_400), ContextCompat.getColor(getActivity(), R.color.purple_400), ContextCompat.getColor(getActivity(), R.color.light_blue_400),};

        mPager.setAdapter(new landerAdapter(getActivity().getSupportFragmentManager(), getActivity()));
        ParallaxViewsPagerTransformer parallaxViewsPagerTransformer = new ParallaxViewsPagerTransformer(R.id.card, R.id.on_board_image, R.id.on_board_title, R.id.on_board_image_circle, R.id.on_board_descriptiom);

        mPager.setPageTransformer(false, parallaxViewsPagerTransformer);
        mPager.setCurrentItem(0);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentColor = ColorUtils.getHEaderColor(colors, position, positionOffset, getActivity());
                currentColor2 = ColorUtils.getHEaderColor(colors2, position, positionOffset, getActivity());
                Objects.requireNonNull(getActivity()).getWindow().setStatusBarColor(currentColor);
                if (position <= 2) {
                    mPager.getChildAt(position).setBackgroundColor(currentColor);
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(Constants.COLOUR_KEY, currentColor2);
                editor.putInt(Constants.PREVIOUS_COLOR, currentColor);
                editor.apply();
                Objects.requireNonNull(getActivity()).getWindow().setStatusBarColor(currentColor);
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
    }

    @Override
    public void onClick(View v) {
        if (v == mLogin) {
            startActivity(new Intent(getActivity(), LoginActivity.class).putExtra(getString(R.string.INTENT_IS_LOGIN_KEY), true));
        } else if (v == mSignUp) {
            startActivity(new Intent(getActivity(), LoginActivity.class).putExtra(getString(R.string.INTENT_IS_LOGIN_KEY), false));
        }
    }


    private class landerAdapter extends FragmentStatePagerAdapter {
        Context context;
        String[] titles;
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
            OnBoardFragmentv2 onBoardFragmentv2 = new OnBoardFragmentv2();
            onBoardFragmentv2.title = titles[position];
            onBoardFragmentv2.imageResource = images[position];
            onBoardFragmentv2.description = descriptions[position];
            return onBoardFragmentv2;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }
    }

    public static class OnBoardFragmentv2 extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        String title;
        String description;
        int imageResource;
        RelativeLayout image;
        RelativeLayout linearLayout;
        public ImageView imageView;
        SharedPreferences sharedPreferences;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View view = inflater.inflate(R.layout.onboard_layout, container, false);
            if (imageResource != 0) {
                ((ImageView) view.findViewById(R.id.on_board_image)).setImageResource(imageResource);
                ((ImageView) view.findViewById(R.id.bg_image)).setImageResource(imageResource);
            }
            if (title != null && !title.equals("")) {
                ((TextView) view.findViewById(R.id.on_board_title)).setText(title);
            }
            if (description != null && !description.equals("")) {
                ((TextView) view.findViewById(R.id.on_board_descriptiom)).setText(description);
            }
            image = view.findViewById(R.id.card);
            imageView = view.findViewById(R.id.on_board_image_circle);
            linearLayout = view.findViewById(R.id.linear_bg_layout);
            TextUtils.findText_and_applyTypeface(linearLayout, Objects.requireNonNull(getActivity()));
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);
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
