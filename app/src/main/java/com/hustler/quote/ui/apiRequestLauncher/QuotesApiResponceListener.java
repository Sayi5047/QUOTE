package com.hustler.quote.ui.apiRequestLauncher;

import com.hustler.quote.ui.pojo.QuotesFromFC;
import com.hustler.quote.ui.pojo.RandomQuotes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sayi on 07-10-2017.
 */

public interface QuotesApiResponceListener {
    void onSuccess(List<QuotesFromFC> quotes);
    void onError(String message);
}
