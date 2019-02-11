package io.hustler.qtzy.ui.apiRequestLauncher.ListnereInterfaces;

import io.hustler.qtzy.ui.apiRequestLauncher.Base.BaseResponse;

public interface ResponseListener {
    public void onSuccess(BaseResponse baseResponse);

    public void onError(String message);
}
