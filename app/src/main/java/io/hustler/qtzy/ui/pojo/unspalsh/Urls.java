package io.hustler.qtzy.ui.pojo.unspalsh;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Sayi on 26-01-2018.
 */

public class Urls implements Serializable {
    private String raw;

    private String regular;

    private String full;

    private String thumb;

    private String small;

    public String getRaw ()
    {
        return raw;
    }

    public void setRaw (String raw)
    {
        this.raw = raw;
    }

    public String getRegular ()
    {
        return regular;
    }

    public void setRegular (String regular)
    {
        this.regular = regular;
    }

    public String getFull ()
    {
        return full;
    }

    public void setFull (String full)
    {
        this.full = full;
    }

    public String getThumb ()
    {
        return thumb;
    }

    public void setThumb (String thumb)
    {
        this.thumb = thumb;
    }

    public String getSmall ()
    {
        return small;
    }

    public void setSmall (String small)
    {
        this.small = small;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "ClassPojo [raw = "+raw+", regular = "+regular+", full = "+full+", thumb = "+thumb+", small = "+small+"]";
    }
}
