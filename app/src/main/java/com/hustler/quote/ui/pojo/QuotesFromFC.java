package com.hustler.quote.ui.pojo;

import java.util.ArrayList;

/**
 * Created by Sayi on 07-10-2017.
 */

public class QuotesFromFC {
    private int id;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private boolean dialogue;

    public boolean getDialogue() {
        return this.dialogue;
    }

    public void setDialogue(boolean dialogue) {
        this.dialogue = dialogue;
    }

//    private boolean private;
//
//    public boolean getPrivate() { return this.private; }
//
//    public void setPrivate(boolean private) { this.private = private; }

    private ArrayList<String> tags;

    public ArrayList<String> getTags() {
        return this.tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    private String url;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private int favorites_count;

    public int getFavoritesCount() {
        return this.favorites_count;
    }

    public void setFavoritesCount(int favorites_count) {
        this.favorites_count = favorites_count;
    }

    private int upvotes_count;

    public int getUpvotesCount() {
        return this.upvotes_count;
    }

    public void setUpvotesCount(int upvotes_count) {
        this.upvotes_count = upvotes_count;
    }

    private int downvotes_count;

    public int getDownvotesCount() {
        return this.downvotes_count;
    }

    public void setDownvotesCount(int downvotes_count) {
        this.downvotes_count = downvotes_count;
    }

    private String author;

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    private String author_permalink;

    public String getAuthorPermalink() {
        return this.author_permalink;
    }

    public void setAuthorPermalink(String author_permalink) {
        this.author_permalink = author_permalink;
    }

    private String body;

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
