package com.hustler.quote.ui.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
public class DailyNotificationService extends IntentService {
    android.support.v4.app.NotificationCompat.Builder mNotification_Builder;
    NotificationManager mNotificationManager;
    List<Quote> quotes;
    int val;

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


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        doTask();

    }

    private void doTask() {
        Log.i("JOB SERVICE", "JOB STARTED");
        new GetQuotesTask().execute();

    }

    public class GetQuotesTask extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("NOTIF TASK", "TASK STARTED");
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param strings The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */


        @Override
        protected Void doInBackground(String... strings) {
            quotes = new QuotesDbHelper(getApplicationContext()).getAllFav_Quotes();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (quotes.size() <= 0) {
                return;
            } else {
                val = new Random().nextInt(quotes.size());
                mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotification_Builder = new NotificationCompat.Builder(getApplicationContext());
                mNotification_Builder
                        .setContentTitle("QUOTZY")
                        .setContentText(quotes.get(val).getQuote_body() + " -- " + quotes.get(val).getQuote_author())
                        .setSmallIcon(R.drawable.ic_launcher);
                mNotificationManager.notify(1, mNotification_Builder.build());
            }

        }
    }
}
