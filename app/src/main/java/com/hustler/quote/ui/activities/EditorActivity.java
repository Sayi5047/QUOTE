package com.hustler.quote.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
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
import com.hustler.quote.ui.adapters.ContentAdapter;
import com.hustler.quote.ui.adapters.Features_adapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.pojo.QuotesFromFC;
import com.hustler.quote.ui.superclasses.App;
import com.hustler.quote.ui.superclasses.BaseActivity;
import com.hustler.quote.ui.utils.TextUtils;
import com.hustler.quote.ui.utils.ToastSnackDialogUtils;

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
    private LinearLayout root_layout;

    private ImageView font_module;
    private ImageView background_image_module;
    private ImageView empty_image_to_be_blurred;
    private ImageView font_size_changer;
    private ImageView save_picture;
    private ImageView close_text_size;
    private ImageView font_family_changer;
    private ImageView font_save_module;
    private ImageView font_share_module;
    private ImageView quoteAnim;
    private ImageView imageView_background;

    private TextView quote_editor_body;
    private TextView quote_editor_author;
    private TextView text_layout;
    private TextView background_layout;

    private QuotesFromFC quote;
    private RelativeLayout quoteLayout;
    private RelativeLayout quote_layout;

    private SeekBar font_size_changing_seekbar;
    private BottomSheetBehavior bottomSheetBehavior;
    private NestedScrollView bottomsheet;

    RecyclerView features_recyclerview;
    ContentAdapter contentAdapter;
    Features_adapter features_adapter;

    public File savedFile;
    ArrayList<String> items = new ArrayList<>();
    String[] itemsTo;
    private Bitmap currentbitmap;


    private int backpressCount = 0;
    private boolean isTextLayout_visible;
    private boolean isImageLoaded = false;
    private String newly_Added_Text;

    //    Varaible to give ids to  newly added items
    int addedTextIds = 0;

    //    Variables for moving items onTouch
    int prevX, prevY;

    //    VIEWS FOR CURRENTLY SELECTED AND PREVIOUS
    View selectedtextID;
    View previousView;

    ScaleGestureDetector scaleGestureDetector;

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

//      level 1 top bar buttons
        font_module = (ImageView) findViewById(R.id.font_style_changer_module);
        background_image_module = (ImageView) findViewById(R.id.font_background_chnager_module);
        font_save_module = (ImageView) findViewById(R.id.save_work_button);
        font_share_module = (ImageView) findViewById(R.id.font_share_module);

//        level 1.1 text font manipulator options
        font_size_changer = (ImageView) findViewById(R.id.spacer_in_top);
//        save_picture = (ImageView) findViewById(R.id.Editor_text_module_save);
        font_family_changer = (ImageView) findViewById(R.id.share_work_button);

//        level 1.2 text background manipulator options
    /*    background_color_changer = (ImageView) findViewById(R.id.Editor_background_module_colored_backgrounds);
        background_opacity_changer = (ImageView) findViewById(R.id.Editor_background_module_picture_filter_changer);
        background_gallery_chooser = (ImageView) findViewById(R.id.Editor_background_module_gallery_backgrounds);
        background_module_blurred_changer = (ImageView) findViewById(R.id.Editor_background_module_blurred_backgrounds);*/
        empty_image_to_be_blurred = (ImageView) findViewById(R.id.empty_image_to_be_blurred);


//        level 1.1.1 font_text_changer_layout
        font_size_changing_seekbar = (SeekBar) findViewById(R.id.progress_slider_bar);
        close_text_size = (ImageView) findViewById(R.id.close_editor_button);

//        level 1.1.2 font color chooser
        /*this recyclerview will be keep on reused in different lavels */
        features_recyclerview = (RecyclerView) findViewById(R.id.content_rv);
        features_recyclerview.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

//      main editor text layout
        quoteAnim = (ImageView) findViewById(R.id.quote_anim);
        quote_editor_body = (TextView) findViewById(R.id.tv_Quote_Body);
        quote_editor_author = (TextView) findViewById(R.id.tv_Quote_Author);
        imageView_background = (ImageView) findViewById(R.id.imageView_background);


        text_layout = (TextView) findViewById(R.id.text_field);
        background_layout = (TextView) findViewById(R.id.background_and_Image_field);
        quote_layout = (RelativeLayout) findViewById(R.id.arena_text_layout);
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

        font_size_changer.setOnClickListener(this);
        font_family_changer.setOnClickListener(this);
//        save_picture.setOnClickListener(this);
        close_text_size.setOnClickListener(this);
//        background_color_changer.setOnClickListener(this);
//        background_gallery_chooser.setOnClickListener(this);
//        background_module_blurred_changer.setOnClickListener(this);
//        background_opacity_changer.setOnClickListener(this);
        font_save_module.setOnClickListener(this);
        font_share_module.setOnClickListener(this);

//        level_1_1_1_editor_font_sizeChanger_seekbar_layout.setVisibility(View.VISIBLE);
//        thirdDetailvisible = false;

//        managingBottomsheet();

        setText_Features_rv();
    }

    private void managingBottomsheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomsheet));
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setHideable(true);

    }


    private void getIntentData() {
        quote = (QuotesFromFC) getIntent().getSerializableExtra(Constants.INTENT_QUOTE_OBJECT);
    }

    private void setViews() {
        if (quote != null) {
            int length = quote.getBody().length();
            root_layout.setBackgroundColor(getResources().getColor(R.color.bg));
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
            quote_editor_body.setTextColor(quote.getColor());
            quote_editor_author.setTextColor(quote.getColor());
            quote_editor_body.setTypeface(App.getZingCursive(this, Constants.FONT_Sans_Bold));
            quote_editor_author.setTypeface(App.getZingCursive(this, Constants.FONT_Sans_Bold));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.text_field: {
//                setBackgroundColorRecyclerView();
                setText_Features_rv();
            }
            break;
            case R.id.background_and_Image_field: {
                setBackground_features_rv();
            }
            break;


//            MAIN NAVIGATOR BUTTONG HANDLING
            case R.id.font_style_changer_module: {
//                isTextLayout_visible = true;
//                if (level_1_1_editor_font_size_manipulator.getVisibility() == View.VISIBLE) {
//                    level_1_1_editor_font_size_manipulator.setVisibility(View.GONE);
//
//
////                } else if (level_1_2_editor_background_manipulator.getVisibility() == View.VISIBLE) {
////                    level_1_2_editor_background_manipulator.setVisibility(View.GONE);
////                    level_1_1_editor_font_size_manipulator.setVisibility(View.VISIBLE);
////
////
////                } else {
//                    level_1_1_editor_font_size_manipulator.setVisibility(View.VISIBLE);
//
//                }
            }
            break;

            case R.id.font_background_chnager_module: {
//                isTextLayout_visible = false;
//                if (level_1_1_editor_font_size_manipulator.getVisibility() == View.VISIBLE) {
//                    level_1_1_editor_font_size_manipulator.setVisibility(View.GONE);
//                    level_1_2_editor_background_manipulator.setVisibility(View.VISIBLE);
//
//                } else if (level_1_2_editor_background_manipulator.getVisibility() == View.VISIBLE) {
//                    level_1_2_editor_background_manipulator.setVisibility(View.GONE);
//                } else {
//                    level_1_1_editor_font_size_manipulator.setVisibility(View.VISIBLE);
//
//                }

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


//            TEXT MODULE CASES

            case R.id.spacer_in_top: {
//                if (level_1_1_1_editor_font_sizeChanger_seekbar_layout.getVisibility() == View.VISIBLE) {
//                    level_1_1_1_editor_font_sizeChanger_seekbar_layout.setVisibility(View.GONE);
//                } else {
//                    level_1_1_1_editor_font_sizeChanger_seekbar_layout.setVisibility(View.VISIBLE);
//                    font_size_changing_seekbar.setOnSeekBarChangeListener(this);
//
//                }
            }
            break;

//            case R.id.Editor_text_module_save: {
//
//            }
//            break;

            case R.id.share_work_button: {

                ToastSnackDialogUtils.show_ShortToast(this, getString(R.string.coming_soon));


            }
            break;
            case R.id.close_editor_button: {
//                thirdDetailvisible = false;
//                level_1_1_1_editor_font_sizeChanger_seekbar_layout.setVisibility(View.GONE);
                super.onBackPressed();
            }
            break;
/*

//            BACKGROUND MODULE CASES
            case R.id.Editor_background_module_colored_backgrounds: {
                if (level_1_1_1_editor_font_sizeChanger_seekbar_layout.getVisibility() == View.VISIBLE) {
                    level_1_1_1_editor_font_sizeChanger_seekbar_layout.setVisibility(View.GONE);
                }
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    bottomSheetBehavior.setPeekHeight(80);
                    setBackgroundColorRecyclerView();


                } else {

                    if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                        bottomSheetBehavior.setPeekHeight(0);
                        features_recyclerview.setAdapter(null);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }
            }
            break;

            case R.id.Editor_background_module_gallery_backgrounds: {
                imageView_background.setVisibility(View.VISIBLE);

                if (isPermissionAvailable()) {

                    launchGallery();
                } else {
                    requestAppPermissions();
                }


            }
            break;
            case R.id.Editor_background_module_blurred_backgrounds: {

                if (!isImageLoaded) {
                    Toast.makeText(this, "Please,select an image to apply blurr effect", Toast.LENGTH_SHORT).show();
                } else {
                    if (level_1_1_1_editor_font_sizeChanger_seekbar_layout.getVisibility() == View.VISIBLE) {
                        level_1_1_1_editor_font_sizeChanger_seekbar_layout.setVisibility(View.GONE);
                    } else {
                        level_1_1_1_editor_font_sizeChanger_seekbar_layout.setVisibility(View.VISIBLE);
                        font_size_changing_seekbar.setOnSeekBarChangeListener(this);

                    }
                }

            }
            break;
            case R.id.Editor_background_module_picture_filter_changer: {
                Toast.makeText(this, "Coming Soon...!", Toast.LENGTH_SHORT).show();
            }
            break;
*/


        }
    }

    private void setBackground_features_rv() {
        text_layout.setTextColor(getResources().getColor(R.color.black_overlay));
        background_layout.setTextColor(getResources().getColor(android.R.color.black));
        background_layout.setTextSize(16.0f);
        text_layout.setTextSize(12.0f);
        features_recyclerview.setAdapter(null);
        features_recyclerview.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slidedown));

        convertFeatures(getResources().getStringArray(R.array.Background_features));
        features_adapter = new Features_adapter(this, "Background_features", itemsTo, new Features_adapter.OnFeature_ItemClickListner() {
            @Override
            public void onItemClick(String clickedItem) {
                ToastSnackDialogUtils.show_ShortToast(EditorActivity.this, clickedItem);
                enable_Selected_Background_Features(clickedItem, getResources().getStringArray(R.array.Background_features));

            }
        });

        features_recyclerview.setAdapter(features_adapter);
        features_recyclerview.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slideup));

    }


    private void setText_Features_rv() {
        text_layout.setTextSize(16.0f);
        background_layout.setTextSize(12.0f);

        background_layout.setTextColor(getResources().getColor(R.color.black_overlay));
        text_layout.setTextColor(getResources().getColor(android.R.color.black));
        features_recyclerview.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slidedown));
        convertFeatures(getResources().getStringArray(R.array.Background_features));
        features_recyclerview.setAdapter(null);


        features_adapter = new Features_adapter(this, "Text_features", itemsTo, new Features_adapter.OnFeature_ItemClickListner() {
            @Override
            public void onItemClick(String clickedItem) {
                ToastSnackDialogUtils.show_ShortToast(EditorActivity.this, clickedItem);
                enable_Selected_Text_Feature(clickedItem, getResources().getStringArray(R.array.Text_features));
            }
        });

        features_recyclerview.setAdapter(features_adapter);
        features_recyclerview.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slideup));

    }

    private void enable_Selected_Text_Feature(String feature, String[] array) {
        if (feature.equalsIgnoreCase(array[0])) {
            final Dialog dialog = new Dialog(this, R.style.EditTextDialog);
            dialog.setContentView(View.inflate(this, R.layout.addtext, null));
            TextView header;
            final EditText addingText;
            Button close, done;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


            header = (TextView) dialog.findViewById(R.id.tv_header);
            addingText = (EditText) dialog.findViewById(R.id.et_text);
            close = (Button) dialog.findViewById(R.id.bt_close);
            done = (Button) dialog.findViewById(R.id.bt_done);

            TextUtils.setFont(this, header, Constants.FONT_Sans_Bold);
            TextUtils.setFont(this, addingText, Constants.FONT_Sans_Bold);
            TextUtils.setFont(this, close, Constants.FONT_NEVIS);
            TextUtils.setFont(this, done, Constants.FONT_NEVIS);

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addedTextIds++;
                    newly_Added_Text = addingText.getText().toString();
                    final TextView textView = new TextView(EditorActivity.this);
                    textView.setTextSize(16.0f);
                    textView.setTextColor(getResources().getColor(R.color.textColor));
                    textView.setText(newly_Added_Text);
                    textView.setId(addedTextIds);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastSnackDialogUtils.show_ShortToast(EditorActivity.this, " " + textView.getId() + " ");
                        }
                    });
                    textView.setOnTouchListener(EditorActivity.this);
                    quote_layout.addView(textView);
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

        } else if (feature.equalsIgnoreCase(itemsTo[1])) {

        } else if (feature.equalsIgnoreCase(itemsTo[2])) {

        } else if (feature.equalsIgnoreCase(itemsTo[3])) {

        } else if (feature.equalsIgnoreCase(itemsTo[4])) {

        } else if (feature.equalsIgnoreCase(itemsTo[5])) {

        } else if (feature.equalsIgnoreCase(itemsTo[6])) {

        } else if (feature.equalsIgnoreCase(itemsTo[7])) {

        } else if (feature.equalsIgnoreCase(itemsTo[8])) {

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

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
//        Toast.makeText(getApplicationContext(), "seekbar touch started!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        float radius = (float) seekBar.getProgress();
//        imageView_background.setDrawingCacheEnabled(true);
        if (isTextLayout_visible) {
            quote_editor_body.setTextSize(radius);
        } else {
            imageView_background.buildDrawingCache();
            currentbitmap = imageView_background.getDrawingCache();
            imageView_background.setImageBitmap(create_blur(currentbitmap, radius));
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
        selectedtextID = v;
        v.setPadding(16, 16, 16, 16);
        if (previousView != null) {
            previousView.setBackground(null);
            previousView = v;
            selectedtextID.setBackground(getResources().getDrawable(R.drawable.tv_bg));

        }
        else {
            previousView=v;
            selectedtextID.setBackground(getResources().getDrawable(R.drawable.tv_bg));

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
            TextView selected = (TextView) selectedtextID;

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
    }
}



