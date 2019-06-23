package io.hustler.qtzy.ui.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import io.hustler.qtzy.ui.ORM.AppDatabase;
import io.hustler.qtzy.ui.ORM.Tables.QuotesTable;

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
