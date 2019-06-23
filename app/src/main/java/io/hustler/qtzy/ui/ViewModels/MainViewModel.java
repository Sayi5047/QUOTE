package io.hustler.qtzy.ui.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import io.hustler.qtzy.ui.ORM.AppDatabase;
import io.hustler.qtzy.ui.ORM.Tables.QuotesTable;

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
