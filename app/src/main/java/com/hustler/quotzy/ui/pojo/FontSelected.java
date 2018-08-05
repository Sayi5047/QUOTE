package com.hustler.quotzy.ui.pojo;

/**
 * Created by anvaya5 on 15/12/2017.
 */

public class FontSelected {


    public int isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(int downloaded) {
        isDownloaded = downloaded;
    }

    public String getFontname_path() {
        return fontname_path;
    }

    public void setFontname_path(String fontname_path) {
        this.fontname_path = fontname_path;
    }

    int isDownloaded;
    String fontname_path;
}
