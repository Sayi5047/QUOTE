package com.hustler.quote.ui.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import androidx.annotation.Nullable;

import com.hustler.quote.R;

public class SettingsFragment extends PreferenceFragment {

    public static SettingsFragment getInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_preference_fragment);
        
    }
}
