package io.hustler.qtzy.ui.apiRequestLauncher;

import io.hustler.qtzy.ui.apiRequestLauncher.Base.BaseResponse;

public interface QuotzyApiResponseListener {
    public void onSuccess(String message);
    public void onDataGet(BaseResponse response);
    public void onError(String message);
}
