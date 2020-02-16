package com.hustler.quote.ui.apiRequestLauncher.ListnereInterfaces;

public interface StickerResponseListener {
    void onSuccess(String responseJson);

    void onError(String errorMessage);
}
