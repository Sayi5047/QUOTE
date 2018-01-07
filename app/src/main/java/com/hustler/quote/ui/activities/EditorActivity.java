package com.hustler.quote.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.Gravity;
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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdView;
import com.hustler.quote.R;
import com.hustler.quote.ui.adapters.ColorsAdapter;
import com.hustler.quote.ui.adapters.Features_adapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.pojo.Quote;
import com.hustler.quote.ui.superclasses.BaseActivity;
import com.hustler.quote.ui.textFeatures.TextFeatures;
import com.hustler.quote.ui.utils.AdUtils;
import com.hustler.quote.ui.utils.AnimUtils;
import com.hustler.quote.ui.utils.FileUtils;
import com.hustler.quote.ui.utils.ImageProcessingUtils;
import com.hustler.quote.ui.utils.PermissionUtils;
import com.hustler.quote.ui.utils.TextUtils;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

import java.io.File;

import static android.view.View.GONE;
import static com.hustler.quote.ui.utils.FileUtils.savetoDevice;

public class EditorActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, View.OnTouchListener {
    private static final int RESULT_LOAD_IMAGE = 1001;
    private static final int MY_PERMISSION_REQUEST_STORAGE_FROM_ONSTART = 1002;
    private static final int MY_PERMISSION_REQUEST_STORAGE_FOR_SAVING_TO_GALLERY = 1003;
    private static final int MY_PERMISSION_REQUEST_STORAGE_FOR_FONTS = 1004;
    private static final int MY_PERMISSION_REQUEST_Launch_gallery = 1007;


    Window windowManager;
    private ImageView light_effect_filter_IV;
    private LinearLayout root_layout;

    private ImageView font_module;
    private ImageView background_image_module;
    private ImageView font_size_changer;
    private ImageView close_text_size;
    private ImageView save_work_button;
    private ImageView share_work_button;
    private ImageView delete_view_button;
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

    RecyclerView features_recyclerview;
    Features_adapter features_adapter;

    public File savedFile;
    String[] itemsTo;


    private int backpressCount = 0;

    private String newly_Added_Text;

    //    Varaible to give ids to  newly added items
    int addedTextIds = 0;

    //    Variables for moving items onTouch
    int prevX, prevY;

    //    VIEWS FOR CURRENTLY SELECTED AND PREVIOUS
    View selectedView;
    View previousSelcted_View;
    View previousstate;
    private String current_Text_feature;
    private String current_Bg_feature;
    private String current_module;
    SpannableString spannableString;
        private AdView mAdView;
    ScaleGestureDetector scaleGestureDetector;
    private boolean isFromEdit_Activity;
    private String selected_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        findViews();
        getIntentData();
        setViews();
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
        root_layout = (LinearLayout) findViewById(R.id.root_Lo);
        windowManager = this.getWindow();
        windowManager.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        quoteLayout = (RelativeLayout) findViewById(R.id.quote_layout);
        text_and_bg_layout = (LinearLayout) findViewById(R.id.text_and_background_layout);
        close_and_done_layout = (LinearLayout) findViewById(R.id.close_and_done_layout);

//      level 1 top bar buttons
        font_module = (ImageView) findViewById(R.id.font_style_changer_module);
        background_image_module = (ImageView) findViewById(R.id.font_background_chnager_module);
        save_work_button = (ImageView) findViewById(R.id.save_work_button);
        share_work_button = (ImageView) findViewById(R.id.font_share_module);
        delete_view_button = (ImageView) findViewById(R.id.delete_view_button);

        font_size_changer = (ImageView) findViewById(R.id.spacer_in_top);
        light_effect_filter_IV = (ImageView) findViewById(R.id.iv_light_effect);


        seekBar = (SeekBar) findViewById(R.id.progress_slider_bar);
        seekBar.setContentDescription("Slide to Rotate");
        close_text_size = (ImageView) findViewById(R.id.close_editor_button);

        features_recyclerview = (RecyclerView) findViewById(R.id.content_rv);
        features_recyclerview.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        imageView_background = (ImageView) findViewById(R.id.imageView_background);


        text_layout = (TextView) findViewById(R.id.text_field);
        background_layout = (TextView) findViewById(R.id.background_and_Image_field);
        close_layout = (TextView) findViewById(R.id.close_tv);
        done_layout = (TextView) findViewById(R.id.done_tv);
        mark_quotzy = (TextView) findViewById(R.id.mark_quotzy_tv);
        core_editor_layout = (RelativeLayout) findViewById(R.id.arena_text_layout);
        clear_button = (Button) findViewById(R.id.bt_clear);


        scaleGestureDetector = new ScaleGestureDetector(this, new SimpleOnscaleGestureListener());







/*setting on click listners */
        font_module.setOnClickListener(this);
        background_image_module.setOnClickListener(this);
        text_layout.setOnClickListener(this);
        background_layout.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        font_size_changer.setOnClickListener(this);
        close_text_size.setOnClickListener(this);
        save_work_button.setOnClickListener(this);
        share_work_button.setOnClickListener(this);
        delete_view_button.setOnClickListener(this);
        close_layout.setOnClickListener(this);
        done_layout.setOnClickListener(this);
        clear_button.setOnClickListener(this);


        setText_Features_rv();
    }

    private void setViews() {
        if (isFromEdit_Activity) {
            quote_editor_body = new TextView(this);
            quote_editor_author = new TextView(this);
            quote_editor_body.setId(addedTextIds);
            addedTextIds++;
            quote_editor_author.setId(addedTextIds);
            addedTextIds++;


            if (quote != null) {
                int length = quote.getQuote_body().length();
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

                quote_editor_body.setText(quote.getQuote_body());
                quote_editor_author.setText(quote.getQuote_author());

                quote_editor_body.setMaxWidth(1050);
                quote_editor_author.setMaxWidth(1050);

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

                quoteLayout.addView(quote_editor_body);
                quoteLayout.addView(quote_editor_author);
                quote_editor_author.setOnTouchListener(this);
                quote_editor_body.setOnTouchListener(this);
            }


        }
    }

    private void getIntentData() {
        if ((Boolean) getIntent().getBooleanExtra(Constants.INTENT_IS_FROM_EDIT_KEY, false)) {
            isFromEdit_Activity = true;
            quote = (Quote) getIntent().getSerializableExtra(Constants.INTENT_QUOTE_OBJECT_KEY);

        } else {
            isFromEdit_Activity = false;
        }
    }

    private void convertFeatures(String[] valueArray) {
        itemsTo = valueArray;
    }

    /*TOUCH LISTNER*/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    /*CLICK LISTNERS*/
    //LEVEL 1
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_clear: {
                if (selectedView == null) {
                    Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text_to_delete));
                } else {
                    selectedView.setBackground(null);
                    previousSelcted_View = null;
                    selectedView = null;
                    clear_button.setVisibility(GONE);
                }
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

                    savetoDevice(quoteLayout, EditorActivity.this, new FileUtils.onSaveComplete() {
                        @Override
                        public void onImageSaveListner(File file) {
                            savedFile = file;
                            Toast.makeText(EditorActivity.this, "File Saved", Toast.LENGTH_SHORT).show();

                        }
                    });
                    if (savedFile != null) {
                        Toast.makeText(EditorActivity.this, "File Already Saved", Toast.LENGTH_SHORT).show();
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
                    uri = Uri.fromFile(savedFile);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(shareIntent, "send"));
                } else {
                    savetoDevice(quoteLayout, EditorActivity.this, new FileUtils.onSaveComplete() {
                        @Override
                        public void onImageSaveListner(File file) {
                            savedFile = file;
                            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                            startActivity(Intent.createChooser(shareIntent, "send"));
                        }
                    });
                }
            }
            break;
            case R.id.delete_view_button: {
                if (selectedView != null) {
                    Toast_Snack_Dialog_Utils.createDialog(this,
                            getString(R.string.are_you_sure),
                            getString(R.string.this_will_delete),
                            getString(R.string.cancel),
                            getString(R.string.delete),
                            new Toast_Snack_Dialog_Utils.Alertdialoglistener() {
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
        }
    }

    /*COLONY BACKGROUND*/
    private void setBackground_features_rv() {
        current_module = Constants.BG;
        text_layout.setTextColor(getResources().getColor(R.color.black_overlay));
        background_layout.setTextColor(getResources().getColor(android.R.color.black));
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
        });

        features_recyclerview.setAdapter(features_adapter);
        features_recyclerview.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slideup));

    }

    /*COLONY TEXT*/
    private void setText_Features_rv() {
        current_module = Constants.TEXT;
        text_layout.setTextSize(16.0f);
        background_layout.setTextSize(12.0f);

        background_layout.setTextColor(getResources().getColor(R.color.black_overlay));
        text_layout.setTextColor(getResources().getColor(android.R.color.black));
        features_recyclerview.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slidedown));
        convertFeatures(getResources().getStringArray(R.array.Background_features));
        features_recyclerview.setAdapter(null);


        features_adapter = new Features_adapter(this, "Text_features", getResources().getStringArray(R.array.Text_features).length, new Features_adapter.OnFeature_ItemClickListner() {
            @Override
            public void onItemClick(String clickedItem) {
//                Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, clickedItem);
                enable_Selected_Text_Feature(clickedItem, getResources().getStringArray(R.array.Text_features));
            }
        });

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
            // TODO: 14/12/2017 do it at end of text area
            colorText(array);

        } else if (feature.equalsIgnoreCase(array[6])) {
            spaceLine(array);

        } else if (feature.equalsIgnoreCase(array[7])) {
            adjustFrameWidth(array);

        } else if (feature.equalsIgnoreCase(array[8])) {
            shadowText(array);
        } else if (feature.equalsIgnoreCase(array[9])) {
            applyFont(array);
        } else if(feature.equalsIgnoreCase(array[10])) {
//            symbolFont(array);
        }
    }

    private void enable_Selected_Background_Features(String clickedItem, String[] bgfeaturesArray) {
        if (clickedItem.equalsIgnoreCase(bgfeaturesArray[0])) {
            current_Bg_feature = bgfeaturesArray[0];
            if (PermissionUtils.isPermissionAvailable(EditorActivity.this)) {
                launchGallery();
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
            applyBlackFilter();

        } else if (clickedItem.equalsIgnoreCase(bgfeaturesArray[4])) {
            current_Bg_feature = bgfeaturesArray[4];
            applyWhiteFilter();
        } else if (clickedItem.equalsIgnoreCase(bgfeaturesArray[5])) {
            current_Bg_feature = bgfeaturesArray[5];
            selected_picture = null;
            bringGradients();
        } else if (clickedItem.equalsIgnoreCase(bgfeaturesArray[6])) {

        } else if (clickedItem.equalsIgnoreCase(bgfeaturesArray[7])) {

        }
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

    private void handle_bg_seekbar(SeekBar seekBar) {
        String[] featuresLocalArray = getResources().getStringArray(R.array.Background_features);
        if (current_module.equalsIgnoreCase(Constants.BG)) {
            if (current_Bg_feature.equalsIgnoreCase(featuresLocalArray[2]) && seekBar.getProgress() >= 0) {
                Glide.with(this).load(selected_picture).asBitmap().centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView_background);
                if (seekBar.getProgress() == 0) {
                    Glide.with(this).load(selected_picture).asBitmap().centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView_background);
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

    private void handle_seekbar_value(SeekBar seekBar) {
        float radius = (float) seekBar.getProgress();
        if (current_module.equalsIgnoreCase(Constants.TEXT)) {
            TextView selected_textView = (TextView) selectedView;
            if (current_Text_feature.equalsIgnoreCase(getResources().getStringArray(R.array.Text_features)[2]) && radius >= 0) {
                float size = radius / 4;
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
        }
    }


    private void handle_close_Feature() {
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
                    Glide.with(this).load(selected_picture).asBitmap().centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView_background);

                } else {

                }

            } else if (current_Bg_feature.equalsIgnoreCase(getResources().getStringArray(R.array.Background_features)[3]) ||
                    current_Bg_feature.equalsIgnoreCase(getResources().getStringArray(R.array.Background_features)[4])) {
                light_effect_filter_IV.setBackground(null);
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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
        dialog.setCancelable(false);

        header = (TextView) dialog.findViewById(R.id.tv_header);
        align_text = (TextView) dialog.findViewById(R.id.tv_align_text);
        addingText = (EditText) dialog.findViewById(R.id.et_text);
        close = (Button) dialog.findViewById(R.id.bt_close);
        done = (Button) dialog.findViewById(R.id.bt_done);
        start = (Button) dialog.findViewById(R.id.bt_start);
        center = (Button) dialog.findViewById(R.id.bt_center);
        end = (Button) dialog.findViewById(R.id.bt_end);
        add_bg = (Button) dialog.findViewById(R.id.bt_add_bg);

        adView=(AdView) dialog.findViewById(R.id.adView);
        AdUtils.loadBannerAd(adView);
        TextUtils.setFont(this, header, Constants.FONT_Google_sans_regular);
        TextUtils.setFont(this, addingText, Constants.FONT_Google_sans_regular);
        TextUtils.setFont(this, align_text, Constants.FONT_Google_sans_regular);
        TextUtils.setFont(this, start, Constants.FONT_Google_sans_regular);
        TextUtils.setFont(this, center, Constants.FONT_Google_sans_regular);
        TextUtils.setFont(this, end, Constants.FONT_Google_sans_regular);
        TextUtils.setFont(this, close, Constants.FONT_NEVIS);
        TextUtils.setFont(this, done, Constants.FONT_NEVIS);
        TextUtils.setFont(this, add_bg, Constants.FONT_NEVIS);

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
                        TextUtils.setFont(EditorActivity.this, textView, Constants.FONT_Google_sans_regular);

                        textView.setText(newly_Added_Text);

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
        final Button close, done, start, center, end, previewText, bold, underline, italic, strikethrough, highlight, colorText;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
        dialog.setCancelable(false);

        header = (TextView) dialog.findViewById(R.id.tv_header);
        align_text = (TextView) dialog.findViewById(R.id.tv_align_text);
        addingText = (EditText) dialog.findViewById(R.id.et_text);
        close = (Button) dialog.findViewById(R.id.bt_close);
        done = (Button) dialog.findViewById(R.id.bt_done);
        start = (Button) dialog.findViewById(R.id.bt_start);
        center = (Button) dialog.findViewById(R.id.bt_center);
        end = (Button) dialog.findViewById(R.id.bt_end);
        previewText = (Button) dialog.findViewById(R.id.bt_add_bg);
        recyclerView = (RecyclerView) dialog.findViewById(R.id.color_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.HORIZONTAL, false));

        bold = (Button) dialog.findViewById(R.id.bt_bold);
        underline = (Button) dialog.findViewById(R.id.bt_underline);
        italic = (Button) dialog.findViewById(R.id.bt_italic);
        strikethrough = (Button) dialog.findViewById(R.id.bt_strikethrough);
        highlight = (Button) dialog.findViewById(R.id.bt_highlight);
        colorText = (Button) dialog.findViewById(R.id.bt_colored);

//        spannableString = new SpannableString(addingText.getText().toString());

        addingText.setText(((TextView) selectedView).getText());
        TextUtils.setFont(this, header, Constants.FONT_Google_sans_regular);
        TextUtils.setFont(this, addingText, Constants.FONT_Google_sans_regular);
        TextUtils.setFont(this, align_text, Constants.FONT_Google_sans_regular);
        TextUtils.setFont(this, start, Constants.FONT_Google_sans_regular);
        TextUtils.setFont(this, center, Constants.FONT_Google_sans_regular);
        TextUtils.setFont(this, end, Constants.FONT_Google_sans_regular);
        TextUtils.setFont(this, bold, Constants.FONT_Google_sans_regular);
        TextUtils.setFont(this, underline, Constants.FONT_Google_sans_regular);
        TextUtils.setFont(this, italic, Constants.FONT_Google_sans_regular);
        TextUtils.setFont(this, strikethrough, Constants.FONT_Google_sans_regular);
        TextUtils.setFont(this, highlight, Constants.FONT_Google_sans_regular);
        TextUtils.setFont(this, colorText, Constants.FONT_Google_sans_regular);

        TextUtils.setFont(this, done, Constants.FONT_NEVIS);
        TextUtils.setFont(this, close, Constants.FONT_NEVIS);
        TextUtils.setFont(this, previewText, Constants.FONT_Google_sans_regular);
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
                        TextUtils.setFont(EditorActivity.this, textView, Constants.FONT_Google_sans_regular);
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
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;

            final boolean[] isShadowApplied = new boolean[1];
            final TextView head_tv, demo_tv;
            RecyclerView colors_rv;
            Button close, choose, shadow;
            ColorsAdapter colorsAdapter;
            final int[] choosen_color = new int[1];


            head_tv = (TextView) dialog.findViewById(R.id.color_text);
            demo_tv = (TextView) dialog.findViewById(R.id._demo_color_text);
            colors_rv = (RecyclerView) dialog.findViewById(R.id.colors_rv);
            close = (Button) dialog.findViewById(R.id.bt_color_close);
            choose = (Button) dialog.findViewById(R.id.bt_color_choose);
            shadow = (Button) dialog.findViewById(R.id.bt_color_shadow);

            TextUtils.setFont(this, head_tv, Constants.FONT_Google_sans_regular);
            TextUtils.setFont(this, close, Constants.FONT_Google_sans_regular);
            TextUtils.setFont(this, choose, Constants.FONT_Google_sans_regular);

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

    private void symbolFont(String[] array){
//        final TextView selectedTextView = (TextView) selectedView;
//        if (selectedView == null) {
//            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
//        } else {
//            if (PermissionUtils.isPermissionAvailable(EditorActivity.this)) {
//                previousstate = selectedView;
//                current_Text_feature = array[10];
//                TextFeatures.getSymbolFonts(EditorActivity.this, selectedTextView);
//            } else {
//                requestAppPermissions_for_fonts();
//            }


//        }
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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;

        final boolean[] isShadowApplied = new boolean[1];
        final TextView head_tv, demo_tv;
        RecyclerView colors_rv;
        Button close, choose, shadow;
        ColorsAdapter colorsAdapter;
        final int[] choosen_color = new int[1];


        head_tv = (TextView) dialog.findViewById(R.id.color_text);
        demo_tv = (TextView) dialog.findViewById(R.id._demo_color_text);
        colors_rv = (RecyclerView) dialog.findViewById(R.id.colors_rv);
        close = (Button) dialog.findViewById(R.id.bt_color_close);
        choose = (Button) dialog.findViewById(R.id.bt_color_choose);
        shadow = (Button) dialog.findViewById(R.id.bt_color_shadow);

        TextUtils.setFont(this, head_tv, Constants.FONT_Google_sans_regular);
        TextUtils.setFont(this, close, Constants.FONT_Google_sans_regular);
        TextUtils.setFont(this, choose, Constants.FONT_Google_sans_regular);

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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;

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

        relativeLayout = (RelativeLayout) dialog.findViewById(R.id.root_Rl);
        demoGradient = (TextView) dialog.findViewById(R.id.demo_gradient);
        demoColor1 = (ImageView) dialog.findViewById(R.id.demo_color_1);
        demoColor2 = (ImageView) dialog.findViewById(R.id.demo_color_2);
        demoColor1Tv = (TextView) dialog.findViewById(R.id.demo_color_1_tv);
        demoColor2Tv = (TextView) dialog.findViewById(R.id.demo_color_2_tv);
        colorsRecycler = (RecyclerView) dialog.findViewById(R.id.colors_recycler);
        preview = (Button) dialog.findViewById(R.id.preview);
        btCancel = (Button) dialog.findViewById(R.id.bt_cancel);
        btApply = (Button) dialog.findViewById(R.id.bt_apply);
        colorsRecycler.setVisibility(GONE);
        preview.setVisibility(GONE);

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
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

/*Permission related Methods*/

    private void requestAppPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                (MY_PERMISSION_REQUEST_STORAGE_FROM_ONSTART));
    }

    private void requestAppPermissions_to_save_to_gallery() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                (MY_PERMISSION_REQUEST_STORAGE_FOR_SAVING_TO_GALLERY));
    }

    private void requestAppPermissions_for_fonts() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                (MY_PERMISSION_REQUEST_STORAGE_FOR_FONTS));
    }

    private void requestAppPermissions_for_launch_gallery() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                (MY_PERMISSION_REQUEST_Launch_gallery));
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
            Glide.with(this).load(picturepath).asBitmap().centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView_background);
//            imageView_background.setImageResource(picturepath);
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
                    savetoDevice(quoteLayout, EditorActivity.this, new FileUtils.onSaveComplete() {
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
                    launchGallery();
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
        } else {
//            App.showToast(activity,"Press again to discard the image and exit");

        }
    }

    /*Methods to handle view movements*/
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        RelativeLayout.LayoutParams view_Parameters = (RelativeLayout.LayoutParams) v.getLayoutParams();
        selectedView = v;
//        v.setPadding(16, 16, 16, 16);
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
        switch (event.getAction()) {

            case MotionEvent.ACTION_MOVE: {
                view_Parameters.topMargin = view_Parameters.topMargin + (int) event.getRawY() - prevY;
                prevY = (int) event.getRawY();
                view_Parameters.leftMargin = view_Parameters.leftMargin + (int) event.getRawX() - prevX;
                prevX = (int) event.getRawX();
                v.setLayoutParams(view_Parameters);
                return true;

            }
            case MotionEvent.ACTION_DOWN: {
                prevX = (int) event.getRawX();
                prevY = (int) event.getRawY();
                view_Parameters.bottomMargin = -2 * v.getHeight();
                view_Parameters.rightMargin = -2 * v.getWidth();
                v.setLayoutParams(view_Parameters);

                return true;
            }
            case MotionEvent.ACTION_UP: {
                view_Parameters.topMargin += (int) event.getRawY() - prevY;
                view_Parameters.leftMargin += (int) event.getRawX() - prevX;
                v.setLayoutParams(view_Parameters);
                return true;
            }
        }
        return false;
    }

    public class SimpleOnscaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (selectedView != null) {
                TextView selected = (TextView) selectedView;
                float size = selected.getTextSize();
                float factor = detector.getScaleFactor();
                float product = size * factor;
                selected.setTextSize(TypedValue.COMPLEX_UNIT_PX, product);
                return true;
            }
            return false;
        }
    }
}
