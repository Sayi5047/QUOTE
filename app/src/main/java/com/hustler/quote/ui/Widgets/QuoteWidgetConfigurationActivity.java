package com.hustler.quote.ui.Widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.activities.BaseActivity;
import com.hustler.quote.ui.activities.EditorActivity;
import com.hustler.quote.ui.apiRequestLauncher.Constants;

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
    RemoteViews remoteViews;
    int mAppWidgetId;
    AppWidgetManager appWidgetManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quote_widget_configuration_layout);
        findViews();
        mAppWidgetId = INVALID_APPWIDGET_ID;
        appWidgetManager = AppWidgetManager.getInstance(this);
        remoteViews = new RemoteViews(this.getPackageName(), R.layout.quote_widget_layout);
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
            remoteViews.setTextColor(R.id.quote_body, ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));

        } else if (v == SizeChanger) {
            // Handle clicks for SizeChanger
            quoteBody.setTextSize(65);
            remoteViews.setTextViewTextSize(R.id.quote_body, TypedValue.COMPLEX_UNIT_DIP, 65);
//            remoteViews.setInt(R.id.root_widget,"setBackgroundResource",R.drawable.alphawhite);
//            remoteViews.setInt(R.id.root_widget,"setBackgroundResource",R.drawable.alphawhite);
        } else if (v == addWidget) {
//            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.root_widget);
//            AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
//            animationDrawable.setEnterFadeDuration(2000);
//            animationDrawable.setExitFadeDuration(4000);
//            animationDrawable.start();
            remoteViews.setInt(R.id.root_widget, "setBackGroundResource", R.drawable.animation_list);

            colorChanger.setVisibility(View.GONE);
            SizeChanger.setVisibility(View.GONE);
            addWidget.setVisibility(View.GONE);
            Intent intent_edit = new Intent(this, EditorActivity.class);
            intent_edit.putExtra(Constants.INTENT_IS_FROM_EDIT_KEY, false);
            PendingIntent pendingIntent_edit = PendingIntent.getActivity(this, 0, intent_edit, 0);
            remoteViews.setOnClickPendingIntent(R.id.root_widget, pendingIntent_edit);
            appWidgetManager.updateAppWidget(mAppWidgetId, remoteViews);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    }

}
