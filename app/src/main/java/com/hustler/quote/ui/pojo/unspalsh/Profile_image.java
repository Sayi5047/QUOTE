package com.hustler.quote.ui.pojo.unspalsh;

/**
 * Created by Sayi on 26-01-2018.
 */

public class Profile_image {
    private String small;

    private String large;

    private String medium;

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    @Override
    public String toString() {
        return "ClassPojo [small = " + small + ", large = " + large + ", medium = " + medium + "]";
    }
}