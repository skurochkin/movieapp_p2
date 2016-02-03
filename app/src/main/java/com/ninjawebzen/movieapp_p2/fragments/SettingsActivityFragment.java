package com.ninjawebzen.movieapp_p2.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.ninjawebzen.movieapp_p2.R;

import java.util.Map;

/**
 * Created by slavkurochkin on 10/17/15.
 */
public class SettingsActivityFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_main);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);

        if (pref == null) {
            return;
        }

        if (pref instanceof ListPreference) {
            ListPreference listPref = (ListPreference) pref;
            listPref.setSummary(listPref.getEntry().toString());
        }
    }

    private void triggerSharePreferencesChange() {
        SharedPreferences pref = getPreferenceScreen().getSharedPreferences();
        Map<String,?> keys = pref.getAll();

        if (keys == null) {
            return;
        }

        for(String key : keys.keySet()) {
            onSharedPreferenceChanged(pref, key);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        triggerSharePreferencesChange();

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}