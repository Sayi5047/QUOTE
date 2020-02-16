package com.hustler.quote.ui.pojo.unspalsh;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class ResGetCollectionsDto implements Serializable {

    @SerializedName("featured")
    private boolean featured;

    @SerializedName("private")
    private boolean jsonMemberPrivate;

    @SerializedName("cover_photo")
    private CoverPhotoDTO coverPhoto;

    @SerializedName("total_photos")
    private int totalPhotos;

    @SerializedName("share_key")
    private String shareKey;

    @SerializedName("description")
    private String description;

    @SerializedName("title")
    private String title;

    @SerializedName("tags")
    private List<TagsDTO> tags;

    @SerializedName("preview_photos")
    private List<PreviewPhotosDTO> previewPhotos;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("curated")
    private boolean curated;

    @SerializedName("links")
    private LinksDTO links;

    @SerializedName("id")
    private int id;

    @SerializedName("published_at")
    private String publishedAt;

    @SerializedName("user")
    private UserDTO user;

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setJsonMemberPrivate(boolean jsonMemberPrivate) {
        this.jsonMemberPrivate = jsonMemberPrivate;
    }

    public boolean isJsonMemberPrivate() {
        return jsonMemberPrivate;
    }

    public void setCoverPhoto(CoverPhotoDTO coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public CoverPhotoDTO getCoverPhoto() {
        return coverPhoto;
    }

    public void setTotalPhotos(int totalPhotos) {
        this.totalPhotos = totalPhotos;
    }

    public int getTotalPhotos() {
        return totalPhotos;
    }

    public void setShareKey(String shareKey) {
        this.shareKey = shareKey;
    }

    public String getShareKey() {
        return shareKey;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTags(List<TagsDTO> tags) {
        this.tags = tags;
    }

    public List<TagsDTO> getTags() {
        return tags;
    }

    public void setPreviewPhotos(List<PreviewPhotosDTO> previewPhotos) {
        this.previewPhotos = previewPhotos;
    }

    public List<PreviewPhotosDTO> getPreviewPhotos() {
        return previewPhotos;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setCurated(boolean curated) {
        this.curated = curated;
    }

    public boolean isCurated() {
        return curated;
    }

    public void setLinks(LinksDTO links) {
        this.links = links;
    }

    public LinksDTO getLinks() {
        return links;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public UserDTO getUser() {
        return user;
    }

    @Override
    public String toString() {
        return
                "ResGetCollectionsDto{" +
                        "featured = '" + featured + '\'' +
                        ",private = '" + jsonMemberPrivate + '\'' +
                        ",cover_photo = '" + coverPhoto + '\'' +
                        ",total_photos = '" + totalPhotos + '\'' +
                        ",share_key = '" + shareKey + '\'' +
                        ",description = '" + description + '\'' +
                        ",title = '" + title + '\'' +
                        ",tags = '" + tags + '\'' +
                        ",preview_photos = '" + previewPhotos + '\'' +
                        ",updated_at = '" + updatedAt + '\'' +
                        ",curated = '" + curated + '\'' +
                        ",links = '" + links + '\'' +
                        ",id = '" + id + '\'' +
                        ",published_at = '" + publishedAt + '\'' +
                        ",user = '" + user + '\'' +
                        "}";
    }
}