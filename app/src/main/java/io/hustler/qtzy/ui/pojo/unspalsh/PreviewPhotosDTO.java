package io.hustler.qtzy.ui.pojo.unspalsh;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class PreviewPhotosDTO implements Serializable {

	@SerializedName("urls")
	private UrlsDTO urls;

	@SerializedName("id")
	private String id;

	public void setUrls(UrlsDTO urls){
		this.urls = urls;
	}

	public UrlsDTO getUrls(){
		return urls;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"PreviewPhotosDTO{" + 
			"urls = '" + urls + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}