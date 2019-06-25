package io.hustler.qtzy.ui.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Objects;

import io.hustler.qtzy.ui.Executors.AppExecutor;
import io.hustler.qtzy.ui.apiRequestLauncher.Constants;
import io.hustler.qtzy.ui.apiRequestLauncher.Restutility;
import io.hustler.qtzy.ui.pojo.listeners.GetCollectionsResponseListener;
import io.hustler.qtzy.ui.pojo.unspalsh.ResGetCollectionsDto;

public class CollectionViewModel extends AndroidViewModel {
    private AppExecutor appExecutor;
    private MutableLiveData<ArrayList<ResGetCollectionsDto>> resGetCollections = new MutableLiveData<>();

    public CollectionViewModel(@NonNull final Application application) {
        super(application);
        appExecutor = AppExecutor.getInstance();
        appExecutor.getNetworkExecutor().execute(new Runnable() {
            @Override
            public void run() {

                String request = Constants.UNSPLASH_GET_FEATURED_COLLECTIONS.replace("30", "100");
                new Restutility(application).getUnsplashImageCollections(Objects.requireNonNull(application), new GetCollectionsResponseListener() {
                    @Override
                    public void onSuccess(final ArrayList<ResGetCollectionsDto> resGetCollectionsDtos) {
                        resGetCollections.setValue(resGetCollectionsDtos);
                        Log.d("COLLECTIONVIEWMODEL", "API CALLED");
                    }

                    @Override
                    public void onError(final String errorMessage) {
                        resGetCollections.setValue(null);


                    }
                }, request);
            }
        });


    }

    public LiveData<ArrayList<ResGetCollectionsDto>> getResGetCollections() {
        return resGetCollections;
    }
}
