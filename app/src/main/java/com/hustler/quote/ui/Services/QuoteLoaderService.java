package com.hustler.quote.ui.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hustler.quote.R;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.database.QuotesDbHelper;
import com.hustler.quote.ui.pojo.Quote;

import java.util.ArrayList;

/**
 * Created by Sayi on 26-02-2018.
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
public class QuoteLoaderService extends Service {
    Quote[] quotesFromdb;
    String[] bodies;
    String[] authors;
    String[] categories;
    SharedPreferences sharedPreferences;

    /**
     * Return the communication channel to the service.  May return null if
     * clients can not bind to the service.  The returned
     * {@link IBinder} is usually for a complex interface
     * that has been <a href="{@docRoot}guide/components/aidl.html">described using
     * aidl</a>.
     * <p>
     * <p><em>Note that unlike other application components, calls on to the
     * IBinder interface returned here may not happen on the main thread
     * of the process</em>.  More information about the main thread can be found in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html">Processes and
     * Threads</a>.</p>
     *
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return an IBinder through which clients can call on to the
     * service.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sharedPreferences.getBoolean(Constants.IS_QUOTES_LOADED_KEY, false) == false) {
            new QuoteLoadTask().execute();
        } else {

        }
        return super.onStartCommand(intent, flags, startId);
    }


    public class QuoteLoadTask extends AsyncTask<String, String, Void> {


        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected void onPreExecute() {

        }


        @Override
        protected Void doInBackground(String... strings) {
            load_from_Arrays();
            load_to_database();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            sharedPreferences.edit().putBoolean(Constants.IS_QUOTES_LOADED_KEY, true).commit();
            stopSelf();
        }
    }

    private void load_to_database() {

        ArrayList<Quote> quotes = new ArrayList<>();
        for (int i = 0; i < quotesFromdb.length; i++) {
            quotes.add(i, quotesFromdb[i]);
        }
        if (quotes != null) {
            for (int i = 0; i < quotes.size(); i++) {
                new QuotesDbHelper(getApplicationContext()).addQuote(quotes.get(i));
            }
            Log.d("Quotes list length", quotes.size() + "");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constants.IS_DB_LOADED_PREFERENCE, true);
            editor.commit();
        }

    }

    private void load_from_Arrays() {

        bodies = getResources().getStringArray(R.array.quote_bodies);
        authors = getResources().getStringArray(R.array.quote_authors);
        categories = getResources().getStringArray(R.array.quote_categories);
//
//        Log.d("VALUES", String.valueOf(bodies.length));
//        Log.d("VALUES", String.valueOf(authors.length));
//        Log.d("VALUES", String.valueOf(categories.length));
//
//        Log.d("BODIES LENGTH", String.valueOf(bodies.length));
//        Log.d("AUTHORS LENGTH", String.valueOf(authors.length));
//        Log.d("Categories LENGTH", String.valueOf(categories.length));
//
        final String[] languages = getResources().getStringArray(R.array.quote_languages);
        quotesFromdb = new Quote[bodies.length];
        for (int i = 0; i < bodies.length; i++) {
            Log.d("ADDED ELEMNT", String.valueOf(i));
            Quote quote = new Quote(bodies[i], authors[i], categories[i], languages[0]);
            quotesFromdb[i] = quote;
        }

    }

}
