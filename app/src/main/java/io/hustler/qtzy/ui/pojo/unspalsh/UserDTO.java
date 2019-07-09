package io.hustler.qtzy.ui.pojo.unspalsh;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class UserDTO implements Serializable {

	@SerializedName("total_photos")
	private int totalPhotos;

	@SerializedName("accepted_tos")
	private boolean acceptedTos;

	@SerializedName("twitter_username")
	private String twitterUsername;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("bio")
	private String bio;

	@SerializedName("total_likes")
	private int totalLikes;

	@SerializedName("portfolio_url")
	private String portfolioUrl;

	@SerializedName("profile_image")
	private ProfileImageDTO profileImage;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("name")
	private String name;

	@SerializedName("location")
	private String location;

	@SerializedName("links")
	private LinksDTO links;

	@SerializedName("total_collections")
	private int totalCollections;

	@SerializedName("id")
	private String id;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("instagram_username")
	private String instagramUsername;

	@SerializedName("username")
	private String username;

	public void setTotalPhotos(int totalPhotos){
		this.totalPhotos = totalPhotos;
	}

	public int getTotalPhotos(){
		return totalPhotos;
	}

	public void setAcceptedTos(boolean acceptedTos){
		this.acceptedTos = acceptedTos;
	}

	public boolean isAcceptedTos(){
		return acceptedTos;
	}

	public void setTwitterUsername(String twitterUsername){
		this.twitterUsername = twitterUsername;
	}

	public String getTwitterUsername(){
		return twitterUsername;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setBio(String bio){
		this.bio = bio;
	}

	public String getBio(){
		return bio;
	}

	public void setTotalLikes(int totalLikes){
		this.totalLikes = totalLikes;
	}

	public int getTotalLikes(){
		return totalLikes;
	}

	public void setPortfolioUrl(String portfolioUrl){
		this.portfolioUrl = portfolioUrl;
	}

	public String getPortfolioUrl(){
		return portfolioUrl;
	}

	public void setProfileImage(ProfileImageDTO profileImage){
		this.profileImage = profileImage;
	}

	public ProfileImageDTO getProfileImage(){
		return profileImage;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setLocation(String location){
		this.location = location;
	}

	public String getLocation(){
		return location;
	}

	public void setLinks(LinksDTO links){
		this.links = links;
	}

	public LinksDTO getLinks(){
		return links;
	}

	public void setTotalCollections(int totalCollections){
		this.totalCollections = totalCollections;
	}

	public int getTotalCollections(){
		return totalCollections;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setInstagramUsername(String instagramUsername){
		this.instagramUsername = instagramUsername;
	}

	public String getInstagramUsername(){
		return instagramUsername;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	@Override
 	public String toString(){
		return 
			"UserDTO{" + 
			"total_photos = '" + totalPhotos + '\'' + 
			",accepted_tos = '" + acceptedTos + '\'' + 
			",twitter_username = '" + twitterUsername + '\'' + 
			",last_name = '" + lastName + '\'' + 
			",bio = '" + bio + '\'' + 
			",total_likes = '" + totalLikes + '\'' + 
			",portfolio_url = '" + portfolioUrl + '\'' + 
			",profile_image = '" + profileImage + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",name = '" + name + '\'' + 
			",location = '" + location + '\'' + 
			",links = '" + links + '\'' + 
			",total_collections = '" + totalCollections + '\'' + 
			",id = '" + id + '\'' + 
			",first_name = '" + firstName + '\'' + 
			",instagram_username = '" + instagramUsername + '\'' + 
			",username = '" + username + '\'' + 
			"}";
		}
}