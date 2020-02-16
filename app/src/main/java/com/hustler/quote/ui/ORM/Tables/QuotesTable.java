package com.hustler.quote.ui.ORM.Tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.hustler.quote.ui.database.Contract;

@Entity(tableName = "mstr_quotes")
public class QuotesTable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = Contract.Quotes.QUOTE_BODY)
    private String quotes;
    @ColumnInfo(name = Contract.Quotes.QUOTE_AUTHOR)
    private String author;
    @ColumnInfo(name = Contract.Quotes.QUOTE_CATEGORY)
    private String category;
    @ColumnInfo(name = Contract.Quotes.QUOTE_LANGUAGE)
    private String language;
    @ColumnInfo(name = Contract.Quotes.QUOTE_IS_LIKED)
    private boolean isliked;

    public QuotesTable( String quotes, String author, String category, String language, boolean isliked) {
        this.quotes = quotes;
        this.author = author;
        this.category = category;
        this.language = language;
        this.isliked = isliked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuotes() {
        return quotes;
    }

    public void setQuotes(String quotes) {
        this.quotes = quotes;
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

    public boolean isIsliked() {
        return isliked;
    }

    public void setIsliked(boolean isliked) {
        this.isliked = isliked;
    }
}
