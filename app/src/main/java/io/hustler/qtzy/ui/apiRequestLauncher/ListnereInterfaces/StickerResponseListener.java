package io.hustler.qtzy.ui.apiRequestLauncher.ListnereInterfaces;

public interface StickerResponseListener {
    void onSuccess(String responseJson);

    void onError(String errorMessage);
}
