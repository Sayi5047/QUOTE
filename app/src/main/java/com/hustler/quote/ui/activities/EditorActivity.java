package com.hustler.quote.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.Type;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.hustler.quote.R;
import com.hustler.quote.ui.adapters.ColorsAdapter;
import com.hustler.quote.ui.adapters.ContentAdapter;
import com.hustler.quote.ui.adapters.Features_adapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.pojo.QuotesFromFC;
import com.hustler.quote.ui.superclasses.App;
import com.hustler.quote.ui.superclasses.BaseActivity;
import com.hustler.quote.ui.textFeatures.TextFeatures;
import com.hustler.quote.ui.utils.TextUtils;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

import java.io.File;
import java.util.ArrayList;

import static com.hustler.quote.ui.utils.FileUtils.savetoDevice;

public class EditorActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, View.OnTouchListener {
    private static final int RESULT_LOAD_IMAGE = 1001;
    private static final int MY_PERMISSION_REQUEST_STORAGE_FOR_GALLERY = 1002;
    private static final int MY_PERMISSION_REQUEST_STORAGE_FOR_SAVING_TO_GALLERY = 1003;


    Window windowManager;

    private LinearLayout Editor_lead_screen_linearLayout;
    private LinearLayout level_1_editor_navigator;
    private LinearLayout level_1_1_editor_font_size_manipulator;
    private LinearLayout level_1_1_1_editor_font_sizeChanger_seekbar_layout;
    private ImageView empty_image_to_be_blurred;
    private ImageView save_picture;
    private ImageView quoteAnim;
    private NestedScrollView bottomsheet;
    private boolean isTextLayout_visible;
    private boolean isImageLoaded = false;


    private LinearLayout root_layout;

    private ImageView font_module;
    private ImageView background_image_module;
    private ImageView font_size_changer;
    private ImageView close_text_size;
    private ImageView font_family_changer;
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

    private QuotesFromFC quote;
    private RelativeLayout quoteLayout;
    private RelativeLayout core_editor_layout;
    private LinearLayout text_and_bg_layout;
    private LinearLayout close_and_done_layout;

    private SeekBar seekBar;
    private Button clear_button;
    private BottomSheetBehavior bottomSheetBehavior;

    RecyclerView features_recyclerview;
    ContentAdapter contentAdapter;
    Features_adapter features_adapter;

    public File savedFile;
    ArrayList<String> items = new ArrayList<>();
    String[] itemsTo;
    private Bitmap currentbitmap;


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
    private String currentfeature;

    ScaleGestureDetector scaleGestureDetector;
    private boolean isFromEdit_Activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
//        setToolbar(this);
//        convertColors();

        findViews();
        getIntentData();
        setViews();
    }

    private void findViews() {
//        All layouts
        root_layout = (LinearLayout) findViewById(R.id.root_Lo);
        windowManager = this.getWindow();
        windowManager.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        level_1_editor_navigator = (LinearLayout) findViewById(R.id.top_editor_navigaotor_level1);
        Editor_lead_screen_linearLayout = (LinearLayout) findViewById(R.id.Main_editor_arena);
        level_1_1_editor_font_size_manipulator = (LinearLayout) findViewById(R.id.top_save_and_share_bar);
//        level_1_2_editor_background_manipulator = (LinearLayout) findViewById(R.id.editor_background_module);/**/
        level_1_1_1_editor_font_sizeChanger_seekbar_layout = (LinearLayout) findViewById(R.id.fontsize_change_module);

        quoteLayout = (RelativeLayout) findViewById(R.id.quote_layout);
        text_and_bg_layout = (LinearLayout) findViewById(R.id.text_and_background_layout);
        close_and_done_layout = (LinearLayout) findViewById(R.id.close_and_done_layout);

//      level 1 top bar buttons
        font_module = (ImageView) findViewById(R.id.font_style_changer_module);
        background_image_module = (ImageView) findViewById(R.id.font_background_chnager_module);
        save_work_button = (ImageView) findViewById(R.id.save_work_button);
        share_work_button = (ImageView) findViewById(R.id.font_share_module);
        delete_view_button = (ImageView) findViewById(R.id.delete_view_button);

//        level 1.1 text font manipulator options
        font_size_changer = (ImageView) findViewById(R.id.spacer_in_top);
//        save_picture = (ImageView) findViewById(R.id.Editor_text_module_save);

//        level 1.2 text background manipulator options
    /*    background_color_changer = (ImageView) findViewById(R.id.Editor_background_module_colored_backgrounds);
        background_opacity_changer = (ImageView) findViewById(R.id.Editor_background_module_picture_filter_changer);
        background_gallery_chooser = (ImageView) findViewById(R.id.Editor_background_module_gallery_backgrounds);
        background_module_blurred_changer = (ImageView) findViewById(R.id.Editor_background_module_blurred_backgrounds);*/
        empty_image_to_be_blurred = (ImageView) findViewById(R.id.empty_image_to_be_blurred);


//        level 1.1.1 font_text_changer_layout
        seekBar = (SeekBar) findViewById(R.id.progress_slider_bar);
        seekBar.setContentDescription("Slide to Rotate");
        close_text_size = (ImageView) findViewById(R.id.close_editor_button);

//        level 1.1.2 font color chooser
        /*this recyclerview will be keep on reused in different lavels */
        features_recyclerview = (RecyclerView) findViewById(R.id.content_rv);
        features_recyclerview.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

//      main editor text layout
        quoteAnim = (ImageView) findViewById(R.id.quote_anim);
//        quote_editor_body = (TextView) findViewById(R.id.tv_Quote_Body);
//        quote_editor_author = (TextView) findViewById(R.id.tv_Quote_Author);
        imageView_background = (ImageView) findViewById(R.id.imageView_background);


        text_layout = (TextView) findViewById(R.id.text_field);
        background_layout = (TextView) findViewById(R.id.background_and_Image_field);
        close_layout = (TextView) findViewById(R.id.close_tv);
        done_layout = (TextView) findViewById(R.id.done_tv);
        core_editor_layout = (RelativeLayout) findViewById(R.id.arena_text_layout);
        clear_button = (Button) findViewById(R.id.bt_clear);


        scaleGestureDetector = new ScaleGestureDetector(this, new SimpleOnscaleGestureListener());


//        imageView_background.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//
//                return true;
//
//            }
//        });




/*setting on click listners */
        font_module.setOnClickListener(this);
        background_image_module.setOnClickListener(this);
        text_layout.setOnClickListener(this);
        background_layout.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        font_size_changer.setOnClickListener(this);
//        font_family_changer.setOnClickListener(this);
//        save_picture.setOnClickListener(this);
        close_text_size.setOnClickListener(this);
//        background_color_changer.setOnClickListener(this);
//        background_gallery_chooser.setOnClickListener(this);
//        background_module_blurred_changer.setOnClickListener(this);
//        background_opacity_changer.setOnClickListener(this);
        save_work_button.setOnClickListener(this);
        share_work_button.setOnClickListener(this);
        delete_view_button.setOnClickListener(this);
        close_layout.setOnClickListener(this);
        done_layout.setOnClickListener(this);
        clear_button.setOnClickListener(this);


//        level_1_1_1_editor_font_sizeChanger_seekbar_layout.setVisibility(View.VISIBLE);
//        thirdDetailvisible = false;

//        managingBottomsheet();

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
                int length = quote.getBody().length();
                root_layout.setBackground(getResources().getDrawable(android.R.drawable.screen_background_light_transparent));
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

                quote_editor_body.setText(quote.getBody());
                quote_editor_author.setText(quote.getAuthor());
                quote_editor_body.setTypeface(App.getZingCursive(this, Constants.FONT_Sans_Bold));
                quote_editor_author.setTypeface(App.getZingCursive(this, Constants.FONT_Sans_Bold));

                quote_editor_body.setMaxWidth(1050);
                quote_editor_body.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                quote_editor_body.setGravity(Gravity.CENTER);
                quote_editor_author.setMaxWidth(1050);
                quote_editor_author.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                quote_editor_author.setGravity(Gravity.CENTER);

                quoteLayout.addView(quote_editor_body);
                quoteLayout.addView(quote_editor_author);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) quote_editor_body.getLayoutParams();

                params.addRule(RelativeLayout.CENTER_VERTICAL);
                RelativeLayout.LayoutParams paramsbottom = (RelativeLayout.LayoutParams) quote_editor_author.getLayoutParams();

                paramsbottom.addRule(RelativeLayout.ALIGN_BOTTOM);

                quote_editor_body.setLayoutParams(params);
                quote_editor_author.setLayoutParams(paramsbottom);


                quote_editor_author.setOnTouchListener(this);
                quote_editor_body.setOnTouchListener(this);
            }


        }
    }

    private void getIntentData() {
        if ((Boolean) getIntent().getBooleanExtra(Constants.INTENT_IS_FROM_EDIT_KEY, false)) {
            isFromEdit_Activity = true;
            quote = (QuotesFromFC) getIntent().getSerializableExtra(Constants.INTENT_QUOTE_OBJECT_KEY);

        } else {
            isFromEdit_Activity = false;
        }
    }


    private void convertColors() {
//        items=null;
        itemsTo = getResources().getStringArray(R.array.allColors);
//        for (int i = 0; i < itemsTo.length; i++) {
//            items.add(i, itemsTo[i]);
//        }
//        Log.d("Colors Added -->", "done");

    }

    private void convertFonts() {
//        items=null;
        itemsTo = getResources().getStringArray(R.array.allfonts);
//        for (int i = 0; i < itemsTo.length; i++) {
//            items.add(i, itemsTo[i]);
//        }
//        Log.d("Fonts Added -->", "done");
    }

    private void convertFeatures(String[] valueArray) {
        itemsTo = valueArray;
    }

    private void managingBottomsheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomsheet));
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setHideable(true);

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
                    clear_button.setVisibility(View.GONE);
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
                close_and_done_layout.setVisibility(View.GONE);
                text_and_bg_layout.setVisibility(View.VISIBLE);
                features_recyclerview.setVisibility(View.VISIBLE);
                seekBar.setProgress(180);
                seekBar.setVisibility(View.GONE);
                handle_close_Feature();


            }
            break;
            case R.id.done_tv: {
                close_and_done_layout.setVisibility(View.GONE);
                text_and_bg_layout.setVisibility(View.VISIBLE);
                features_recyclerview.setVisibility(View.VISIBLE);
                seekBar.setVisibility(View.GONE);
            }
            break;
            case R.id.save_work_button: {
                if (isPermissionAvailable()) {

                    savedFile = savetoDevice(quoteLayout);
                    if (savedFile != null) {
                        Toast.makeText(this, "File Saved", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    requestAppPermissions_to_save_to_gallery();
                }

            }
            break;
            case R.id.font_share_module: {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, quote_editor_body.getText());
                shareIntent.putExtra(Intent.EXTRA_TITLE, quote_editor_author.getText());
                Uri uri = null;
                if (savedFile != null) {
                    uri = Uri.fromFile(savedFile);
                } else {
                    savedFile = savetoDevice(quoteLayout);
                    if (savedFile != null) {
                        uri = Uri.fromFile(savedFile);
                    } else {
                        App.showToast(this, getString(R.string.Unable_to_save_share_image));
                        return;
                    }
                }
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/jpeg");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "send"));

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
                                    close_and_done_layout.setVisibility(View.GONE);
                                    text_and_bg_layout.setVisibility(View.VISIBLE);
                                    features_recyclerview.setVisibility(View.VISIBLE);
                                    seekBar.setProgress(180);
                                    seekBar.setVisibility(View.GONE);
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
//            case R.id.share_work_button: {
//                Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.coming_soon));
//            }
//            break;
            case R.id.close_editor_button: {
                super.onBackPressed();
            }
            break;
//
////            MAIN NAVIGATOR BUTTONG HANDLING
//            case R.id.font_style_changer_module: {
////                isTextLayout_visible = true;
////                if (level_1_1_editor_font_size_manipulator.getVisibility() == View.VISIBLE) {
////                    level_1_1_editor_font_size_manipulator.setVisibility(View.GONE);
////
////
//////                } else if (level_1_2_editor_background_manipulator.getVisibility() == View.VISIBLE) {
//////                    level_1_2_editor_background_manipulator.setVisibility(View.GONE);
//////                    level_1_1_editor_font_size_manipulator.setVisibility(View.VISIBLE);
//////
//////
//////                } else {
////                    level_1_1_editor_font_size_manipulator.setVisibility(View.VISIBLE);
////
////                }
//            }
//            break;
//
//            case R.id.font_background_chnager_module: {
////                isTextLayout_visible = false;
////                if (level_1_1_editor_font_size_manipulator.getVisibility() == View.VISIBLE) {
////                    level_1_1_editor_font_size_manipulator.setVisibility(View.GONE);
////                    level_1_2_editor_background_manipulator.setVisibility(View.VISIBLE);
////
////                } else if (level_1_2_editor_background_manipulator.getVisibility() == View.VISIBLE) {
////                    level_1_2_editor_background_manipulator.setVisibility(View.GONE);
////                } else {
////                    level_1_1_editor_font_size_manipulator.setVisibility(View.VISIBLE);
////
////                }
//
//            }
//            break;
////            BACKGROUND MODULE CASES
//            case R.id.Editor_background_module_colored_backgrounds: {
//                if (level_1_1_1_editor_font_sizeChanger_seekbar_layout.getVisibility() == View.VISIBLE) {
//                    level_1_1_1_editor_font_sizeChanger_seekbar_layout.setVisibility(View.GONE);
//                }
//                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                    bottomSheetBehavior.setPeekHeight(80);
//                    setBackgroundColorRecyclerView();
//
//
//                } else {
//
//                    if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
//                        bottomSheetBehavior.setPeekHeight(0);
//                        features_recyclerview.setAdapter(null);
//                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                    }
//                }
//            }
//            break;
//
//            case R.id.Editor_background_module_gallery_backgrounds: {
//                imageView_background.setVisibility(View.VISIBLE);
//
//                if (isPermissionAvailable()) {
//
//                    launchGallery();
//                } else {
//                    requestAppPermissions();
//                }
//
//
//            }
//            break;
//            case R.id.Editor_background_module_blurred_backgrounds: {
//
//                if (!isImageLoaded) {
//                    Toast.makeText(this, "Please,select an image to apply blurr effect", Toast.LENGTH_SHORT).show();
//                } else {
//                    if (level_1_1_1_editor_font_sizeChanger_seekbar_layout.getVisibility() == View.VISIBLE) {
//                        level_1_1_1_editor_font_sizeChanger_seekbar_layout.setVisibility(View.GONE);
//                    } else {
//                        level_1_1_1_editor_font_sizeChanger_seekbar_layout.setVisibility(View.VISIBLE);
//                        seekBar.setOnSeekBarChangeListener(this);
//
//                    }
//                }
//
//            }
//            break;
//            case R.id.Editor_background_module_picture_filter_changer: {
//                Toast.makeText(this, "Coming Soon...!", Toast.LENGTH_SHORT).show();
//            }
//            break;
//*/
        }
    }


    /*COLONY BACKGROUND*/
    private void setBackground_features_rv() {
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
                Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, clickedItem);
                enable_Selected_Background_Features(clickedItem, getResources().getStringArray(R.array.Background_features));

            }
        });

        features_recyclerview.setAdapter(features_adapter);
        features_recyclerview.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slideup));

    }

    /*COLONY TEXT*/
    private void setText_Features_rv() {
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
            add_and_EditText(array, false);
        } else if (feature.equalsIgnoreCase(array[1])) {
            if (selectedView == null) {
                Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
            } else {
                add_and_EditText(array, true);
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
        }
    }


    private void enable_Selected_Background_Features(String clickedItem, String[] stringArray) {

    }

    private void launchGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    private void setBackgroundColorRecyclerView() {
        convertColors();
        contentAdapter = new ContentAdapter(this, itemsTo, new ContentAdapter.onItemClickListener() {
            @Override
            public void onItemColorClick(int color) {
                isImageLoaded = false;
                if (imageView_background.getDrawingCache() != null) {
                    currentbitmap = null;
                    imageView_background.destroyDrawingCache();
                }
                imageView_background.setVisibility(View.GONE);

                quoteLayout.setBackgroundColor(color);
            }

            @Override
            public void onItemFontClick(String font) {

            }
        }, false);
        features_recyclerview.setAdapter(contentAdapter);
    }

    /*Recyclerview related methods*/
    private void setFontColorRecyclerView() {
        convertColors();
        contentAdapter = new ContentAdapter(this, itemsTo, new ContentAdapter.onItemClickListener() {
            @Override
            public void onItemColorClick(int color) {
                quote_editor_body.setTextColor(color);
                quote_editor_author.setTextColor(color);
            }

            @Override
            public void onItemFontClick(String font) {

            }
        }, false);
        features_recyclerview.setAdapter(contentAdapter);
    }

    private void setFontTypeRecyclerview() {
        convertFonts();
        contentAdapter = new ContentAdapter(this, itemsTo, new ContentAdapter.onItemClickListener() {
            @Override
            public void onItemColorClick(int color) {

            }

            @Override
            public void onItemFontClick(String font) {

                quote_editor_body.setTypeface(App.getZingCursive(EditorActivity.this, font));
                quote_editor_author.setTypeface(App.getZingCursive(EditorActivity.this, font));
            }

        }, true);
        features_recyclerview.setAdapter(contentAdapter);
    }

/*Blurring method*/

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public Bitmap create_blur(Bitmap src_Bitmap, float radius) {
        /*Handle radius*/
        if (radius < 0) {
            radius = 0.1f;
        } else if (radius > 25) {
            radius = 25.0f;
        }

        /*Create a bitmap with the sousrce*/
//        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        /*Create a renderscript object*/
        RenderScript renderScript = RenderScript.create(this);

        Allocation blurr_Input_Allocation = Allocation.createFromBitmap(renderScript, src_Bitmap);
        Type type = blurr_Input_Allocation.getType();
        Allocation blurr_Output_Allocation = Allocation.createTyped(renderScript, type);


//        Create ScriptIntrensicBlur object (Hero of the story)

        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(renderScript, blurr_Input_Allocation.getElement());

        /* SET INPUT
        * SET RADIUS
        * SET FOR EACH PIXEL IN OUTPUT ALLOCATION
        * COPY THE DATA TO BITMAP
        * DESTORY IT
        * RETURN IT*/
        intrinsicBlur.setRadius(radius);
        intrinsicBlur.setInput(blurr_Input_Allocation);
        intrinsicBlur.forEach(blurr_Output_Allocation);
//        Copy to bitmap
//        destroy all to free memory
        blurr_Output_Allocation.copyTo(src_Bitmap);
        blurr_Input_Allocation.destroy();
        intrinsicBlur.destroy();

        return src_Bitmap;


    }

    /*Seekbar methods*/
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        handle_seekbar_value(seekBar);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    private void handle_close_Feature() {
        TextView current_text_view = (TextView) selectedView;

        if (currentfeature.equalsIgnoreCase(getResources().getStringArray(R.array.Text_features)[2])) {
            current_text_view.setTextSize(25);
        }
        if (currentfeature.equalsIgnoreCase(getResources().getStringArray(R.array.Text_features)[3])) {
            current_text_view.setRotation(0);
        }
        if (currentfeature.equalsIgnoreCase(getResources().getStringArray(R.array.Text_features)[4])) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                current_text_view.setLetterSpacing(0);
            }
        }
        if (currentfeature.equalsIgnoreCase(getResources().getStringArray(R.array.Text_features)[6])) {
            current_text_view.setLineSpacing(15, 1.0f);

        }
        if (currentfeature.equalsIgnoreCase(getResources().getStringArray(R.array.Text_features)[7])) {
            current_text_view.setPadding(16, 16, 16, 16);

        }
    }

    private void handle_seekbar_value(SeekBar seekBar) {
        float radius = (float) seekBar.getProgress();
        TextView selected_textView = (TextView) selectedView;
        if (currentfeature.equalsIgnoreCase(getResources().getStringArray(R.array.Text_features)[2]) && radius >= 0) {
// TODO: 13/12/21617 implement a new seekbar
            float size = radius / 6;
            selected_textView.setTextSize(size);
        } else if (currentfeature.equalsIgnoreCase(getResources().getStringArray(R.array.Text_features)[3]) && radius >= 0) {
            float degree = radius - 180;
            selected_textView.setRotation(degree);
        } else if (currentfeature.equalsIgnoreCase(getResources().getStringArray(R.array.Text_features)[4]) && radius >= 0) {
            float degree = radius / 500;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                selected_textView.setLetterSpacing(degree);
            } else {
                Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.sorry));
            }
        } else if (currentfeature.equalsIgnoreCase(getResources().getStringArray(R.array.Text_features)[6]) && radius >= 0) {
            float degrer = radius / 100;
            selected_textView.setLineSpacing(15, degrer);
        } else if (currentfeature.equalsIgnoreCase(getResources().getStringArray(R.array.Text_features)[7]) && radius >= 0) {
            int degrer = (int) radius*3;
            Log.d("PADDIG DEGEREE", degrer + "");
            selected_textView.setMaxWidth(degrer);
        }
        ////        imageView_background.setDrawingCacheEnabled(true);
//        if (isTextLayout_visible) {
//            quote_editor_body.setTextSize(radius);
//        } else {
//            imageView_background.buildDrawingCache();
//            currentbitmap = imageView_background.getDrawingCache();
//            imageView_background.setImageBitmap(create_blur(currentbitmap, radius));
//        }

    }


    //    FEATURES
// THIS MEHOD CAN't be moved outside becuase it has lot of dependies in this page.
    private void add_and_EditText(String[] array, final boolean isEdit) {
        currentfeature = array[0];
        final int[] alignment = new int[1];
        final Dialog dialog = new Dialog(this, R.style.EditTextDialog);
        dialog.setContentView(View.inflate(this, R.layout.addtext, null));
        final TextView header, align_text;
        final EditText addingText;
        Button close, done, start, center, end;
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


        if (isEdit) {
            addingText.setText(((TextView) selectedView).getText());
        }
        TextUtils.setFont(this, header, Constants.FONT_Sans_Bold);
        TextUtils.setFont(this, addingText, Constants.FONT_Sans_Bold);
        TextUtils.setFont(this, align_text, Constants.FONT_Sans_Bold);
        TextUtils.setFont(this, start, Constants.FONT_Sans_Bold);
        TextUtils.setFont(this, center, Constants.FONT_Sans_Bold);
        TextUtils.setFont(this, end, Constants.FONT_Sans_Bold);
        TextUtils.setFont(this, close, Constants.FONT_NEVIS);
        TextUtils.setFont(this, done, Constants.FONT_NEVIS);

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
                        textView.setTextSize(16.0f);
                        textView.setTextColor(getResources().getColor(R.color.textColor));
                        textView.setMaxWidth(core_editor_layout.getWidth());
                        TextUtils.setFont(EditorActivity.this, textView, Constants.FONT_Sans_Bold);
                        textView.setText(newly_Added_Text);
                        textView.setId(addedTextIds);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast_Snack_Dialog_Utils.show_ShortToast(EditorActivity.this, " " + textView.getId() + " ");
                            }
                        });
                        set_text_alignment(alignment[0], textView);


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

    private void resizeText(String[] array) {
         /*RESIZE*/
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            features_recyclerview.setVisibility(View.GONE);
            seekBar.setVisibility(View.VISIBLE);
            text_and_bg_layout.setVisibility(View.GONE);
            close_and_done_layout.setVisibility(View.VISIBLE);
            previousstate = selectedView;
            currentfeature = array[2];


        }
    }

    private void rotateText(String[] array) {
         /*RESIZE*/
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            features_recyclerview.setVisibility(View.GONE);
            seekBar.setVisibility(View.VISIBLE);
            text_and_bg_layout.setVisibility(View.GONE);
            close_and_done_layout.setVisibility(View.VISIBLE);
            previousstate = selectedView;
            currentfeature = array[3];


        }
    }

    private void spaceText(String[] array) {
         /*RESIZE*/
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            features_recyclerview.setVisibility(View.GONE);
            seekBar.setVisibility(View.VISIBLE);
            text_and_bg_layout.setVisibility(View.GONE);
            close_and_done_layout.setVisibility(View.VISIBLE);
            previousstate = selectedView;
            currentfeature = array[4];


        }
    }

    private void spaceLine(String[] array) {
         /*RESIZE*/
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            features_recyclerview.setVisibility(View.GONE);
            seekBar.setVisibility(View.VISIBLE);
            text_and_bg_layout.setVisibility(View.GONE);
            close_and_done_layout.setVisibility(View.VISIBLE);
            previousstate = selectedView;
            currentfeature = array[6];


        }
    }

    private void adjustFrameWidth(String[] array) {
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            features_recyclerview.setVisibility(View.GONE);
            seekBar.setVisibility(View.VISIBLE);
            text_and_bg_layout.setVisibility(View.GONE);
            close_and_done_layout.setVisibility(View.VISIBLE);
            previousstate = selectedView;
            currentfeature = array[7];


        }
    }

    private void colorText(String[] array) {
        final TextView selectedTextView = (TextView) selectedView;
//        RelativeLayout.LayoutParams params = ((RelativeLayout.LayoutParams) selectedTextView.getLayoutParams());
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            currentfeature = array[5];
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

            TextUtils.setFont(this, head_tv, Constants.FONT_Sans_Bold);
            TextUtils.setFont(this, close, Constants.FONT_Sans_Bold);
            TextUtils.setFont(this, choose, Constants.FONT_Sans_Bold);

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
            currentfeature = array[8];
            TextFeatures.apply_Text_Shadow(EditorActivity.this, selectedTextView);


        }
    }


    private void applyFont(String[] array) {
        final TextView selectedTextView = (TextView) selectedView;
        if (selectedView == null) {
            Toast_Snack_Dialog_Utils.show_ShortToast(this, getString(R.string.please_select_text));
        } else {
            previousstate = selectedView;
            currentfeature = array[9];
            TextFeatures.apply_font(EditorActivity.this, selectedTextView);


        }
    }

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




/*Permission related Methods*/

    private void requestAppPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                (MY_PERMISSION_REQUEST_STORAGE_FOR_GALLERY));
    }

    private void requestAppPermissions_to_save_to_gallery() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                (MY_PERMISSION_REQUEST_STORAGE_FOR_SAVING_TO_GALLERY));
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
                currentbitmap = null;
                imageView_background.destroyDrawingCache();
            }
            Glide.with(this).load(picturepath).asBitmap().centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView_background);
            isImageLoaded = true;
//            imageView_background.setImageResource(picturepath);
        }
    }

    private boolean isPermissionAvailable() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE_FOR_GALLERY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchGallery();
                }
            }
            break;
            case MY_PERMISSION_REQUEST_STORAGE_FOR_SAVING_TO_GALLERY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    savedFile = savetoDevice(quoteLayout);
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
            selectedView.setBackground(getResources().getDrawable(R.drawable.tv_bg));

        } else {
            clear_button.setVisibility(View.VISIBLE);
            previousSelcted_View = v;
            selectedView.setBackground(getResources().getDrawable(R.drawable.tv_bg));

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
                Log.d("TextSizeStart", String.valueOf(size));

                float factor = detector.getScaleFactor();
                Log.d("Factor", String.valueOf(factor));


                float product = size * factor;
                Log.d("TextSize", String.valueOf(product));
                selected.setTextSize(TypedValue.COMPLEX_UNIT_PX, product);
//            textView.setRotation(product);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                selected.setLetterSpacing(product);
//            }

                size = selected.getTextSize();
                Log.d("TextSizeEnd", String.valueOf(size));
                return true;
            }
            return false;
        }
    }
}



