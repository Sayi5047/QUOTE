package io.hustler.qtzy.ui.apiRequestLauncher;

import io.hustler.qtzy.ui.pojo.QuotzyBaseResponse;

public interface QuotzyApiResponseListener {
    public void onSuccess(String message);

    public void onDataGet(QuotzyBaseResponse response);
    public void onError(String message);
}
