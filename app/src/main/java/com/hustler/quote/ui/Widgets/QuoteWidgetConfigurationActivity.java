package com.hustler.quote.ui.Widgets;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hustler.quote.R;
import com.hustler.quote.ui.Services.RandomQuoteUpdateService;
import com.hustler.quote.ui.activities.BaseActivity;
import com.hustler.quote.ui.activities.EditorActivity;
import com.hustler.quote.ui.adapters.QuoteWidgetConfigAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.pojo.Quote;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

/**
 * Created by anvaya5 on 23/01/2018.
 */
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
public class QuoteWidgetConfigurationActivity extends BaseActivity implements View.OnClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

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
    AlarmManager alarmManager;
    SharedPreferences sharedPreferences;
    PendingIntent service;

    public QuoteWidgetConfigurationActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quote_widget_configuration_layout);
        findViews();
        mAppWidgetId = INVALID_APPWIDGET_ID;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        appWidgetManager = AppWidgetManager.getInstance(this);
        remoteViews = new RemoteViews(this.getPackageName(), R.layout.quote_widget_layout);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
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
        root = findViewById(R.id.root);
        mainWidgetLayout = findViewById(R.id.main_widget_layout);
        rootWidget = findViewById(R.id.root_widget);
        quoteBody = findViewById(R.id.quote_body);
        quoteAuthor = findViewById(R.id.quote_author);
        btLayout = findViewById(R.id.bt_layout);
        widgetEditQuote = findViewById(R.id.widget_edit_quote);
        widgetEditRefresh = findViewById(R.id.widget_edit_refresh);
        navLayout = findViewById(R.id.nav_layout);
        btClose = findViewById(R.id.bt_close);
        btDone = findViewById(R.id.bt_done);
        widgetPreferencePager = findViewById(R.id.widget_preference_pager);
        widgetPreferencePager.setAdapter(new QuoteWidgetConfigAdapter(getFragmentManager(), this));

        btClose.setOnClickListener(this);
        btDone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btClose) {
            // Handle clicks for btClose
            finish();
        } else if (v == btDone) {
//            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.root_widget);
//            AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
//            animationDrawable.setEnterFadeDuration(2000);
//            animationDrawable.setExitFadeDuration(4000);
//            animationDrawable.start();

            startServices();

        }
    }

    private void startServices() {
        Intent intent_edit = new Intent(this, EditorActivity.class);
        intent_edit.putExtra(Constants.INTENT_IS_FROM_EDIT_KEY, 1);
        Quote quote = new Gson().fromJson(sharedPreferences.getString(Constants.Widget_current_object, null), Quote.class);
        intent_edit.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, (quote));

        PendingIntent pendingIntent_edit = PendingIntent.getActivity(this, 0, intent_edit, 0);

        remoteViews.setOnClickPendingIntent(R.id.root_widget, pendingIntent_edit);
        remoteViews.setTextViewText(R.id.quote_body, quote.getQuote_body());
        remoteViews.setTextViewText(R.id.quote_author, quote.getQuote_author());

        appWidgetManager.updateAppWidget(mAppWidgetId, remoteViews);

        Intent intent = new Intent(QuoteWidgetConfigurationActivity.this, RandomQuoteUpdateService.class);

        if (service == null) {
            service = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime(),
                Long.parseLong(sharedPreferences.getString(getString(R.string.widget_shared_prefs_update_key), "6000")),
                service);
        Log.d("FREQUENCY", getString(R.string.widget_shared_prefs_quotes_resource_key));
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.widget_shared_prefs_layout_size_key))) {
            String[] sizes = this.getResources().getStringArray(R.array.layout_styles_vals);

            String val = sharedPreferences.getString(key, null) == null ? sizes[0] : sharedPreferences.getString(key, null);


        } else if (key.equals(getString(R.string.widget_shared_prefs_background_key))) {
//            Log.d("Layout Key", sharedPreferences.getString(key, null));

            Boolean val = sharedPreferences.getBoolean(key, true);
            if (val == true) {
                final ColorPicker colorPicker = new ColorPicker(this);

                colorPicker.setCallback(new ColorPickerCallback() {
                    @Override
                    public void onColorChosen(@ColorInt int color) {
                        mainWidgetLayout.setBackgroundColor(color);
                        remoteViews.setInt(R.id.root_widget, "setBackgroundColor", color);
                        colorPicker.cancel();
                    }

                });
                colorPicker.show();
            } else {

            }
        } else if (key.equals(getString(R.string.widget_shared_prefs_show_author_key))) {
            Log.d("Layout Key", String.valueOf(sharedPreferences.getBoolean(key, true)));
            Boolean val = sharedPreferences.getBoolean(key, true);
            if (val == true) {
                quoteAuthor.setVisibility(View.GONE);
                remoteViews.setViewVisibility(R.id.quote_author, View.GONE);
            } else {
                quoteAuthor.setVisibility(View.VISIBLE);
                remoteViews.setViewVisibility(R.id.quote_author, View.VISIBLE);
            }

        } else if (key.equals(getString(R.string.widget_shared_prefs_show_quotes_mark_key))) {
            Boolean val = sharedPreferences.getBoolean(key, false);
            Log.d("Layout Key", String.valueOf(val));
            if (val == true) {
                quoteBody.setText(" \"SAYI MANOJ SUGAVASI IS A GOOD BOY\"");
            } else {
                quoteBody.setText("SAYI MANOJ SUGAVASI IS A GOOD BOY");
            }

        } else if (key.equals(getString(R.string.widget_shared_prefs_text_key))) {
            Log.d("Layout Key", String.valueOf(sharedPreferences.getBoolean(key, true)));
            Boolean val = sharedPreferences.getBoolean(key, true);
            if (val == true) {
                final ColorPicker colorPicker = new ColorPicker(this);

                colorPicker.setCallback(new ColorPickerCallback() {
                    @Override
                    public void onColorChosen(@ColorInt int color) {
                        quoteBody.setTextColor(color);
                        quoteAuthor.setTextColor(color);
                        remoteViews.setTextColor(R.id.quote_body, color);
                        remoteViews.setTextColor(R.id.quote_author, color);
                        colorPicker.cancel();
                    }

                });
                colorPicker.show();
            }
        }
    }
//    public class Bootreciever extends BroadcastReceiver {
//
//
//        public Bootreciever() {
//        }
//
//        @Override
//
//        public void onReceive(Context context, Intent intent) {
//        Toast_Snack_Dialog_Utils.show_ShortToast(QuoteWidgetConfigurationActivity.this, "DEVICE SUCCESSFULLY BOOTED");
//
//            if(sharedPreferences.getBoolean(context.getString(R.string.widget_added_key),false)==true){
//                Log.d("PREFERNCE LOADED",String.valueOf(sharedPreferences.getBoolean(context.getString(R.string.widget_added_key),false)));
//                startServices();
//            }
//            else {
//
//            }
//        }
//    }
    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}





