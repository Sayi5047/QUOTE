package io.hustler.qtzy.ui.apiRequestLauncher;

public interface QuotzyApiResponseListener {
    public void onSuccess(String message);

    public void onError(String message);
}
