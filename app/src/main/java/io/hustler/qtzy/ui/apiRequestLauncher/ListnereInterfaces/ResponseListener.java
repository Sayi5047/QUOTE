package io.hustler.qtzy.ui.apiRequestLauncher.ListnereInterfaces;

import io.hustler.qtzy.ui.apiRequestLauncher.Base.BaseResponse;

public interface ResponseListener {
    void onSuccess(BaseResponse baseResponse);

    void onError(String message);
}
