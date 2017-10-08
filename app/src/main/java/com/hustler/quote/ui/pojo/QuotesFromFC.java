package com.hustler.quote.ui.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sayi on 07-10-2017.
 */

public class QuotesFromFC implements Serializable {
    private int id;
    private boolean dialogue;
    private ArrayList<String> tags;
    private String url;
    private int favorites_count;
    private int upvotes_count;
    private int downvotes_count;
    private String author;
    private String author_permalink;
    private String body;
    private int color;


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

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


    public ArrayList<String> getTags() {
        return this.tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }


    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public int getFavoritesCount() {
        return this.favorites_count;
    }

    public void setFavoritesCount(int favorites_count) {
        this.favorites_count = favorites_count;
    }


    public int getUpvotesCount() {
        return this.upvotes_count;
    }

    public void setUpvotesCount(int upvotes_count) {
        this.upvotes_count = upvotes_count;
    }


    public int getDownvotesCount() {
        return this.downvotes_count;
    }

    public void setDownvotesCount(int downvotes_count) {
        this.downvotes_count = downvotes_count;
    }


    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getAuthorPermalink() {
        return this.author_permalink;
    }

    public void setAuthorPermalink(String author_permalink) {
        this.author_permalink = author_permalink;
    }


    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }


}
