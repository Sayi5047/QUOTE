package com.hustler.quote.ui.apiRequestLauncher.ListnereInterfaces;

import com.hustler.quote.ui.apiRequestLauncher.Base.BaseResponse;

public interface QuotzyApiResponseListener {
    void onSuccess(String message);
    void onDataGet(BaseResponse response);
    void onError(String message);
}
