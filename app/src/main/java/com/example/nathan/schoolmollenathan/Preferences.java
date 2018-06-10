package com.example.nathan.schoolmollenathan;

import android.content.SharedPreferences;

/**
 * Created by Nathan on 10/06/2018.
 */

public class Preferences {
    private static SharedPreferences preferences;

    public static SharedPreferences getPreferences() {
        return preferences;
    }

    public static void setPreferences(SharedPreferences preferences) {
        Preferences.preferences = preferences;
    }

    //Renvoi null si les deux types sont sélectionner. Sinon, renvoie l'unique type sélectionné
    public static String getSchoolType(){
        String status = null;
        SharedPreferences prefs = Preferences.getPreferences();
        if(!prefs.getBoolean("public", false) && prefs.getBoolean("private", false)){
            status = "private";
        }else if (prefs.getBoolean("public", false) && !prefs.getBoolean("private", false)){
            status = "public";
        }
        return status;
    }
}
