package com.hustler.quote.ui.pojo;

import java.util.List;

/**
 * Created by Sayi on 07-10-2017.
 */

public class RandomQuotes {

    int page;
    boolean last_page;
    List<QuotesFromFC> quotes;

    public RandomQuotes(int page, boolean last_page, List<QuotesFromFC> quotes) {
        this.page = page;
        this.last_page = last_page;
        this.quotes = quotes;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isLast_page() {
        return last_page;
    }

    public void setLast_page(boolean last_page) {
        this.last_page = last_page;
    }

    public List<QuotesFromFC> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<QuotesFromFC> quotes) {
        this.quotes = quotes;
    }
}
