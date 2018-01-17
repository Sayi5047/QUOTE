package com.hustler.quote.ui.apiRequestLauncher;

import com.hustler.quote.ui.pojo.ImagesFromPixaBay;
import com.hustler.quote.ui.pojo.QuotesFromFC;

import java.util.List;

/**
 * Created by Sayi on 17-01-2018.
 */

public interface ImagesApiResponceListner {
    void onSuccess(List<ImagesFromPixaBay> images);

    void onError(String message);
}
