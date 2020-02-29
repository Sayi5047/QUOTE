package com.hustler.quote.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.collection.SparseArrayCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdView;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.hustler.quote.R;
import com.hustler.quote.ui.Executors.AppExecutor;
import com.hustler.quote.ui.ORM.AppDatabase;
import com.hustler.quote.ui.ORM.Tables.QuotesTable;
import com.hustler.quote.ui.Widgets.RotationGestureDetector;
import com.hustler.quote.ui.adapters.ColorsAdapter;
import com.hustler.quote.ui.adapters.Features_adapter;
import com.hustler.quote.ui.adapters.ImagesAdapter;
import com.hustler.quote.ui.adapters.SizesAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.apiRequestLauncher.ListnereInterfaces.StickerResponseListener;
import com.hustler.quote.ui.apiRequestLauncher.Restutility;
import com.hustler.quote.ui.apiRequestLauncher.response.Data;
import com.hustler.quote.ui.customviews.DoodleView;
import com.hustler.quote.ui.customviews.Sticker.StickerImageView;
import com.hustler.quote.ui.customviews.Sticker.StickerView;
import com.hustler.quote.ui.listeners.DoodleViewTouchListener;
import com.hustler.quote.ui.pojo.Quote;
import com.hustler.quote.ui.superclasses.BaseActivity;
import com.hustler.quote.ui.textFeatures.TextFeatures;
import com.hustler.quote.ui.utils.AdUtils;
import com.hustler.quote.ui.utils.AnimUtils;
import com.hustler.quote.ui.utils.FileUtils;
import com.hustler.quote.ui.utils.ImageProcessingUtils;
import com.hustler.quote.ui.utils.InternetUtils;
import com.hustler.quote.ui.utils.PermissionUtils;
import com.hustler.quote.ui.utils.TextUtils;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static android.view.View.GONE;

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
public class EditorActivity extends BaseActivity implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener,
        View.OnTouchListener,
        View.OnLongClickListener,
        RotationGestureDetector.OnRotateGestureListener {

    private static final int RESULT_LOAD_IMAGE = 1001;
    private static final int MY_PERMISSION_REQUEST_STORAGE_FROM_ONSTART = 1002;
    private static final int MY_PERMISSION_REQUEST_STORAGE_FOR_SAVING_TO_GALLERY = 1003;
    private static final int MY_PERMISSION_REQUEST_STORAGE_FOR_FONTS = 1004;
    private static final int MY_PERMISSION_REQUEST_Launch_gallery = 1007;
    boolean isForPhotoText = false;
    private int imageFitType;
    private int backPressCount = 0;
    private int isFromEdit_Activity;
    int addedTextIds = 0;
    int prevX, prevY;
    private int pointer_Id_1;
    int gifFrameCount = 0;
    DoodleView doodleView;
    ImageView doodleBtn,
            doodle_erase_btn,
            doodle_color_btn,
            doodle_brush_size_btn,
            doodle_shadow_btn,
            doodle_undo_btn,
            doodle_redo_btn;
    ImageView save_work_button;
    ImageView share_work_button;
    ImageView delete_work_button;


    float nsx, nsy, nfx, nfy;
    private float fx;
    private float fy;

    private boolean isHeightMeasured = false;


    private File savedFile;
    private Quote quote;
    SpannableString spannableString;

    private String newly_Added_Text;
    private String current_Text_feature;
    private String current_Bg_feature;
    @Nullable
    private String current_module = null;
    @Nullable
    private String selected_picture;
    private String guestImage;


    Window windowManager;
    Features_adapter features_adapter;

    RecyclerView featuresRecyclerview;
    @Nullable
    View selectedView;
    @Nullable
    StickerImageView selected_sticker, previous_sticker;
    @Nullable
    View previousselctedView;
    @Nullable
    View previousState;

    private ImageView light_effect_filter_IV;
    private ImageView imageView_background;
    private TextView text_layout;
    private TextView background_layout;
    private SeekBar seekBar;
    private Button clear_button;

    private RelativeLayout root_layout;
    private LinearLayout features_layout;
    private RelativeLayout quoteLayout;
    private RelativeLayout core_editor_layout;
    private LinearLayout text_and_bg_layout;
    private LinearLayout close_and_done_layout;

    private RotationGestureDetector rotationGestureDetector;
    ScaleGestureDetector scaleGestureDetector;
    private boolean doodleEnabled = false;
    private boolean doodleShadowEnabled = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.bg));
        String called_from = getIntent().getStringExtra("called");
        if (!"add".equalsIgnoreCase(called_from)) {
            findViews();
            getIntentData();
            setViews();
            rotationGestureDetector = new RotationGestureDetector(EditorActivity.this);
        } else {
            Toast.makeText(EditorActivity.this, "ACTIVITY IS NULL", Toast.LENGTH_SHORT).show();
            EditorActivity.this.finish();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!PermissionUtils.isPermissionAvailable(EditorActivity.this)) {
            requestAppPermissions();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!isHeightMeasured) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(EditorActivity.this).edit();
            editor.putInt(Constants.SAHRED_PREFS_DEVICE_HEIGHT_KEY, quoteLayout.getHeight());
            editor.apply();
            isHeightMeasured = true;
        }
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        isFromEdit_Activity = 0;

        File file;
        String filePath = null;
        String action = intent.getAction();
        if (Objects.equals(action, Intent.ACTION_VIEW)) {
            try {
                filePath = intent.getData().getPath();
                Log.d("ACTION_VIEW", filePath);
            } catch (NullPointerException ne) {
                finish();
                ne.printStackTrace();
            }
        } else if (Objects.equals(action, Intent.ACTION_SEND)) {
            Bundle bundle = intent.getExtras();
            Uri uri = null;
            if (bundle != null) {
                uri = (Uri) bundle.get(Intent.EXTRA_STREAM);
            }

            if (uri != null && "content".equals(uri.getScheme())) {
                Cursor cursor = this.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
                cursor.moveToFirst();
                filePath = cursor.getString(0);
                cursor.close();
            } else {
                filePath = uri.getPath();
            }
        }
        try {
            if (filePath != null) {
                file = new File(filePath);
                Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, file.getAbsolutePath());
                FileUtils.unzipAndSave(file, EditorActivity.this, action);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*TOUCH LISTENERS*/
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        rotationGestureDetector.onTouchEvent(event);
        return true;
    }

    /*CLICK LISTENERS*/
    //LEVEL 1
    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.bt_clear: {
                if (selectedView != null) {
                    selectedView.setBackground(null);
                    previousselctedView = null;
                    selectedView = null;
                }
                if (selected_sticker == null) {
                    Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text_to_delete));
                } else {
                    selected_sticker.setControlItemsHidden(true);
                    selected_sticker = null;
                    previous_sticker = null;
                }
                clear_button.setVisibility(GONE);

            }
            case R.id.text_field: {
                setText_Features_rv();
            }
            break;
            case R.id.background_and_Image_field: {
                setBackground_features_rv();
            }
            break;
            case R.id.close_tv: {
                close_and_done_layout.setVisibility(GONE);
                text_and_bg_layout.setVisibility(View.VISIBLE);
                featuresRecyclerview.setVisibility(View.VISIBLE);
                seekBar.setProgress(180);
                seekBar.setVisibility(GONE);
                handle_close_Feature();


            }
            break;
            case R.id.done_tv: {
                close_and_done_layout.setVisibility(GONE);
                text_and_bg_layout.setVisibility(View.VISIBLE);
                featuresRecyclerview.setVisibility(View.VISIBLE);
                seekBar.setVisibility(GONE);
            }
            break;
            case R.id.save_work_button: {
                if (PermissionUtils.isPermissionAvailable(EditorActivity.this)) {
                    if (selectedView != null) {
                        selectedView.setBackground(null);
                        clear_button.setVisibility(GONE);
                        previousselctedView = null;
                        selectedView = null;
                    }
                    if (selected_sticker != null) {
                        selected_sticker.setControlItemsHidden(true);
                        previousselctedView = null;
                        selected_sticker = null;
                    }

                    if (imageView_background.getBackground() == null) {
                        imageView_background.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.color.textColor));
                    }

                    FileUtils.saveProjectWithAds(quoteLayout, EditorActivity.this, file -> {
                        savedFile = file;
                        AppExecutor.getInstance().getMainThreadExecutor().execute(() -> {
                            Toast.makeText(EditorActivity.this, "File Saved", Toast.LENGTH_SHORT).show();
                            FileUtils.showPostSaveDialog(EditorActivity.this, savedFile);
                        });

                    });
//                    if (savedFile != null) {
//                        Toast.makeText(EditorActivity.this, "File Already Saved", Toast.LENGTH_SHORT).show();
//                    }
                } else {
                    requestAppPermissions_to_save_to_gallery();
                }

            }
            break;
            case R.id.font_share_module: {
                final Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("image/jpeg");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri uri = null;
                if (savedFile != null) {
                    if (Build.VERSION.SDK_INT >= 24) {
                        uri = FileProvider.getUriForFile(EditorActivity.this, getString(R.string.file_provider_authority), savedFile);
                    } else {
                        uri = Uri.fromFile(savedFile);
                    }
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(shareIntent, "send"));
                } else {
                    FileUtils.saveProjectWithAds(quoteLayout, EditorActivity.this, new FileUtils.onSaveComplete() {
                        @Override
                        public void onImageSaveListener(File file) {
                            savedFile = file;
                            Uri uri1 = null;
                            if (Build.VERSION.SDK_INT >= 24) {
                                uri1 = FileProvider.getUriForFile(EditorActivity.this, getString(R.string.file_provider_authority), savedFile);
                            } else {
                                uri1 = Uri.fromFile(savedFile);
                            }
                            shareIntent.putExtra(Intent.EXTRA_STREAM, uri1);
                            startActivity(Intent.createChooser(shareIntent, "send"));
                        }
                    });
                }
            }
            break;
            case R.id.delete_view_button: {
                if (selectedView != null) {
                    Toast_Snack_Dialog_Utils.createDialog(this, getString(R.string.are_you_sure), getString(R.string.this_will_delete), getString(R.string.cancel), getString(R.string.delete), new Toast_Snack_Dialog_Utils.Alertdialoglistener() {
                        @Override
                        public void onPositiveSelection() {
                            close_and_done_layout.setVisibility(GONE);
                            text_and_bg_layout.setVisibility(View.VISIBLE);
                            featuresRecyclerview.setVisibility(View.VISIBLE);
                            seekBar.setProgress(180);
                            seekBar.setVisibility(GONE);
                            core_editor_layout.removeView(selectedView);
                            selectedView = null;
                        }

                        @Override
                        public void onNegativeSelection() {

                        }
                    });
                } else {
                    Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text_to_delete));
                }
            }
            break;
            case R.id.close_editor_button: {
                if (doodleEnabled) {
                    doodleBtn.performClick();
                } else {
                    super.onBackPressed();
                }
            }
            break;
            case R.id.doodle_btn: {
                if (null == doodleView) {
                    doodleView = new DoodleView(EditorActivity.this);
                    doodleView.setOnTouchListener(new DoodleViewTouchListener());
                    doodleView.setBrushSize(15);
                    core_editor_layout.addView(doodleView);
                    Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, "Doodle added");
                    doodleBtn.setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_bright), PorterDuff.Mode.SRC_IN);
                    doodleEnabled = true;
                    enableDoodleView();
                } else {
                    if (doodleEnabled) {
                        disableDoodleView();
                    } else {
                        enableDoodleView();
                    }
                }

            }
            break;
            case R.id.doodle_eraser_btn: {
                if (doodleView != null) {
                    doodleView.clear();
                    doodleShadowEnabled = false;
                    doodleView.EnableShadow(false);
                    doodle_shadow_btn.setColorFilter(null);
                } else {
                    Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, "Draw something to erase");
                }
            }
            break;
            case R.id.doodle_undo_btn: {
                if (doodleView != null) {
                    doodleView.removeLastPath();
                } else {
                    Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, "Draw something to erase");
                }
            }
            break;
            case R.id.doodle_redo_btn: {
                if (doodleView != null) {
                    doodleView.restoreLastRemovedPath();
                } else {
                    Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, "Draw something to erase");
                }
            }
            break;
            case R.id.doodle_color_btn: {
                if (doodleView != null) {
                    colorDoodle();
                } else {
                    Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, "Enable Doodle View first and then try");
                }
            }
            break;
            case R.id.doodle_shadow_btn: {
                if (doodleView == null) {
                    Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, "Enable Doodle View first and then try");
                } else {
                    if (doodleShadowEnabled) {
                        doodleView.EnableShadow(false);
                        doodleShadowEnabled = false;
                        doodle_shadow_btn.setColorFilter(null);

                    } else {
                        doodleView.EnableShadow(true);
                        doodleShadowEnabled = true;
                        doodle_shadow_btn.setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_bright), PorterDuff.Mode.SRC_IN);

                    }
                }

            }
            break;

            case R.id.doodle_brush_button: {
                if (doodleView != null) {
                    launchBrushWidthDialog(doodleView);
                } else {
                    Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, "Enable Doodle View first and then try");
                }
            }
            break;
        }
    }

    private void launchBrushWidthDialog(final DoodleView doodleView) {

        final Dialog dialog = new Dialog(this, R.style.EditTextDialog);
        dialog.setContentView(R.layout.brush_size_layout);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.white_rounded_drawable);

        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;

        final TextView head_tv;
        final ImageView demo_icon;
        // AdView // AdView;
        SeekBar colors_rv;
        Button close, choose;


        head_tv = dialog.findViewById(R.id.color_text);
        demo_icon = dialog.findViewById(R.id.demo_icon);
        colors_rv = dialog.findViewById(R.id.size_scrollBar);
        close = dialog.findViewById(R.id.bt_color_close);
        choose = dialog.findViewById(R.id.bt_color_choose);
        dialog.findViewById(R.id.bt_color_shadow);
        // AdView = dialog.findViewById(R.id.adView);

        TextUtils.setFont(this, head_tv, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, close, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, choose, Constants.FONT_CIRCULAR);
        // AdUtils.loadBannerAd(adView, EditorActivity.this);

        colors_rv.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                demo_icon.setScaleX((float) seekBar.getProgress() / 25);
                demo_icon.setScaleY((float) seekBar.getProgress() / 25);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                doodleView.setBrushSize(seekBar.getProgress());


            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void enableDoodleView() {
        doodleView.setOnTouchListener(new DoodleViewTouchListener());
        doodleEnabled = true;
        doodleBtn.setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_bright), PorterDuff.Mode.SRC_IN);
        enableDoodleOptions();
    }

    private void disableDoodleView() {
        doodleView.setOnTouchListener(null);
        doodleEnabled = false;
        doodleBtn.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        disableDoodleOptions();
    }

    private void enableEraserView() {
        doodle_erase_btn.setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_bright), PorterDuff.Mode.SRC_IN);
    }

    private void disableEraserView() {

        doodle_erase_btn.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
    }

    /*Seekbar methods*/
    @Override
    public void onProgressChanged(@NonNull SeekBar seekBar, int progress, boolean fromUser) {
        handle_seekbar_value(seekBar);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(@NonNull SeekBar seekBar) {
        handle_bg_seekbar(seekBar);
    }

    @Override
    public void onBackPressed() {
        backPressCount++;

        if (backPressCount >= 2) {
            this.finish();
            overridePendingTransition(R.anim.slideup, R.anim.slidedown);
        } else {
            Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, "Press again to discard the image and exit");
        }

    }

    /*Methods to handle view movements*/
    @Override
    public boolean onTouch(@NonNull View v, @NonNull MotionEvent event) {
        RelativeLayout.LayoutParams selected_text_view_parameters = (RelativeLayout.LayoutParams) v.getLayoutParams();
        final int INAVALID_POINTER_ID = -1;
        if (null != doodleView) {
            disableDoodleView();

        }
        if (v instanceof TextView) {
            selectedView = v;
            handleTouchForTextView(v);

        } else if (v instanceof StickerImageView) {
            selected_sticker = (StickerImageView) v;
            handleTouchForStickerView((StickerImageView) v);

        } else if (v instanceof RelativeLayout && v.getId() == R.id.arena_text_layout) {
            if (null != selectedView)
                clearButKeepView();
//                clear_button.performClick();
        }

        if (selectedView instanceof TextView || selected_sticker instanceof StickerView) {

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN: {
                    pointer_Id_1 = event.getPointerId(event.getActionIndex());
                    Log.d("ACTION DOWN", pointer_Id_1 + "");

                    try {
                        fx = event.getX(event.findPointerIndex(pointer_Id_1));
                        fy = event.getY(event.findPointerIndex(pointer_Id_1));
                    } catch (IllegalArgumentException iae) {
                        iae.printStackTrace();
                    }
                    return true;
                }

                case MotionEvent.ACTION_MOVE: {

                    if (pointer_Id_1 != INAVALID_POINTER_ID) {

                        Log.d("ACTION move x POINTE", pointer_Id_1 + "");
                        try {
                            nfx = event.getX(event.findPointerIndex(pointer_Id_1));
                            nfy = event.getY(event.findPointerIndex(pointer_Id_1));
                        } catch (IllegalArgumentException iae) {
                            iae.printStackTrace();
                        }


                        selected_text_view_parameters.topMargin = selected_text_view_parameters.topMargin + (int) nfy - (int) fy;
                        prevY = (int) nfy;
                        selected_text_view_parameters.leftMargin = selected_text_view_parameters.leftMargin + (int) nfx - (int) fx;
                        prevX = (int) nfx;
                        v.setLayoutParams(selected_text_view_parameters);
                        Log.d("ACTION MOVE", "TRUE RETURNED");

                        return true;
                    } else {
                        Log.d("ACTION MOVE", "FALSE RETURNED");

                        return false;
                    }


                }

                case MotionEvent.ACTION_UP: {

                    pointer_Id_1 = INAVALID_POINTER_ID;
                    Log.d("ACTION UP", pointer_Id_1 + "");

                    return true;
                }

//            }

                case MotionEvent.ACTION_CANCEL: {
                    int pointer_ID_2 = pointer_Id_1 = INAVALID_POINTER_ID;
                    Log.d("ACTION CANCEL", pointer_ID_2 + " <--2,1--> " + pointer_Id_1);

                    return true;
                }
            }
        }
        return false;
    }

    private void clearButKeepView() {
        clear_button.setVisibility(GONE);
        assert selectedView != null;
        selectedView.setBackground(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri fileUrl = data.getData();
            String[] filePaths = {MediaStore.Images.Media.DATA};
            assert fileUrl != null;
            Cursor cursor = getContentResolver().query(fileUrl, filePaths, null, null, null);
            Objects.requireNonNull(cursor).moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePaths[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            if (imageView_background.getDrawingCache() != null) {
                imageView_background.destroyDrawingCache();
            }
            selected_picture = picturePath;
            if (!isForPhotoText) {
                if (imageFitType == 3) {
                    Glide.with(EditorActivity.this).load(picturePath).into(imageView_background);

                } else if (imageFitType == 2) {
                    Glide.with(EditorActivity.this).load(picturePath).fitCenter().into(imageView_background);

                } else {
                    Glide.with(EditorActivity.this).load(picturePath).centerCrop().into(imageView_background);

                }
            } else {
                Bitmap bitmap = BitmapFactory.decodeFile(selected_picture);
                Shader shader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                ((TextView) selectedView).getPaint().setShader(shader);
            }
        } else if (requestCode == 2) {
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            Bitmap bitmap = data.getExtras().getParcelable("data");
            imageView_background.setImageBitmap(bitmap);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE_FROM_ONSTART: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
            }
            break;
            case MY_PERMISSION_REQUEST_STORAGE_FOR_SAVING_TO_GALLERY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FileUtils.saveProjectWithAds(quoteLayout, EditorActivity.this, new FileUtils.onSaveComplete() {
                        @Override
                        public void onImageSaveListener(File file) {
                            savedFile = file;
                        }
                    });
                }
            }
            break;
            case MY_PERMISSION_REQUEST_STORAGE_FOR_FONTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.permissin_granted));
                }
            }
            break;
            case MY_PERMISSION_REQUEST_Launch_gallery: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    handle_gallery_dialog();
                }
            }
            break;
        }
    }


    private void findViews() {
//        All layouts
        root_layout = findViewById(R.id.root_Lo);
        windowManager = getWindow();
        windowManager.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        quoteLayout = findViewById(R.id.quote_layout);
        text_and_bg_layout = findViewById(R.id.text_and_background_layout);
//        main_editor_layout = (LinearLayout) findViewById(R.id.Main_editor_arena);
        close_and_done_layout = findViewById(R.id.close_and_done_layout);

//      level 1 top bar buttons
//        font_module = (ImageView) findViewById(R.id.font_style_changer_module);
//        background_image_module = (ImageView) findViewById(R.id.font_background_chnager_module);


        save_work_button = findViewById(R.id.save_work_button);
        share_work_button = findViewById(R.id.font_share_module);
        delete_work_button = findViewById(R.id.delete_view_button);

        doodleBtn = findViewById(R.id.doodle_btn);
        doodle_erase_btn = findViewById(R.id.doodle_eraser_btn);
        doodle_color_btn = findViewById(R.id.doodle_color_btn);
        doodle_brush_size_btn = findViewById(R.id.doodle_brush_button);
        doodle_shadow_btn = findViewById(R.id.doodle_shadow_btn);
        doodle_undo_btn = findViewById(R.id.doodle_undo_btn);
        doodle_redo_btn = findViewById(R.id.doodle_redo_btn);

        doodleBtn.setOnClickListener(this);
        doodle_erase_btn.setOnClickListener(this);
        doodle_color_btn.setOnClickListener(this);
        doodle_brush_size_btn.setOnClickListener(this);
        doodle_shadow_btn.setOnClickListener(this);
        doodle_undo_btn.setOnClickListener(this);
        doodle_redo_btn.setOnClickListener(this);

        ImageView font_size_changer = findViewById(R.id.doodle_btn);
        light_effect_filter_IV = findViewById(R.id.iv_light_effect);


        seekBar = findViewById(R.id.progress_slider_bar);
        seekBar.setContentDescription("Slide to Rotate");
        ImageView close_text_size = findViewById(R.id.close_editor_button);

        featuresRecyclerview = findViewById(R.id.content_rv);
        featuresRecyclerview.setLayoutManager(new LinearLayoutManager(EditorActivity.this, LinearLayoutManager.HORIZONTAL, false));

        imageView_background = findViewById(R.id.imageView_background);


        text_layout = findViewById(R.id.text_field);
        background_layout = findViewById(R.id.background_and_Image_field);
        TextView close_layout = findViewById(R.id.close_tv);
        TextView done_layout = findViewById(R.id.done_tv);
        TextView mark_quotzy = findViewById(R.id.mark_quotzy_tv);
        core_editor_layout = findViewById(R.id.arena_text_layout);
        core_editor_layout.setClipChildren(false);
//        core_editor_layout.setOnTouchListener(this);
        clear_button = findViewById(R.id.bt_clear);
        features_layout = findViewById(R.id.features_layout);
        setText_Features_rv();
        scaleGestureDetector = new ScaleGestureDetector(EditorActivity.this, new SimpleOnscaleGestureListener());
        /*setting on click listners */
//        font_module.setOnClickListener(this);
//        background_image_module.setOnClickListener(this);
        text_layout.setOnClickListener(this);
        background_layout.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        font_size_changer.setOnClickListener(this);
        close_text_size.setOnClickListener(this);
        save_work_button.setOnClickListener(this);
        share_work_button.setOnClickListener(this);
        delete_work_button.setOnClickListener(this);
        close_layout.setOnClickListener(this);
        done_layout.setOnClickListener(this);
        clear_button.setOnClickListener(this);
        TextUtils.setFont(EditorActivity.this, mark_quotzy, Constants.FONT_CIRCULAR);
        TextUtils.setFont(EditorActivity.this, text_layout, Constants.FONT_CIRCULAR);
        TextUtils.setFont(EditorActivity.this, background_layout, Constants.FONT_CIRCULAR);
        TextUtils.setFont(EditorActivity.this, close_layout, Constants.FONT_CIRCULAR);
        TextUtils.setFont(EditorActivity.this, done_layout, Constants.FONT_CIRCULAR);
//        core_editor_layout.setOnTouchListener(this);

    }

    private void setViews() {
        if (isFromEdit_Activity == 2) {
            if (guestImage == null) {
                Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.image_unavailable));
            } else {
                imageView_background.setBackground(null);
                final ProgressBar progressBar = new ProgressBar(EditorActivity.this);
                progressBar.setVisibility(View.VISIBLE);
                Glide.with(EditorActivity.this).load(guestImage).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(GONE);
                        Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.image_unavailable));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(GONE);
                        return false;
                    }


                }).centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(imageView_background);
                selected_picture = guestImage;
            }
        }
    }

    private void getIntentData() {
        int value = getIntent().getIntExtra(Constants.INTENT_IS_FROM_EDIT_KEY, 0);
        switch (value) {
            case 1: {
                isFromEdit_Activity = 1;
                int quoteId = getIntent().getIntExtra(Constants.INTENT_QUOTE_OBJECT_KEY, 0);
                if (quoteId > 0) {
                    final LiveData<QuotesTable> quotesTableLiveData = AppDatabase.getmAppDatabaseInstance(EditorActivity.this).quotesDao().getQuotesById(quoteId);
                    quotesTableLiveData.observe(this, new Observer<QuotesTable>() {
                        @Override
                        public void onChanged(@Nullable QuotesTable quotesTable) {
                            quote = new Quote();
                            quote.setAuthor(quotesTable.getAuthor());
                            quote.setQuote(quotesTable.getQuotes());
                            quotesTableLiveData.removeObservers(EditorActivity.this);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView quote_editor_body = new TextView(EditorActivity.this);
                                    TextView quote_editor_author = new TextView(EditorActivity.this);
                                    quote_editor_body.setId(addedTextIds);
                                    addedTextIds++;
                                    quote_editor_author.setId(addedTextIds);
                                    addedTextIds++;


                                    if (quote != null) {
                                        int length = quote.getQuote().length();
                                        root_layout.setBackground(ContextCompat.getDrawable(EditorActivity.this, android.R.drawable.screen_background_light_transparent));
                                        if (length > 230) {
                                            quote_editor_body.setTextSize(20.0f);
                                        } else if (length < 230 && length > 150) {
                                            quote_editor_body.setTextSize(25.0f);

                                        } else if (length > 100 && length < 150) {
                                            quote_editor_body.setTextSize(30.0f);

                                        } else if (length > 50 && length < 100) {
                                            quote_editor_body.setTextSize(35.0f);

                                        } else if (length > 2 && length < 50) {
                                            quote_editor_body.setTextSize(40.0f);

                                        } else {
                                            quote_editor_body.setTextSize(45.0f);

                                        }

                                        quote_editor_body.setText(quote.getQuote());
                                        quote_editor_author.setText(quote.getAuthor());
                                        DisplayMetrics displayMetrics = new DisplayMetrics();
                                        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                                        quote_editor_body.setMaxWidth(displayMetrics.widthPixels);
                                        quote_editor_author.setMaxWidth(displayMetrics.widthPixels);

                                        quote_editor_author.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        quote_editor_body.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                                        quote_editor_body.setGravity(Gravity.CENTER);
                                        quote_editor_author.setGravity(Gravity.CENTER);

                                        quote_editor_body.setY(core_editor_layout.getHeight() >> 1);
                                        quote_editor_author.setX(core_editor_layout.getWidth() >> 1);

                                        core_editor_layout.addView(quote_editor_body);
                                        core_editor_layout.addView(quote_editor_author);
                                        if (null != doodleView) {
                                            disableDoodleView();

                                        }
                                        quote_editor_author.setLongClickable(true);
                                        quote_editor_author.setOnLongClickListener(EditorActivity.this);
                                        quote_editor_body.setLongClickable(true);
                                        quote_editor_body.setOnLongClickListener(EditorActivity.this);
                                        quote_editor_author.setOnTouchListener(EditorActivity.this);
                                        quote_editor_body.setOnTouchListener(EditorActivity.this);
                                    }

                                }
                            });
                        }
                    });
                }
            }
            break;
            case 0: {
                isFromEdit_Activity = 0;
                Intent intent = getIntent();
                File file;
                String path = null;

                String action = intent.getAction();
                if (Objects.equals(action, Intent.ACTION_VIEW)) {
                    try {
                        path = Objects.requireNonNull(intent.getData()).getPath();
                        Log.d("ACTION_VIEW", path);
                    } catch (NullPointerException ne) {
                        finish();
                        ne.printStackTrace();
                    }
                } else if (Objects.equals(action, Intent.ACTION_SEND)) {
                    Bundle bundle = intent.getExtras();
                    Uri uri = null;
                    if (bundle != null) {
                        uri = (Uri) bundle.get(Intent.EXTRA_STREAM);
                    }
                    if (uri != null) {
                        path = uri.getPath();
                        Log.d("ACTION_SEND", path);
                    }
                }
                try {
                    if (path != null) {
                        file = new File(path);
                        Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, file.getAbsolutePath());
                        FileUtils.unzipAndSave(file, EditorActivity.this, action);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            break;
            case 2: {
                isFromEdit_Activity = 2;
                guestImage = getIntent().getStringExtra(Constants.INTENT_UNSPLASH_IMAGE_FOR_EDIOTR_KEY);

            }
            break;
        }

    }


    @Override
    public boolean onLongClick(@NonNull View v) {

        v.bringToFront();
        core_editor_layout.forceLayout();
        core_editor_layout.invalidate();

        SparseArrayCompat<TextView> sparseArrayCompat = new SparseArrayCompat<TextView>();
        sparseArrayCompat.put(v.getId(), (TextView) v);
        Log.d("SPARSE ARRAY", String.valueOf(sparseArrayCompat.get(v.getId())));
        return true;

    }

    @Override
    public void onRotate(@NonNull RotationGestureDetector rotationGestureDetector) {
        if (selectedView != null) {
            View selected = selectedView;
            if (selected instanceof TextView) {
                TextView selectedText = ((TextView) selected);
                selectedText.setRotation(rotationGestureDetector.getmAngle());

                Log.d("RotationGestureDetector", "Rotation: " + rotationGestureDetector.getmAngle());
            }

        }
    }

    /*COLONY BACKGROUND*/
    private void setBackground_features_rv() {
        current_module = Constants.BG;
        text_layout.setTextColor(getResources().getColor(R.color.colorPrimary1));
        background_layout.setTextColor(ContextCompat.getColor(EditorActivity.this, R.color.colorAccent));
        background_layout.setTextSize(16.0f);
        text_layout.setTextSize(12.0f);
        featuresRecyclerview.setAdapter(null);
        featuresRecyclerview.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slidedown));

        features_adapter = new Features_adapter(this, "Background_features", getResources().getStringArray(R.array.Background_features).length, clickedItem -> {
//                Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, clickedItem);
            enable_Selected_Background_Features(clickedItem, getResources().getStringArray(R.array.Background_features));

        }, false);

        featuresRecyclerview.setAdapter(features_adapter);
        featuresRecyclerview.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slideup));

    }

    /*COLONY TEXT*/
    private void setText_Features_rv() {
        current_module = Constants.TEXT;
        text_layout.setTextSize(16.0f);
        background_layout.setTextSize(12.0f);

        background_layout.setTextColor(getResources().getColor(R.color.colorPrimary1));
        text_layout.setTextColor(ContextCompat.getColor(EditorActivity.this, R.color.colorAccent));

        featuresRecyclerview.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slidedown));
        featuresRecyclerview.setAdapter(null);


        features_adapter = new Features_adapter(this, "Text_features", getResources().getStringArray(R.array.Text_features).length, clickedItem -> enable_Selected_Text_Feature(clickedItem, getResources().getStringArray(R.array.Text_features)), true);

        featuresRecyclerview.setAdapter(features_adapter);
        featuresRecyclerview.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slideup));

    }

    // LEVEL 2
    private void enable_Selected_Text_Feature(String feature, String[] array) {
        if (feature.equalsIgnoreCase(array[0])) {
            addText(array, false);
        } else if (feature.equalsIgnoreCase(array[1])) {
            if (selectedView == null) {
                Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
            } else {
                editText(array, true);
            }
        } else if (feature.equalsIgnoreCase(array[2])) {
            resizeText(array);

        } else if (feature.equalsIgnoreCase(array[3])) {
            rotateText(array);

        } else if (feature.equalsIgnoreCase(array[4])) {
            spaceText(array);

        } else if (feature.equalsIgnoreCase(array[5])) {
            // TODO: 14/12/2017 do it at end of quotes area
            colorText(array);

        } else if (feature.equalsIgnoreCase(array[6])) {
            spaceLine(array);

        } else if (feature.equalsIgnoreCase(array[7])) {
            adjustFrameWidth(array);

        } else if (feature.equalsIgnoreCase(array[8])) {
            final TextView selectedTextView = (TextView) selectedView;
            if (selectedTextView != null && selectedTextView.getPaint().getMaskFilter() != null) {
                Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.cant_apply));
            } else {
                shadowText(array);
            }
        } else if (feature.equalsIgnoreCase(array[9])) {
            applyFont(array);
        } else if (feature.equalsIgnoreCase(array[10])) {
            applyPhotoText(array);
        } else if (feature.equalsIgnoreCase(array[11])) {
            gradienteText(array);
        } else if (feature.equalsIgnoreCase(array[12])) {
            setCanvasSize(PreferenceManager.getDefaultSharedPreferences(EditorActivity.this).getInt(Constants.SAHRED_PREFS_DEVICE_HEIGHT_KEY, 1080));
        } else if (array[13].equalsIgnoreCase(feature)) {
            final TextView selectedTextView = (TextView) selectedView;

            if (selectedTextView != null && selectedTextView.getShadowDx() >= 1) {
                Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.remove_shadow));
            } else {
                textFx(array);

            }
        } else if (feature.equalsIgnoreCase(array[13])) {
//            hollowText(array);
        }
    }

    private void applyPhotoText(String[] array) {
        if (null != selectedView && selectedView instanceof TextView) {
            if (PermissionUtils.isPermissionAvailable(EditorActivity.this)) {
                previousState = selectedView;
                current_Text_feature = array[9];
                launchGallery(true);
            } else {
                requestAppPermissions_for_fonts();
            }
        } else {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        }


    }

    private void setCanvasSize(final int deviceHeight) {
        final Dialog dialog = new Dialog(EditorActivity.this, R.style.EditTextDialog);
        dialog.setContentView(R.layout.canvas_size_dialog_layout);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.EditTextDialog;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);
        dialog.setCancelable(false);
        LinearLayout root;
        RecyclerView sizesRv;


        root = dialog.findViewById(R.id.root);
        sizesRv = dialog.findViewById(R.id.sizes_rv);
        // AdView // AdView;
        // AdView = dialog.findViewById(R.id.adView);
        // AdUtils.loadBannerAd(adView, EditorActivity.this);
        TextUtils.findText_and_applyTypeface(root, EditorActivity.this);
        sizesRv.setLayoutManager(new GridLayoutManager(EditorActivity.this, 3));
        ArrayList<Integer> myImageList = new ArrayList<>();
        myImageList.add(R.drawable.ic_1_1);
        myImageList.add(R.drawable.ic_2_3);
        myImageList.add(R.drawable.ic_3_2);
        myImageList.add(R.drawable.ic_3_4);
        myImageList.add(R.drawable.ic_4_3);
        myImageList.add(R.drawable.ic_9_16);
        myImageList.add(R.drawable.ic_16_9);
        myImageList.add(R.drawable.ic_original);

        sizesRv.setAdapter(new SizesAdapter(EditorActivity.this, myImageList, new SizesAdapter.OnSizeClickListner() {
            @Override
            public void onSizeClicked(int position) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int deviceWidth;

                deviceWidth = displayMetrics.widthPixels;
                int totalval = 2 * deviceWidth;

                switch (position) {
                    case 0: {
//                        SIZE 1:1
                        setLayout(deviceWidth, deviceWidth, dialog);
                    }
                    break;
                    case 1: {
                        int singlepart = Math.round(5 / totalval);
                        int new_Width = 2 * singlepart > deviceWidth ? deviceWidth : 2 * singlepart;
                        int new_Height = 3 * singlepart > deviceHeight ? deviceHeight : 3 * singlepart;
                        setLayout(new_Width, new_Height, dialog);


                    }
                    break;
                    case 2: {
                        int singlepart = Math.round(totalval / 5);
                        int new_Width = 3 * singlepart > deviceWidth ? deviceWidth : 3 * singlepart;
                        int new_Height = 2 * singlepart > deviceHeight ? deviceHeight : 2 * singlepart;
                        setLayout(new_Width, new_Height, dialog);
                    }
                    break;
                    case 3: {
                        int singlepart = Math.round(totalval / 7);
                        int new_Width = 3 * singlepart > deviceWidth ? deviceWidth : 3 * singlepart;
                        int new_Height = 4 * singlepart > deviceHeight ? deviceHeight : 4 * singlepart;
                        setLayout(new_Width, new_Height, dialog);
                    }
                    break;
                    case 4: {
                        int singlepart = Math.round(totalval / 7);
                        int new_Width = 4 * singlepart > deviceWidth ? deviceWidth : 4 * singlepart;
                        int new_Height = 3 * singlepart > deviceHeight ? deviceHeight : 3 * singlepart;
                        setLayout(new_Width, new_Height, dialog);
                    }
                    break;
                    case 5: {
                        int singlepart = Math.round(totalval / 25);
                        int new_Width = 9 * singlepart > deviceWidth ? deviceWidth : 9 * singlepart;
                        int new_Height = 16 * singlepart > deviceHeight ? deviceHeight : 16 * singlepart;
                        setLayout(new_Width, new_Height, dialog);
                    }
                    break;
                    case 6: {
                        int singlepart = Math.round(totalval / 25);
                        int new_Width = 16 * singlepart > deviceWidth ? deviceWidth : 16 * singlepart;
                        int new_Height = 9 * singlepart > deviceHeight ? deviceHeight : 9 * singlepart;
                        setLayout(new_Width, new_Height, dialog);
                    }
                    break;
                    case 7: {
//                        int singlepart = Math.round(totalval / 25);
//                        int new_Width = 16 * singlepart > deviceWidth ? deviceWidth : 16 * singlepart;
//                        int new_Height = 9 * singlepart > deviceHeight ? deviceHeight : 9 * singlepart;
                        setLayout(deviceWidth, deviceHeight, dialog);
                    }
                    break;
                    default: {

                    }
                    break;
                }
            }
        }));
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(@NonNull DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
                    dialog.dismiss();
                    return true;
                } else {
                    return false;
                }
            }
        });
        dialog.show();
        dialog.setCancelable(false);


    }

    private void setLayout(int deviceWidth, int deviceHeight, @NonNull Dialog dialog) {
        RelativeLayout.LayoutParams relativeLayout;
        relativeLayout = new RelativeLayout.LayoutParams(deviceWidth, deviceHeight);
        relativeLayout.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.setMargins(0, 125, 0, 105);

        quoteLayout.setLayoutParams(relativeLayout);

        quoteLayout.setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            quoteLayout.setElevation(4f);
        }
        if (selected_picture != null) {
            imageView_background.setBackground(null);
            Glide.with(EditorActivity.this).load(selected_picture).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView_background);
        }
        dialog.dismiss();
    }

    private void enable_Selected_Background_Features(@Nullable String clickedItem, final String[] bgfeaturesArray) {
        if (clickedItem != null) {
            if (clickedItem.equalsIgnoreCase(bgfeaturesArray[1])) {
                current_Bg_feature = bgfeaturesArray[1];
                if (PermissionUtils.isPermissionAvailable(EditorActivity.this)) {
                    handle_gallery_dialog();
                } else {
                    requestAppPermissions_for_launch_gallery();
                }
            } else if (clickedItem.equalsIgnoreCase(bgfeaturesArray[2])) {
                current_Bg_feature = bgfeaturesArray[2];
                selected_picture = null;
                final Handler handler = new Handler();
                Runnable backGroundColorTask = new Runnable() {
                    @Override
                    public void run() {
                        colorbg(bgfeaturesArray);

                    }
                };
                handler.removeCallbacks(backGroundColorTask);
                handler.post(backGroundColorTask);

            } else if (clickedItem.equalsIgnoreCase(bgfeaturesArray[3])) {
                current_Bg_feature = bgfeaturesArray[3];
                if (selected_picture != null) {
                    blurrImage(bgfeaturesArray);
                } else {
                    Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.please_select_image));
                }
            } else if (clickedItem.equalsIgnoreCase(bgfeaturesArray[4])) {
                current_Bg_feature = bgfeaturesArray[4];
                applyBlackFilter();
            } else if (clickedItem.equalsIgnoreCase(bgfeaturesArray[0])) {
                current_Bg_feature = bgfeaturesArray[0];
                if (InternetUtils.isConnectedtoNet(EditorActivity.this) == true) {
                    seachImages();
//                    addSticker("https://firebasestorage.googleapis.com/v0/b/nimble-card-239502.appspot.com/o/stickers%2F1.png?alt=media&token=87c9262c-ad8e-4bb1-b7f0-d741df62c1ba");

                } else {
                    Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.internet_required_images));
                }
            } else if (clickedItem.equalsIgnoreCase(bgfeaturesArray[5])) {
                current_Bg_feature = bgfeaturesArray[5];
                applyWhiteFilter();
            } else if (clickedItem.equalsIgnoreCase(bgfeaturesArray[6])) {
                selected_picture = null;
                current_Bg_feature = bgfeaturesArray[6];
                bringGradients();


            } else if (clickedItem.equalsIgnoreCase(bgfeaturesArray[7])) {

            }
        } else {
            Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.please_select_feature_again));
        }

    }

    private void seachImages() {
        final Dialog dialog = new Dialog(EditorActivity.this, R.style.EditTextDialog_non_floater);
        dialog.setContentView(R.layout.search_images);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog_non_floater;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);
        dialog.setCancelable(false);

        // AdView // AdView;
        // AdView = dialog.findViewById(R.id.adView);


        RelativeLayout scrool;
        RelativeLayout root;
        TextView searchHeader;
        LinearLayout searchBoxView;
        final EditText searchBox;
        ImageView searchButton;
        final ProgressBar progressBar;
        final RecyclerView imagesRecycler;
        TextView diclaimer;
        ImagesAdapter imagesAdapter;


        scrool = dialog.findViewById(R.id.scrool);
        root = dialog.findViewById(R.id.root);
        searchHeader = dialog.findViewById(R.id.search_header);
        searchBoxView = dialog.findViewById(R.id.search_Box_view);
        searchBox = dialog.findViewById(R.id.search_box);
        searchButton = dialog.findViewById(R.id.search_button);
        progressBar = dialog.findViewById(R.id.progress_bar);
        imagesRecycler = dialog.findViewById(R.id.images_recycler);
        // AdView = dialog.findViewById(R.id.adView);
        diclaimer = dialog.findViewById(R.id.diclaimer);
        getRandomImages("Hello", imagesRecycler, progressBar, dialog);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchBox.getText() == null) {
                    searchBox.setError(getString(R.string.please_enter));
                } else {
                    getRandomImages(searchBox.getText().toString(), imagesRecycler, progressBar, dialog);

                }
            }
        });

        TextUtils.findText_and_applyTypeface(root, EditorActivity.this);
        // AdUtils.loadBannerAd(adView, EditorActivity.this);
        dialog.setCancelable(false);
        dialog.show();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(@NonNull DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
                    dialog.dismiss();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public void getRandomImages(@Nullable String word, @NonNull final RecyclerView recyclerView, @NonNull final ProgressBar progressBar, @NonNull final Dialog dialog) {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(null);
        recyclerView.setVisibility(GONE);
        recyclerView.setLayoutManager(new GridLayoutManager(EditorActivity.this, 2));

        String request = null;
//        if (word == null) {
//            request = Constants.API_GET_IMAGES_FROM_PIXABAY + "&per_page=150" + "&order=popular";
//        } else {
//            request = Constants.API_GET_IMAGES_FROM_PIXABAY + "&q=" + word + "&per_page=150" + "&order=popular";
//
//        }
        new Restutility(EditorActivity.this).getStickersByQuery(EditorActivity.this, new StickerResponseListener() {
            @Override
            public void onSuccess(String responseJson) {
                Data data = new Gson().fromJson(responseJson, Data.class);
                progressBar.setVisibility(GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(new ImagesAdapter(EditorActivity.this, data.getData(), new ImagesAdapter.ImagesOnClickListner() {
                    @Override
                    public void onImageClicked(String stillImage, String gifImage, int frameCount) {
//                        selected_picture = stillImage;
                        addSticker(stillImage, frameCount);
//                        imageView_background.setBackground(null);
//                        Glide.with(EditorActivity.this)
//                                .load(stillImage)
//
//                                .centerCrop()
//                                .placeholder(ContextCompat.getDrawable(getApplicationContext(), (R.drawable.ic_quotation_mark)))
//                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                                .into(imageView_background);
                        dialog.dismiss();
                    }
                }));
            }

            @Override
            public void onError(String message) {
                Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, message);
            }
        }, word);
    }

    private void addSticker(String Biglink, int frameCount) {
        final StickerImageView stickerImageView = new StickerImageView(EditorActivity.this);
        Glide.with(EditorActivity.this).load(Biglink).into(((ImageView) stickerImageView.getMainView()));
        core_editor_layout.addView(stickerImageView);
        if (null != doodleView) {
            disableDoodleView();

        }
        if (gifFrameCount <= 0) {
            gifFrameCount = frameCount;
        } else if (gifFrameCount <= frameCount) {
            gifFrameCount = frameCount;
        }
        if (null != selected_sticker) {
            selected_sticker.setControlItemsHidden(true);
            previousselctedView = selected_sticker;
        }
        selected_sticker = stickerImageView;
        stickerImageView.setOnTouchListener(this);
    }

    /*HNADLER METHODS*/
    private void handle_gallery_dialog() {

        final Dialog dialog = new Dialog(EditorActivity.this);
        dialog.setContentView(R.layout.image_fit_type_layout_dialog);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);
        dialog.setCancelable(false);

        // AdView // AdView;
        // AdView = dialog.findViewById(R.id.adView);
        // AdUtils.loadBannerAd(adView, EditorActivity.this);
        LinearLayout root;
        final RadioGroup radioGroup;
        final RadioButton crop, fit, none;
        Button launch;
        root = dialog.findViewById(R.id.root_Lo);
        radioGroup = dialog.findViewById(R.id.rd_group);
        none = dialog.findViewById(R.id.rb_none);
        crop = dialog.findViewById(R.id.rb_centerCrop);
        fit = dialog.findViewById(R.id.rb_fitcenter);
        launch = dialog.findViewById(R.id.bt_save);
        TextUtils.findText_and_applyTypeface(root, EditorActivity.this);
        launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (crop.getId() == radioGroup.getCheckedRadioButtonId()) {
                    imageFitType = 1;
                } else if (fit.getId() == radioGroup.getCheckedRadioButtonId()) {
                    imageFitType = 2;
                } else {
                    imageFitType = 3;
                }
                launchGallery(false);
                dialog.cancel();

            }
        });
        dialog.show();

    }

    private void handle_bg_seekbar(@NonNull SeekBar seekBar) throws NullPointerException {
        String[] featuresLocalArray = getResources().getStringArray(R.array.Background_features);
        if (current_module == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.we_missed_bg));
        } else {
            if (Constants.BG.equalsIgnoreCase(current_module)) {
                if (current_Bg_feature.equalsIgnoreCase(featuresLocalArray[3]) && seekBar.getProgress() >= 0) {
                    Glide.with(EditorActivity.this).load(selected_picture).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView_background);
                    if (seekBar.getProgress() == 0) {
                        Glide.with(EditorActivity.this).load(selected_picture).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView_background);
                    } else {
                        imageView_background.setDrawingCacheEnabled(true);
                        imageView_background.buildDrawingCache();
                        if (imageView_background.getDrawingCache().isRecycled()) {

                        } else {
//                        Bitmap bitmap = imageView_background.getDrawingCache();
//                        float blurr_progress = seekBar.getProgress() <= 0 ? 0.1f : seekBar.getProgress() / 15;
//                        Bitmap newblurredImage = create_blur(bitmap, blurr_progress);
//                        imageView_background.setImageBitmap(newblurredImage);
                            imageView_background.destroyDrawingCache();
                        }
                        Bitmap bitmap = imageView_background.getDrawingCache();
                        float radius;
                        if (Math.round(seekBar.getProgress() / 15) <= 0) {
                            radius = 0.1f;
                        } else {
                            radius = Math.round(seekBar.getProgress() / 15);
                        }
                        float blurr_progress = seekBar.getProgress() <= 0 ? 0.1f : radius;
                        Bitmap newblurredImage = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            newblurredImage = ImageProcessingUtils.create_blur(bitmap, blurr_progress, EditorActivity.this);
                        } else {
                            Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.sorry));
                        }
                        imageView_background.setImageBitmap(newblurredImage);
                    }
                } else if ((current_Bg_feature.equalsIgnoreCase(featuresLocalArray[4]) || current_Bg_feature.equalsIgnoreCase(featuresLocalArray[5])) && seekBar.getProgress() >= 0) {
                    double opacity = ((double) ((seekBar.getProgress() * 10) / 360) / 10);
                    light_effect_filter_IV.setAlpha((float) opacity);
                }
            }
        }

    }

    private void handle_seekbar_value(SeekBar seekBar) throws NullPointerException {
        float radius = (float) seekBar.getProgress();
        if (current_module == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.click_text));
        } else {
            if (Constants.TEXT.equalsIgnoreCase(current_module)) {
                if (selectedView != null) {
                    TextView selected_textView = (TextView) selectedView;
                    if (current_Text_feature.equalsIgnoreCase(getResources()
                            .getStringArray(R.array.Text_features)[2]) && radius >= 0) {
                        float size = radius / 2;
                        selected_textView.setTextSize(size);
                    } else if (current_Text_feature.equalsIgnoreCase(getResources()
                            .getStringArray(R.array.Text_features)[3]) && radius >= 0) {
                        float degree = radius - 180;
                        selected_textView.setRotation(degree);
                    } else if (current_Text_feature.equalsIgnoreCase(getResources()
                            .getStringArray(R.array.Text_features)[4]) && radius >= 0) {
                        float degree = radius / 500;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            selected_textView.setLetterSpacing(degree);
                        } else {
                            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.sorry));
                        }
                    } else if (current_Text_feature.equalsIgnoreCase(getResources()
                            .getStringArray(R.array.Text_features)[6]) && radius >= 0) {
                        float degrer = radius / 100;
                        selected_textView.setLineSpacing(15, degrer);
                    } else if (current_Text_feature.equalsIgnoreCase(getResources().getStringArray(R.array.Text_features)[7]) && radius >= 0) {
                        int degrer = (int) radius * 3;
//                Log.d("PADDIG DEGEREE", degrer + "");
                        selected_textView.setMaxWidth(degrer);
                    }
                } else {
                    Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.click_text));
                }

            }
        }
    }

    private void handle_close_Feature() throws NullPointerException {
        if (current_module == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.click_text));
        } else {
            if (current_module.equalsIgnoreCase(Constants.TEXT)) {
                TextView current_text_view = (TextView) selectedView;
                if (current_Text_feature.equalsIgnoreCase(getResources().getStringArray(R.array.Text_features)[2])) {
                    current_text_view.setTextSize(25);
                }
                if (current_Text_feature.equalsIgnoreCase(getResources().getStringArray(R.array.Text_features)[3])) {
                    current_text_view.setRotation(0);
                }
                if (current_Text_feature.equalsIgnoreCase(getResources().getStringArray(R.array.Text_features)[4])) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        current_text_view.setLetterSpacing(0);
                    }
                }
                if (current_Text_feature.equalsIgnoreCase(getResources().getStringArray(R.array.Text_features)[6])) {
                    current_text_view.setLineSpacing(15, 1.0f);
                }
                if (current_Text_feature.equalsIgnoreCase(getResources().getStringArray(R.array.Text_features)[7])) {
                    current_text_view.setPadding(16, 16, 16, 16);
                }
            } else if (current_module.equalsIgnoreCase(Constants.BG)) {
                if (current_Bg_feature.equalsIgnoreCase(getResources().getStringArray(R.array.Background_features)[2])) {
                    if (selected_picture != null) {
                        Glide.with(EditorActivity.this).load(selected_picture).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView_background);

                    } else {

                    }

                } else if (current_Bg_feature.equalsIgnoreCase(getResources().getStringArray(R.array.Background_features)[3]) || current_Bg_feature.equalsIgnoreCase(getResources().getStringArray(R.array.Background_features)[4])) {
                    light_effect_filter_IV.setBackground(null);
                }
            }

        }
    }

    private void handleTouchForStickerView(StickerImageView currentlySelectedSticker) {
        if (previousselctedView != null) {
            if (previousselctedView instanceof StickerImageView) {
                ((StickerImageView) previousselctedView).setControlItemsHidden(true);
            } else {
                previousselctedView.setBackground(null);
            }
            previousselctedView = selected_sticker;
            clear_button.setVisibility(View.VISIBLE);
            assert selected_sticker != null;
            selected_sticker.setControlItemsHidden(false);
            selected_sticker.bringToFront();
        } else {
            clear_button.setVisibility(View.VISIBLE);
            previousselctedView = currentlySelectedSticker;
            Objects.requireNonNull(selected_sticker).setControlItemsHidden(false);
        }
    }

    private void handleTouchForTextView(View currentlySelectedTextView) {
        if (previousselctedView != null) {
            if (previousselctedView instanceof StickerImageView) {
                ((StickerImageView) previousselctedView).setControlItemsHidden(true);
            } else {
                previousselctedView.setBackground(null);
            }
            previousselctedView = currentlySelectedTextView;
            clear_button.setVisibility(View.VISIBLE);
            Objects.requireNonNull(selectedView).setBackground(ContextCompat.getDrawable(EditorActivity.this, R.drawable.tv_bg));
        } else {
            clear_button.setVisibility(View.VISIBLE);
            previousselctedView = currentlySelectedTextView;
            Objects.requireNonNull(selectedView).setBackground(ContextCompat.getDrawable(EditorActivity.this, R.drawable.tv_bg));
        }
    }
    /*HANDLER METHODS*/

    //    FEATURES
// THIS MEHOD CAN't be moved outside becuase it has lot of dependies in this page.
    private void addText(String[] array, final boolean isEdit) {
        current_Text_feature = array[0];
        final int[] alignment = new int[1];
        final Dialog dialog = new Dialog(this, R.style.EditTextDialog);
        dialog.setContentView(View.inflate(this, R.layout.addtext, null));
        final TextView header, align_text;
        final EditText addingText;
        // AdView // AdView;
        final Button close, done, start, center, end, add_bg;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
        dialog.setCancelable(false);
        header = dialog.findViewById(R.id.tv_header);
        align_text = dialog.findViewById(R.id.tv_align_text);
        addingText = dialog.findViewById(R.id.et_text);
        close = dialog.findViewById(R.id.bt_close);
        done = dialog.findViewById(R.id.bt_done);
        start = dialog.findViewById(R.id.bt_start);
        center = dialog.findViewById(R.id.bt_center);
        end = dialog.findViewById(R.id.bt_end);
        add_bg = dialog.findViewById(R.id.bt_add_bg);

        // AdView = dialog.findViewById(R.id.adView);
        // AdUtils.loadBannerAd(adView, EditorActivity.this);
        TextUtils.setFont(this, header, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, addingText, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, align_text, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, start, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, center, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, end, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, close, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, done, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, add_bg, Constants.FONT_CIRCULAR);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alignment[0] = 1;
                addingText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            }
        });
        center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alignment[0] = 2;
                addingText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alignment[0] = 3;
                addingText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            }
        });

        add_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.LTGRAY);
                spannableString = new SpannableString(addingText.getText().toString());
                spannableString.setSpan(backgroundColorSpan, 0, addingText.getText().toString().length(), 0);
                addingText.setText("");
                addingText.setText(spannableString);
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    set_text_alignment(alignment[0], ((TextView) selectedView));
                    ((TextView) selectedView).setText(addingText.getText());
                } else {
                    addedTextIds++;
                    newly_Added_Text = addingText.getText().toString();
                    if (newly_Added_Text.length() <= 0) {
                        addingText.setError(getString(R.string.please_enter));
                    } else {
                        final TextView textView = new TextView(EditorActivity.this);
                        textView.setTextSize(20.0f);
                        textView.setTextColor(getResources().getColor(R.color.textColor));
                        textView.setMaxWidth(core_editor_layout.getWidth());
                        TextUtils.setFont(EditorActivity.this, textView, Constants.FONT_CIRCULAR);

                        textView.setText(new StringBuilder().append(newly_Added_Text).append(" ").toString());

                        textView.setX(core_editor_layout.getWidth() - (core_editor_layout.getWidth() - 100));

//                        textView.setY(core_editor_layout.getHeight() / 2);
                        textView.setId(addedTextIds);
                        textView.setOnClickListener(v1 -> Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, new StringBuilder().append(" ").append(textView.getId()).append(" ").toString()));
                        set_text_alignment(alignment[0], textView);

                        textView.setPadding(8, 8, 8, 8);
                        textView.setTextColor(Color.WHITE);
                        textView.setLongClickable(true);
                        textView.setOnLongClickListener(EditorActivity.this);
                        textView.setOnTouchListener(EditorActivity.this);
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextDirection(View.TEXT_DIRECTION_LOCALE);
                        core_editor_layout.addView(textView);
                        if (null != doodleView) {
                            disableDoodleView();

                        }
                    }


                }

                dialog.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    private void editText(String[] array, final boolean isEdit) {
        current_Text_feature = array[0];
        final int[] alignment = new int[1];
        final Dialog dialog = new Dialog(this, R.style.EditTextDialog);
        dialog.setContentView(View.inflate(this, R.layout.edittext_dialog, null));
        final TextView header, align_text;
        final EditText addingText;
        final RecyclerView recyclerView;
        // AdView // AdView;
        final Button close, done, start, center, end, previewText, bold, underline, italic, strikethrough, highlight, colorText;
        try {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
        dialog.setCancelable(false);

        header = dialog.findViewById(R.id.tv_header);
        align_text = dialog.findViewById(R.id.tv_align_text);
        addingText = dialog.findViewById(R.id.et_text);
        close = dialog.findViewById(R.id.bt_close);
        done = dialog.findViewById(R.id.bt_done);
        start = dialog.findViewById(R.id.bt_start);
        center = dialog.findViewById(R.id.bt_center);
        end = dialog.findViewById(R.id.bt_end);
        previewText = dialog.findViewById(R.id.bt_add_bg);
        recyclerView = dialog.findViewById(R.id.color_rv);
        // AdView = dialog.findViewById(R.id.adView);
        recyclerView.setLayoutManager(new LinearLayoutManager(EditorActivity.this, RecyclerView.HORIZONTAL, false));
        // AdUtils.loadBannerAd(adView, EditorActivity.this);
        bold = dialog.findViewById(R.id.bt_bold);
        underline = dialog.findViewById(R.id.bt_underline);
        italic = dialog.findViewById(R.id.bt_italic);
        strikethrough = dialog.findViewById(R.id.bt_strikethrough);
        highlight = dialog.findViewById(R.id.bt_highlight);
        colorText = dialog.findViewById(R.id.bt_colored);

//        spannableString = new SpannableString(addingText.getText().toString());

        addingText.setText(((TextView) selectedView).getText());
        TextUtils.setFont(this, header, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, addingText, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, align_text, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, start, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, center, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, end, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, bold, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, underline, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, italic, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, strikethrough, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, highlight, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, colorText, Constants.FONT_CIRCULAR);

        TextUtils.setFont(this, done, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, close, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, previewText, Constants.FONT_CIRCULAR);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alignment[0] = 1;
                recyclerView.setVisibility(GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    addingText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                } else {
                    Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.sorry));
                }
            }
        });
        center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alignment[0] = 2;
                recyclerView.setVisibility(GONE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    addingText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                } else {
                    Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.sorry));
                }

            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alignment[0] = 3;
                recyclerView.setVisibility(GONE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    addingText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                } else {
                    Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.sorry));
                }

            }
        });

        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(GONE);

                setSpan(new StyleSpan(Typeface.BOLD), addingText);


            }


        });
        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(GONE);
                setSpan(new UnderlineSpan(), addingText);
            }
        });
        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(GONE);
                setSpan(new StyleSpan(Typeface.ITALIC), addingText);

            }
        });
        strikethrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(GONE);
                setSpan(new StrikethroughSpan(), addingText);
            }
        });
        highlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                final int[] selectedColor = new int[1];
                recyclerView.setAdapter(new ColorsAdapter(EditorActivity.this, new ColorsAdapter.OnColorClickListener() {
                    @Override
                    public void onColorClick(int color) {
                        selectedColor[0] = color;
                        setSpan(new BackgroundColorSpan(selectedColor[0]), addingText);
                    }
                }));
            }
        });

        colorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                final int[] selectedColor = new int[1];
                recyclerView.setAdapter(new ColorsAdapter(EditorActivity.this, new ColorsAdapter.OnColorClickListener() {
                    @Override
                    public void onColorClick(int color) {
                        selectedColor[0] = color;
                        setSpan(new ForegroundColorSpan(selectedColor[0]), addingText);
                    }
                }));
            }
        });

//        previewText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.LTGRAY);
//                spannableString.setSpan(backgroundColorSpan, 0, addingText.getText().toString().length(), 0);
//                addingText.setText("");
//                addingText.setText(spannableString);
//            }
//        });
        done.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    set_text_alignment(alignment[0], ((TextView) selectedView));
                    ((TextView) selectedView).setText(addingText.getText());
                } else {
                    addedTextIds++;
                    newly_Added_Text = addingText.getText().toString();
                    if (newly_Added_Text.length() <= 0) {
                        addingText.setError(getString(R.string.please_enter));
                    } else {
                        final TextView textView = new TextView(EditorActivity.this);
                        textView.setTextSize(20.0f);
                        textView.setTextColor(getResources().getColor(R.color.textColor));
                        textView.setMaxWidth(core_editor_layout.getWidth());
                        TextUtils.setFont(EditorActivity.this, textView, Constants.FONT_CIRCULAR);
                        if (spannableString != null) {
                            textView.setText(spannableString);
                        } else {
                            textView.setText(newly_Added_Text);

                        }
                        textView.setX(core_editor_layout.getWidth() / 2 - 250);
//                        textView.setY(core_editor_layout.getHeight() / 2);
                        textView.setId(addedTextIds);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, " " + textView.getId() + " ");
                            }
                        });
                        set_text_alignment(alignment[0], textView);

                        textView.setPadding(16, 16, 16, 16);
                        textView.setOnTouchListener(EditorActivity.this);
                        textView.setOnLongClickListener(EditorActivity.this);
                        textView.setLongClickable(true);

                        core_editor_layout.addView(textView);

                        if (null != doodleView) {
                            disableDoodleView();

                        }
                    }


                }

                dialog.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    public void setSpan(Object styleSpan, @NonNull EditText addingText) {
        spannableString = new SpannableString(addingText.getText().toString());
        spannableString.setSpan(styleSpan, 0, addingText.getText().toString().length(), 0);
        setSpannableText(addingText, spannableString);

    }

    private void setSpannableText(EditText addingText, SpannableString spannableString) {
        addingText.setText("");
        addingText.setText(spannableString);
    }

    private void resizeText(String[] array) {
        /*RESIZE*/
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            featuresRecyclerview.setVisibility(GONE);
            seekBar.setVisibility(View.VISIBLE);
            text_and_bg_layout.setVisibility(GONE);
            close_and_done_layout.setVisibility(View.VISIBLE);
            previousState = selectedView;
            current_Text_feature = array[2];


        }
    }

    private void rotateText(String[] array) {
        /*RESIZE*/
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            featuresRecyclerview.setVisibility(GONE);
            seekBar.setVisibility(View.VISIBLE);
            text_and_bg_layout.setVisibility(GONE);
            close_and_done_layout.setVisibility(View.VISIBLE);
            previousState = selectedView;
            current_Text_feature = array[3];


        }
    }

    private void spaceText(String[] array) {
        /*RESIZE*/
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            featuresRecyclerview.setVisibility(GONE);
            seekBar.setVisibility(View.VISIBLE);
            text_and_bg_layout.setVisibility(GONE);
            close_and_done_layout.setVisibility(View.VISIBLE);
            previousState = selectedView;
            current_Text_feature = array[4];


        }
    }

    private void spaceLine(String[] array) {
        /*RESIZE*/
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            featuresRecyclerview.setVisibility(GONE);
            seekBar.setVisibility(View.VISIBLE);
            text_and_bg_layout.setVisibility(GONE);
            close_and_done_layout.setVisibility(View.VISIBLE);
            previousState = selectedView;
            current_Text_feature = array[6];


        }
    }

    private void adjustFrameWidth(String[] array) {
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            featuresRecyclerview.setVisibility(GONE);
            seekBar.setVisibility(View.VISIBLE);
            text_and_bg_layout.setVisibility(GONE);
            close_and_done_layout.setVisibility(View.VISIBLE);
            previousState = selectedView;
            current_Text_feature = array[7];


        }
    }

    private void colorText(String[] array) {
        final TextView selectedTextView = (TextView) selectedView;
//        RelativeLayout.LayoutParams params = ((RelativeLayout.LayoutParams) selectedTextView.getLayoutParams());
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            current_Text_feature = array[5];
            final Dialog dialog = new Dialog(this,R.style.EditTextDialog);
            dialog.setContentView(R.layout.colors_dialog_layout);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);
            dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;

            final TextView head_tv, demo_tv;
            // AdView // AdView;
            RecyclerView colors_rv;
            MaterialButton close, choose;
            ColorsAdapter colorsAdapter;
            final int[] chooser_color = new int[1];


            head_tv = dialog.findViewById(R.id.color_text);
            demo_tv = dialog.findViewById(R.id._demo_color_text);
            colors_rv = dialog.findViewById(R.id.colors_rv);
            close = dialog.findViewById(R.id.bt_color_close);
            choose = dialog.findViewById(R.id.bt_color_choose);
            dialog.findViewById(R.id.bt_color_shadow);
            // AdView = dialog.findViewById(R.id.adView);
            TextUtils.setFont(this, head_tv, Constants.FONT_CIRCULAR);
            TextUtils.setFont(this, close, Constants.FONT_CIRCULAR);
            TextUtils.setFont(this, choose, Constants.FONT_CIRCULAR);
            // AdUtils.loadBannerAd(adView, EditorActivity.this);
            colors_rv.setLayoutManager(new GridLayoutManager(this, 6));

            colorsAdapter = new ColorsAdapter(this, color -> {
                chooser_color[0] = color;
                demo_tv.setTextColor(color);

            });
            close.setOnClickListener(v -> dialog.dismiss());
            choose.setOnClickListener(v -> {
                if (chooser_color.length < 0) {
                    Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.select_colors_apply));
                } else {
                    dialog.dismiss();
                    selectedTextView.getPaint().setShader(null);
                    selectedTextView.setTextColor(chooser_color[0]);
                }

            });
            dialog.setCancelable(false);
            colors_rv.setAdapter(colorsAdapter);
            dialog.show();
        }
    }

    private void colorDoodle() {
//        RelativeLayout.LayoutParams params = ((RelativeLayout.LayoutParams) selectedTextView.getLayoutParams());
        if (doodleView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            final Dialog dialog = new Dialog(this, R.style.EditTextDialog);
            dialog.setContentView(R.layout.colors_dialog_layout);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.white_rounded_drawable);

            dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;

            final TextView head_tv, demo_tv;
            // AdView // AdView;
            RecyclerView colors_rv;
            Button close, choose;
            ColorsAdapter colorsAdapter;
            final int[] chooser_color = new int[1];


            head_tv = dialog.findViewById(R.id.color_text);
            demo_tv = dialog.findViewById(R.id._demo_color_text);
            colors_rv = dialog.findViewById(R.id.colors_rv);
            close = dialog.findViewById(R.id.bt_color_close);
            choose = dialog.findViewById(R.id.bt_color_choose);
            dialog.findViewById(R.id.bt_color_shadow);
            // AdView = dialog.findViewById(R.id.adView);
            TextUtils.setFont(this, head_tv, Constants.FONT_CIRCULAR);
            TextUtils.setFont(this, close, Constants.FONT_CIRCULAR);
            TextUtils.setFont(this, choose, Constants.FONT_CIRCULAR);
            // AdUtils.loadBannerAd(adView, EditorActivity.this);
            colors_rv.setLayoutManager(new GridLayoutManager(this, 6));

            colorsAdapter = new ColorsAdapter(this, new ColorsAdapter.OnColorClickListener() {
                @Override
                public void onColorClick(int color) {
                    chooser_color[0] = color;
                    demo_tv.setTextColor(color);

                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doodleView.setSetColor(chooser_color[0]);
                    dialog.dismiss();

                }
            });
            dialog.setCancelable(false);
            colors_rv.setAdapter(colorsAdapter);
            dialog.show();
        }
    }

    private void shadowText(String[] array) {
        final TextView selectedTextView = (TextView) selectedView;
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            previousState = selectedView;
            current_Text_feature = array[8];
            TextFeatures.apply_Text_Shadow(EditorActivity.this, selectedTextView);


        }
    }

    private void applyFont(String[] array) {
        final TextView selectedTextView = (TextView) selectedView;

        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            if (PermissionUtils.isPermissionAvailable(EditorActivity.this)) {
                previousState = selectedView;
                current_Text_feature = array[9];
                TextFeatures.applyFont(EditorActivity.this, selectedTextView);
            } else {
                requestAppPermissions_for_fonts();
            }


        }
    }

    private void gradienteText(String[] array) {
        final TextView selectedTextView = (TextView) selectedView;
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            previousState = selectedView;
            current_Text_feature = array[11];
            TextFeatures.setGradients(EditorActivity.this, selectedTextView);
        }
    }

    private void textFx(String[] array) {
        final TextView selectedTextView = (TextView) selectedView;
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            previousState = selectedView;
            current_Text_feature = array[13];
            TextFeatures.setVfx(EditorActivity.this, selectedTextView);
        }
    }

    private void hollowText(String[] array) {
        final TextView selectedTextView = (TextView) selectedView;
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            previousState = selectedView;
            current_Text_feature = array[13];
            TextFeatures.setHollowText(EditorActivity.this, selectedTextView);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void set_text_alignment(int position, @NonNull TextView textView) {
        switch (position) {
            case 1:
                textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                break;
            case 2:
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                break;
            case 3:
                textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                break;
        }

    }

    // BG FEATURES
    private void colorbg(String[] array) {
//        RelativeLayout.LayoutParams params = ((RelativeLayout.LayoutParams) selectedTextView.getLayoutParams());

        final Dialog dialog = new Dialog(this,R.style.EditTextDialog);
        dialog.setContentView(R.layout.colors_dialog_layout);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);

        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;

        final TextView head_tv, demo_tv;
        RecyclerView colors_rv;
        Button close, choose;
        ColorsAdapter colorsAdapter;
        final int[] choosen_color = new int[1];

        // AdView // AdView;
        head_tv = dialog.findViewById(R.id.color_text);
        demo_tv = dialog.findViewById(R.id._demo_color_text);
        colors_rv = dialog.findViewById(R.id.colors_rv);
        close = dialog.findViewById(R.id.bt_color_close);
        choose = dialog.findViewById(R.id.bt_color_choose);
        // AdView = dialog.findViewById(R.id.adView);
        TextUtils.setFont(this, head_tv, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, close, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, choose, Constants.FONT_CIRCULAR);
        // AdUtils.loadBannerAd(adView, EditorActivity.this);
        colors_rv.setLayoutManager(new GridLayoutManager(this, 6));

        colorsAdapter = new ColorsAdapter(this, new ColorsAdapter.OnColorClickListener() {
            @Override
            public void onColorClick(int color) {
                choosen_color[0] = color;
                demo_tv.setTextColor(color);

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (choosen_color.length < 0) {
                    Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.select_colors_apply));
                } else {
                    dialog.dismiss();
                    imageView_background.setImageDrawable(null);
                    imageView_background.setBackgroundColor(choosen_color[0]);
                }

            }
        });
        dialog.setCancelable(false);
        colors_rv.setAdapter(colorsAdapter);
        dialog.show();

    }

    private void bringGradients() {
        final Dialog dialog = new Dialog(EditorActivity.this);
        dialog.setContentView(R.layout.gradient_bg_layout);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);

        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
        // AdView // AdView;
        RelativeLayout relativeLayout;
        TextView headerTv;
        final TextView demoGradient;
        final ImageView demoColor1;
        final ImageView demoColor2;
        final TextView demoColor1Tv;
        final TextView demoColor2Tv;
        final RecyclerView colorsRecycler;
        final Button preview;
        Button btCancel;
        Button btApply;
        final ColorsAdapter colorsAdapter;
        final ColorsAdapter colorsAdapter2;
        final GradientDrawable[] output_drawable = new GradientDrawable[1];

        final int[] firstColor = {0};
        final int[] secondColor = {0};
        final int[] selected_color = {0};

        relativeLayout = dialog.findViewById(R.id.root_Rl);
        demoGradient = dialog.findViewById(R.id.demo_gradient);
        demoColor1 = dialog.findViewById(R.id.demo_color_1);
        demoColor2 = dialog.findViewById(R.id.demo_color_2);
        demoColor1Tv = dialog.findViewById(R.id.demo_color_1_tv);
        demoColor2Tv = dialog.findViewById(R.id.demo_color_2_tv);
        colorsRecycler = dialog.findViewById(R.id.colors_recycler);
        preview = dialog.findViewById(R.id.preview);
        btCancel = dialog.findViewById(R.id.bt_cancel);
        btApply = dialog.findViewById(R.id.bt_apply);
        // AdView = dialog.findViewById(R.id.adView);
        colorsRecycler.setVisibility(GONE);
        preview.setVisibility(GONE);
        // AdUtils.loadBannerAd(adView, EditorActivity.this);
        colorsRecycler.setLayoutManager(new LinearLayoutManager(EditorActivity.this, LinearLayoutManager.HORIZONTAL, false));
        TextUtils.findText_and_applyTypeface(relativeLayout, EditorActivity.this);
        colorsAdapter = new ColorsAdapter(EditorActivity.this, new ColorsAdapter.OnColorClickListener() {
            @Override
            public void onColorClick(int color) {
                firstColor[0] = color;
                demoColor1.setBackgroundColor(color);
                demoColor1Tv.setTextColor(color);
                preview.setVisibility(View.VISIBLE);

            }
        });
        colorsAdapter2 = new ColorsAdapter(EditorActivity.this, new ColorsAdapter.OnColorClickListener() {
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
                output_drawable[0] = AnimUtils.createDrawable(firstColor[0], secondColor[0], EditorActivity.this);
                demoGradient.setBackground(output_drawable[0]);
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
                dialog.dismiss();
                imageView_background.setImageDrawable(null);
                if (output_drawable[0] == null) {
                    output_drawable[0] = AnimUtils.createDrawable(firstColor[0], secondColor[0], EditorActivity.this);
                }
                selected_picture = null;
                imageView_background.setBackground(output_drawable[0]);
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    private void blurrImage(String[] bgfeaturesArray) {
        featuresRecyclerview.setVisibility(GONE);
        seekBar.setVisibility(View.VISIBLE);
        text_and_bg_layout.setVisibility(GONE);
        close_and_done_layout.setVisibility(View.VISIBLE);
    }

    private void applyBlackFilter() {
        light_effect_filter_IV.setBackground(ContextCompat.getDrawable(EditorActivity.this, android.R.drawable.screen_background_dark_transparent));
        featuresRecyclerview.setVisibility(GONE);
        seekBar.setVisibility(View.VISIBLE);
        text_and_bg_layout.setVisibility(GONE);
        close_and_done_layout.setVisibility(View.VISIBLE);
    }

    private void applyWhiteFilter() {
        light_effect_filter_IV.setBackground(ContextCompat.getDrawable(EditorActivity.this, android.R.drawable.screen_background_light_transparent));
        featuresRecyclerview.setVisibility(GONE);
        seekBar.setVisibility(View.VISIBLE);
        text_and_bg_layout.setVisibility(GONE);
        close_and_done_layout.setVisibility(View.VISIBLE);
    }

    private void launchGallery(boolean isForPhotoText) {
        this.isForPhotoText = isForPhotoText;
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }



    /*PERMISSION REQUEST METHODS*/

    private void requestAppPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, (MY_PERMISSION_REQUEST_STORAGE_FROM_ONSTART));
    }

    private void requestAppPermissions_to_save_to_gallery() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, (MY_PERMISSION_REQUEST_STORAGE_FOR_SAVING_TO_GALLERY));
    }

    private void requestAppPermissions_for_fonts() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, (MY_PERMISSION_REQUEST_STORAGE_FOR_FONTS));
    }

    private void requestAppPermissions_for_launch_gallery() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, (MY_PERMISSION_REQUEST_Launch_gallery));
    }
    /*PERMISSION REQUEST METHODS*/


    private class SimpleOnscaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(@NonNull ScaleGestureDetector detector) {
            if (selectedView != null) {
                View selected = selectedView;
                if (selected instanceof TextView) {
                    TextView selectedText = ((TextView) selected);
                    float size = selectedText.getTextSize();
                    float factor = detector.getScaleFactor();
                    float product = size * factor;
                    selectedText.setTextSize(TypedValue.COMPLEX_UNIT_PX, product);
                }

                return true;
            }
            return false;
        }

    }


    private void enableDoodleOptions() {
        disableView(save_work_button);
        disableView(share_work_button);
        disableView(delete_work_button);
        enableView(doodle_undo_btn);
        enableView(doodle_redo_btn);
        enableView(doodle_shadow_btn);
        enableView(doodle_color_btn);
        enableView(doodle_brush_size_btn);
        enableView(doodle_erase_btn);


    }

    private void disableDoodleOptions() {
        disableView(doodle_undo_btn);
        disableView(doodle_redo_btn);
        disableView(doodle_shadow_btn);
        disableView(doodle_color_btn);
        disableView(doodle_brush_size_btn);
        disableView(doodle_erase_btn);
        enableView(save_work_button);
        enableView(share_work_button);
        enableView(delete_work_button);
    }

    private void disableView(ImageView view) {
        view.setVisibility(GONE);
    }

    private void enableView(ImageView vIew) {
        vIew.setVisibility(View.VISIBLE);
    }
}

