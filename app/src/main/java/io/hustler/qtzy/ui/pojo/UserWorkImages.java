package io.hustler.qtzy.ui.pojo;

/**
 * Created by Sayi on 17-12-2017.
 */

public class UserWorkImages {

    public UserWorkImages(String[] imagesPaths, String[] imageNames) {
        this.imagesPaths = imagesPaths;
        this.imageNames = imageNames;
    }

    public String[] getImagesPaths() {
        return imagesPaths;
    }

    public void setImagesPaths(String[] imagesPaths) {
        this.imagesPaths = imagesPaths;
    }

    public String[] getImageNames() {
        return imageNames;
    }

    public void setImageNames(String[] imageNames) {
        this.imageNames = imageNames;
    }

    String[] imagesPaths;
    String[] imageNames;
}
