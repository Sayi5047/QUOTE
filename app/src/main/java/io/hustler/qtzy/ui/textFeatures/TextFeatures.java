package io.hustler.qtzy.ui.textFeatures;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.EmbossMaskFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.provider.FontRequest;
import android.support.v4.provider.FontsContractCompat;
import android.support.v4.util.ArraySet;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;

import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.CustomSpan.HollowSpan;
import io.hustler.qtzy.ui.activities.EditorActivity;
import io.hustler.qtzy.ui.adapters.ColorsAdapter;
import io.hustler.qtzy.ui.adapters.DownloadedFontAdapter;
import io.hustler.qtzy.ui.adapters.GoogleFontsAdapter;
import io.hustler.qtzy.ui.adapters.LocalFontAdapter;
import io.hustler.qtzy.ui.pojo.FontSelected;
import io.hustler.qtzy.ui.pojo.QueryBuilder;
import io.hustler.qtzy.ui.utils.AdUtils;
import io.hustler.qtzy.ui.utils.AnimUtils;
import io.hustler.qtzy.ui.utils.InternetUtils;
import io.hustler.qtzy.ui.utils.TextUtils;
import io.hustler.qtzy.ui.utils.Toast_Snack_Dialog_Utils;

import static android.view.View.GONE;

/**
 * Created by Sayi on 15/12/2017.
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
public class TextFeatures {


    @Nullable
    private static Handler mHandler = null;
    @Nullable
    static Typeface downloadedTypeface = null;
    public static final String[] selected_type_face = new String[1];

    public static void apply_Text_Shadow(@NonNull final Activity activity, final TextView selectedTextView) {

        final Dialog dialog = new Dialog(activity, R.style.EditTextDialog);
        dialog.setContentView(R.layout.shadow_text_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);
        AdView adView;
        final TextView demoShadowText;
        AppCompatSeekBar opacitySeekbar;
        AppCompatSeekBar shadowRadiusSeekbar;
        AppCompatSeekBar posXSeekbar;
        AppCompatSeekBar posYSeekbar;
        RecyclerView rvShadowColor;
        Button btShadowClose;
        Button btShadowApply;
        LinearLayout root;

        root = dialog.findViewById(R.id.root);
        /*VARIABLES FOR SHADOW*/
        final float[] radius = {checkFornull(selectedTextView.getShadowRadius(), 2)};
        final float[] x = {checkFornull(selectedTextView.getShadowDx(), 1)};
        final float[] y = {checkFornull(selectedTextView.getShadowDy(), 1)};
        final int[] shadow_color = {(int) checkFornull((float) selectedTextView.getShadowColor(), activity.getResources().getColor(R.color.black_overlay))};
        final float[] opacity = {checkFornull(selectedTextView.getAlpha(), 1.0f)};

        demoShadowText = dialog.findViewById(R.id.demo_shadow_text);
        adView = dialog.findViewById(R.id.adView);
        opacitySeekbar = dialog.findViewById(R.id.opacity_seekbar);
        shadowRadiusSeekbar = dialog.findViewById(R.id.shadow_radius_seekbar);
        posXSeekbar = dialog.findViewById(R.id.pos_x_seekbar);
        posYSeekbar = dialog.findViewById(R.id.pos_y_seekbar);
        AdUtils.loadBannerAd(adView, activity);
        rvShadowColor = dialog.findViewById(R.id.rv_shadow_color);
        rvShadowColor.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));


        btShadowClose = dialog.findViewById(R.id.bt_shadow_close);
        btShadowApply = dialog.findViewById(R.id.bt_shadow_apply);
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
    @NonNull
    public static FontSelected apply_font(@NonNull final Activity editorActivity, @NonNull final TextView selectedTextView) {
        final Dialog dialog = new Dialog(editorActivity, R.style.EditTextDialog_non_floater);
        dialog.setContentView(R.layout.apply_font_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog_non_floater;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setStatusBarColor(ContextCompat.getColor(editorActivity, R.color.colorAccent));
        }
        AdView adView;

        ScrollView root;
        final TextView demoText;
        RecyclerView rvAppFont;
        RecyclerView rvDownloadedFont;
        RecyclerView rvGoogleFont;
        Button btShadowClose;
        final AutoCompleteTextView searchBox;
        Button btShadowApply;
        final ImageView searchButton;
        final ArraySet<String> familyNameSet;
        LocalFontAdapter localFontAdapter;
        DownloadedFontAdapter downloadedFontAdapter;
        GoogleFontsAdapter googleFontsAdapter;
        final int[] isDownloaded = new int[3];
        final FontSelected fontSelected = new FontSelected();


        root = dialog.findViewById(R.id.root);

        demoText = dialog.findViewById(R.id.tv_demo_text);
        adView = dialog.findViewById(R.id.adView);
        AdUtils.loadBannerAd(adView, editorActivity);
        rvGoogleFont = dialog.findViewById(R.id.rv_downloaded_font);
        rvAppFont = dialog.findViewById(R.id.rv_app_font);
        rvDownloadedFont = dialog.findViewById(R.id.rv_symbol_font);

        btShadowClose = dialog.findViewById(R.id.bt_shadow_close);
        btShadowApply = dialog.findViewById(R.id.bt_shadow_apply);
        searchButton = dialog.findViewById(R.id.search_button);
        searchBox = dialog.findViewById(R.id.search_box);
        familyNameSet = new ArraySet<>();
        familyNameSet.addAll(Arrays.asList(editorActivity.getResources().getStringArray(R.array.family_names)));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(editorActivity,
                android.R.layout.simple_dropdown_item_1line,
                editorActivity.getResources().getStringArray(R.array.family_names));
        searchBox.setAdapter(adapter);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String familyName = searchBox.getText().toString();
                if (checkFamilyValid(familyName)) {
                    if (InternetUtils.isConnectedtoNet(editorActivity)) {
                        requestDownload(editorActivity, familyName, demoText);
                    } else {
                        Toast_Snack_Dialog_Utils.show_ShortToast(editorActivity, editorActivity.getString(R.string.internet_required));
                    }
                } else {
                    searchBox.setError(editorActivity.getString(R.string.invalid_family_name));
                }

            }

            private boolean checkFamilyValid(@Nullable String familyName) {
                return (familyName != null && familyNameSet.contains(familyName));
            }
        });

        btShadowClose.setOnClickListener((View.OnClickListener) editorActivity);
        btShadowApply.setOnClickListener((View.OnClickListener) editorActivity);

        rvAppFont.setLayoutManager(new LinearLayoutManager(editorActivity, LinearLayoutManager.HORIZONTAL, false));
        rvDownloadedFont.setLayoutManager(new LinearLayoutManager(editorActivity, LinearLayoutManager.HORIZONTAL, false));
        rvGoogleFont.setLayoutManager(new LinearLayoutManager(editorActivity, LinearLayoutManager.HORIZONTAL, false));

        localFontAdapter = new LocalFontAdapter(editorActivity, getLocalFonts(editorActivity), new LocalFontAdapter.onFontClickListener() {
            @Override
            public void onFontClicked(String font, int isDownloadedFonts) {
                selected_type_face[0] = font;
                isDownloaded[0] = isDownloadedFonts;
                TextUtils.setFont(editorActivity, demoText, selected_type_face[0]);
                fontSelected.setFontname_path(selected_type_face[0]);
                fontSelected.setDownloaded(isDownloaded[0]);
            }
        });
        final String finalLOcation = Environment.getExternalStorageDirectory() + File.separator + editorActivity.getString(R.string.Quotzy) + File.separator + editorActivity.getString(R.string.Fonts);

        downloadedFontAdapter = new DownloadedFontAdapter(editorActivity, getDownloadedFonts(
                new File(finalLOcation)), new DownloadedFontAdapter.onFontClickListner() {
            @Override
            public void onFontClicked(String font, int isDownloadedFonts) {
                selected_type_face[0] = font;
                isDownloaded[1] = isDownloadedFonts;
                demoText.setTypeface(Typeface.createFromFile(font));
                fontSelected.setFontname_path(selected_type_face[0]);
                fontSelected.setDownloaded(isDownloaded[1]);

            }
        });

        String[] familyNames = editorActivity.getResources().getStringArray(R.array.family_names);
        googleFontsAdapter = new GoogleFontsAdapter(editorActivity, familyNames, new GoogleFontsAdapter.onFontClickListner() {
            @Override
            public void onFontClicked(@NonNull String font, int isDownloadFont) {
                selected_type_face[0] = font;
                isDownloaded[2] = isDownloadFont;
                if (InternetUtils.isConnectedtoNet(editorActivity)) {
                    requestDownload(editorActivity, font, demoText);
                } else {
                    Toast_Snack_Dialog_Utils.show_ShortToast(editorActivity, editorActivity.getString(R.string.internet_required));
                }
                fontSelected.setFontname_path(selected_type_face[0]);
                fontSelected.setDownloaded(isDownloaded[2]);
            }
        });

        rvAppFont.setAdapter(localFontAdapter);
        rvDownloadedFont.setAdapter(downloadedFontAdapter);
        rvGoogleFont.setAdapter(googleFontsAdapter);

        btShadowApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selected_type_face[0] == null) {
                    Toast_Snack_Dialog_Utils.show_ShortToast(editorActivity, editorActivity.getString(R.string.select_font));
                } else {
                    if (isDownloaded[0] == 1) {
                        if (selected_type_face[0].equalsIgnoreCase("fonts/zingcursive.otf") || selected_type_face[0].equalsIgnoreCase("fonts/regulaer2.otf")) {
                            TextUtils.setFont(editorActivity, selectedTextView, selected_type_face[0]);
                        }

                    } else if (isDownloaded[1] == 2) {
                        selectedTextView.setTypeface(Typeface.createFromFile(selected_type_face[0]));

                    } else {
                        if (downloadedTypeface != null) {

                            selectedTextView.setTypeface(downloadedTypeface);
                        } else {
                            Toast_Snack_Dialog_Utils.show_ShortToast(editorActivity, editorActivity.getString(R.string.font_not_downloaded));
                        }
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

    private static void requestDownload(@NonNull Activity activity, @NonNull String familyName, @NonNull final TextView demoText) {


        QueryBuilder queryBuilder = new QueryBuilder(familyName)
                .withWidth(25)
                .withWeight(500)
                .withItalic(0)
                .withBestEffort(true);

        String query = queryBuilder.build();

        FontRequest request = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                query,
                R.array.com_google_android_gms_fonts_certs);
        FontsContractCompat.FontRequestCallback callback = new FontsContractCompat.FontRequestCallback() {
            @Override
            public void onTypefaceRequestFailed(int reason) {
                super.onTypefaceRequestFailed(reason);
            }

            @Override
            public void onTypefaceRetrieved(Typeface typeface) {
                demoText.setTypeface(typeface);
                downloadedTypeface = typeface;


            }

        };
        FontsContractCompat
                .requestFont(activity, request, callback,
                        getHandlerThreadHandler());
    }

    @Nullable
    private static Handler getHandlerThreadHandler() {
        if (mHandler == null) {
            HandlerThread handlerThread = new HandlerThread("fonts");
            handlerThread.start();
            mHandler = new Handler(handlerThread.getLooper());
        }
        return mHandler;
    }


//    METHOD TO GET SYMBOL FONTS

//    public static FontSelected getSymbolFonts(final EditorActivity editorActivity,final TextView selectedTextView){
//        Dialog dialog=new Dialog(editorActivity,R.style.EditTextDialog);
//        dialog.setContentView(R.layout.activity_symbol_fonts);
//    }

    @NonNull
    public static String[] getDownloadedFonts(File file) {
        String[] FilePathStrings = new String[0];
        String[] FileNameStrings = new String[0];
        File[] listFile;
        file.mkdir();
//        GET TTF FILES AND COLLECT IN LIST1
        if (file.isDirectory()) {
            listFile = file.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, @NonNull String name) {
                    if (name.endsWith(".ttf") || name.endsWith(".otf") || name.endsWith(".TTF")) {
                        return true;
                    } else return name.endsWith(".OTF");
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


    @NonNull
    private static String[] getSymbolFonts(Activity activity) {
        String[] localFonts = activity.getResources().getStringArray(R.array.allfonts);
        return localFonts;
    }

    @NonNull
    private static String[] getLocalFonts(Activity activity) {
        return activity.getResources().getStringArray(R.array.allfonts);
    }

    public static void setGradients(@NonNull final EditorActivity editorActivity, @NonNull final TextView selectedTextView) {
        final Dialog dialog = new Dialog(editorActivity, R.style.EditTextDialog);
        dialog.setContentView(R.layout.geadient_text_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);
        dialog.setCancelable(false);

        AdView adView;
        final TextView gradientText;
        final TextView gradientPreviewText;
        final RadioGroup rdGroup;
        final RadioButton rbJpeg;
        final Button preview;
        Button btCancel;
        Button btApply;
        final ImageView demoColor1;
        final ImageView demoColor2;
        final ImageView demoColor5;
        final ImageView demoColor4;
        final ImageView demoColor3;

        final TextView demoColor1Tv;
        final TextView demoColor2Tv;
        final RecyclerView colorsRecycler;
        final ColorsAdapter colorsAdapter;
        final ColorsAdapter colorsAdapter2, colorsAdapter3, colorsAdapter4, colorsAdapter5;
        final Shader[] shader_gradient = new Shader[1];
        final int[] firstColor = {0};
        final int[] secondColor = {0};
        final int[] selected_color = {0};
        final int[] colors = new int[5];

        gradientText = dialog.findViewById(R.id.gradient_text);
        gradientPreviewText = dialog.findViewById(R.id.gradient_preview_text);
        rdGroup = dialog.findViewById(R.id.rd_group);
        rbJpeg = dialog.findViewById(R.id.rb_jpeg);
        preview = dialog.findViewById(R.id.preview);
        adView = dialog.findViewById(R.id.adView);
        btCancel = dialog.findViewById(R.id.bt_cancel);
        btApply = dialog.findViewById(R.id.bt_apply);
        demoColor1 = dialog.findViewById(R.id.demo_color_1);
        demoColor2 = dialog.findViewById(R.id.demo_color_2);
        demoColor3 = dialog.findViewById(R.id.demo_color_3);

        demoColor4 = dialog.findViewById(R.id.demo_color_4);

        demoColor5 = dialog.findViewById(R.id.demo_color_5);
        demoColor1Tv = dialog.findViewById(R.id.demo_color_1_tv);
        demoColor2Tv = dialog.findViewById(R.id.demo_color_2_tv);
        colorsRecycler = dialog.findViewById(R.id.colors_recycler);
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
                colors[0] = color;
                demoColor1.setBackgroundColor(color);
                demoColor1.setImageDrawable(null);
                demoColor1Tv.setTextColor(color);
                preview.setVisibility(View.VISIBLE);

            }
        });
        colorsAdapter2 = new ColorsAdapter(editorActivity, new ColorsAdapter.OnColorClickListener() {
            @Override
            public void onColorClick(int color) {
                colors[1] = color;
                demoColor2.setBackgroundColor(color);
                demoColor2.setImageDrawable(null);
                demoColor2Tv.setTextColor(color);
                preview.setVisibility(View.VISIBLE);


            }
        });
        colorsAdapter3 = new ColorsAdapter(editorActivity, new ColorsAdapter.OnColorClickListener() {
            @Override
            public void onColorClick(int color) {
                colors[2] = color;
                demoColor3.setBackgroundColor(color);
                demoColor3.setImageDrawable(null);
                preview.setVisibility(View.VISIBLE);


            }
        });
        colorsAdapter4 = new ColorsAdapter(editorActivity, new ColorsAdapter.OnColorClickListener() {
            @Override
            public void onColorClick(int color) {
                colors[3] = color;
                demoColor4.setBackgroundColor(color);
                demoColor4.setImageDrawable(null);
                preview.setVisibility(View.VISIBLE);


            }
        });
        colorsAdapter5 = new ColorsAdapter(editorActivity, new ColorsAdapter.OnColorClickListener() {
            @Override
            public void onColorClick(int color) {
                colors[4] = color;
                demoColor5.setBackgroundColor(color);
                demoColor5.setImageDrawable(null);
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
        demoColor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorsRecycler.setVisibility(View.VISIBLE);
                colorsRecycler.setAdapter(null);
                selected_color[0] = 0;
                colorsRecycler.setAdapter(colorsAdapter3);
            }
        });
        demoColor4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorsRecycler.setVisibility(View.VISIBLE);
                colorsRecycler.setAdapter(null);
                selected_color[0] = 0;
                colorsRecycler.setAdapter(colorsAdapter4);
            }
        });
        demoColor5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorsRecycler.setVisibility(View.VISIBLE);
                colorsRecycler.setAdapter(null);
                selected_color[0] = 0;
                colorsRecycler.setAdapter(colorsAdapter5);
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
                    float[] position = {0, 1};
                    LinearGradient lin_grad = new LinearGradient(0, 0, 0, selectedTextView.getWidth(), colors, null, Shader.TileMode.MIRROR);
                    Matrix matrix = new Matrix();
                    matrix.setRotate(90);
                    lin_grad.setLocalMatrix(matrix);
                    shader_gradient[0] = lin_grad;
                    gradientPreviewText.setText(null);
                    gradientPreviewText.setText("SOMETHING");
                    gradientPreviewText.getPaint().setShader(null);
//                    Bitmap bitmap= BitmapFactory.decodeResource(editorActivity.getResources(), AnimUtils.createDrawable(firstColor[0], secondColor[0], editorActivity));

                    ((TextView) dialog.findViewById(R.id.gradient_preview_text)).getPaint().setShader(shader_gradient[0]);
                    gradientText.setBackground(AnimUtils.createDrawable(colors[0], colors[1], editorActivity));

                } else {
                    RadialGradient lin_grad = new RadialGradient(0, 3, 5, colors[0], colors[1], Shader.TileMode.REPEAT);
                    Matrix matrix = new Matrix();
                    matrix.setRotate(90);
                    lin_grad.setLocalMatrix(matrix);
                    shader_gradient[0] = lin_grad;
                    gradientPreviewText.getPaint().setShader(null);
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
                    selectedTextView.setBackground(null);
                    selectedTextView.setBackground(ContextCompat.getDrawable(editorActivity.getApplicationContext(), R.drawable.tv_bg));

//                    Bitmap bitmap= BitmapFactory.decodeResource(editorActivity.getResources(),R.drawable.ic_cat_test);
//
//                    Shader shader=new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

                    selectedTextView.getPaint().setShader(shader_gradient[0]);
                    dialog.dismiss();
                } else {
                    Toast_Snack_Dialog_Utils.show_ShortToast(editorActivity, editorActivity.getString(R.string.select_to_apply));
                }
            }
        });

        dialog.show();
    }

    public static void setVfx(@NonNull EditorActivity editorActivity, final TextView selectedTextView) {
        final Dialog dialog = new Dialog(editorActivity, R.style.EditTextDialog);
        dialog.setContentView(R.layout.vfx_text_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);

        AdView adView;
        TextView tvHead;
        final TextView demoShadowText;
        AppCompatSeekBar lightingDirectionSeekBar;
        TextView lighting_direction_tv;
        final AppCompatSeekBar lightingStrngthSeekbar;
        final TextView lighting_strngth_tv;
        final AppCompatSeekBar highlightsSeekbar;
        final TextView highlights_tv;
        final AppCompatSeekBar blurrRadius;
        final TextView blurr_radius_tv;
        TextView tvShadowColor;
        RecyclerView rvShadowColor;
        Button btShadowClose;
        Button btShadowApply;
        final Spinner spinner;
        LinearLayout root;
        final int[] selectedVfx = new int[1];

        root = dialog.findViewById(R.id.root);
        /*VARIABLES FOR SHADOW*/
        final float[] radius = new float[1];
        final float[] x = new float[1];
        final float[] y = new float[1];
        final int[] shadow_color = new int[1];
        final float[] opacity = new float[3];

        tvHead = dialog.findViewById(R.id.tv_head);
        demoShadowText = dialog.findViewById(R.id.demo_shadow_text);
        lighting_direction_tv = dialog.findViewById(R.id.tv_opacity);
        lighting_strngth_tv = dialog.findViewById(R.id.tv_shadow_radius);
        highlights_tv = dialog.findViewById(R.id.tv_x_pos);
        blurr_radius_tv = dialog.findViewById(R.id.tv_y_pos);
        tvShadowColor = dialog.findViewById(R.id.tv_shadow_color);
        adView = dialog.findViewById(R.id.adView);
        lightingDirectionSeekBar = dialog.findViewById(R.id.opacity_seekbar);
        lightingStrngthSeekbar = dialog.findViewById(R.id.shadow_radius_seekbar);
        highlightsSeekbar = dialog.findViewById(R.id.pos_x_seekbar);
        blurrRadius = dialog.findViewById(R.id.pos_y_seekbar);
        spinner = dialog.findViewById(R.id.fx_spinner);
        AdUtils.loadBannerAd(adView, editorActivity);
        rvShadowColor = dialog.findViewById(R.id.rv_shadow_color);
        rvShadowColor.setVisibility(GONE);
        rvShadowColor.setLayoutManager(new LinearLayoutManager(editorActivity, LinearLayoutManager.HORIZONTAL, false));


        btShadowClose = dialog.findViewById(R.id.bt_shadow_close);
        btShadowApply = dialog.findViewById(R.id.bt_shadow_apply);
        demoShadowText.setTextColor(selectedTextView.getTextColors());

        /*Applying font to all TextViews and Edittexts*/
        TextUtils.findText_and_applyTypeface(root, editorActivity);
        lightingDirectionSeekBar.setProgress(0);
        lightingStrngthSeekbar.setProgress(0);
        highlightsSeekbar.setProgress(0);
        blurrRadius.setProgress(0);
        selectedVfx[0] = 0;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 2) {
                    lighting_strngth_tv.setVisibility(GONE);
                    highlights_tv.setVisibility(GONE);
                    blurr_radius_tv.setVisibility(GONE);
                    lightingStrngthSeekbar.setVisibility(GONE);
                    highlightsSeekbar.setVisibility(GONE);
                    blurrRadius.setVisibility(GONE);
                } else {

                    lighting_strngth_tv.setVisibility(View.VISIBLE);
                    highlights_tv.setVisibility(View.VISIBLE);
                    blurr_radius_tv.setVisibility(View.VISIBLE);
                    lightingStrngthSeekbar.setVisibility(View.VISIBLE);
                    highlightsSeekbar.setVisibility(View.VISIBLE);
                    blurrRadius.setVisibility(View.VISIBLE);
                }

                selectedVfx[0] = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setSelection(0);
            }
        });

        /*SEEKBAR LISTNERS*/
        lightingDirectionSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /*alpha applies between 0 to 1*/
                opacity[0] = (0f);
                opacity[2] = (0.8f);

                if (selectedVfx[0] == 0) {
                    opacity[1] = 1;
                    applyFilter(demoShadowText, opacity, radius[0], x[0], y[0]);
                    Log.e("EMBOSS VALUES", opacity[0] + File.separator + opacity[1] + File.separator + opacity[2] + File.separator + radius[0] + File.separator + x[0] + File.separator + y[0]);
//                    applyFilter(demoShadowText, new float[] { 0f, 1f, 0.5f }, 0.8f, 3f, 3f);
                } else if (selectedVfx[0] == 1) {
                    opacity[1] = -1;
                    applyFilter(demoShadowText, opacity, radius[0], x[0], y[0]);
                } else if (selectedVfx[0] > 2 && selectedVfx[0] == 2) {
                    opacity[0] = progress == 0.0f ? 1.0f : progress;
                    applyBlurrFilter(demoShadowText, BlurMaskFilter.Blur.NORMAL, progress);
                } else if (selectedVfx[0] > 2 && selectedVfx[0] == 3) {
                    opacity[0] = progress;
                    opacity[0] = progress == 0.0f ? 1.0f : progress;
                    applyBlurrFilter(demoShadowText, BlurMaskFilter.Blur.SOLID, progress);

                } else if (selectedVfx[0] > 2 && selectedVfx[0] == 4) {
                    opacity[0] = progress;
                    opacity[0] = progress == 0.0f ? 1.0f : progress;
                    applyBlurrFilter(demoShadowText, BlurMaskFilter.Blur.INNER, progress);

                } else if (selectedVfx[0] > 2 && selectedVfx[0] == 5) {
                    opacity[0] = progress;
                    opacity[0] = progress == 0.0f ? 1.0f : progress;
                    applyBlurrFilter(demoShadowText, BlurMaskFilter.Blur.OUTER, progress);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        lightingStrngthSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radius[0] = progress % 10;

                if (selectedVfx[0] == 1) {
                    opacity[1] = 1;
                    applyFilter(demoShadowText, opacity, radius[0], x[0], y[0]);
                } else if (selectedVfx[0] == 2) {
                    opacity[1] = -1;
                    applyFilter(demoShadowText, opacity, radius[0], x[0], y[0]);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        highlightsSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                x[0] = progress;
                if (selectedVfx[0] == 1) {
                    opacity[1] = 1;
                    applyFilter(demoShadowText, opacity, radius[0], x[0], y[0]);
                } else if (selectedVfx[0] == 2) {
                    opacity[1] = -1;
                    applyFilter(demoShadowText, opacity, radius[0], x[0], y[0]);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        blurrRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                y[0] = progress;

                if (selectedVfx[0] == 1) {
                    opacity[1] = 1;
                    applyFilter(demoShadowText, opacity, radius[0], x[0], y[0]);
                } else if (selectedVfx[0] == 2) {
                    opacity[1] = -1;
                    applyFilter(demoShadowText, opacity, radius[0], x[0], y[0]);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        /*SETTING COLOR OF RECYCLERVIEW*/
//        ColorsAdapter colorsAdapter = new ColorsAdapter(editorActivity, new ColorsAdapter.OnColorClickListener() {
//            @Override
//            public void onColorClick(int color) {
//                shadow_color[0] = color;
//                TextUtils.applyTextShadow(demoShadowText, radius[0], x[0], y[0], shadow_color[0]);
//
//            }
//        });
//
//        rvShadowColor.setAdapter(colorsAdapter);
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

                if (selectedVfx[0] == 0) {
                    opacity[1] = 1;
                    applyFilter(selectedTextView, opacity, radius[0], x[0], y[0]);
//                    applyFilter(selectedTextView, new float[] { 0f, 1f, 0.5f }, 0.8f, 3f, 3f);

                } else if (selectedVfx[0] == 1) {
                    opacity[1] = -1;
                    applyFilter(selectedTextView, opacity, radius[0], x[0], y[0]);
//                    applyFilter(selectedTextView, new float[] { 0f, -1f, 0.5f }, 0.8f, 15f, 1f);
                } else if (selectedVfx[0] >= 2 && selectedVfx[0] == 2) {
                    applyBlurrFilter(selectedTextView, BlurMaskFilter.Blur.NORMAL, opacity[0]);
                } else if (selectedVfx[0] > 2 && selectedVfx[0] == 3) {
                    applyBlurrFilter(selectedTextView, BlurMaskFilter.Blur.SOLID, opacity[0]);

                } else if (selectedVfx[0] > 2 && selectedVfx[0] == 4) {
                    applyBlurrFilter(selectedTextView, BlurMaskFilter.Blur.INNER, opacity[0]);

                } else if (selectedVfx[0] > 2 && selectedVfx[0] == 5) {
                    applyBlurrFilter(selectedTextView, BlurMaskFilter.Blur.OUTER, opacity[0]);

                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private static void applyFilter(TextView textView, float[] direction, float ambient, float specular, float blurRadius) {
        textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        EmbossMaskFilter filter = new EmbossMaskFilter(
                direction, ambient, specular, blurRadius);
        textView.getPaint().setMaskFilter(filter);
    }

    private static void applyBlurrFilter(
            TextView textView, BlurMaskFilter.Blur style, float val) {
        textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        val = val <= 0.0f ? 1.0f : val;
        float radius = textView.getTextSize() / val < 0 ? 0 : textView.getTextSize() / val;
        BlurMaskFilter filter = new BlurMaskFilter(radius, style);
        textView.getPaint().setMaskFilter(filter);
    }

    public static void setHollowText(@NonNull final EditorActivity editorActivity, final TextView selectedTextView) {
        final Dialog dialog = new Dialog(editorActivity, R.style.EditTextDialog);
        dialog.setContentView(R.layout.hollow_text_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);
        dialog.show();
        dialog.setCancelable(false);
        TextView tvHead;
        AppCompatSpinner fxSpinner;
        final TextView demoShadowText;
        AppCompatSeekBar opacitySeekbar;
        TextView tvOpacity;
        Button btShadowClose;
        Button btShadowApply;
        AdView adView;
        LinearLayout root;


        final ArrayList<PathEffect> pathEffects = new ArrayList<>();
        pathEffects.add(null);
        final int[] selectedOption = new int[1];
        tvHead = dialog.findViewById(R.id.tv_head);
        root = dialog.findViewById(R.id.root);
        fxSpinner = dialog.findViewById(R.id.fx_spinner);
        demoShadowText = dialog.findViewById(R.id.demo_shadow_text);
        opacitySeekbar = dialog.findViewById(R.id.opacity_seekbar);
        tvOpacity = dialog.findViewById(R.id.tv_opacity);
        btShadowClose = dialog.findViewById(R.id.bt_shadow_close);
        btShadowApply = dialog.findViewById(R.id.bt_shadow_apply);
        adView = dialog.findViewById(R.id.adView);
        AdUtils.loadBannerAd(adView, editorActivity);
        TextUtils.findText_and_applyTypeface(root, editorActivity);
        pathEffects.add(getDashPathEffect(6));
        pathEffects.add(getCornerDashPathEffect(6));
        pathEffects.add(getTrianglePathEffect(6));
        final int[] position = new int[1];
        int strokeWidth = 5;
        final HollowSpan hollowSpan = new HollowSpan(strokeWidth);
        final SpannableString spannableString = new SpannableString(demoShadowText.getText().toString());
        spannableString.setSpan(hollowSpan, 0, demoShadowText.getText().length(), 0);

        final SpannableString
                spannableString_Selected = new SpannableString(selectedTextView.getText().toString());
        spannableString_Selected.setSpan(hollowSpan, 0, selectedTextView.getText().length(), 0);

        fxSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position[0] = i;
                hollowSpan.setPathEffect(pathEffects.get(i));
                demoShadowText.setText(spannableString, TextView.BufferType.SPANNABLE);
//                switch (i) {
//                    case 0: {
//
//                    }
//                    break;
//                    case 1: {
//
//                    }
//                    break;
//                    case 2: {
//
//                    }
//                    break;
//                    case 3: {
//
//                    }
//                    break;
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                position[0] = 0;
            }
        });


        btShadowClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        btShadowApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hollowSpan.setPathEffect(pathEffects.get(position[0]));
                selectedTextView.setText(spannableString_Selected, TextView.BufferType.SPANNABLE);
                dialog.dismiss();
            }
        });

    }

    public static PathEffect getDashPathEffect(int strokeWidth) {
        return new DashPathEffect(new float[]{strokeWidth * 3, strokeWidth}, 0);
    }

    public static PathEffect getCornerDashPathEffect(int strokeWidth) {
        PathEffect dash = getDashPathEffect(strokeWidth);
        PathEffect corner = new CornerPathEffect(strokeWidth);
        return new ComposePathEffect(dash, corner);
    }

    public static PathEffect getTrianglePathEffect(int strokeWidth) {
        return new PathDashPathEffect(
                getTriangle(strokeWidth),
                strokeWidth,
                0.0f,
                PathDashPathEffect.Style.ROTATE);
    }

    @NonNull
    public static Path getTriangle(float size) {
        Path path = new Path();
        float half = size / 2;
        path.moveTo(-half, -half);
        path.lineTo(half, -half);
        path.lineTo(0, half);
        path.close();
        return path;
    }

}
