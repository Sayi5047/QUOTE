package io.hustler.qtzy.ui.activities;

import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.apiRequestLauncher.Constants;
import io.hustler.qtzy.ui.fragments.CategoriesFragment;
import io.hustler.qtzy.ui.fragments.Categoris_wallpaper_fragment;
import io.hustler.qtzy.ui.utils.TextUtils;

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
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.WHITE));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();

        Integer val = getIntent().getIntExtra(Constants.INTENT_SECONDACTIVITY_CONSTANT, 0);
        TextUtils.setFont(this, headerNae, Constants.FONT_CIRCULAR);
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (val) {
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

    protected void setCollapsingToolbar(String title) {
        setSupportActionBar(toolbar);
        toolbar.setTitle(title);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    protected void setCollapsingToolbar(final String title, final CollapsingToolbarLayout ctl) {
        setSupportActionBar(toolbar);

        final int color1 = TextUtils.getMatColor(this, "mdcolor_500");
        final int color2 = TextUtils.getMatColor(this, "mdcolor_500");
        Log.d("VERTICALOFFSERTcolor1", String.valueOf(color1));
        Log.d("VERTICALOFFSERTcolor2", String.valueOf(color2));
        int high = color2;
        int low = color1;
        if (color1 > color2) {
            high = color1;
            low = color2;
        }
        int[] colors = {color1, color2};
        final GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        gradientDrawable.setGradientRadius(90);
        ctl.setBackground(gradientDrawable);
        getWindow().setStatusBarColor(color1);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        TextUtils.setFont_For_Ctl(ctl, SecondActivity.this, title);
        if (ColorUtils.calculateLuminance(color2) > 0.5) {
            ctl.setExpandedTitleColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            ctl.setCollapsedTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

            headerNae.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            ;
        } else {
            ctl.setExpandedTitleColor(ContextCompat.getColor(getApplicationContext(), R.color.WHITE));
            ctl.setCollapsedTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.WHITE));
            headerNae.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.WHITE));

        }
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
//                    ctl.setBackgroundColor(color2);
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
