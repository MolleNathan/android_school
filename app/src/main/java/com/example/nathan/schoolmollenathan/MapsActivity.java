package com.example.nathan.schoolmollenathan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nathan on 10/06/2018.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ActionBar.OnFragmentInteractionListener, LocationListener {


    private SchoolService schoolService;
    private GoogleMap mMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        schoolService = RequestInterface.getInterface(SchoolService.class);

        Fragment actionBar = new ActionBar();
        Bundle arguments = new Bundle();
        arguments.putString("title", "Carte");
        arguments.putString("menu", "map");
        actionBar.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, actionBar).commit();

        PlaceAutocompleteFragment placeAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);
        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                mMap.moveCamera(CameraUpdateFactory.newLatLng( place.getLatLng()));

                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }
        });


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 177);


        }

        Location location;
        populateListEcole();

        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("latitude", 0);
        double longitude = intent.getDoubleExtra("longitude", 0);

        if (lat != 0 && longitude != 0){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, longitude)));

            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            location = Localisation.getLocation(this, false);
        }
        else {
            location = Localisation.getLocation(this, true);
        }

        mMap.setMyLocationEnabled(true);


    }

    public void populateListEcole() {
        schoolService.getEcole(Preferences.getSchoolType()).enqueue(new Callback<JsonResponseSchool>() {

            @Override
            public void onResponse(Call<JsonResponseSchool> call, Response<JsonResponseSchool> response) {
                if (response.code() == 200) {
                    final List<School> listSchool = response.body().getSchools();
                    for(School school : listSchool){
                        BitmapDescriptor color;
                        if(school.getNb_student() < 50){
                            color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                        }
                        else if(school.getNb_student() >= 50 && school.getNb_student() < 200){
                            color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                        }
                        else {
                            color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                        }
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(school.getLatitude(), school.getLongitude()))
                                .title(school.getName())
                                .icon(color)

                        );
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonResponseSchool> call, Throwable t) {

            }
        });
    }


    public void onLocationChanged(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
    }


    @Override
    public void onFragmentInteraction(String test) {

    }
}
