package se.newton.chatapp.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import se.newton.chatapp.R;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
