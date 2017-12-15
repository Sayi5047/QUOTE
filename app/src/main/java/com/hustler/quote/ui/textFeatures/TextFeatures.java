package com.hustler.quote.ui.textFeatures;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.adapters.ColorsAdapter;
import com.hustler.quote.ui.utils.TextUtils;

/**
 * Created by anvaya5 on 15/12/2017.
 */

public class TextFeatures {

    public static void apply_Text_Shadow(final Activity activity, final TextView selectedTextView) {

        final Dialog dialog = new Dialog(activity, R.style.EditTextDialog);
        dialog.setContentView(R.layout.shadow_text_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        TextView tvHead;
        final TextView demoShadowText;
        AppCompatSeekBar opacitySeekbar;
        TextView tvOpacity;
        AppCompatSeekBar shadowRadiusSeekbar;
        TextView tvShadowRadius;
        AppCompatSeekBar posXSeekbar;
        TextView tvXPos;
        AppCompatSeekBar posYSeekbar;
        TextView tvYPos;
        TextView tvShadowColor;
        RecyclerView rvShadowColor;
        Button btShadowClose;
        Button btShadowApply;
        LinearLayout root;

        root = (LinearLayout) dialog.findViewById(R.id.root);
        /*VARIABLES FOR SHADOW*/
        final float[] radius = {checkFornull(selectedTextView.getShadowRadius(), 2)};
        final float[] x = { checkFornull(selectedTextView.getShadowDx(), 1)};
        final float[] y = { checkFornull(selectedTextView.getShadowDy(), 1)};
        final int[] shadow_color = {(int) checkFornull((float) selectedTextView.getShadowColor(), activity.getResources().getColor(R.color.black_overlay))};
        final float[] opacity = {checkFornull(selectedTextView.getAlpha(), 1.0f)};

        tvHead = (TextView) dialog.findViewById(R.id.tv_head);
        demoShadowText = (TextView) dialog.findViewById(R.id.demo_shadow_text);
        tvOpacity = (TextView) dialog.findViewById(R.id.tv_opacity);
        tvShadowRadius = (TextView) dialog.findViewById(R.id.tv_shadow_radius);
        tvXPos = (TextView) dialog.findViewById(R.id.tv_x_pos);
        tvYPos = (TextView) dialog.findViewById(R.id.tv_y_pos);
        tvShadowColor = (TextView) dialog.findViewById(R.id.tv_shadow_color);

        opacitySeekbar = (AppCompatSeekBar) dialog.findViewById(R.id.opacity_seekbar);
        shadowRadiusSeekbar = (AppCompatSeekBar) dialog.findViewById(R.id.shadow_radius_seekbar);
        posXSeekbar = (AppCompatSeekBar) dialog.findViewById(R.id.pos_x_seekbar);
        posYSeekbar = (AppCompatSeekBar) dialog.findViewById(R.id.pos_y_seekbar);

        rvShadowColor = (RecyclerView) dialog.findViewById(R.id.rv_shadow_color);
        rvShadowColor.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));


        btShadowClose = (Button) dialog.findViewById(R.id.bt_shadow_close);
        btShadowApply = (Button) dialog.findViewById(R.id.bt_shadow_apply);
        demoShadowText.setTextColor(selectedTextView.getTextColors());

        /*Applying font to all TextViews and Edittexts*/
        TextUtils.findText_and_applyTypeface(root, activity);
        if(selectedTextView.getShadowRadius()>0){
            TextUtils.applyTextShadow(demoShadowText, radius[0], x[0], y[0], shadow_color[0]);
        }
        opacitySeekbar.setProgress((int)radius[0]);
        shadowRadiusSeekbar.setProgress((int)radius[0]);
        posXSeekbar.setProgress((int)x[0]);
        posYSeekbar.setProgress((int)y[0]);

        /*SEEKBAR LISTNERS*/
        opacitySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /*alpha applies between 0 to 1*/
                opacity[0] =  (progress * 0.1f);
                demoShadowText.setAlpha(opacity[0]);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        shadowRadiusSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 radius[0] = progress;
                TextUtils.applyTextShadow(demoShadowText, progress, x[0], y[0], shadow_color[0]);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        posXSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 x[0] = progress;
                TextUtils.applyTextShadow(demoShadowText, radius[0], progress, y[0], shadow_color[0]);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        posYSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 y[0] = progress;
                TextUtils.applyTextShadow(demoShadowText, radius[0], x[0], progress, shadow_color[0]);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /*SETTING COLOR OF RECYCLERVIEW*/
        ColorsAdapter colorsAdapter = new ColorsAdapter(activity, new ColorsAdapter.OnColorClickListener() {
            @Override
            public void onColorClick(int color) {
                shadow_color[0] = color;
                TextUtils.applyTextShadow(demoShadowText, radius[0], x[0], y[0], shadow_color[0]);

            }
        });

        rvShadowColor.setAdapter(colorsAdapter);
        /*BUTTON LISTNERS*/
        btShadowClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int radius = 0;
                final int x = 0;
                final int y = 0;
                final int[] shadow_color = {0};
                final float[] opacity = {0};
                dialog.dismiss();
            }
        });
        btShadowApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTextView.setAlpha(opacity[0]);
                TextUtils.applyTextShadow(selectedTextView, radius[0], x[0], y[0], shadow_color[0]);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static float checkFornull(float val, float default_Val) {
        if (val > 0) {
            return val;
        } else {
            return default_Val;
        }
    }

    public static float checkforopacitynull(float val, float default_Val) {
        if (val <= 1) {
            return val;
        } else {
            return default_Val;
        }
    }

}
