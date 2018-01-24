package com.hustler.quote.ui.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.hustler.quote.R;

/**
 * Created by anvaya5 on 24/01/2018.
 */

public class widget_layout_config_fragment extends PreferenceFragment {


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.layout_config);
    }
}
