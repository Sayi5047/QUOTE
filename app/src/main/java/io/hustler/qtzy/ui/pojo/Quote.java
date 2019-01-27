package io.hustler.qtzy.ui.pojo;

import android.graphics.drawable.GradientDrawable;

import java.io.Serializable;

/**
 * Created by anvaya5 on 27/12/2017.
 */

public class Quote implements Serializable {
    long id;
    String quote;
    String author;
    String category;
    String language;
    GradientDrawable color;

    public Quote(long id, String quote, String author, String category, String language, GradientDrawable color) {
        this.id = id;
        this.quote = quote;
        this.author = author;
        this.category = category;
        this.language = language;
        this.color = color;
    }

    public GradientDrawable getColor() {
        return color;
    }

    public void setColor(GradientDrawable color) {
        this.color = color;
    }


    public Quote(long id, String quote, String author, String category, String language, GradientDrawable color, int isLiked) {
        this.id = id;
        this.quote = quote;
        this.author = author;
        this.category = category;
        this.language = language;
        this.color = color;
        this.isLiked = isLiked;
    }

    public int getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(int isLiked) {
        this.isLiked = isLiked;
    }

    int isLiked;

    public Quote(String quote, String author, String category, String language) {
        this.quote = quote;
        this.author = author;
        this.category = category;
        this.language = language;
    }

    public Quote() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


//    @Override
//    public int compareTo(@NonNull Object o) {
//        Quote quote = (Quote) o;
//        return quote.equals(quote.quote) ? 0 : 1  ;
//    }
}
