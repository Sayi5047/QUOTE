package io.hustler.qtzy.ui.Services;

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
import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.Widgets.EditWidgetProvider2;
import io.hustler.qtzy.ui.apiRequestLauncher.Constants;
import io.hustler.qtzy.ui.database.QuotesDbHelper;
import io.hustler.qtzy.ui.pojo.Quote;

import java.util.List;
import java.util.Random;

/**
 * Created by Sayi on 25-01-2018.
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
public class RandomQuoteUpdateService extends Service {
    List<Quote> quotesList;
    SharedPreferences sharedPreferences;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
//                if (sharedPreferences.getString(getApplicationContext().getString(R.string.widget_shared_prefs_quotes_resource_key), null).equalsIgnoreCase("Favourite")) {
//                    quotesList = new QuotesDbHelper(getApplicationContext()).getAllFav_Quotes();
//                } else if (quotesList == null || sharedPreferences.getString(getApplicationContext().getString(R.string.widget_shared_prefs_quotes_resource_key), null).equalsIgnoreCase("Random")) {
//                    quotesList = new QuotesDbHelper(getApplicationContext()).getAllQuotes();
//                    Log.e("DB CALLED", "FIRST TIME");
//                }
            }
        }).run();

//        ArrayList<String> quotes = new ArrayList<>();
//        ArrayList<String> quotesAuthors = new ArrayList<>();
//        for (int i = 0; i < quotesList.size(); i++) {
//            quotes.add(quotesList.get(0).getQuote());
//            quotesAuthors.add(quotesList.get(0).getAuthor());
//        }


        Random random = new Random();
        int randomINt = random.nextInt(quotesList.size())+0;
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.quote_widget_layout);
        remoteViews.setTextViewText(R.id.quote, quotesList.get(randomINt).getQuote());
        remoteViews.setTextViewText(R.id.author, quotesList.get(randomINt).getAuthor());

        Gson gson = new Gson();
        String val = gson.toJson(quotesList.get(randomINt));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.Widget_current_object, val);
        editor.apply();

        ComponentName theWidget = new ComponentName(this, EditWidgetProvider2.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(theWidget, remoteViews);
        return super.onStartCommand(intent, flags, startId);
    }
}
