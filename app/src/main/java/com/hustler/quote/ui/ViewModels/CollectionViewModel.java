package com.hustler.quote.ui.ViewModels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;
import android.util.Log;

import com.hustler.quote.ui.Executors.AppExecutor;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.apiRequestLauncher.Restutility;
import com.hustler.quote.ui.pojo.listeners.GetCollectionsResponseListener;
import com.hustler.quote.ui.pojo.unspalsh.ResGetCollectionsDto;

import java.util.ArrayList;
import java.util.Objects;

public class CollectionViewModel extends AndroidViewModel {
    private static final String TAG = "COLLECTION VIEWMODEL";
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
//        Log.i(TAG,getResGetCollections().toString());
        return resGetCollections;
    }
}
