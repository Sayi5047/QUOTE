package com.hustler.quote.ui.ViewModels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import java.util.List;

import com.hustler.quote.ui.ORM.AppDatabase;
import com.hustler.quote.ui.ORM.Tables.QuotesTable;

public class MainViewModel extends AndroidViewModel {

    public LiveData<List<QuotesTable>> mainFragmentQuotesList;
    public LiveData<List<QuotesTable>> quotesListByCategory;
    AppDatabase appDatabase;

    public MainViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getmAppDatabaseInstance(application);
        mainFragmentQuotesList = appDatabase.quotesDao().loadAllbyCategory("Attitude");
    }

    public LiveData<List<QuotesTable>> getMainFragmentQuotes() {
        return mainFragmentQuotesList;
    }
}
