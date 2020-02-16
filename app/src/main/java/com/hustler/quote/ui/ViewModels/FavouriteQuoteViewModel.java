package com.hustler.quote.ui.ViewModels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import java.util.List;

import com.hustler.quote.ui.ORM.AppDatabase;
import com.hustler.quote.ui.ORM.Tables.QuotesTable;

public class FavouriteQuoteViewModel extends AndroidViewModel {
    private AppDatabase appDatabase;
    private LiveData<List<QuotesTable>> favQuotesLiveData;

    public FavouriteQuoteViewModel(@NonNull Application application) {
        super(application);
        favQuotesLiveData = appDatabase.quotesDao().getlikedQuotes(true);
    }

    public LiveData<List<QuotesTable>> getFavQuotesLiveData() {
        return favQuotesLiveData;
    }
}
