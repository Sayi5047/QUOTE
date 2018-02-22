package com.hustler.quote.ui.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.hustler.quote.R;
import com.hustler.quote.ui.database.QuotesDbHelper;
import com.hustler.quote.ui.pojo.Quote;

import java.util.List;
import java.util.Random;

/**
 * Created by Sayi on 22-02-2018.
 */

public class DailyNotificationService extends IntentService {
    android.support.v4.app.NotificationCompat.Builder mNotification_Builder;
    NotificationManager mNotificationManager;
    List<Quote> quotes;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DailyNotificationService(String name) {
        super("DailyNotificationService");
    }

    public DailyNotificationService() {
        super("DailyNotificationService");

    }

    private void doTask() {
        Log.i("JOB SERVICE", "JOB STARTED");

        if (quotes == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    quotes = new QuotesDbHelper(getApplicationContext()).getAllFav_Quotes();
                }
            }).run();
        } else {

        }
        int val = new Random().nextInt(quotes.size());
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotification_Builder = new NotificationCompat.Builder(this);
        mNotification_Builder
                .setContentTitle("QUOTZY")
                .setContentText(quotes.get(val).getQuote_body() + " -- " + quotes.get(val).getQuote_author())
                .setSmallIcon(R.drawable.ic_launcher);
        mNotificationManager.notify(1, mNotification_Builder.build());
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        doTask();

    }
}
