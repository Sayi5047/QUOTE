package com.hustler.quote.ui.activities;

import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hustler.quote.ui.apiRequestLauncher.Constants;

import java.text.DecimalFormat;
import java.util.Objects;

import com.hustler.quote.R;
import com.hustler.quote.ui.utils.TextUtils;

public class CreditsActivity extends AppCompatActivity {
    Toolbar toolbar;
    CollapsingToolbarLayout ctl;
    AppBarLayout appBarLayout;
    TextView header_name;
    RelativeLayout root;
    Button bt_cose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        toolbar = findViewById(R.id.toolbar2);
        ctl = findViewById(R.id.ctl);
        appBarLayout = findViewById(R.id.app_bar);
        header_name = findViewById(R.id.header_name);

        root = findViewById(R.id.root_Rl);
        bt_cose = findViewById(R.id.bt_close);
        setDailogCollapsinngToolBar(ctl, toolbar, header_name, appBarLayout);

        TextUtils.findText_and_applyTypeface(root, CreditsActivity.this);
        TextUtils.findText_and_applyamim_slideup(root, CreditsActivity.this);

    }


    protected void setDailogCollapsinngToolBar(final CollapsingToolbarLayout ctl, Toolbar toolbar, final TextView header_name, AppBarLayout appBarLayout) {
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
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
        Objects.requireNonNull(getWindow()).setStatusBarColor(color1);
        ctl.setBackground(gradientDrawable);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        TextUtils.setFont_For_Ctl(ctl, CreditsActivity.this, "Credits");
        if (ColorUtils.calculateLuminance(color2) > 0.5) {
            ctl.setExpandedTitleColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            ctl.setCollapsedTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            header_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            Objects.requireNonNull(toolbar.getNavigationIcon()).setTint(getResources().getColor(R.color.colorAccent));

        } else {
            ctl.setExpandedTitleColor(ContextCompat.getColor(getApplicationContext(), R.color.WHITE));
            ctl.setCollapsedTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.WHITE));
            header_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.WHITE));
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
                    header_name.setVisibility(View.VISIBLE);
                    ctl.setBackgroundColor(color);

                } else {

                    ctl.setTitleEnabled(true);
                    header_name.setVisibility(View.GONE);


                }
            }
        });
    }

}
