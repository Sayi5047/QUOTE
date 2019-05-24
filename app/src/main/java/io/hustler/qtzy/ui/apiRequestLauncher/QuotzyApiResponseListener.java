package io.hustler.qtzy.ui.apiRequestLauncher;

import io.hustler.qtzy.ui.apiRequestLauncher.Base.BaseResponse;

public interface QuotzyApiResponseListener {
    void onSuccess(String message);
    void onDataGet(BaseResponse response);
    void onError(String message);
}
