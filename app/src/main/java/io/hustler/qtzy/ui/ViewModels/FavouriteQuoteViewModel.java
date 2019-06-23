package io.hustler.qtzy.ui.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import io.hustler.qtzy.ui.ORM.AppDatabase;
import io.hustler.qtzy.ui.ORM.Tables.QuotesTable;

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
