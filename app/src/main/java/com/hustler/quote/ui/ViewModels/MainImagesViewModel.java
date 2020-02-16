package com.hustler.quote.ui.ViewModels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.hustler.quote.ui.ORM.AppDatabase;

import java.util.List;

import com.hustler.quote.ui.ORM.Tables.ImagesTable;

public class MainImagesViewModel extends AndroidViewModel {
    private AppDatabase appDatabase;
    private LiveData<List<ImagesTable>> imagesLiveData;

    public MainImagesViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getmAppDatabaseInstance(application);
        imagesLiveData = appDatabase.imagesDao().getAllImages();

    }

    public LiveData<List<ImagesTable>> getImagesLiveData() {
        return imagesLiveData;
    }


}
