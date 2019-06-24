package io.hustler.qtzy.ui.pojo.listeners;

import java.util.ArrayList;

import io.hustler.qtzy.ui.pojo.unspalsh.ResGetCollectionsDto;

public interface GetCollectionsResponseListener {
    public void onSuccess(ArrayList<ResGetCollectionsDto> resGetCollectionsDto);

    public void onError(String errorMessage);
}
