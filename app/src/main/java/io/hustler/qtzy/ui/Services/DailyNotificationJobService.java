package io.hustler.qtzy.ui.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import java.util.List;
import java.util.Random;

import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.Executors.AppExecutor;
import io.hustler.qtzy.ui.ORM.AppDatabase;
import io.hustler.qtzy.ui.ORM.Tables.QuotesTable;
import io.hustler.qtzy.ui.activities.QuoteDetailsActivity;
import io.hustler.qtzy.ui.apiRequestLauncher.Constants;
import io.hustler.qtzy.ui.pojo.Quote;

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
    android.support.v4.app.NotificationCompat.Builder mNotification_Builder;
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
