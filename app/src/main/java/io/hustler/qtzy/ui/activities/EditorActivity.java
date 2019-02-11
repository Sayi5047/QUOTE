package io.hustler.qtzy.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdView;


import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.Widgets.RotationGestureDetector;
import io.hustler.qtzy.ui.adapters.ColorsAdapter;
import io.hustler.qtzy.ui.adapters.Features_adapter;
import io.hustler.qtzy.ui.adapters.ImagesAdapter;
import io.hustler.qtzy.ui.adapters.SizesAdapter;
import io.hustler.qtzy.ui.apiRequestLauncher.Constants;
import io.hustler.qtzy.ui.apiRequestLauncher.ImagesApiResponceListner;
import io.hustler.qtzy.ui.apiRequestLauncher.Restutility;
import io.hustler.qtzy.ui.customviews.Sticker.StickerImageView;
import io.hustler.qtzy.ui.pojo.ImagesFromPixaBay;
import io.hustler.qtzy.ui.pojo.Quote;
import io.hustler.qtzy.ui.superclasses.BaseActivity;
import io.hustler.qtzy.ui.textFeatures.TextFeatures;
import io.hustler.qtzy.ui.utils.AdUtils;
import io.hustler.qtzy.ui.utils.AnimUtils;
import io.hustler.qtzy.ui.utils.FileUtils;
import io.hustler.qtzy.ui.utils.ImageProcessingUtils;
import io.hustler.qtzy.ui.utils.InternetUtils;
import io.hustler.qtzy.ui.utils.PermissionUtils;
import io.hustler.qtzy.ui.utils.TextUtils;
import io.hustler.qtzy.ui.utils.Toast_Snack_Dialog_Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.view.View.GONE;
import static io.hustler.qtzy.ui.utils.FileUtils.savetoDeviceWithAds;
import static io.hustler.qtzy.ui.utils.FileUtils.show_post_save_dialog;

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
    public File savedFile;
    //    private LinearLayout main_editor_layout;
    public int deviceHeight;
    Window windowManager;
    RecyclerView features_recyclerview;
    Features_adapter features_adapter;
    String[] itemsTo;
    //    Varaible to give ids to  newly added items
    int addedTextIds = 0;
    //    Variables for moving items onTouch
    int prevX, prevY;
    //    VIEWS FOR CURRENTLY SELECTED AND PREVIOUS
    View selectedView;
    StickerImageView selected_sticker, previous_sticker;
    View previousSelcted_View;
    View previousstate;
    SpannableString spannableString;
    ScaleGestureDetector scaleGestureDetector;
    float nsx, nsy, nfx, nfy;
    private ImageView light_effect_filter_IV;
    private RelativeLayout root_layout;
    //    private ImageView font_module;
//    private ImageView background_image_module;
    private ImageView font_size_changer;
    private ImageView close_text_size;
    private ImageView save_work_button;
    private ImageView share_work_button;
    private ImageView delete_view_button, options;
    private ImageView imageView_background;
    private TextView quote_editor_body;
    private TextView quote_editor_author;
    private TextView text_layout;
    private TextView background_layout;
    private TextView close_layout;
    private TextView done_layout;
    private TextView mark_quotzy;
    private Quote quote;
    private RelativeLayout quoteLayout;
    private RelativeLayout core_editor_layout;
    private LinearLayout text_and_bg_layout;
    private LinearLayout close_and_done_layout;
    private SeekBar seekBar;
    private Button clear_button;
    private int imagefittype;
    private int backpressCount = 0;
    private String newly_Added_Text;
    private String current_Text_feature;
    private String current_Bg_feature;
    private String current_module = null;
    private AdView mAdView;
    private int isFromEdit_Activity;
    private String selected_picture;
    private boolean isHeightMeasured = false;
    private String geust_image;
    private RotationGestureDetector rotationGestureDetector;
    private int pointer_Id_1, pointer_ID_2;
    private float fx;
    private float fy;
    //    private float sx;
//    private float sy;
    private LinearLayout features_layout;

//    final int INVALIDPOINTERID = -1;
//    int mActivePointerId = INVALIDPOINTERID;
//    in

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//            getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_rect));
//            getWindow().setClipToOutline(true);
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

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
        if (PermissionUtils.isPermissionAvailable(EditorActivity.this)) {

        } else {
            requestAppPermissions();
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
        delete_view_button = findViewById(R.id.delete_view_button);
        options = findViewById(R.id.options);

        font_size_changer = findViewById(R.id.spacer_in_top);
        light_effect_filter_IV = findViewById(R.id.iv_light_effect);


        seekBar = findViewById(R.id.progress_slider_bar);
        seekBar.setContentDescription("Slide to Rotate");
        close_text_size = findViewById(R.id.close_editor_button);

        features_recyclerview = findViewById(R.id.content_rv);
        features_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        imageView_background = findViewById(R.id.imageView_background);


        text_layout = findViewById(R.id.text_field);
        background_layout = findViewById(R.id.background_and_Image_field);
        close_layout = findViewById(R.id.close_tv);
        done_layout = findViewById(R.id.done_tv);
        mark_quotzy = findViewById(R.id.mark_quotzy_tv);
        core_editor_layout = findViewById(R.id.arena_text_layout);
//        core_editor_layout.setOnTouchListener(this);
        clear_button = findViewById(R.id.bt_clear);
        features_layout = findViewById(R.id.features_layout);
        setText_Features_rv();
        scaleGestureDetector = new ScaleGestureDetector(getApplicationContext(), new SimpleOnscaleGestureListener());
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
        delete_view_button.setOnClickListener(this);
        options.setOnClickListener(this);
        close_layout.setOnClickListener(this);
        done_layout.setOnClickListener(this);
        clear_button.setOnClickListener(this);
        TextUtils.setFont(EditorActivity.this, mark_quotzy, Constants.FONT_CIRCULAR);
        TextUtils.setFont(EditorActivity.this, text_layout, Constants.FONT_CIRCULAR);
        TextUtils.setFont(EditorActivity.this, background_layout, Constants.FONT_CIRCULAR);
        TextUtils.setFont(EditorActivity.this, close_layout, Constants.FONT_CIRCULAR);
        TextUtils.setFont(EditorActivity.this, done_layout, Constants.FONT_CIRCULAR);


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (isHeightMeasured) {
        } else {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            editor.putInt(Constants.SAHRED_PREFS_DEVICE_HEIGHT_KEY, quoteLayout.getHeight());
            editor.apply();
            isHeightMeasured = true;
        }
    }

    private void setViews() {
        if (isFromEdit_Activity == 1) {
            quote_editor_body = new TextView(getApplicationContext());
            quote_editor_author = new TextView(getApplicationContext());
            quote_editor_body.setId(addedTextIds);
            addedTextIds++;
            quote_editor_author.setId(addedTextIds);
            addedTextIds++;


            if (quote != null) {
                int length = quote.getQuote().length();
                root_layout.setBackground(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.screen_background_light_transparent));
//            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(quote.getColor()));
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

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    quote_editor_author.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    quote_editor_body.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                } else {
                    Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.sorry));
                }

                quote_editor_body.setGravity(Gravity.CENTER);
                quote_editor_author.setGravity(Gravity.CENTER);

                quote_editor_body.setY(core_editor_layout.getHeight() / 2);
                quote_editor_author.setX(core_editor_layout.getWidth() / 2);

                core_editor_layout.addView(quote_editor_body);
                core_editor_layout.addView(quote_editor_author);
                quote_editor_author.setLongClickable(true);
                quote_editor_author.setOnLongClickListener(EditorActivity.this);
                quote_editor_body.setLongClickable(true);
                quote_editor_body.setOnLongClickListener(EditorActivity.this);
                quote_editor_author.setOnTouchListener(EditorActivity.this);
                quote_editor_body.setOnTouchListener(EditorActivity.this);
            }
        } else if (isFromEdit_Activity == 2) {
            if (geust_image == null) {
                Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.image_unavailable));
            } else {
                imageView_background.setBackground(null);
                final ProgressBar progressBar = new ProgressBar(getApplicationContext());
                progressBar.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(geust_image).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(GONE);
                        Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.image_unavailable));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(GONE);
                        return false;
                    }
                }).centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageView_background);
                selected_picture = geust_image;
            }
        }
    }

    private void getIntentData() {
        int value = getIntent().getIntExtra(Constants.INTENT_IS_FROM_EDIT_KEY, 0);
        switch (value) {
            case 1: {
                isFromEdit_Activity = 1;
                quote = (Quote) getIntent().getSerializableExtra(Constants.INTENT_QUOTE_OBJECT_KEY);
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
                        path = intent.getData().getPath();
                        Log.d("ACTION_VIEW", path);
                    } catch (NullPointerException ne) {
                        finish();
                        ne.printStackTrace();
                    }
//                    Uri uri = FileProvider.getUriForFile(EditorActivity.this, getString(R.string.file_provider_authority), new File(path));
//                    path = uri.getPath();
//                    path=new File(Environment.getExternalStorageDirectory(),path).getAbsolutePath();

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
                        FileUtils.unzipandSave(file, EditorActivity.this, action);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            break;
            case 2: {
                isFromEdit_Activity = 2;
                geust_image = getIntent().getStringExtra(Constants.INTENT_UNSPLASH_IMAGE_FOR_EDIOTR_KEY);

            }
            break;
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        isFromEdit_Activity = 0;

        File file;
        String path = null;

        String action = intent.getAction();
        if (Objects.equals(action, Intent.ACTION_VIEW)) {
            try {
                path = intent.getData().getPath();
                Log.d("ACTION_VIEW", path);
            } catch (NullPointerException ne) {
                finish();
                ne.printStackTrace();
            }
//                    Uri uri = FileProvider.getUriForFile(EditorActivity.this, getString(R.string.file_provider_authority), new File(path));
//                    path = uri.getPath();
//                    path=new File(Environment.getExternalStorageDirectory(),path).getAbsolutePath();

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
                FileUtils.unzipandSave(file, EditorActivity.this, action);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void convertFeatures(String[] valueArray) {
        itemsTo = valueArray;
    }

    /*TOUCH LISTNER*/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        rotationGestureDetector.onTouchEvent(event);
        return true;
    }

    /*CLICK LISTNERS*/
    //LEVEL 1
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_clear: {
                if (selectedView == null) {
                } else {
                    selectedView.setBackground(null);
                    previousSelcted_View = null;
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
                features_recyclerview.setVisibility(View.VISIBLE);
                seekBar.setProgress(180);
                seekBar.setVisibility(GONE);
                handle_close_Feature();


            }
            break;
            case R.id.done_tv: {
                close_and_done_layout.setVisibility(GONE);
                text_and_bg_layout.setVisibility(View.VISIBLE);
                features_recyclerview.setVisibility(View.VISIBLE);
                seekBar.setVisibility(GONE);
            }
            break;
            case R.id.save_work_button: {
                if (PermissionUtils.isPermissionAvailable(EditorActivity.this)) {
                    if (selectedView != null) {
                        selectedView.setBackground(null);
                        clear_button.setVisibility(GONE);
                        previousSelcted_View = null;
                        selectedView = null;

                    }
                    savetoDeviceWithAds(quoteLayout, EditorActivity.this, new FileUtils.onSaveComplete() {
                        @Override
                        public void onImageSaveListner(File file) {
                            savedFile = file;
                            Toast.makeText(getApplicationContext(), "File Saved", Toast.LENGTH_SHORT).show();
                            show_post_save_dialog(EditorActivity.this, savedFile);

                        }
                    });
                    if (savedFile != null) {
                        Toast.makeText(getApplicationContext(), "File Already Saved", Toast.LENGTH_SHORT).show();
                    }
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
//                shareIntent.putExtra(Intent.EXTRA_SUBJECT, quote_editor_body.getText());
//                shareIntent.putExtra(Intent.EXTRA_TITLE, quote_editor_author.getText());
                Uri uri = null;
                if (savedFile != null) {
                    if (Build.VERSION.SDK_INT >= 24) {
                        uri = FileProvider.getUriForFile(getApplicationContext(), getString(R.string.file_provider_authority), savedFile);
                    } else {
                        uri = Uri.fromFile(savedFile);
                    }
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(shareIntent, "send"));
                } else {
                    savetoDeviceWithAds(quoteLayout, EditorActivity.this, new FileUtils.onSaveComplete() {
                        @Override
                        public void onImageSaveListner(File file) {
                            savedFile = file;
                            Uri uri1 = null;
                            if (Build.VERSION.SDK_INT >= 24) {
                                uri1 = FileProvider.getUriForFile(getApplicationContext(), getString(R.string.file_provider_authority), savedFile);
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
                        public void onPositiveselection() {
                            close_and_done_layout.setVisibility(GONE);
                            text_and_bg_layout.setVisibility(View.VISIBLE);
                            features_recyclerview.setVisibility(View.VISIBLE);
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
                super.onBackPressed();
            }
            break;
            case R.id.options: {
//                StickerView stickerView = new StickerView(EditorActivity.this);
//                try {
//                    stickerView.addSticker(new DrawableSticker(new BitmapDrawable(FileUtils.drawable_from_url("http://drive.google.com/uc?export=view&id=1tDf8pd2C_FNKQOVc7dZL2LTYp2-sPyQB;"))));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                core_editor_layout.addView(stickerView);
                if (features_layout.getVisibility() == View.VISIBLE) {
                    features_layout.setVisibility(GONE);
                } else {
                    features_layout.setVisibility(View.VISIBLE);
                }
            }
            break;
        }
    }

    /*COLONY BACKGROUND*/
    private void setBackground_features_rv() {
        current_module = Constants.BG;
        text_layout.setTextColor(getResources().getColor(R.color.black_overlay));
        background_layout.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        background_layout.setTextSize(16.0f);
        text_layout.setTextSize(12.0f);
        features_recyclerview.setAdapter(null);
        features_recyclerview.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slidedown));

        convertFeatures(getResources().getStringArray(R.array.Background_features));
        features_adapter = new Features_adapter(this, "Background_features", getResources().getStringArray(R.array.Background_features).length, new Features_adapter.OnFeature_ItemClickListner() {
            @Override
            public void onItemClick(String clickedItem) {
//                Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, clickedItem);
                enable_Selected_Background_Features(clickedItem, getResources().getStringArray(R.array.Background_features));

            }
        }, false);

        features_recyclerview.setAdapter(features_adapter);
        features_recyclerview.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slideup));

    }

    /*COLONY TEXT*/
    private void setText_Features_rv() {
        current_module = Constants.TEXT;
        text_layout.setTextSize(16.0f);
        background_layout.setTextSize(12.0f);

        background_layout.setTextColor(getResources().getColor(R.color.black_overlay));
        text_layout.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

        features_recyclerview.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slidedown));
        convertFeatures(getResources().getStringArray(R.array.Background_features));
        features_recyclerview.setAdapter(null);


        features_adapter = new Features_adapter(this, "Text_features", getResources().getStringArray(R.array.Text_features).length, new Features_adapter.OnFeature_ItemClickListner() {
            @Override
            public void onItemClick(String clickedItem) {
                enable_Selected_Text_Feature(clickedItem, getResources().getStringArray(R.array.Text_features));
            }
        }, true);

        features_recyclerview.setAdapter(features_adapter);
        features_recyclerview.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slideup));

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
            gradienteText(array);
        } else if (feature.equalsIgnoreCase(array[11])) {
            setCanvasSize(array, PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt(Constants.SAHRED_PREFS_DEVICE_HEIGHT_KEY, 1080));
        } else if (feature.equalsIgnoreCase(array[12])) {
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

    private void setCanvasSize(String[] array, final int deviceHeight) {
        final Dialog dialog = new Dialog(EditorActivity.this, R.style.EditTextDialog);
        dialog.setContentView(R.layout.canvas_size_dialog_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);
        dialog.setCancelable(false);
        LinearLayout root;
        TextView canvasSizeHeader;
        RecyclerView sizesRv;


        root = dialog.findViewById(R.id.root);
        canvasSizeHeader = dialog.findViewById(R.id.canvas_size_header);
        sizesRv = dialog.findViewById(R.id.sizes_rv);
        AdView adView;
        adView = dialog.findViewById(R.id.adView);
        AdUtils.loadBannerAd(adView, EditorActivity.this);
        TextUtils.findText_and_applyTypeface(root, EditorActivity.this);
        sizesRv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
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
                        int singlepart = Math.round(totalval / 5);
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
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
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

    private void setLayout(int deviceWidth, int deviceHeight, Dialog dialog) {
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
            Glide.with(getApplicationContext()).load(selected_picture).asBitmap().crossFade().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView_background);
        }
        dialog.dismiss();
    }

    private void enable_Selected_Background_Features(String clickedItem, String[] bgfeaturesArray) {
        if (clickedItem != null) {
            if (clickedItem.equalsIgnoreCase(bgfeaturesArray[0])) {
                current_Bg_feature = bgfeaturesArray[0];
                if (PermissionUtils.isPermissionAvailable(EditorActivity.this)) {
                    handle_gallery_dialog(EditorActivity.this);
                } else {
                    requestAppPermissions_for_launch_gallery();
                }
            } else if (clickedItem.equalsIgnoreCase(bgfeaturesArray[1])) {
                current_Bg_feature = bgfeaturesArray[1];
                selected_picture = null;
                colorbg(bgfeaturesArray);

            } else if (clickedItem.equalsIgnoreCase(bgfeaturesArray[2])) {
                current_Bg_feature = bgfeaturesArray[2];
                if (selected_picture != null) {
                    blurrImage(bgfeaturesArray);
                } else {
                    Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.please_select_image));
                }
            } else if (clickedItem.equalsIgnoreCase(bgfeaturesArray[3])) {
                current_Bg_feature = bgfeaturesArray[3];
//                applyBlackFilter();
                bringGradients();
            } else if (clickedItem.equalsIgnoreCase(bgfeaturesArray[4])) {
                current_Bg_feature = bgfeaturesArray[4];
//                applyWhiteFilter();
                if (InternetUtils.isConnectedtoNet(EditorActivity.this) == true) {
//                    seachImages();
                    addSticker("http://drive.google.com/uc?export=view&id=1tDf8pd2C_FNKQOVc7dZL2LTYp2-sPyQB");

                } else {
                    Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.internet_required_images));
                }
            }
// else if (clickedItem.equalsIgnoreCase(bgfeaturesArray[5])) {
//                current_Bg_feature = bgfeaturesArray[5];
////            selected_picture = null;
////                bringGradients();
//            } else if (clickedItem.equalsIgnoreCase(bgfeaturesArray[6])) {
//                current_Bg_feature = bgfeaturesArray[6];
////            selected_picture = null;
//
//            } else if (clickedItem.equalsIgnoreCase(bgfeaturesArray[7])) {
//
//            }
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

        AdView adView;
        adView = dialog.findViewById(R.id.adView);


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
        adView = dialog.findViewById(R.id.adView);
        diclaimer = dialog.findViewById(R.id.diclaimer);
        getRandomImages(null, imagesRecycler, progressBar, dialog);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchBox.getText() == null) {
                    searchBox.setError(getString(R.string.please_enter));
                } else {
//                    getRandomImages(searchBox.getText().toString(), imagesRecycler, progressBar, dialog);

                }
            }
        });

        TextUtils.findText_and_applyTypeface(root, EditorActivity.this);
        AdUtils.loadBannerAd(adView, EditorActivity.this);
        dialog.setCancelable(false);
        dialog.show();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
                    dialog.dismiss();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public void getRandomImages(String word, final RecyclerView recyclerView, final ProgressBar progressBar, final Dialog dialog) {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(null);
        recyclerView.setVisibility(GONE);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        String request;
        if (word == null) {
            request = Constants.API_GET_IMAGES_FROM_PIXABAY + "&per_page=150" + "&order=popular";
        } else {
            request = Constants.API_GET_IMAGES_FROM_PIXABAY + "&q=" + word + "&per_page=150" + "&order=popular";

        }
        new Restutility(EditorActivity.this).getRandomImages(getApplicationContext(), new ImagesApiResponceListner() {
            @Override
            public void onSuccess(List<ImagesFromPixaBay> images) {
                progressBar.setVisibility(GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(new ImagesAdapter(EditorActivity.this, (ArrayList<ImagesFromPixaBay>) images, new ImagesAdapter.ImagesOnClickListner() {
                    @Override
                    public void onImageClicked(String previreLink, String Biglink) {
                        selected_picture = Biglink;
                        imageView_background.setBackground(null);
                        Glide.with(getApplicationContext()).load(Biglink).asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageView_background);
                        dialog.dismiss();
                    }
                }));
            }

            @Override
            public void onError(String message) {
                Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, message);
            }
        }, request);
    }

    private void addSticker(String Biglink) {
        final StickerImageView stickerImageView = new StickerImageView(EditorActivity.this);
        Glide.with(EditorActivity.this).load(Biglink).asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        stickerImageView.setImageBitmap(resource);
                    }
                });
        core_editor_layout.addView(stickerImageView);
        if (null != selected_sticker) {
            selected_sticker.setControlItemsHidden(true);
            previousSelcted_View = selected_sticker;
        }
        selected_sticker = stickerImageView;
        selected_sticker.setOnTouchListener(this);
    }

    private void handle_gallery_dialog(EditorActivity editorActivity) {

        final Dialog dialog = new Dialog(EditorActivity.this, R.style.EditTextDialog);
        dialog.setContentView(R.layout.image_fit_type_layout_dialog);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);
        dialog.setCancelable(false);

        AdView adView;
        adView = dialog.findViewById(R.id.adView);
        AdUtils.loadBannerAd(adView, EditorActivity.this);
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
                    imagefittype = 1;
                } else if (fit.getId() == radioGroup.getCheckedRadioButtonId()) {
                    imagefittype = 2;
                } else {
                    imagefittype = 3;
                }
                launchGallery();
                dialog.cancel();

            }
        });
        dialog.show();

    }

    /*Seekbar methods*/
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        handle_seekbar_value(seekBar);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        handle_bg_seekbar(seekBar);
    }

    private void handle_bg_seekbar(SeekBar seekBar) throws NullPointerException {
        String[] featuresLocalArray = getResources().getStringArray(R.array.Background_features);
        if (current_module == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.we_missed_bg));
        } else {
            if (Constants.BG.equalsIgnoreCase(current_module)) {
                if (current_Bg_feature.equalsIgnoreCase(featuresLocalArray[2]) && seekBar.getProgress() >= 0) {
                    Glide.with(getApplicationContext()).load(selected_picture).asBitmap().centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView_background);
                    if (seekBar.getProgress() == 0) {
                        Glide.with(getApplicationContext()).load(selected_picture).asBitmap().centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView_background);
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
                } else if ((current_Bg_feature.equalsIgnoreCase(featuresLocalArray[3]) || current_Bg_feature.equalsIgnoreCase(featuresLocalArray[4])) && seekBar.getProgress() >= 0) {
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
                    if (current_Text_feature.equalsIgnoreCase(getResources().getStringArray(R.array.Text_features)[2]) && radius >= 0) {
                        float size = radius / 2;
                        selected_textView.setTextSize(size);
                    } else if (current_Text_feature.equalsIgnoreCase(getResources().getStringArray(R.array.Text_features)[3]) && radius >= 0) {
                        float degree = radius - 180;
                        selected_textView.setRotation(degree);
                    } else if (current_Text_feature.equalsIgnoreCase(getResources().getStringArray(R.array.Text_features)[4]) && radius >= 0) {
                        float degree = radius / 500;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            selected_textView.setLetterSpacing(degree);
                        } else {
                            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.sorry));
                        }
                    } else if (current_Text_feature.equalsIgnoreCase(getResources().getStringArray(R.array.Text_features)[6]) && radius >= 0) {
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
                        Glide.with(getApplicationContext()).load(selected_picture).asBitmap().centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView_background);

                    } else {

                    }

                } else if (current_Bg_feature.equalsIgnoreCase(getResources().getStringArray(R.array.Background_features)[3]) || current_Bg_feature.equalsIgnoreCase(getResources().getStringArray(R.array.Background_features)[4])) {
                    light_effect_filter_IV.setBackground(null);
                }
            }

        }
    }


    //    FEATURES
// THIS MEHOD CAN't be moved outside becuase it has lot of dependies in this page.
    private void addText(String[] array, final boolean isEdit) {
        current_Text_feature = array[0];
        final int[] alignment = new int[1];
        final Dialog dialog = new Dialog(this, R.style.EditTextDialog);
        dialog.setContentView(View.inflate(this, R.layout.addtext, null));
        final TextView header, align_text;
        final EditText addingText;
        AdView adView;
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

        adView = dialog.findViewById(R.id.adView);
        AdUtils.loadBannerAd(adView, EditorActivity.this);
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    addingText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                } else {
                    Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, getString(R.string.sorry));
                }
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
                        final TextView textView = new TextView(getApplicationContext());
                        textView.setTextSize(20.0f);
                        textView.setTextColor(getResources().getColor(R.color.textColor));
                        textView.setMaxWidth(core_editor_layout.getWidth());
                        TextUtils.setFont(EditorActivity.this, textView, Constants.FONT_CIRCULAR);

                        textView.setText(newly_Added_Text + " ");

                        textView.setX(core_editor_layout.getWidth() - (core_editor_layout.getWidth() - 100));
//                        textView.setY(core_editor_layout.getHeight() / 2);
                        textView.setId(addedTextIds);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, " " + textView.getId() + " ");
                            }
                        });
                        set_text_alignment(alignment[0], textView);

                        textView.setPadding(8, 8, 8, 8);
                        textView.setLongClickable(true);
                        textView.setOnLongClickListener(EditorActivity.this);
                        textView.setOnTouchListener(EditorActivity.this);
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextDirection(View.TEXT_DIRECTION_LOCALE);
                        core_editor_layout.addView(textView);
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
        AdView adView;
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
        adView = dialog.findViewById(R.id.adView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.HORIZONTAL, false));
        AdUtils.loadBannerAd(adView, EditorActivity.this);
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
                        final TextView textView = new TextView(getApplicationContext());
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

    public void setSpan(Object styleSpan, EditText addingText) {
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
            features_recyclerview.setVisibility(GONE);
            seekBar.setVisibility(View.VISIBLE);
            text_and_bg_layout.setVisibility(GONE);
            close_and_done_layout.setVisibility(View.VISIBLE);
            previousstate = selectedView;
            current_Text_feature = array[2];


        }
    }

    private void rotateText(String[] array) {
        /*RESIZE*/
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            features_recyclerview.setVisibility(GONE);
            seekBar.setVisibility(View.VISIBLE);
            text_and_bg_layout.setVisibility(GONE);
            close_and_done_layout.setVisibility(View.VISIBLE);
            previousstate = selectedView;
            current_Text_feature = array[3];


        }
    }

    private void spaceText(String[] array) {
        /*RESIZE*/
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            features_recyclerview.setVisibility(GONE);
            seekBar.setVisibility(View.VISIBLE);
            text_and_bg_layout.setVisibility(GONE);
            close_and_done_layout.setVisibility(View.VISIBLE);
            previousstate = selectedView;
            current_Text_feature = array[4];


        }
    }

    private void spaceLine(String[] array) {
        /*RESIZE*/
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            features_recyclerview.setVisibility(GONE);
            seekBar.setVisibility(View.VISIBLE);
            text_and_bg_layout.setVisibility(GONE);
            close_and_done_layout.setVisibility(View.VISIBLE);
            previousstate = selectedView;
            current_Text_feature = array[6];


        }
    }

    private void adjustFrameWidth(String[] array) {
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            features_recyclerview.setVisibility(GONE);
            seekBar.setVisibility(View.VISIBLE);
            text_and_bg_layout.setVisibility(GONE);
            close_and_done_layout.setVisibility(View.VISIBLE);
            previousstate = selectedView;
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
            final Dialog dialog = new Dialog(this, R.style.EditTextDialog);
            dialog.setContentView(R.layout.colors_dialog_layout);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);

            dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;

            final boolean[] isShadowApplied = new boolean[1];
            final TextView head_tv, demo_tv;
            AdView adView;
            RecyclerView colors_rv;
            Button close, choose, shadow;
            ColorsAdapter colorsAdapter;
            final int[] choosen_color = new int[1];


            head_tv = dialog.findViewById(R.id.color_text);
            demo_tv = dialog.findViewById(R.id._demo_color_text);
            colors_rv = dialog.findViewById(R.id.colors_rv);
            close = dialog.findViewById(R.id.bt_color_close);
            choose = dialog.findViewById(R.id.bt_color_choose);
            shadow = dialog.findViewById(R.id.bt_color_shadow);
            adView = dialog.findViewById(R.id.adView);
            TextUtils.setFont(this, head_tv, Constants.FONT_CIRCULAR);
            TextUtils.setFont(this, close, Constants.FONT_CIRCULAR);
            TextUtils.setFont(this, choose, Constants.FONT_CIRCULAR);
            AdUtils.loadBannerAd(adView, EditorActivity.this);
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
                        selectedTextView.getPaint().setShader(null);
                        selectedTextView.setTextColor(choosen_color[0]);
                    }

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
            previousstate = selectedView;
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
                previousstate = selectedView;
                current_Text_feature = array[9];
                TextFeatures.apply_font(EditorActivity.this, selectedTextView);
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
            previousstate = selectedView;
            current_Text_feature = array[10];
            TextFeatures.setGradients(EditorActivity.this, selectedTextView);
        }
    }

    private void textFx(String[] array) {
        final TextView selectedTextView = (TextView) selectedView;
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            previousstate = selectedView;
            current_Text_feature = array[12];
            TextFeatures.setVfx(EditorActivity.this, selectedTextView);
        }
    }

    private void hollowText(String[] array) {
        final TextView selectedTextView = (TextView) selectedView;
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            previousstate = selectedView;
            current_Text_feature = array[13];
            TextFeatures.setHollowText(EditorActivity.this, selectedTextView);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void set_text_alignment(int position, TextView textView) {
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

        final Dialog dialog = new Dialog(this, R.style.EditTextDialog);
        dialog.setContentView(R.layout.colors_dialog_layout);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);

        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;

        final boolean[] isShadowApplied = new boolean[1];
        final TextView head_tv, demo_tv;
        RecyclerView colors_rv;
        Button close, choose, shadow;
        ColorsAdapter colorsAdapter;
        final int[] choosen_color = new int[1];

        AdView adView;
        head_tv = dialog.findViewById(R.id.color_text);
        demo_tv = dialog.findViewById(R.id._demo_color_text);
        colors_rv = dialog.findViewById(R.id.colors_rv);
        close = dialog.findViewById(R.id.bt_color_close);
        choose = dialog.findViewById(R.id.bt_color_choose);
        shadow = dialog.findViewById(R.id.bt_color_shadow);
        adView = dialog.findViewById(R.id.adView);
        TextUtils.setFont(this, head_tv, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, close, Constants.FONT_CIRCULAR);
        TextUtils.setFont(this, choose, Constants.FONT_CIRCULAR);
        AdUtils.loadBannerAd(adView, EditorActivity.this);
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
        final Dialog dialog = new Dialog(EditorActivity.this, R.style.EditTextDialog);
        dialog.setContentView(R.layout.gradient_bg_layout);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);

        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
        AdView adView;
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
        adView = dialog.findViewById(R.id.adView);
        colorsRecycler.setVisibility(GONE);
        preview.setVisibility(GONE);
        AdUtils.loadBannerAd(adView, EditorActivity.this);
        colorsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
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
        features_recyclerview.setVisibility(GONE);
        seekBar.setVisibility(View.VISIBLE);
        text_and_bg_layout.setVisibility(GONE);
        close_and_done_layout.setVisibility(View.VISIBLE);
    }

    private void applyBlackFilter() {
        light_effect_filter_IV.setBackground(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.screen_background_dark_transparent));
        features_recyclerview.setVisibility(GONE);
        seekBar.setVisibility(View.VISIBLE);
        text_and_bg_layout.setVisibility(GONE);
        close_and_done_layout.setVisibility(View.VISIBLE);
    }

    private void applyWhiteFilter() {
        light_effect_filter_IV.setBackground(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.screen_background_light_transparent));
        features_recyclerview.setVisibility(GONE);
        seekBar.setVisibility(View.VISIBLE);
        text_and_bg_layout.setVisibility(GONE);
        close_and_done_layout.setVisibility(View.VISIBLE);
    }

    private void launchGallery() {
//        final ImageView selectedTextView = (ImageView) selectedView;
//        if (selectedView == null) {
//            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
//        } else {
//            previousstate = selectedView;
//        }
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    /*Permission related Methods*/

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri fileurl = data.getData();
            String[] filePaths = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(fileurl, filePaths, null, null, null);
            cursor.moveToFirst();
            int coloumnIndex = cursor.getColumnIndex(filePaths[0]);
            String picturepath = cursor.getString(coloumnIndex);
            cursor.close();
//           imageView_background.setImageBitmap(BitmapFactory.decodeFile(picturepath));
            if (imageView_background.getDrawingCache() != null) {
                imageView_background.destroyDrawingCache();
            }
            selected_picture = picturepath;
//            try {
//                Intent cropIntent = new Intent("com.android.camera.action.CROP");
//                cropIntent.setDataAndType(fileurl, "image/*");
//                cropIntent.putExtra("crop", "true");
//                cropIntent.putExtra("aspectX", 2);
//                cropIntent.putExtra("aspectY", 2);
//                cropIntent.putExtra("outputX", 512);
//                cropIntent.putExtra("outputY", 512);
//                cropIntent.putExtra("return-data", true);
//                startActivityForResult(cropIntent, 2);
//            } catch (ActivityNotFoundException anfe) {
//                Toast_Snack_Dialog_Utils.show_ShortToast(this, "ACTIVITY NOT FOUND");
//            }

            if (imagefittype == 3) {
                Glide.with(getApplicationContext()).load(picturepath).asBitmap().crossFade().into(imageView_background);

            } else if (imagefittype == 2) {
                Glide.with(getApplicationContext()).load(picturepath).asBitmap().fitCenter().crossFade().into(imageView_background);

            } else {
                Glide.with(getApplicationContext()).load(picturepath).asBitmap().centerCrop().crossFade().into(imageView_background);

            }
//            ImageView imageView = new ImageView(EditorActivity.this);
//            imageView.setImageURI(fileurl);
//            imageView.setOnTouchListener(this);
////            imageView.setMaxWidth(600);
////            imageView.setMaxHeight(600);
//            core_editor_layout.addView(imageView);
//            imageView_background.setImageResource(picturepath);
        } else if (requestCode == 2) {
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

            Bitmap bitmap = data.getExtras().getParcelable("data");
//            ImageView im_crop = (ImageView) findViewById(R.id.im_crop);
//            im_crop.setImageBitmap(bitmap);
//            Glide.with(getApplicationContext()).load(bitmap).asBitmap().crossFade().into(imageView_background);
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
                    savetoDeviceWithAds(quoteLayout, EditorActivity.this, new FileUtils.onSaveComplete() {
                        @Override
                        public void onImageSaveListner(File file) {
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
                    handle_gallery_dialog(EditorActivity.this);
                }
            }
            break;
        }
    }

    @Override
    public void onBackPressed() {
        backpressCount++;

        if (backpressCount >= 2) {
            this.finish();
            overridePendingTransition(R.anim.slideup, R.anim.slidedown);
        } else {
            Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, "Press again to discard the image and exit");
        }

    }

    /*Methods to handle view movements*/
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        RelativeLayout.LayoutParams selected_text_view_parameters = (RelativeLayout.LayoutParams) v.getLayoutParams();
        final int INAVALID_POINTER_ID = -1;

        if (v instanceof TextView) {
            selectedView = v;
            handleTouchForTextView(v);
        } else if (v instanceof StickerImageView) {
            selected_sticker = (StickerImageView) v;
            handleTouchForStickerView((StickerImageView) v);
        } else if (v instanceof RelativeLayout && v.getId() == R.id.arena_text_layout) {
            clear_button.performClick();
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                pointer_Id_1 = event.getPointerId(event.getActionIndex());
                Log.d("ACTION DOWN", pointer_Id_1 + "");
//                prevX = (int) event.getRawX();
//                prevY = (int) event.getRawY();
//                selected_text_view_parameters.bottomMargin = -2 * v.getHeight();
//                selected_text_view_parameters.rightMargin = -2 * v.getWidth();
////                v.setLayoutParams(selected_text_view_parameters);
                try {
                    fx = event.getX(event.findPointerIndex(pointer_Id_1));
                    fy = event.getY(event.findPointerIndex(pointer_Id_1));
                } catch (IllegalArgumentException iae) {
                    iae.printStackTrace();
                }
                return true;
            }
//            case MotionEvent.ACTION_POINTER_DOWN: {
//                pointer_ID_2 = event.getPointerId(event.getActionIndex());
//                Log.d("ACTION POINTER DOWN", pointer_ID_2 + "");
//
////                sx = event.getX(event.findPointerIndex(pointer_Id_1));
////                sy = event.getY(event.findPointerIndex(pointer_Id_1));
//
//                return true;
//            }
            case MotionEvent.ACTION_MOVE: {

                if (pointer_Id_1 != INAVALID_POINTER_ID) {
//                        nsx = event.getX(event.findPointerIndex(pointer_Id_1));
//                        nsy = event.getY(event.findPointerIndex(pointer_Id_1));

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

//                selected_text_view_parameters.topMargin = selected_text_view_parameters.topMargin + (int) nfy - (int) fy;
//                selected_text_view_parameters.leftMargin = selected_text_view_parameters.leftMargin + (int) nfx - (int) fx;
//
////                selected_text_view_parameters.topMargin += (int) event.getRawY() - prevY;
////                selected_text_view_parameters.leftMargin += (int) event.getRawX() - prevX;
//                v.setLayoutParams(selected_text_view_parameters);
                return true;
            }

//            case MotionEvent.ACTION_POINTER_UP: {
////                pointer_ID_2 = INAVALID_POINTER_ID;
//                Log.d("ACTION POINTER UP", pointer_ID_2 + "");
//
//                return true;
//            }

            case MotionEvent.ACTION_CANCEL: {
                pointer_ID_2 = pointer_Id_1 = INAVALID_POINTER_ID;
                Log.d("ACTION CANCEL", pointer_ID_2 + " <--2,1--> " + pointer_Id_1);

                return true;
            }
        }
        return false;
    }

    private void handleTouchForStickerView(StickerImageView v) {
        if (selected_sticker != null) {
            selected_sticker.setControlItemsHidden(true);
            previousSelcted_View = selected_sticker;
            selected_sticker = v;
            clear_button.setVisibility(View.VISIBLE);
            ((StickerImageView) selected_sticker).setControlItemsHidden(false);
            selected_sticker.bringToFront();
        } else {
            clear_button.setVisibility(View.VISIBLE);
            selected_sticker = v;
            ((StickerImageView) selectedView).setControlItemsHidden(false);

        }
    }

    private void handleTouchForTextView(View v) {
        if (previousSelcted_View != null) {
            previousSelcted_View.setBackground(null);
            previousSelcted_View = v;
            clear_button.setVisibility(View.VISIBLE);
            selectedView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tv_bg));
        } else {
            clear_button.setVisibility(View.VISIBLE);
            previousSelcted_View = v;
            selectedView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tv_bg));
        }
    }


    @Override
    public boolean onLongClick(View v) {

        v.bringToFront();
        core_editor_layout.forceLayout();
        core_editor_layout.invalidate();

        SparseArrayCompat<TextView> sparseArrayCompat = new SparseArrayCompat<TextView>();
        sparseArrayCompat.put(v.getId(), (TextView) v);
        Log.d("SPARSE ARRAY", String.valueOf(sparseArrayCompat.get(v.getId())));
        return true;

    }

    @Override
    public void onRotate(RotationGestureDetector rotationGestureDetector) {
        if (selectedView != null) {
            View selected = selectedView;
            if (selected instanceof TextView) {
                TextView selectedText = ((TextView) selected);
                selectedText.setRotation(rotationGestureDetector.getmAngle());

                Log.d("RotationGestureDetector", "Rotation: " + Float.toString(rotationGestureDetector.getmAngle()));
            }

        }
    }

    private class SimpleOnscaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
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
}

