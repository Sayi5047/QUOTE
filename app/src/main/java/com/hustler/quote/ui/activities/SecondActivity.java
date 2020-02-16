package com.hustler.quote.ui.activities;

import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hustler.quote.ui.apiRequestLauncher.Constants;

import java.text.DecimalFormat;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.hustler.quote.R;

import com.hustler.quote.ui.fragments.CategoriesFragment;
import com.hustler.quote.ui.fragments.Categoris_wallpaper_fragment;
import com.hustler.quote.ui.utils.TextUtils;

public class SecondActivity extends AppCompatActivity {
    @BindView(R.id.root)
    FrameLayout frameLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private FragmentManager fragmentManager;
    @BindView(R.id.header_name)
    TextView headerNae;

    @BindView(R.id.ctl)
    CollapsingToolbarLayout ctl;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_rect));
            getWindow().setClipToOutline(true);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();

        TextUtils.setFont(this, headerNae, Constants.FONT_CIRCULAR);
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (getIntent().getIntExtra(Constants.INTENT_SECONDACTIVITY_CONSTANT, 0)) {
            case 1:
                fragmentTransaction.replace(R.id.root, new CategoriesFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                headerNae.setText(getString(R.string.categories));
                setCollapsingToolbar(headerNae.getText().toString(), ctl);

                break;
            case 2:
                fragmentTransaction.replace(R.id.root, new Categoris_wallpaper_fragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                headerNae.setText(getString(R.string.collections));
                setCollapsingToolbar(headerNae.getText().toString(), ctl);

                break;

        }
    }


    protected void setCollapsingToolbar(final String title, final CollapsingToolbarLayout ctl) {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getApplicationContext(), (R.drawable.ic_keyboard_backspace_white_24dp)));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final int color1 = TextUtils.getMatColor(this, "mdcolor_800");
        final int color2 = TextUtils.getMatColor(this, "mdcolor_800");
        int[] colors = {color1, color2};
        final GradientDrawable gradientDrawable;
        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        gradientDrawable.setGradientRadius(135);
        getWindow().setStatusBarColor(color1);
        ctl.setBackground(gradientDrawable);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        TextUtils.setFont_For_Ctl(ctl, SecondActivity.this, title);
        if (ColorUtils.calculateLuminance(color2) > 0.5) {
            ctl.setExpandedTitleColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            ctl.setCollapsedTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            headerNae.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            Objects.requireNonNull(toolbar.getNavigationIcon()).setTint(getResources().getColor(R.color.colorAccent));

        } else {
            ctl.setExpandedTitleColor(ContextCompat.getColor(getApplicationContext(), R.color.WHITE));
            ctl.setCollapsedTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.WHITE));
            headerNae.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.WHITE));
            Objects.requireNonNull(toolbar.getNavigationIcon()).setTint(getResources().getColor(R.color.WHITE));


        }
        ctl.setCollapsedTitleTypeface(Typeface.createFromAsset(this.getResources().getAssets(), Constants.FONT_CIRCULAR));
        ctl.setExpandedTitleTypeface(Typeface.createFromAsset(this.getResources().getAssets(), Constants.FONT_CIRCULAR));
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollrange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollrange == -1) {
                    scrollrange = appBarLayout.getTotalScrollRange();
                }
                Float val = Math.abs(Float.valueOf(new DecimalFormat("0.00").format((float) verticalOffset / scrollrange)));
                int color = ColorUtils.blendARGB(color1, color2, val);
                getWindow().setStatusBarColor(color);
                if (val < 0.65) {
                    ctl.setBackgroundColor(color);
                } else {
                    ctl.setBackground(gradientDrawable);
                }
                ctl.setBackground(gradientDrawable);
                if (scrollrange + verticalOffset == 0) {
                    ctl.setTitleEnabled(false);
                    headerNae.setVisibility(View.VISIBLE);
                    ctl.setBackgroundColor(color);

                } else {

                    ctl.setTitleEnabled(true);
                    headerNae.setVisibility(View.GONE);


                }
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
