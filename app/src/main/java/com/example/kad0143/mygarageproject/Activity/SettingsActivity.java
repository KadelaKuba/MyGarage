package com.example.kad0143.mygarageproject.Activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.kad0143.mygarageproject.R;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Loads the XML preferences file.
        addPreferencesFromResource(R.xml.preferences);
    }
}
