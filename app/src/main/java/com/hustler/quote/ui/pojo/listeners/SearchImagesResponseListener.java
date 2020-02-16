package com.hustler.quote.ui.pojo.listeners;

import com.hustler.quote.ui.pojo.ResGetSearchResultsDto;

/**
 * Created by Sayi on 01-02-2018.
 */

public interface SearchImagesResponseListener {
    void onSuccess(ResGetSearchResultsDto response);
    void onError(String error);
}
