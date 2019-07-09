package io.hustler.qtzy.ui.pojo.unspalsh;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class CoverPhotoDTO implements Serializable {

	@SerializedName("current_user_collections")
	private List<Object> currentUserCollections;

	@SerializedName("color")
	private String color;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("sponsored")
	private boolean sponsored;

	@SerializedName("liked_by_user")
	private boolean likedByUser;

	@SerializedName("urls")
	private UrlsDTO urls;

	@SerializedName("alt_description")
	private String altDescription;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("width")
	private int width;

	@SerializedName("links")
	private LinksDTO links;

	@SerializedName("id")
	private String id;

	@SerializedName("categories")
	private List<Object> categories;

	@SerializedName("user")
	private UserDTO user;

	@SerializedName("height")
	private int height;

	@SerializedName("likes")
	private int likes;

	public void setCurrentUserCollections(List<Object> currentUserCollections){
		this.currentUserCollections = currentUserCollections;
	}

	public List<Object> getCurrentUserCollections(){
		return currentUserCollections;
	}

	public void setColor(String color){
		this.color = color;
	}

	public String getColor(){
		return color;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setSponsored(boolean sponsored){
		this.sponsored = sponsored;
	}

	public boolean isSponsored(){
		return sponsored;
	}

	public void setLikedByUser(boolean likedByUser){
		this.likedByUser = likedByUser;
	}

	public boolean isLikedByUser(){
		return likedByUser;
	}

	public void setUrls(UrlsDTO urls){
		this.urls = urls;
	}

	public UrlsDTO getUrls(){
		return urls;
	}

	public void setAltDescription(String altDescription){
		this.altDescription = altDescription;
	}

	public String getAltDescription(){
		return altDescription;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setWidth(int width){
		this.width = width;
	}

	public int getWidth(){
		return width;
	}

	public void setLinks(LinksDTO links){
		this.links = links;
	}

	public LinksDTO getLinks(){
		return links;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setCategories(List<Object> categories){
		this.categories = categories;
	}

	public List<Object> getCategories(){
		return categories;
	}

	public void setUser(UserDTO user){
		this.user = user;
	}

	public UserDTO getUser(){
		return user;
	}

	public void setHeight(int height){
		this.height = height;
	}

	public int getHeight(){
		return height;
	}

	public void setLikes(int likes){
		this.likes = likes;
	}

	public int getLikes(){
		return likes;
	}

	@Override
 	public String toString(){
		return 
			"CoverPhotoDTO{" + 
			"current_user_collections = '" + currentUserCollections + '\'' + 
			",color = '" + color + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",sponsored = '" + sponsored + '\'' + 
			",liked_by_user = '" + likedByUser + '\'' + 
			",urls = '" + urls + '\'' + 
			",alt_description = '" + altDescription + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",width = '" + width + '\'' + 
			",links = '" + links + '\'' + 
			",id = '" + id + '\'' + 
			",categories = '" + categories + '\'' + 
			",user = '" + user + '\'' + 
			",height = '" + height + '\'' + 
			",likes = '" + likes + '\'' + 
			"}";
		}
}