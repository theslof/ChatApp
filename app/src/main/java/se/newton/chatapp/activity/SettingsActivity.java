package se.newton.chatapp.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import se.newton.chatapp.R;
import se.newton.chatapp.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(null,"SettingsActivity");
        super.onCreate(savedInstanceState);

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
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(listener);
        themeChange(PreferenceManager.getDefaultSharedPreferences(this));

        SettingsFragment settingsFragment = new SettingsFragment();
        getFragmentManager().beginTransaction().replace(R.id.settings_container, settingsFragment).commit();

        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(l -> onBackPressed());
    }

    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    private void themeChange(SharedPreferences sharedPreferences) {
        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String appTheme = sharedPreferences.getString("themeChanger", "App theme");
        if (appTheme.equals("App theme")) {
            this.setTheme(R.style.AppTheme_NoActionBar);
        } else if (appTheme.equals("Dragomir")) {
            this.setTheme(R.style.Dragomir);
        } else if (appTheme.equals("Elias 1")) {
            this.setTheme(R.style.Elias1);
        } else if (appTheme.equals("Elias 2")) {
            this.setTheme(R.style.Elias2);
        } else if (appTheme.equals("Jonas")) {
            this.setTheme(R.style.Jonas);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
