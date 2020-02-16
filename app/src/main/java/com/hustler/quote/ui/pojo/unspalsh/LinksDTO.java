package com.hustler.quote.ui.pojo.unspalsh;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class LinksDTO implements Serializable {

	@SerializedName("followers")
	private String followers;

	@SerializedName("portfolio")
	private String portfolio;

	@SerializedName("following")
	private String following;

	@SerializedName("self")
	private String self;

	@SerializedName("html")
	private String html;

	@SerializedName("photos")
	private String photos;

	@SerializedName("likes")
	private String likes;

	public void setFollowers(String followers){
		this.followers = followers;
	}

	public String getFollowers(){
		return followers;
	}

	public void setPortfolio(String portfolio){
		this.portfolio = portfolio;
	}

	public String getPortfolio(){
		return portfolio;
	}

	public void setFollowing(String following){
		this.following = following;
	}

	public String getFollowing(){
		return following;
	}

	public void setSelf(String self){
		this.self = self;
	}

	public String getSelf(){
		return self;
	}

	public void setHtml(String html){
		this.html = html;
	}

	public String getHtml(){
		return html;
	}

	public void setPhotos(String photos){
		this.photos = photos;
	}

	public String getPhotos(){
		return photos;
	}

	public void setLikes(String likes){
		this.likes = likes;
	}

	public String getLikes(){
		return likes;
	}

	@Override
 	public String toString(){
		return 
			"LinksDTO{" + 
			"followers = '" + followers + '\'' + 
			",portfolio = '" + portfolio + '\'' + 
			",following = '" + following + '\'' + 
			",self = '" + self + '\'' + 
			",html = '" + html + '\'' + 
			",photos = '" + photos + '\'' + 
			",likes = '" + likes + '\'' + 
			"}";
		}
}