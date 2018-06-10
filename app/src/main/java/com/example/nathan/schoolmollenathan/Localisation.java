package com.example.nathan.schoolmollenathan;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Nathan on 10/06/2018.
 */

public class Localisation {

    private static String provider;
    private static LocationManager locationManager;
    private static Context context;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public static Location getLocation(Context newContext, boolean update){
        context = newContext;
        checkLocationPermission();
        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

        if(locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)){
            provider = LocationManager.GPS_PROVIDER;
        }else {
            provider = LocationManager.NETWORK_PROVIDER;
        }

        if(update){
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    10000,
                    10, (LocationListener) context);
        }

        Location location = locationManager.getLastKnownLocation(provider);
        return location;
    }

    public static boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                new AlertDialog.Builder(context)
                        .setTitle("Acces localisation")
                        .setMessage("Demande d'accès a votre position actuelle")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions((Activity) context,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


}
