package com.hustler.quote.ui.activities;

import android.preference.PreferenceActivity;
import android.os.Bundle;

import com.hustler.quote.R;

public class MyPreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.test_prefs);
    }
}
