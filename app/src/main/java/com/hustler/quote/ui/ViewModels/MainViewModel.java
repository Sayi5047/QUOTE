package com.hustler.quote.ui.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hustler.quote.ui.ORM.AppDatabase;
import com.hustler.quote.ui.ORM.Tables.QuotesTable;

import java.util.List;

public class MainViewModel extends ViewModel {

    public LiveData<List<QuotesTable>> mainFragmentQuotesList = new MutableLiveData<>();
    AppDatabase appDatabase;


    public void getQuotes(Context context) {
        appDatabase = AppDatabase.getmAppDatabaseInstance(context);
        mainFragmentQuotesList = appDatabase.quotesDao().loadAllbyCategory("Attitude");

    }


}
