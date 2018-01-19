package com.hustler.quote.ui.textFeatures;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Environment;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.hustler.quote.R;
import com.hustler.quote.ui.activities.EditorActivity;
import com.hustler.quote.ui.adapters.ColorsAdapter;
import com.hustler.quote.ui.adapters.DownloadedFontAdapter;
import com.hustler.quote.ui.adapters.LocalFontAdapter;
import com.hustler.quote.ui.adapters.SymbolFontAdapter;
import com.hustler.quote.ui.pojo.FontSelected;
import com.hustler.quote.ui.utils.AdUtils;
import com.hustler.quote.ui.utils.AnimUtils;
import com.hustler.quote.ui.utils.TextUtils;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

import java.io.File;
import java.io.FilenameFilter;

import static android.view.View.GONE;

/**
 * Created by anvaya5 on 15/12/2017.
 */

public class TextFeatures {


    public static void apply_Text_Shadow(final Activity activity, final TextView selectedTextView) {

        final Dialog dialog = new Dialog(activity, R.style.EditTextDialog);
        dialog.setContentView(R.layout.shadow_text_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        AdView adView;
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
        final float[] x = {checkFornull(selectedTextView.getShadowDx(), 1)};
        final float[] y = {checkFornull(selectedTextView.getShadowDy(), 1)};
        final int[] shadow_color = {(int) checkFornull((float) selectedTextView.getShadowColor(), activity.getResources().getColor(R.color.black_overlay))};
        final float[] opacity = {checkFornull(selectedTextView.getAlpha(), 1.0f)};

        tvHead = (TextView) dialog.findViewById(R.id.tv_head);
        demoShadowText = (TextView) dialog.findViewById(R.id.demo_shadow_text);
        tvOpacity = (TextView) dialog.findViewById(R.id.tv_opacity);
        tvShadowRadius = (TextView) dialog.findViewById(R.id.tv_shadow_radius);
        tvXPos = (TextView) dialog.findViewById(R.id.tv_x_pos);
        tvYPos = (TextView) dialog.findViewById(R.id.tv_y_pos);
        tvShadowColor = (TextView) dialog.findViewById(R.id.tv_shadow_color);
        adView = (AdView) dialog.findViewById(R.id.adView);
        opacitySeekbar = (AppCompatSeekBar) dialog.findViewById(R.id.opacity_seekbar);
        shadowRadiusSeekbar = (AppCompatSeekBar) dialog.findViewById(R.id.shadow_radius_seekbar);
        posXSeekbar = (AppCompatSeekBar) dialog.findViewById(R.id.pos_x_seekbar);
        posYSeekbar = (AppCompatSeekBar) dialog.findViewById(R.id.pos_y_seekbar);
        AdUtils.loadBannerAd(adView, activity);
        rvShadowColor = (RecyclerView) dialog.findViewById(R.id.rv_shadow_color);
        rvShadowColor.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));


        btShadowClose = (Button) dialog.findViewById(R.id.bt_shadow_close);
        btShadowApply = (Button) dialog.findViewById(R.id.bt_shadow_apply);
        demoShadowText.setTextColor(selectedTextView.getTextColors());

        /*Applying font to all TextViews and Edittexts*/
        TextUtils.findText_and_applyTypeface(root, activity);
        if (selectedTextView.getShadowRadius() > 0) {
            TextUtils.applyTextShadow(demoShadowText, radius[0], x[0], y[0], shadow_color[0]);
        }
        opacitySeekbar.setProgress((int) radius[0]);
        shadowRadiusSeekbar.setProgress((int) radius[0]);
        posXSeekbar.setProgress((int) x[0]);
        posYSeekbar.setProgress((int) y[0]);

        /*SEEKBAR LISTNERS*/
        opacitySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /*alpha applies between 0 to 1*/
                opacity[0] = (progress * 0.1f);
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

    //  METHOD TO APLLY FONT
    public static FontSelected apply_font(final EditorActivity editorActivity, final TextView selectedTextView) {
        final Dialog dialog = new Dialog(editorActivity, R.style.EditTextDialog);
        dialog.setContentView(R.layout.apply_font_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        AdView adView;

        LinearLayout root;
        TextView tvAppFonts;
        final TextView demoText;
        RecyclerView rvAppFont;
        LinearLayout root2;
        TextView tvSymbolFonts;
        RecyclerView rvSymbolFont;
        LinearLayout root3;
        TextView tvDownloadedFonts;
        RecyclerView rvDownloadedFont;
        Button btShadowClose;
        Button btShadowApply;
        LocalFontAdapter localFontAdapter1;
        SymbolFontAdapter symbolFontAdapter;
        DownloadedFontAdapter downloadedFontAdapter;
        final Boolean[] isDownloaded = new Boolean[1];
        final FontSelected fontSelected = new FontSelected();

        final String[] selected_type_face = new String[1];


        root = (LinearLayout) dialog.findViewById(R.id.root);
        root2 = (LinearLayout) dialog.findViewById(R.id.root2);
        root3 = (LinearLayout) dialog.findViewById(R.id.root3);

        tvAppFonts = (TextView) dialog.findViewById(R.id.tv_app_fonts);
        demoText = (TextView) dialog.findViewById(R.id.tv_demo_text);
        tvSymbolFonts = (TextView) dialog.findViewById(R.id.tv_symbol_fonts);
        tvDownloadedFonts = (TextView) dialog.findViewById(R.id.tv_downloaded_fonts);
        adView = (AdView) dialog.findViewById(R.id.adView);
        AdUtils.loadBannerAd(adView, editorActivity);
        rvDownloadedFont = (RecyclerView) dialog.findViewById(R.id.rv_downloaded_font);
        rvAppFont = (RecyclerView) dialog.findViewById(R.id.rv_app_font);
        rvSymbolFont = (RecyclerView) dialog.findViewById(R.id.rv_symbol_font);

        btShadowClose = (Button) dialog.findViewById(R.id.bt_shadow_close);
        btShadowApply = (Button) dialog.findViewById(R.id.bt_shadow_apply);

        btShadowClose.setOnClickListener(editorActivity);
        btShadowApply.setOnClickListener(editorActivity);
        TextUtils.findText_and_applyTypeface(root, editorActivity);

        rvAppFont.setLayoutManager(new LinearLayoutManager(editorActivity, LinearLayoutManager.HORIZONTAL, false));
        rvSymbolFont.setLayoutManager(new LinearLayoutManager(editorActivity, LinearLayoutManager.HORIZONTAL, false));
        rvDownloadedFont.setLayoutManager(new LinearLayoutManager(editorActivity, LinearLayoutManager.HORIZONTAL, false));

        localFontAdapter1 = new LocalFontAdapter(false, editorActivity, getLocalFonts(editorActivity), new LocalFontAdapter.onFontClickListner() {
            @Override
            public void onFontClicked(String font, boolean isDownloadFont) {
                selected_type_face[0] = font;
                isDownloaded[0] = isDownloadFont;
                TextUtils.setFont(editorActivity, demoText, selected_type_face[0]);
                fontSelected.setFontname_path(selected_type_face[0]);
                fontSelected.setDownloaded(isDownloaded[0]);
            }
        });
        symbolFontAdapter = new SymbolFontAdapter(false, editorActivity, getSymbolFonts(editorActivity), new SymbolFontAdapter.onFontClickListner() {
            @Override
            public void onFontClicked(String font, boolean isDownloadFont) {
                selected_type_face[0] = font;
                isDownloaded[0] = isDownloadFont;
                TextUtils.setFont(editorActivity, demoText, selected_type_face[0]);
                fontSelected.setFontname_path(selected_type_face[0]);
                fontSelected.setDownloaded(isDownloaded[0]);

            }
        });
        downloadedFontAdapter = new DownloadedFontAdapter(true, editorActivity, getDownloadedFonts(editorActivity,
                new File(Environment.getExternalStorageDirectory() + File.separator + "Fonts")), new DownloadedFontAdapter.onFontClickListner() {
            @Override
            public void onFontClicked(String font, boolean isDownloadFont) {
                selected_type_face[0] = font;
                isDownloaded[0] = isDownloadFont;
                demoText.setTypeface(Typeface.createFromFile(selected_type_face[0]));
                fontSelected.setFontname_path(selected_type_face[0]);
                fontSelected.setDownloaded(isDownloaded[0]);
            }
        });

        rvAppFont.setAdapter(localFontAdapter1);
        rvSymbolFont.setAdapter(symbolFontAdapter);
        rvDownloadedFont.setAdapter(downloadedFontAdapter);

        btShadowApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selected_type_face[0] == null) {
                    Toast_Snack_Dialog_Utils.show_ShortToast(editorActivity, editorActivity.getString(R.string.select_font));
                } else {
                    if (isDownloaded[0]) {
                        selectedTextView.setTypeface(Typeface.createFromFile(selected_type_face[0]));
                    } else {
                        TextUtils.setFont(editorActivity, selectedTextView, selected_type_face[0]);
                    }
                }
                dialog.dismiss();


            }


        });


        btShadowClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
        return fontSelected;

    }

//    METHOD TO GET SYMBOL FONTS

//    public static FontSelected getSymbolFonts(final EditorActivity editorActivity,final TextView selectedTextView){
//        Dialog dialog=new Dialog(editorActivity,R.style.EditTextDialog);
//        dialog.setContentView(R.layout.activity_symbol_fonts);
//    }

    public static String[] getDownloadedFonts(Activity activity, File file) {
        String[] FilePathStrings = new String[0];
        String[] FileNameStrings = new String[0];
        File[] listFile, listFile0, listFile1;
        file.mkdir();
//        GET TTF FILES AND COLLECT IN LIST1
        if (file.isDirectory()) {
            listFile = file.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if (name.endsWith(".ttf")) {
                        return true;
                    } else if (name.endsWith(".otf")) {
                        return true;
                    } else if (name.endsWith(".TTF")) {
                        return true;
                    } else if (name.endsWith(".OTF")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });

//            GET OTF FILES AND COLLECT IN LIST 2
//            if (file.isDirectory()) {
//                listFile1 = file.listFiles(new FilenameFilter() {
//                    @Override
//                    public boolean accept(File dir, String name) {
//                        return name.endsWith(".otf");
//                    }
//                });
////                CONVERT THEM TO ARRAYLIST TO MERGE BOTH
//                List list = new ArrayList(Arrays.asList(listFile0));
//                listFile = new File[list.size()];
//                list.addAll(Arrays.asList(listFile1));
////                GET THEM BACK FILE ARRAYS
//                for (int i = 0; i < list.size(); i++) {
//                    listFile[i] = (File) list.get(i);
//                }
            // Create a String array for FilePathStrings
            FilePathStrings = new String[listFile.length];
            // Create a String array for FileNameStrings
            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {
                // Get the path of the image file
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                // Get the name image file
                FileNameStrings[i] = listFile[i].getName();
            }
        }
        return FilePathStrings;

    }


    private static String[] getSymbolFonts(Activity activity) {
        String[] localFonts = activity.getResources().getStringArray(R.array.allfonts);
        return localFonts;
    }

    private static String[] getLocalFonts(Activity activity) {
        String[] localFonts = activity.getResources().getStringArray(R.array.allfonts);
        return localFonts;
    }

    public static void setGradients(final EditorActivity editorActivity, final TextView selectedTextView) {
        final Dialog dialog = new Dialog(editorActivity, R.style.EditTextDialog);
        dialog.setContentView(R.layout.geadient_text_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setCancelable(false);

        AdView adView;
        LinearLayout root;
        final TextView gradientText;
        final TextView gradientPreviewText;
        TextView gradientTypeText;
        final RadioGroup rdGroup;
        final RadioButton rbJpeg;
        RadioButton rbPng;
        final Button preview;
        Button btCancel;
        Button btApply;
        final TextView demoGradient;
        final ImageView demoColor1;
        final ImageView demoColor2;
        final TextView demoColor1Tv;
        final TextView demoColor2Tv;
        final RecyclerView colorsRecycler;
        final ColorsAdapter colorsAdapter;
        final ColorsAdapter colorsAdapter2;
        final GradientDrawable[] output_drawable = new GradientDrawable[1];
        final Shader[] shader_gradient = new Shader[1];
        final int[] firstColor = {0};
        final int[] secondColor = {0};
        final int[] selected_color = {0};
        final int[] colors;

        root = (LinearLayout) dialog.findViewById(R.id.root);
        gradientText = (TextView) dialog.findViewById(R.id.gradient_text);
        gradientPreviewText = (TextView) dialog.findViewById(R.id.gradient_preview_text);
        gradientTypeText = (TextView) dialog.findViewById(R.id.gradient_type_text);
        rdGroup = (RadioGroup) dialog.findViewById(R.id.rd_group);
        rbJpeg = (RadioButton) dialog.findViewById(R.id.rb_jpeg);
        rbPng = (RadioButton) dialog.findViewById(R.id.rb_png);
        preview = (Button) dialog.findViewById(R.id.preview);
        adView = (AdView) dialog.findViewById(R.id.adView);
        btCancel = (Button) dialog.findViewById(R.id.bt_cancel);
        btApply = (Button) dialog.findViewById(R.id.bt_apply);
        demoGradient = (TextView) dialog.findViewById(R.id.demo_gradient);
        demoColor1 = (ImageView) dialog.findViewById(R.id.demo_color_1);
        demoColor2 = (ImageView) dialog.findViewById(R.id.demo_color_2);
        demoColor1Tv = (TextView) dialog.findViewById(R.id.demo_color_1_tv);
        demoColor2Tv = (TextView) dialog.findViewById(R.id.demo_color_2_tv);
        colorsRecycler = (RecyclerView) dialog.findViewById(R.id.colors_recycler);
        AdUtils.loadBannerAd(adView, editorActivity);
        dialog.setCancelable(false);
        colorsRecycler.setVisibility(GONE);
        preview.setVisibility(GONE);

        int[] colors2 = {Color.RED, Color.YELLOW};
        float[] position = {0, 1};
        LinearGradient lin_grad = new LinearGradient(0, 0, 0, 50, colors2, position, Shader.TileMode.MIRROR);
        shader_gradient[0] = lin_grad;
        gradientPreviewText.getPaint().setShader(shader_gradient[0]);

        colorsRecycler.setLayoutManager(new LinearLayoutManager(editorActivity, LinearLayoutManager.HORIZONTAL, false));
        colorsAdapter = new ColorsAdapter(editorActivity, new ColorsAdapter.OnColorClickListener() {
            @Override
            public void onColorClick(int color) {
                firstColor[0] = color;
                demoColor1.setBackgroundColor(color);
                demoColor1Tv.setTextColor(color);
                preview.setVisibility(View.VISIBLE);

            }
        });
        colorsAdapter2 = new ColorsAdapter(editorActivity, new ColorsAdapter.OnColorClickListener() {
            @Override
            public void onColorClick(int color) {
                secondColor[0] = color;
                demoColor2.setBackgroundColor(color);
                demoColor2Tv.setTextColor(color);
                preview.setVisibility(View.VISIBLE);


            }
        });

        demoColor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorsRecycler.setVisibility(View.VISIBLE);
                colorsRecycler.setAdapter(null);
                selected_color[0] = 0;
                colorsRecycler.setAdapter(colorsAdapter);
            }
        });

        demoColor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorsRecycler.setVisibility(View.VISIBLE);
                colorsRecycler.setAdapter(null);
                selected_color[0] = 0;
                colorsRecycler.setAdapter(colorsAdapter2);
            }
        });
        demoColor1Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorsRecycler.setVisibility(View.VISIBLE);
                colorsRecycler.setAdapter(null);
                selected_color[0] = 0;
                colorsRecycler.setAdapter(colorsAdapter);
            }
        });

        demoColor2Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorsRecycler.setVisibility(View.VISIBLE);
                colorsRecycler.setAdapter(null);
                selected_color[0] = 0;
                colorsRecycler.setAdapter(colorsAdapter2);
            }
        });

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdGroup.getCheckedRadioButtonId() == rbJpeg.getId()) {
                    int[] colors = {firstColor[0], secondColor[0]};
                    float[] position = {0, 1};
                    LinearGradient lin_grad = new LinearGradient(0, 0, 0, 50, colors, position, Shader.TileMode.MIRROR);
                    shader_gradient[0] = lin_grad;
                    gradientPreviewText.setText(null);
                    gradientPreviewText.setText("SOMETHING");
                    gradientPreviewText.getPaint().setShader(null);
//                    Bitmap bitmap= BitmapFactory.decodeResource(editorActivity.getResources(), AnimUtils.createDrawable(firstColor[0], secondColor[0], editorActivity));

                    ((TextView) dialog.findViewById(R.id.gradient_preview_text)).getPaint().setShader(shader_gradient[0]);
                    gradientText.setBackground(AnimUtils.createDrawable(firstColor[0], secondColor[0], editorActivity));

                } else {
                    int[] colors = {firstColor[0], secondColor[0]};
                    RadialGradient lin_grad = new RadialGradient(0, 3, 5, colors[0], colors[1], Shader.TileMode.REPEAT);
                    shader_gradient[0] = lin_grad;
                    ((TextView) dialog.findViewById(R.id.gradient_preview_text)).getPaint().setShader(shader_gradient[0]);
                    gradientText.setBackground(AnimUtils.createDrawable(firstColor[0], secondColor[0], editorActivity));

                }
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shader_gradient[0] != null) {
                    selectedTextView.getPaint().setShader(shader_gradient[0]);
                    dialog.dismiss();
                } else {
                    Toast_Snack_Dialog_Utils.show_ShortToast(editorActivity, editorActivity.getString(R.string.select_to_apply));
                }
            }
        });

        dialog.show();
    }
}
