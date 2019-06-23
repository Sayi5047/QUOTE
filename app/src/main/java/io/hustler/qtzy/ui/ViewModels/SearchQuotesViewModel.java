package io.hustler.qtzy.ui.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import io.hustler.qtzy.ui.ORM.AppDatabase;
import io.hustler.qtzy.ui.ORM.Tables.QuotesTable;

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
