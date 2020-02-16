package com.hustler.quote.ui.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.hustler.quote.ui.Executors.AppExecutor;
import com.hustler.quote.ui.apiRequestLauncher.Constants;

import java.util.List;
import java.util.Random;

import com.hustler.quote.R;

import com.hustler.quote.ui.ORM.AppDatabase;
import com.hustler.quote.ui.ORM.Tables.QuotesTable;
import com.hustler.quote.ui.activities.QuoteDetailsActivity;
import com.hustler.quote.ui.pojo.Quote;

/**
 * Created by Sayi on 22-02-2018.
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
public class DailyNotificationJobService extends JobService {
    androidx.core.app.NotificationCompat.Builder mNotification_Builder;
    @Nullable
    NotificationManager mNotificationManager;
    AppDatabase appDatabase;
    AppExecutor appExecutor;
    List<Quote> quotes;
    int val;
    private static final int DAILY_NOTIFICATION_ID = 1;


    @Override
    public boolean onStartJob(JobParameters params) {
        appDatabase = AppDatabase.getmAppDatabaseInstance(this);
        final Intent intentTobeSent = new Intent(getApplicationContext(), QuoteDetailsActivity.class);
        intentTobeSent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        appExecutor = AppExecutor.getInstance();
        appExecutor.getDiskExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final List<QuotesTable> quotesTables = appDatabase.quotesDao().loadAllbyCategoryNONLIVEDATA("Attitude");

                appExecutor.getMainThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (quotesTables.size() > 0) {
                            val = new Random().nextInt(quotes.size());


                            intentTobeSent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quotes.get(val));
                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intentTobeSent, 0);
                            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotification_Builder = new NotificationCompat.Builder(getApplicationContext());
                            mNotification_Builder
                                    .setContentTitle("QUOTZY")
                                    .setContentText(quotesTables.get(val).getQuotes() + " -- " + quotesTables.get(val).getAuthor())
                                    .setContentIntent(pendingIntent).setSmallIcon(R.drawable.ic_launcher)
                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                                    .setBadgeIconType(R.drawable.ic_launcher)
                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(quotesTables.get(val).getQuotes() + " -- " + quotesTables.get(val).getAuthor()))
                                    .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setAutoCancel(false).build()
                            ;
                            assert mNotificationManager != null;
                            mNotificationManager.notify(DAILY_NOTIFICATION_ID, mNotification_Builder.build());
                        }
                    }
                });
            }
        });
        jobFinished(params, false);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
