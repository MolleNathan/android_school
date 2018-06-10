package com.example.nathan.schoolmollenathan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nathan on 10/06/2018.
 */

public class UpdateActivity extends AppCompatActivity implements ActionBar.OnFragmentInteractionListener, AdapterView.OnItemSelectedListener {

    private SchoolService schoolService;
    private School school;

    ArrayAdapter<CharSequence> adapter;

    private EditText editName;
    private EditText editaddress;
    private EditText editNbStud;
    private EditText editLat;
    private EditText editLong;
    private EditText editMail;
    private EditText editZipCode;
    private EditText editSchedule;
    private EditText editPhone;
    private EditText editCity;
    private Spinner editType;
    private ArrayList<EditText> listEdit = new ArrayList<EditText>();

    private Button buttonValid;
    private Button buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

        schoolService = RequestInterface.getInterface(SchoolService.class);

        //Mise en place du spinner de type
        editType = findViewById(R.id.inputType);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editType.setAdapter(adapter);
        editType.setOnItemSelectedListener(this);

        editName = findViewById(R.id.inputName);
        listEdit.add(editName);
        editaddress = findViewById(R.id.inputAddress);
        listEdit.add(editaddress);
        editNbStud = findViewById(R.id.inputNbStud);
        listEdit.add(editNbStud);
        editLat = findViewById(R.id.inputLat);
        listEdit.add(editLat);
        editLong = findViewById(R.id.inputLong);
        listEdit.add(editLong);
        editMail = findViewById(R.id.inputMail);
        listEdit.add(editMail);
        editZipCode = findViewById(R.id.inputCode);
        listEdit.add(editZipCode);
        editSchedule = findViewById(R.id.inputTimes);
        listEdit.add(editSchedule);
        editPhone = findViewById(R.id.inputPhone);
        listEdit.add(editPhone);
        editCity = findViewById(R.id.inputCity);
        listEdit.add(editCity);

        buttonValid = findViewById(R.id.btnReset);
        buttonCancel = findViewById(R.id.btnCancel);


        Intent i = getIntent();
        school = (School) i.getSerializableExtra("school");

        populateDatas(school, adapter);

        //Mise en place de l'actionbar
        Fragment actionBar = new ActionBar();
        Bundle arguments = new Bundle();
        arguments.putString("title", "Modification " + school.getName());
        arguments.putString("menu", "ecoleUpdate");
        actionBar.setArguments(arguments);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, actionBar).commit();

        buttonValid.setOnClickListener(genericOnClickListener);
        buttonCancel.setOnClickListener(genericOnClickListener);

    }

    private void populateDatas(School school, ArrayAdapter<CharSequence> adapter) {

        editName.setText(school.getName());
        editaddress.setText(school.getAddress());
        editNbStud.setText(Integer.toString(school.getNb_student()));
        editLat.setText(Double.toString(school.getLatitude()));
        editLong.setText(Double.toString(school.getLongitude()));
        editMail.setText(school.getMail());
        editZipCode.setText(school.getPostal_code());
        editSchedule.setText(school.getSchedule());
        editPhone.setText(school.getPhone_number());
        editCity.setText(school.getCity());
        editType.setSelection(adapter.getPosition(school.getStatus()));

    }

    final View.OnClickListener genericOnClickListener = new View.OnClickListener() {
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.btnReset:
                    boolean noErrors = true;
                    CharSequence errorMessage = getText(R.string.empty_champ);
                    for(EditText edit : listEdit){
                        Log.e("text", edit.getText().toString());
                        if(edit.getText().toString().equals("")){
                            noErrors = false;
                            edit.setError(errorMessage);
                        }
                    }
                    if(noErrors){
                        final School newSchool = new School(
                                school.getId(),
                                editName.getText().toString(),
                                editaddress.getText().toString(),
                                Integer.parseInt(editNbStud.getText().toString()),
                                editType.getSelectedItem().toString(),
                                Double.parseDouble(editLat.getText().toString()),
                                Double.parseDouble(editLong.getText().toString()),
                                editMail.getText().toString(),
                                editZipCode.getText().toString(),
                                editSchedule.getText().toString(),
                                editPhone.getText().toString(),
                                editCity.getText().toString());
                        buttonValid.setEnabled(false);
                        schoolService.updateEcole(newSchool.getId(), newSchool).enqueue(new Callback<JsonResponseSchool>() {
                            @Override
                            public void onResponse(Call<JsonResponseSchool> call, Response<JsonResponseSchool> response) {

                                if (response.code() == 200) {
                                    //On renvoie la nouvelle école et on quitte l'application
                                    Toast.makeText(UpdateActivity.this, "School modifiée", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent();
                                    i.putExtra("school", newSchool);
                                    setResult(1, i);
                                    finish();
                                } else {
                                    Toast.makeText(UpdateActivity.this, "Erreur : " + response.body().getError(), Toast.LENGTH_LONG).show();
                                }
                                buttonValid.setEnabled(true);
                            }

                            @Override
                            public void onFailure(Call<JsonResponseSchool> call, Throwable t) {
                                Toast.makeText(UpdateActivity.this, "Erreur, impossible de contacter le back", Toast.LENGTH_LONG).show();
                                buttonValid.setEnabled(true);
                            }
                        });
                    }

                    break;
            }
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onFragmentInteraction(String test) {

    }
}
