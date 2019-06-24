package io.hustler.qtzy.ui.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import io.hustler.qtzy.ui.ORM.AppDatabase;
import io.hustler.qtzy.ui.ORM.Tables.ImagesTable;

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
