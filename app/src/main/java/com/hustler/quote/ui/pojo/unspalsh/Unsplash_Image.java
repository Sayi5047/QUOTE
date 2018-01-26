package com.hustler.quote.ui.pojo.unspalsh;

/**
 * Created by Sayi on 26-01-2018.
 */

public class Unsplash_Image {
    private String[] current_user_collections;

    private Urls urls;

    private String width;

    private Links links;

    private String id;

    private String updated_at;

    private String height;

    private String color;

    private String description;

    private String likes;

    private String created_at;

    private String[] categories;

    private String sponsored;

    private User user;

    private String liked_by_user;

    public String[] getCurrent_user_collections() {
        return current_user_collections;
    }

    public void setCurrent_user_collections(String[] current_user_collections) {
        this.current_user_collections = current_user_collections;
    }

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String

    getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public String getSponsored() {
        return sponsored;
    }

    public void setSponsored(String sponsored) {
        this.sponsored = sponsored;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLiked_by_user() {
        return liked_by_user;
    }

    public void setLiked_by_user(String liked_by_user) {
        this.liked_by_user = liked_by_user;
    }

    @Override
    public String toString() {
        return "ClassPojo [current_user_collections = " + current_user_collections + ", urls = " + urls + ", width = " + width + ", links = " + links + ", id = " + id + ", updated_at = " + updated_at + ", height = " + height + ", color = " + color + ", description = " + description + ", likes = " + likes + ", created_at = " + created_at + ", categories = " + categories + ", sponsored = " + sponsored + ", user = " + user + ", liked_by_user = " + liked_by_user + "]";
    }
}
