package se.newton.chatapp.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import se.newton.chatapp.R;
import se.newton.chatapp.fragment.SettingsFragment;
import se.newton.chatapp.service.ThemeChanger;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(null,"SettingsActivity");

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                switch (key) {
                    case "themeChanger":
                        recreate();
                        break;
                }
            }
        };
        ThemeChanger.changeTheme(PreferenceManager.getDefaultSharedPreferences(this), this );
       
        super.onCreate(savedInstanceState);

        SettingsFragment settingsFragment = new SettingsFragment();
        getFragmentManager().beginTransaction().replace(R.id.settings_container, settingsFragment).commit();

        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(l -> onBackPressed());
    }

    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(listener);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
