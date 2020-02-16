package com.hustler.quote.ui.apiRequestLauncher;

import com.hustler.quote.ui.apiRequestLauncher.Base.BaseResponse;

import java.util.ArrayList;

import com.hustler.quote.ui.fragments.CategoriesFragment;
import com.hustler.quote.ui.pojo.Quote;

public class ResponseQuotesService extends BaseResponse {
    public static class Quotes {
        private String quote, author, country, category;
        private Long id;

        public static Quote returnInstance(String quote, String author, String country, String category) {
            return new Quote(quote, author, country, category);
        }

        public Quotes(String quote, String author, String country, String category, Long id) {
            this.quote = quote;
            this.author = author;
            this.country = country;
            this.category = category;
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
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

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }

    ArrayList<CategoriesFragment.Quotes> data = new ArrayList<>();

    public ArrayList<CategoriesFragment.Quotes> getData() {
        return data;
    }

    public void setData(ArrayList<CategoriesFragment.Quotes> data) {
        this.data = data;
    }
}
