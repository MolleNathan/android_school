package com.example.nathan.schoolmollenathan;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nathan on 10/06/2018.
 */

public class ListActivity extends FragmentActivity implements ActionBar.OnFragmentInteractionListener, LocationListener {

    SchoolService schoolService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        schoolService = RequestInterface.getInterface(SchoolService.class);
        Fragment actionBar = new ActionBar();
        Bundle arguments = new Bundle();
        arguments.putString("title", "Liste des écoles");
        arguments.putString("menu", "ecoleList");
        actionBar.setArguments(arguments);
        populateListView();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, actionBar).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateListView();
    }

    private void populateListView(){

        schoolService.getEcole(Preferences.getSchoolType()).enqueue(new Callback<JsonResponseSchool>() {
            @Override
            public void onResponse(Call<JsonResponseSchool> call, Response<JsonResponseSchool> response) {
                if(response.code() == 200){
                    final List<School> listSchool = response.body().getSchools();

                    ListView listView = findViewById(R.id.listView);
                    SchoolAdapter adapter = new SchoolAdapter(ListActivity.this, R.layout.list_ecole_object, listSchool);
                    listView.setAdapter(adapter);


                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            School school = listSchool.get(position);
                            Intent i = new Intent(view.getContext(), DetailActivity.class);
                            i.putExtra("school", school);
                            view.getContext().startActivity(i);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonResponseSchool> call, Throwable t) {
                Log.e("a", t.toString());
                Toast.makeText(ListActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public void onFragmentInteraction(String test) {

    }

    @Override
    public void onLocationChanged(Location location) {

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
}
