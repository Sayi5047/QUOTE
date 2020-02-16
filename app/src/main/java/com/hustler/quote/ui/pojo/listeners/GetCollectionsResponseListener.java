package com.hustler.quote.ui.pojo.listeners;

import java.util.ArrayList;

import com.hustler.quote.ui.pojo.unspalsh.ResGetCollectionsDto;

public interface GetCollectionsResponseListener {
    public void onSuccess(ArrayList<ResGetCollectionsDto> resGetCollectionsDto);

    public void onError(String errorMessage);
}
