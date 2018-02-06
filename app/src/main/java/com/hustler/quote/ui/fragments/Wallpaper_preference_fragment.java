package com.hustler.quote.ui.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.hustler.quote.R;

/**
 * Created by anvaya5 on 06/02/2018.
 */

public class Wallpaper_preference_fragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.daily_wallpaper_preferences);
    }
}
