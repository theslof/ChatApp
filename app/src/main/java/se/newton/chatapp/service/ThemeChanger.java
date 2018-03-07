package se.newton.chatapp.service;

import android.content.Context;
import android.content.SharedPreferences;

import se.newton.chatapp.R;

/**
 * Created by Martin on 2018-03-07.
 */

public class ThemeChanger {

  public static void changeTheme(SharedPreferences sharedPreferences, Context context) {
        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String appTheme = sharedPreferences.getString("themeChanger", "App theme");
        if (appTheme.equals("App theme")) {
            context.setTheme(R.style.AppTheme_NoActionBar);
        } else if (appTheme.equals("Dragomir")) {
            context.setTheme(R.style.Dragomir);
        } else if (appTheme.equals("Elias 1")) {
            context.setTheme(R.style.Elias1);
        } else if (appTheme.equals("Elias 2")) {
            context.setTheme(R.style.Elias2);
        } else if (appTheme.equals("Jonas")) {
            context.setTheme(R.style.Jonas);
        }
    }
}
