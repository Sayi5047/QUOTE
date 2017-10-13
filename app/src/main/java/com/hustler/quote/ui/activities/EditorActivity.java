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
    private LinearLayout mainImageLayout;
    private LinearLayout bottomLayout;
    private ImageView editorText;
    private ImageView backgroundImage;
    private LinearLayout secodDetail;
    private ImageView textoEditorSize;
    private ImageView textoEditorFont;
    private ImageView close_text_size;
    private ImageView textoEditorColor;
    private LinearLayout thirdDetail;
    private TextView tvQuoteBody;
    private ImageView quoteAnim;
    private TextView tvQuoteAuthor;
    private QuotesFromFC quote;
    private LinearLayout quoteLayout;
    private SeekBar seekBar;

    private BottomSheetBehavior bottomSheetBehavior;
    private NestedScrollView bottomsheet;
    CoordinatorLayout root;
    RecyclerView colorbgrecyclerview;
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
        itemsTo = getResources().getStringArray(R.array.allColors);
        for (int i = 0; i < itemsTo.length; i++) {
            items.add(i, itemsTo[i]);
        }
        Log.d("Colors Added -->", "done");

    }

    private void convertFonts() {
        itemsTo = getResources().getStringArray(R.array.allfonts);
        for (int i = 0; i < itemsTo.length; i++) {
            items.add(i, itemsTo[i]);
        }
        Log.d("Fonts Added -->", "done");
    }


    private void findViews() {

        mainImageLayout = (LinearLayout) findViewById(R.id.main_image_layout);
        bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
        secodDetail = (LinearLayout) findViewById(R.id.secod_detail);
        thirdDetail = (LinearLayout) findViewById(R.id.third_detail);
        quoteLayout = (LinearLayout) findViewById(R.id.quote_layout);
        root = (CoordinatorLayout) findViewById(R.id.root_cl);


        editorText = (ImageView) findViewById(R.id.editor_text);
        backgroundImage = (ImageView) findViewById(R.id.background_image);
        textoEditorSize = (ImageView) findViewById(R.id.texto_editor_size);
        textoEditorFont = (ImageView) findViewById(R.id.texto_editor_font);
        textoEditorColor = (ImageView) findViewById(R.id.texto_editor_color);
        quoteAnim = (ImageView) findViewById(R.id.quote_anim);
        close_text_size = (ImageView) findViewById(R.id.close_text_size);


        tvQuoteBody = (TextView) findViewById(R.id.tv_Quote_Body);
        tvQuoteAuthor = (TextView) findViewById(R.id.tv_Quote_Author);

        seekBar = (SeekBar) findViewById(R.id.seekbar_tv);

        colorbgrecyclerview = (RecyclerView) findViewById(R.id.content_rv);
        colorbgrecyclerview.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));


//        bottomsheet = (NestedScrollView) findViewById(R.id.bottomsheet);


        textoEditorSize.setOnClickListener(this);
        backgroundImage.setOnClickListener(this);
        textoEditorFont.setOnClickListener(this);
        close_text_size.setOnClickListener(this);

        thirdDetail.setVisibility(View.VISIBLE);
        thirdDetailvisible = false;

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
            root.setBackgroundColor(getResources().getColor(R.color.bg));
//            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(quote.getColor()));
            if (length > 230) {
                tvQuoteBody.setTextSize(20.0f);
            } else if (length < 230 && length > 150) {
                tvQuoteBody.setTextSize(25.0f);

            } else if (length > 100 && length < 150) {
                tvQuoteBody.setTextSize(30.0f);

            } else if (length > 50 && length < 100) {
                tvQuoteBody.setTextSize(35.0f);

            } else if (length > 2 && length < 50) {
                tvQuoteBody.setTextSize(40.0f);

            } else {
                tvQuoteBody.setTextSize(45.0f);

            }
            tvQuoteBody.setText(quote.getBody());
            tvQuoteAuthor.setText(quote.getAuthor());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editor_text: {
                if (secodDetail.getVisibility() == View.VISIBLE) {
                    secodDetail.setVisibility(View.GONE);

                } else {
                    secodDetail.setVisibility(View.VISIBLE);

                }
            }
            case R.id.background_image: {

//                if (thirdDetailvisible) {
//                    thirdDetail.setVisibility(View.VISIBLE);
//
//                } else {
//                    thirdDetail.setVisibility(View.VISIBLE);
//                    thirdDetailvisible=true;
//
//                }
            }
            case R.id.texto_editor_font: {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    bottomSheetBehavior.setPeekHeight(80);
                    setFontTypeRecyclerview();

                } else {

                    if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                        bottomSheetBehavior.setPeekHeight(0);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }
            }
            case R.id.texto_editor_color: {

                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    bottomSheetBehavior.setPeekHeight(80);
                    setFontColorRecyclerView();

                } else {

                    if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                        bottomSheetBehavior.setPeekHeight(0);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }


            }
            case R.id.close_text_size: {
                thirdDetailvisible = false;
                thirdDetail.setVisibility(View.GONE);
            }
        }
    }

    private void setFontColorRecyclerView() {
        convertFonts();
        contentAdapter = new ContentAdapter(this, items, new ContentAdapter.onItemClickListener() {
            @Override
            public void onItemColorClick(int color) {
                tvQuoteBody.setTextColor(color);
                tvQuoteAuthor.setTextColor(color);
            }

            @Override
            public void onItemFontClick(String font) {

            }
        }, false);
        colorbgrecyclerview.setAdapter(contentAdapter);
    }

    private void setFontTypeRecyclerview() {
        convertFonts();
        contentAdapter = new ContentAdapter(this, items, new ContentAdapter.onItemClickListener() {
            @Override
            public void onItemColorClick(int color) {

            }

            @Override
            public void onItemFontClick(String font) {

                tvQuoteBody.setTypeface(App.getZingCursive(EditorActivity.this, font));
                tvQuoteAuthor.setTypeface(App.getZingCursive(EditorActivity.this, font));
            }

        }, true);
        colorbgrecyclerview.setAdapter(contentAdapter);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        Toast.makeText(getApplicationContext(), "seekbar progress: " + progress, Toast.LENGTH_SHORT).show();
        float size = (float) progress;
        tvQuoteBody.setTextSize(size);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Toast.makeText(getApplicationContext(), "seekbar touch started!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Toast.makeText(getApplicationContext(), "seekbar touch stopped!", Toast.LENGTH_SHORT).show();
    }

}



