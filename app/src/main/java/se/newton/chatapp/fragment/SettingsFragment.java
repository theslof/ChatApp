package se.newton.chatapp.fragment;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;


import se.newton.chatapp.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getActivity().setTitle("Settings");
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
