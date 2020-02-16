package com.hustler.quote.ui.ViewModels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import java.util.List;

import com.hustler.quote.ui.ORM.AppDatabase;
import com.hustler.quote.ui.ORM.Tables.QuotesTable;

public class CategoryQuotesViewModel extends AndroidViewModel {
    AppDatabase appDatabase;
    private LiveData<List<QuotesTable>> categoryQuotesliveData;

    public CategoryQuotesViewModel(@NonNull Application application, String category) {
        super(application);
        appDatabase = AppDatabase.getmAppDatabaseInstance(application);
        appDatabase.quotesDao().loadAllbyCategory(category);
    }

    public LiveData<List<QuotesTable>> getCategoryQuotesliveData() {
        return categoryQuotesliveData;
    }

    ;
}
