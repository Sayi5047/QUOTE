package io.hustler.qtzy.ui.pojo.listeners;

import io.hustler.qtzy.ui.pojo.ResGetSearchResultsDto;

/**
 * Created by Sayi on 01-02-2018.
 */

public interface SearchImagesResponseListener {
    void onSuccess(ResGetSearchResultsDto response);
    void onError(String error);
}
