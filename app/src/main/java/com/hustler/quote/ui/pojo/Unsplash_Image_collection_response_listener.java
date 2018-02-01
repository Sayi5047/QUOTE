package com.hustler.quote.ui.pojo;

/**
 * Created by Sayi on 01-02-2018.
 */

public interface Unsplash_Image_collection_response_listener {
    void onSuccess(UnsplashImages_Collection_Response response);
    void onError(String error);
}
