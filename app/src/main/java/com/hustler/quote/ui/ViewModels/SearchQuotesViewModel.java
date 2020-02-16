package com.hustler.quote.ui.ViewModels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import java.util.List;

import com.hustler.quote.ui.ORM.AppDatabase;
import com.hustler.quote.ui.ORM.Tables.QuotesTable;

public class SearchQuotesViewModel extends AndroidViewModel {
    private LiveData<List<QuotesTable>> quotesSearchResultLiveData;
    private LiveData<List<QuotesTable>> quotesSearchResultLiveData2;
    private AppDatabase appDatabase;

    public SearchQuotesViewModel(@NonNull Application application, String query) {
        super(application);
        appDatabase = AppDatabase.getmAppDatabaseInstance(application);
        quotesSearchResultLiveData = appDatabase.quotesDao().findQuotesByQuery(query);
        quotesSearchResultLiveData2 = appDatabase.quotesDao().loadAllbyCategory(query);
    }

    public LiveData<List<QuotesTable>> getQuotesbyQuerySearchResultLiveData() {

        return quotesSearchResultLiveData;
    }

    public LiveData<List<QuotesTable>> getQuotesbyCategorySearchResultLiveData() {

        return quotesSearchResultLiveData2;
    }

}
