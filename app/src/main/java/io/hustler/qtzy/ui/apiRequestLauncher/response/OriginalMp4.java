package io.hustler.qtzy.ui.apiRequestLauncher.response;

import com.google.gson.annotations.SerializedName;

//
public class OriginalMp4{

	@SerializedName("mp4")
	private String mp4;

	@SerializedName("width")
	private String width;

	@SerializedName("mp4_size")
	private String mp4Size;

	@SerializedName("height")
	private String height;

	public void setMp4(String mp4){
		this.mp4 = mp4;
	}

	public String getMp4(){
		return mp4;
	}

	public void setWidth(String width){
		this.width = width;
	}

	public String getWidth(){
		return width;
	}

	public void setMp4Size(String mp4Size){
		this.mp4Size = mp4Size;
	}

	public String getMp4Size(){
		return mp4Size;
	}

	public void setHeight(String height){
		this.height = height;
	}

	public String getHeight(){
		return height;
	}
}