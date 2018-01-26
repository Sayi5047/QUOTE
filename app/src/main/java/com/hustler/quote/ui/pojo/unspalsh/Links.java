package com.hustler.quote.ui.pojo.unspalsh;

/**
 * Created by Sayi on 26-01-2018.
 */

public class Links {
    private String download_location;

    private String download;

    private String html;

    private String self;

    public String getDownload_location() {
        return download_location;
    }

    public void setDownload_location(String download_location) {
        this.download_location = download_location;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    @Override
    public String toString() {
        return "ClassPojo [download_location = " + download_location + ", download = " + download + ", html = " + html + ", self = " + self + "]";
    }
}
