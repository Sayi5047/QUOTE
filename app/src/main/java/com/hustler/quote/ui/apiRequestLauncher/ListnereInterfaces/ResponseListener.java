package com.hustler.quote.ui.apiRequestLauncher.ListnereInterfaces;

import com.hustler.quote.ui.apiRequestLauncher.Base.BaseResponse;

public interface ResponseListener {
    void onSuccess(BaseResponse baseResponse);

    void onError(String message);
}
