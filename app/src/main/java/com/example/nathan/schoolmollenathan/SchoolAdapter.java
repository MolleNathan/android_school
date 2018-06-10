package com.example.nathan.schoolmollenathan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nathan on 10/06/2018.
 */
public class SchoolAdapter extends ArrayAdapter<School> {


    public SchoolAdapter(@NonNull Context context, int resource, @NonNull List<School> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final School school = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_ecole_object, parent, false);
        }

        final SchoolService schoolService = RequestInterface.getInterface(SchoolService.class);

        TextView name = convertView.findViewById(R.id.nom_ecole);
        TextView address = convertView.findViewById(R.id.adresse_ecole);
        TextView nombre = convertView.findViewById(R.id.nb_eleve);
        TextView distance = convertView.findViewById(R.id.distance_ecole);
        name.setText(school != null ? school.getName() : "");
        address.setText(school != null ? school.getAddress() : "");
        nombre.setText(school.getNb_student() + " élèves");

        Location location = Localisation.getLocation((Activity) getContext(), false);

        if(location != null){
            Location schoolLoc = new Location("");
            schoolLoc.setLatitude(school.getLatitude());
            schoolLoc.setLongitude(school.getLongitude());
            String distanceString = String.format("%.0f", location.distanceTo(schoolLoc) / 1000);

            distance.setText(distanceString + " km");
        }
        else {
            distance.setText("Distance indisponible");
        }



        ImageView state = convertView.findViewById(R.id.ecoleState);
        ImageButton map_button = convertView.findViewById(R.id.map_button);
        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra("latitude", school.getLatitude());
                intent.putExtra("longitude", school.getLongitude());
                getContext().startActivity(intent);
            }
        });

        ImageButton delete_button = convertView.findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext()).setTitle("Suppression")
                        .setMessage("Etes vous sûre de vouloir supprimer l’école " + school.getName())
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                schoolService.removeEcole(school.getId()).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        notifyDataSetChanged();
                                        Toast.makeText(getContext(), "School supprimée", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Non", null)
                        .show();

            }
        });
        map_button.setImageResource(R.drawable.ic_map);
        delete_button.setImageResource(R.drawable.ic_delete);
        if(school.getNb_student() < 50){
            state.setImageResource(R.drawable.ic_dislike);
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.red));
        }
        else {
            state.setImageResource(R.drawable.ic_like);
            if(school.getNb_student() >= 50 && school.getNb_student() < 200){
                convertView.setBackgroundColor(convertView.getResources().getColor(R.color.orange));
            }
            else {
                convertView.setBackgroundColor(convertView.getResources().getColor(R.color.green));
            }
        }


        return convertView;
    }
}
