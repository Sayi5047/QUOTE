package com.hustler.quote.ui.activities;

import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hustler.quote.R;
import com.hustler.quote.ui.adapters.ContentAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.pojo.QuotesFromFC;
import com.hustler.quote.ui.superclasses.App;
import com.hustler.quote.ui.superclasses.BaseActivity;

import java.util.ArrayList;

public class EditorActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private LinearLayout leadScreen_layout;
    private LinearLayout level_1_editor_navigator;
    private ImageView font_module;
    private ImageView background_image_module;
    private LinearLayout level_1_1_editor_font_size_manipulator;
    private ImageView font_size_changer, background_color_changer;
    private ImageView font_color_changer, background_gallery_chooser;
    private ImageView close_text_size, background_opacity_changer;
    private ImageView font_family_changer;
    private LinearLayout level_1_1_1_editor_font_sizeChanger_seekbar_layout;
    private TextView quote_editor_body;
    private ImageView quoteAnim;
    private TextView quote_editor_author;
    private QuotesFromFC quote;
    private LinearLayout quoteLayout, level_1_2_editor_background_manipulator;
    private SeekBar font_size_changing_seekbar;

    private BottomSheetBehavior bottomSheetBehavior;
    private NestedScrollView bottomsheet;
    CoordinatorLayout root_layout;
    RecyclerView bottomlayout_recyclerview;
    ContentAdapter contentAdapter;

    private boolean thirdDetailvisible;

    ArrayList<String> items = new ArrayList<>();
    String[] itemsTo;

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
        root_layout = (CoordinatorLayout) findViewById(R.id.root_cl);

        level_1_editor_navigator = (LinearLayout) findViewById(R.id.top_editor_navigaotor_level1);
        leadScreen_layout = (LinearLayout) findViewById(R.id.Editor_lead_screen_linearLayout);
        level_1_1_editor_font_size_manipulator = (LinearLayout) findViewById(R.id.editor_font_module);
        level_1_2_editor_background_manipulator = (LinearLayout) findViewById(R.id.editor_background_module);
        level_1_1_1_editor_font_sizeChanger_seekbar_layout = (LinearLayout) findViewById(R.id.fontsize_change_module);

        quoteLayout = (LinearLayout) findViewById(R.id.quote_layout);

//      level 1 top bar buttons
        font_module = (ImageView) findViewById(R.id.font_style_changer_module);
        background_image_module = (ImageView) findViewById(R.id.font_background_chnager_module);

//        level 1.1 text font manipulator options
        font_size_changer = (ImageView) findViewById(R.id.Editor_text_module_Size);
        font_color_changer = (ImageView) findViewById(R.id.Editor_text_module_font_color);
        font_family_changer = (ImageView) findViewById(R.id.Editor_text_module_font_style_changer);

//        level 1.2 text background manipulator options
        background_color_changer = (ImageView) findViewById(R.id.Editor_background_module_colored_backgrounds);
        background_gallery_chooser = (ImageView) findViewById(R.id.Editor_background_module_gallery_backgrounds);
        background_opacity_changer = (ImageView) findViewById(R.id.Editor_background_module_picture_opacity_changer);


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




/*setting on click listners */
        font_module.setOnClickListener(this);
        background_image_module.setOnClickListener(this);

        font_size_changer.setOnClickListener(this);
        font_family_changer.setOnClickListener(this);
        font_color_changer.setOnClickListener(this);
        close_text_size.setOnClickListener(this);

//        level_1_1_1_editor_font_sizeChanger_seekbar_layout.setVisibility(View.VISIBLE);
//        thirdDetailvisible = false;

        managingBottomsheet();
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
            quoteLayout.setBackgroundColor(quote.getColor());
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
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

//            MAIN NAVIGATOR BUTTONG HANDLING
            case R.id.font_style_changer_module: {
                if (level_1_1_editor_font_size_manipulator.getVisibility() == View.VISIBLE) {
                    level_1_1_editor_font_size_manipulator.setVisibility(View.GONE);


                } else if (level_1_2_editor_background_manipulator.getVisibility() == View.VISIBLE) {
                    level_1_2_editor_background_manipulator.setVisibility(View.GONE);
                    level_1_1_editor_font_size_manipulator.setVisibility(View.VISIBLE);


                } else {
                    level_1_1_editor_font_size_manipulator.setVisibility(View.VISIBLE);

                }
            }
            break;

            case R.id.font_background_chnager_module: {
                if (level_1_1_editor_font_size_manipulator.getVisibility() == View.VISIBLE) {
                    level_1_1_editor_font_size_manipulator.setVisibility(View.GONE);
                    level_1_2_editor_background_manipulator.setVisibility(View.VISIBLE);

                } else if (level_1_2_editor_background_manipulator.getVisibility() == View.VISIBLE) {
                    level_1_2_editor_background_manipulator.setVisibility(View.GONE);
                } else {
                    level_1_1_editor_font_size_manipulator.setVisibility(View.VISIBLE);

                }

            }
            break;


//            TEXT MODULE CASES

            case R.id.Editor_text_module_Size: {
                if (level_1_1_1_editor_font_sizeChanger_seekbar_layout.getVisibility() == View.VISIBLE) {
                    level_1_1_1_editor_font_sizeChanger_seekbar_layout.setVisibility(View.GONE);
                } else {
                    level_1_1_1_editor_font_sizeChanger_seekbar_layout.setVisibility(View.VISIBLE);
                    font_size_changing_seekbar.setOnSeekBarChangeListener(this);

                }
            }
            break;


            case R.id.Editor_text_module_font_color: {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    bottomSheetBehavior.setPeekHeight(80);
                    setFontColorRecyclerView();

                } else {

                    if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                        bottomSheetBehavior.setPeekHeight(0);
                        bottomlayout_recyclerview.setAdapter(null);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }
            }
            break;

            case R.id.Editor_text_module_font_style_changer: {

                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    bottomSheetBehavior.setPeekHeight(80);
                    setFontTypeRecyclerview();

                } else {

                    if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                        bottomSheetBehavior.setPeekHeight(0);
                        bottomlayout_recyclerview.setAdapter(null);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }


            }
            break;
            case R.id.font_change_close_button: {
//                thirdDetailvisible = false;
                level_1_1_1_editor_font_sizeChanger_seekbar_layout.setVisibility(View.GONE);
            }
            break;


//            BACKGROUND MODULE CASES
        }
    }


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


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
//        Toast.makeText(getApplicationContext(), "seekbar progress: " + progress, Toast.LENGTH_SHORT).show();
        float size = (float) progress;
        quote_editor_body.setTextSize(size);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
//        Toast.makeText(getApplicationContext(), "seekbar touch started!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
//        Toast.makeText(getApplicationContext(), "seekbar touch stopped!", Toast.LENGTH_SHORT).show();
    }

}



