package com.example.nathan.schoolmollenathan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CheckBox;

/**
 * Created by Nathan on 10/06/2018.
 */

public class ConfigActivity extends FragmentActivity implements ActionBar.OnFragmentInteractionListener {

    CheckBox publicImage;
    CheckBox privateImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        Fragment actionBar = new ActionBar();
        Bundle arguments = new Bundle();
        arguments.putString("title", "Configuration");
        arguments.putString("menu", "config");
        actionBar.setArguments(arguments);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, actionBar).commit();

        publicImage = findViewById(R.id.public_toggle);
        privateImage = findViewById(R.id.private_toggle);

        final SharedPreferences prefs = Preferences.getPreferences();
        final boolean publicCache = prefs.getBoolean("public", true);
        boolean privateCache = prefs.getBoolean("private", true);

        publicImage.setChecked(publicCache);
        privateImage.setChecked(privateCache);

        publicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!publicImage.isChecked() && !privateImage.isChecked()){
                    privateImage.setChecked(true);
                }
                prefs.edit()
                        .putBoolean("public", publicImage.isChecked())
                        .putBoolean("private", privateImage.isChecked())
                        .apply();
            }
        });

        privateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!publicImage.isChecked() && !privateImage.isChecked()){
                    privateImage.setChecked(true);
                }
                prefs.edit()
                        .putBoolean("private", privateImage.isChecked())
                        .putBoolean("public", publicImage.isChecked())
                        .apply();
            }
        });



    }

    @Override
    public void onFragmentInteraction(String test) {

    }
}
