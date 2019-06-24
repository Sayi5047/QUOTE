package io.hustler.qtzy.ui.apiRequestLauncher.response;

import com.google.gson.annotations.SerializedName;

//
public class Onclick{

	@SerializedName("url")
	private String url;

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}
}