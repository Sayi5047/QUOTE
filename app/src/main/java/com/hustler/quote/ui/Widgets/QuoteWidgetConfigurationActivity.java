package com.hustler.quote.ui.Widgets;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.activities.BaseActivity;

import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

/**
 * Created by anvaya5 on 23/01/2018.
 */

public class QuoteWidgetConfigurationActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout root;
    private LinearLayout mainWidgetLayout;
    private LinearLayout rootWidget;
    private TextView quoteBody;
    private TextView quoteAuthor;
    private ImageView widgetEditQuote;
    private ImageView widgetEditRefresh;
    private Button colorChanger;
    private Button SizeChanger;
    private Button addWidget;

    int mAppWidgetId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quote_widget_configuration_layout);
        findViews();
        mAppWidgetId = INVALID_APPWIDGET_ID;
        intialiseAppWidget();


    }

    private void intialiseAppWidget() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID);

        }
        if (mAppWidgetId == INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    private void findViews() {
        root = (LinearLayout) findViewById(R.id.root);
        mainWidgetLayout = (LinearLayout) findViewById(R.id.main_widget_layout);
        rootWidget = (LinearLayout) findViewById(R.id.root_widget);
        quoteBody = (TextView) findViewById(R.id.quote_body);
        quoteAuthor = (TextView) findViewById(R.id.quote_author);
        widgetEditQuote = (ImageView) findViewById(R.id.widget_edit_quote);
        widgetEditRefresh = (ImageView) findViewById(R.id.widget_edit_refresh);
        colorChanger = (Button) findViewById(R.id.color_changer);
        SizeChanger = (Button) findViewById(R.id.Size_changer);
        addWidget = (Button) findViewById(R.id.add_widget);

        colorChanger.setOnClickListener(this);
        SizeChanger.setOnClickListener(this);
        addWidget.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == colorChanger) {
            // Handle clicks for colorChanger

            quoteBody.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));

        } else if (v == SizeChanger) {
            // Handle clicks for SizeChanger
            quoteBody.setTextSize(10);
        } else if (v == addWidget) {
            colorChanger.setVisibility(View.GONE);
            SizeChanger.setVisibility(View.GONE);
            addWidget.setVisibility(View.GONE);
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    }

}
