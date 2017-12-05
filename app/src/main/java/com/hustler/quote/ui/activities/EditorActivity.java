package com.hustler.quote.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.pojo.QuotesFromFC;
import com.hustler.quote.ui.superclasses.App;
import com.hustler.quote.ui.superclasses.BaseActivity;
import com.hustler.quote.ui.utils.ToastSnackDialogUtils;

import java.io.File;
import java.util.ArrayList;

import static com.hustler.quote.ui.utils.FileUtils.savetoDevice;

public class EditorActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private static final int RESULT_LOAD_IMAGE = 1001;
    private static final int MY_PERMISSION_REQUEST_STORAGE_FOR_GALLERY = 1002;
    private static final int MY_PERMISSION_REQUEST_STORAGE_FOR_SAVING_TO_GALLERY = 1003;


    Window windowManager;
    private LinearLayout Editor_lead_screen_linearLayout,
            level_1_editor_navigator,
            level_1_1_editor_font_size_manipulator,
            level_1_1_1_editor_font_sizeChanger_seekbar_layout,
            root_layout;
    private ImageView font_module,
            background_image_module,
            empty_image_to_be_blurred,
            font_size_changer,
            save_picture,
            close_text_size,
            font_family_changer, font_save_module, font_share_module, quoteAnim, imageView_background;
    private TextView quote_editor_body,
            quote_editor_author;
    private QuotesFromFC quote;
    private RelativeLayout quoteLayout;
    private SeekBar font_size_changing_seekbar;

    private BottomSheetBehavior bottomSheetBehavior;
    private NestedScrollView bottomsheet;
    RecyclerView bottomlayout_recyclerview;

    ContentAdapter contentAdapter;
    public File savedFile;
    ArrayList<String> items = new ArrayList<>();
    String[] itemsTo;
    private int backpressCount = 0;
    private boolean isTextLayout_visible;
    private Bitmap currentbitmap;
    private boolean isImageLoaded = false;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-10-10 22:39:19 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
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


    private void findViews() {
//        All layouts
        root_layout = (LinearLayout) findViewById(R.id.root_cl);
        windowManager = this.getWindow();
        windowManager.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        level_1_editor_navigator = (LinearLayout) findViewById(R.id.top_editor_navigaotor_level1);
        Editor_lead_screen_linearLayout = (LinearLayout) findViewById(R.id.Editor_lead_screen_linearLayout);
        level_1_1_editor_font_size_manipulator = (LinearLayout) findViewById(R.id.editor_font_module);
//        level_1_2_editor_background_manipulator = (LinearLayout) findViewById(R.id.editor_background_module);/**/
        level_1_1_1_editor_font_sizeChanger_seekbar_layout = (LinearLayout) findViewById(R.id.fontsize_change_module);

        quoteLayout = (RelativeLayout) findViewById(R.id.quote_layout);

//      level 1 top bar buttons
        font_module = (ImageView) findViewById(R.id.font_style_changer_module);
        background_image_module = (ImageView) findViewById(R.id.font_background_chnager_module);
        font_save_module = (ImageView) findViewById(R.id.font_save_module);
        font_share_module = (ImageView) findViewById(R.id.font_share_module);

//        level 1.1 text font manipulator options
        font_size_changer = (ImageView) findViewById(R.id.Editor_text_module_Size);
        save_picture = (ImageView) findViewById(R.id.Editor_text_module_save);
        font_family_changer = (ImageView) findViewById(R.id.Editor_text_share);

//        level 1.2 text background manipulator options
    /*    background_color_changer = (ImageView) findViewById(R.id.Editor_background_module_colored_backgrounds);
        background_opacity_changer = (ImageView) findViewById(R.id.Editor_background_module_picture_filter_changer);
        background_gallery_chooser = (ImageView) findViewById(R.id.Editor_background_module_gallery_backgrounds);
        background_module_blurred_changer = (ImageView) findViewById(R.id.Editor_background_module_blurred_backgrounds);*/
        empty_image_to_be_blurred = (ImageView) findViewById(R.id.empty_image_to_be_blurred);


//        level 1.1.1 font_text_changer_layout
        font_size_changing_seekbar = (SeekBar) findViewById(R.id.fontChange_seekbar);
        close_text_size = (ImageView) findViewById(R.id.font_change_close_button);

//        level 1.1.2 font color chooser
        /*this recyclerview will be keep on reused in different lavels */
        bottomlayout_recyclerview = (RecyclerView) findViewById(R.id.content_rv);
        bottomlayout_recyclerview.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

//      main editor text layout
        quoteAnim = (ImageView) findViewById(R.id.quote_anim);
        quote_editor_body = (TextView) findViewById(R.id.tv_Quote_Body);
        quote_editor_author = (TextView) findViewById(R.id.tv_Quote_Author);
        imageView_background = (ImageView) findViewById(R.id.imageView_background);
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

        font_size_changer.setOnClickListener(this);
        font_family_changer.setOnClickListener(this);
        save_picture.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()) {

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
            case R.id.font_save_module: {
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

            case R.id.Editor_text_module_Size: {
//                if (level_1_1_1_editor_font_sizeChanger_seekbar_layout.getVisibility() == View.VISIBLE) {
//                    level_1_1_1_editor_font_sizeChanger_seekbar_layout.setVisibility(View.GONE);
//                } else {
//                    level_1_1_1_editor_font_sizeChanger_seekbar_layout.setVisibility(View.VISIBLE);
//                    font_size_changing_seekbar.setOnSeekBarChangeListener(this);
//
//                }
            }
            break;

            case R.id.Editor_text_module_save: {

            }
            break;

            case R.id.Editor_text_share: {

                ToastSnackDialogUtils.show_ShortToast(this, getString(R.string.coming_soon));


            }
            break;
            case R.id.font_change_close_button: {
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
                        bottomlayout_recyclerview.setAdapter(null);
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
        bottomlayout_recyclerview.setAdapter(contentAdapter);
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
        bottomlayout_recyclerview.setAdapter(contentAdapter);
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
        bottomlayout_recyclerview.setAdapter(contentAdapter);
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
}



