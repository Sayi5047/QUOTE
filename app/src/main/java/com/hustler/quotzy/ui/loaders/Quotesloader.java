package com.hustler.quotzy.ui.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.hustler.quotzy.ui.database.QuotesDbHelper;
import com.hustler.quotzy.ui.pojo.Quote;

import java.util.List;

/**
 * Created by Sayi on 08-02-2018.
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
@Deprecated
public class Quotesloader extends AsyncTaskLoader<List<Quote>> {
    Context context;
    List<Quote> quotes;

    public Quotesloader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public List<Quote> loadInBackground() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                quotes = new QuotesDbHelper(context).getQuotesByCategory("Attitude");
            }
        }).run();
        return quotes;
    }

    @Override
    public void deliverResult(List<Quote> data) {

        if (isReset()) {
            if (quotes != null) {
                onReleaseResources(data);
            }
        }
        List<Quote> oldQuotes = quotes;
        quotes = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
        if (oldQuotes != null) {
            onReleaseResources(oldQuotes);
        }

    }

    @Override
    protected void onStartLoading() {
        if (quotes != null) {
            deliverResult(quotes);
        }
        if (quotes == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {

        cancelLoad();
    }

    @Override
    public void onCanceled(List<Quote> data) {
        super.onCanceled(data);
        onReleaseResources(quotes);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (quotes != null) {
            onReleaseResources(quotes);
            quotes = null;
        }
    }

    protected void onReleaseResources(List<Quote> quotes) {
        // For a simple List<> there is nothing to do.  For something
        // like a Cursor, we would close it here.
        if (quotes != null) {
            quotes = null;
        }
    }
}
