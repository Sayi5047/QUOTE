package com.hustler.quote.ui.pojo.unspalsh;

import java.util.ArrayList;

/**
 * Created by Sayi on 26-01-2018.
 */

public interface ImagesFromUnsplashResponse {
    void onSuccess(Unsplash_Image[] unsplash_images);

    void onError(String error);
}
