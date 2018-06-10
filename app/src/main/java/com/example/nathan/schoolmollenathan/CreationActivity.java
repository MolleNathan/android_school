package com.example.nathan.schoolmollenathan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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

public class CreationActivity extends FragmentActivity implements ActionBar.OnFragmentInteractionListener, AdapterView.OnItemSelectedListener {

    private boolean modify;
    private String type = "creation";
    private SchoolService schoolService;
    private School school;

    ArrayAdapter<CharSequence> adapter;

    private EditText editName;
    private EditText editaddress;
    private EditText editNbStud;
    private EditText editLat;
    private EditText editLong;
    private EditText editMail;
    private EditText editPostal;
    private EditText editHoraires;
    private EditText editTel;
    private EditText editCommune;
    private Spinner editType;
    private ArrayList<EditText> listEdit = new ArrayList<EditText>();

    private Button buttonValid;
    private Button buttonCancel;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

        editType = findViewById(R.id.inputType);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editType.setAdapter(adapter);
        editType.setOnItemSelectedListener(this);
        editType.setSelection(0);


        schoolService = RequestInterface.getInterface(SchoolService.class);

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
        editPostal = findViewById(R.id.inputCode);
        listEdit.add(editPostal);
        editHoraires = findViewById(R.id.inputTimes);
        listEdit.add(editHoraires);
        editTel = findViewById(R.id.inputPhone);
        listEdit.add(editTel);
        editCommune = findViewById(R.id.inputCity);
        listEdit.add(editCommune);

        buttonValid = findViewById(R.id.btnReset);
        buttonCancel = findViewById(R.id.btnCancel);


        Fragment actionBar = new ActionBar();
        Bundle arguments = new Bundle();

        arguments.putString("title", "Création");
        arguments.putString("menu", "creation");

        actionBar.setArguments(arguments);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, actionBar).commit();

        buttonValid.setOnClickListener(genericOnClickListener);
        buttonCancel.setOnClickListener(genericOnClickListener);

    }

    final View.OnClickListener genericOnClickListener = new View.OnClickListener() {
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.btnReset:
                    boolean continu = true;
                    CharSequence errorMessage = getText(R.string.empty_champ);
                    for (EditText edit : listEdit) {
                        Log.e("text", edit.getText().toString());
                        if (edit.getText().toString().equals("")) {
                            continu = false;
                            edit.setError(errorMessage);
                        }
                    }
                    if (continu) {
                        buttonValid.setEnabled(false);
                        school = new School(
                                editName.getText().toString(),
                                editaddress.getText().toString(),
                                Integer.parseInt(editNbStud.getText().toString()),
                                editType.getSelectedItem().toString(),
                                Double.parseDouble(editLat.getText().toString()),
                                Double.parseDouble(editLong.getText().toString()),
                                editMail.getText().toString(),
                                editPostal.getText().toString(),
                                editHoraires.getText().toString(),
                                editTel.getText().toString(),
                                editCommune.getText().toString());
                        schoolService.saveEcole(school).enqueue(new Callback<JsonResponseSchool>() {
                            @Override
                            public void onResponse(Call<JsonResponseSchool> call, Response<JsonResponseSchool> response) {
                                if (response.code() == 201) {
                                    Toast.makeText(CreationActivity.this, "School crée", Toast.LENGTH_LONG);
                                    finish();
                                } else {
                                    Toast.makeText(CreationActivity.this, "Erreur : " + response.body().getError(), Toast.LENGTH_LONG);
                                }
                                buttonValid.setEnabled(true);
                            }

                            @Override
                            public void onFailure(Call<JsonResponseSchool> call, Throwable t) {
                                Toast.makeText(CreationActivity.this, "Erreur, impossible de contacter le back", Toast.LENGTH_LONG);
                                buttonValid.setEnabled(true);
                            }
                        });
                    }

                    break;

                case R.id.btnCancel:
                    finish();
                    break;
            }
        }
    };

    @Override
    public void onFragmentInteraction(String test) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
