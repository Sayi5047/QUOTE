package io.hustler.qtzy.ui.pojo.listeners;

import io.hustler.qtzy.ui.pojo.UnsplashImages_Collection_Response;

/**
 * Created by Sayi on 01-02-2018.
 */

public interface SearchImagesResponseListener {
    void onSuccess(UnsplashImages_Collection_Response response);
    void onError(String error);
}
