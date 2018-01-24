package com.hustler.quote.ui.Widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.activities.BaseActivity;
import com.hustler.quote.ui.activities.EditorActivity;
import com.hustler.quote.ui.adapters.TabsFragmentPagerAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;

import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

/**
 * Created by anvaya5 on 23/01/2018.
 */

public class QuoteWidgetConfigurationActivity extends BaseActivity implements View.OnClickListener {

    private Button colorChanger;
    private Button SizeChanger;
    private Button addWidget;
    RemoteViews remoteViews;
    int mAppWidgetId;

    private RelativeLayout root;
    private LinearLayout mainWidgetLayout;
    private LinearLayout rootWidget;
    private TextView quoteBody;
    private TextView quoteAuthor;
    private LinearLayout btLayout;
    private ImageView widgetEditQuote;
    private ImageView widgetEditRefresh;
    private LinearLayout navLayout;
    private Button btClose;
    private Button btDone;
    private ViewPager widgetPreferencePager;
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
        root = (RelativeLayout) findViewById(R.id.root);
        mainWidgetLayout = (LinearLayout) findViewById(R.id.main_widget_layout);
        rootWidget = (LinearLayout) findViewById(R.id.root_widget);
        quoteBody = (TextView) findViewById(R.id.quote_body);
        quoteAuthor = (TextView) findViewById(R.id.quote_author);
        btLayout = (LinearLayout) findViewById(R.id.bt_layout);
        widgetEditQuote = (ImageView) findViewById(R.id.widget_edit_quote);
        widgetEditRefresh = (ImageView) findViewById(R.id.widget_edit_refresh);
        navLayout = (LinearLayout) findViewById(R.id.nav_layout);
        btClose = (Button) findViewById(R.id.bt_close);
        btDone = (Button) findViewById(R.id.bt_done);
        widgetPreferencePager = (ViewPager) findViewById(R.id.widget_preference_pager);
        widgetPreferencePager.setAdapter(new TabsFragmentPagerAdapter(this, getSupportFragmentManager()));

        btClose.setOnClickListener(this);
        btDone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btClose) {
            // Handle clicks for btClose
        } else if (v == btDone) {
            // Handle clicks for btDone
        } else if (v == btDone) {
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




