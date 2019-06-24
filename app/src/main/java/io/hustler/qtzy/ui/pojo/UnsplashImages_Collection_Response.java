package io.hustler.qtzy.ui.pojo;

import java.util.ArrayList;

import io.hustler.qtzy.ui.pojo.unspalsh.Unsplash_Image;

/**
 * Created by Sayi on 01-02-2018.
 */

public class UnsplashImages_Collection_Response {

    public ArrayList<Unsplash_Image[]> getResults() {
        return results;
    }

    public void setResults(ArrayList<Unsplash_Image[]> results) {
        this.results = results;
    }

    ArrayList<Unsplash_Image[]> results;
}
