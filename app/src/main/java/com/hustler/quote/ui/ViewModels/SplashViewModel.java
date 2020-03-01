package com.hustler.quote.ui.ViewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hustler.quote.BuildConfig;
import com.hustler.quote.ui.Executors.AppExecutor;
import com.hustler.quote.ui.ORM.AppDatabase;
import com.hustler.quote.ui.ORM.Tables.QuotesTable;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;

public class SplashViewModel extends ViewModel {


    public MutableLiveData<Boolean> isQuotesLoaded = new MutableLiveData<>();
    public MutableLiveData<String> quotesLoadingProgress = new MutableLiveData<>();
    public MutableLiveData<Boolean> launchNextActObserver = new MutableLiveData<>();
    public MutableLiveData<Boolean> saveToSharedPrefs = new MutableLiveData<>();
    public MutableLiveData<Boolean> loadingLayoutObserver = new MutableLiveData<>();


    public void getQuotesCount(AppDatabase appDatabase, AppExecutor appExecutor) {
        if (BuildConfig.DEBUG) {
            Log.d("SPLASH_VIEW_MODEL", "get Quotes Content Called");
        }
        appExecutor.getDiskExecutor().execute(() -> {
            if (appDatabase.quotesDao().getQuotesCount() > 100) {
                isQuotesLoaded.postValue(true);
            } else {
                isQuotesLoaded.postValue(false);
            }
        });
    }

    public void loadQuotesToDataBase(AppDatabase appDatabase, AppExecutor appExecutor, ArrayList<QuotesTable> quotesTableArrayList) {
        if (BuildConfig.DEBUG) {
            Log.d("SPLASH_VIEW_MODEL", "loadQuotesToDataBase Called");
        }
        loadingLayoutObserver.postValue(true);
        appExecutor.getDiskExecutor().execute(() -> {
            int filledSize = 0;
            String percentage;
            double currentPerc;
            int loadedSize = quotesTableArrayList.size();
            for (QuotesTable quotesTable : quotesTableArrayList) {
                appDatabase.quotesDao().insertUser(quotesTable);
                filledSize++;
                currentPerc = (filledSize * 100 / (double) loadedSize);
                percentage = new DecimalFormat("0.00").format(currentPerc);
                quotesLoadingProgress.postValue(MessageFormat.format("Completed {0}  %", percentage));
                if (BuildConfig.DEBUG && (currentPerc) > 99) {
                    Log.d("SPLASH_VIEW_MODEL", "run: Completed Percentage " + percentage);
                }
            }
            loadingLayoutObserver.postValue(false);
            saveToSharedPrefs.postValue(true);
            launchNextActObserver.postValue(true);
        });

    }

}
