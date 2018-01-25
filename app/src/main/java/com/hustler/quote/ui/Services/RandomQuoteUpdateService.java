package com.hustler.quote.ui.Services;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.hustler.quote.R;
import com.hustler.quote.ui.Widgets.EditWidgetProvider2;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.database.QuotesDbHelper;
import com.hustler.quote.ui.pojo.Quote;

import java.util.List;
import java.util.Random;

/**
 * Created by Sayi on 25-01-2018.
 */

public class RandomQuoteUpdateService extends Service {
    List<Quote> quotesList;
    SharedPreferences sharedPreferences;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (quotesList == null) {
            quotesList = new QuotesDbHelper(getApplicationContext()).getAllQuotes();
            Log.e("DB CALLED", "FIRST TIME");
        } else {

        }
//        ArrayList<String> quotes = new ArrayList<>();
//        ArrayList<String> quotesAuthors = new ArrayList<>();
//        for (int i = 0; i < quotesList.size(); i++) {
//            quotes.add(quotesList.get(0).getQuote_body());
//            quotesAuthors.add(quotesList.get(0).getQuote_author());
//        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Random random = new Random();
        int randomINt = random.nextInt(quotesList.size());
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.quote_widget_layout);
        remoteViews.setTextViewText(R.id.quote_body, quotesList.get(randomINt).getQuote_body());
        remoteViews.setTextViewText(R.id.quote_author, quotesList.get(randomINt).getQuote_author());
        Gson gson = new Gson();
        String val = gson.toJson(quotesList.get(randomINt));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.Widget_current_object, val);
        editor.commit();
        ComponentName theWidget = new ComponentName(this, EditWidgetProvider2.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(theWidget, remoteViews);
        return super.onStartCommand(intent, flags, startId);
    }
}
