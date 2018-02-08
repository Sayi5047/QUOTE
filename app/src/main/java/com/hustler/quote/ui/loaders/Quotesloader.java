package com.hustler.quote.ui.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.hustler.quote.ui.database.QuotesDbHelper;
import com.hustler.quote.ui.pojo.Quote;

import java.util.List;

/**
 * Created by Sayi on 08-02-2018.
 */

public class Quotesloader extends AsyncTaskLoader<List<Quote>> {
    Context context;
    List<Quote> quotes;

    public Quotesloader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public List<Quote> loadInBackground() {
        quotes = new QuotesDbHelper(context).getAllQuotes();
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
