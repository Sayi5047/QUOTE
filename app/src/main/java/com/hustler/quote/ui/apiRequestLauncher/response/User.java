package com.hustler.quote.ui.apiRequestLauncher.response;

import com.google.gson.annotations.SerializedName;

//
public class User{

	@SerializedName("avatar_url")
	private String avatarUrl;

	@SerializedName("profile_url")
	private String profileUrl;

	@SerializedName("banner_url")
	private String bannerUrl;

	@SerializedName("banner_image")
	private String bannerImage;

	@SerializedName("display_name")
	private String displayName;

	@SerializedName("is_verified")
	private boolean isVerified;

	@SerializedName("username")
	private String username;

	public void setAvatarUrl(String avatarUrl){
		this.avatarUrl = avatarUrl;
	}

	public String getAvatarUrl(){
		return avatarUrl;
	}

	public void setProfileUrl(String profileUrl){
		this.profileUrl = profileUrl;
	}

	public String getProfileUrl(){
		return profileUrl;
	}

	public void setBannerUrl(String bannerUrl){
		this.bannerUrl = bannerUrl;
	}

	public String getBannerUrl(){
		return bannerUrl;
	}

	public void setBannerImage(String bannerImage){
		this.bannerImage = bannerImage;
	}

	public String getBannerImage(){
		return bannerImage;
	}

	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}

	public String getDisplayName(){
		return displayName;
	}

	public void setIsVerified(boolean isVerified){
		this.isVerified = isVerified;
	}

	public boolean isIsVerified(){
		return isVerified;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}
}