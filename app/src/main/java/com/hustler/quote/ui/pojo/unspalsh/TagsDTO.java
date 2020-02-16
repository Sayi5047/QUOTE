package com.hustler.quote.ui.pojo.unspalsh;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class TagsDTO implements Serializable {

	@SerializedName("title")
	private String title;

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	@Override
 	public String toString(){
		return 
			"TagsDTO{" + 
			"title = '" + title + '\'' + 
			"}";
		}
}