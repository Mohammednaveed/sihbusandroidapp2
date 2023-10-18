package com.example.finalbustraking;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    private static final String PREFS_NAME = "MyPrefs";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String USER_TYPE = "userType";

    public static void setLoggedIn(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(IS_LOGGED_IN, loggedIn);
        editor.apply();
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(IS_LOGGED_IN, false); // Default is false
    }

    public static void setUserType(Context context, String userType) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(USER_TYPE, userType);
        editor.apply();
    }

    public static String getUserType(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(USER_TYPE, ""); // Default is an empty string
    }
}
