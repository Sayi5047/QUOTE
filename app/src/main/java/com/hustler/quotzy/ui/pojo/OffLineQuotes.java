package com.hustler.quotzy.ui.pojo;

import java.io.Serializable;

/**
 * Created by Sayi on 04-08-2018.
 */

public class OffLineQuotes implements Serializable {
    public String quoteAuthor, quoteText;

    public String getQuoteAuthor() {
        return quoteAuthor;
    }

    public void setQuoteAuthor(String quoteAuthor) {
        this.quoteAuthor = quoteAuthor;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }
}
