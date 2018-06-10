package com.example.nathan.schoolmollenathan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by Nathan on 10/06/2018.
 */

public class DetailActivity extends AppCompatActivity implements ActionBar.OnFragmentInteractionListener, AdapterView.OnItemSelectedListener {


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

    private Button buttonValid;
    private Button buttonCancel;


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

        editName = findViewById(R.id.inputName);
        editaddress = findViewById(R.id.inputAddress);
        editNbStud = findViewById(R.id.inputNbStud);
        editLat = findViewById(R.id.inputLat);
        editLong = findViewById(R.id.inputLong);
        editMail = findViewById(R.id.inputMail);
        editPostal = findViewById(R.id.inputCode);
        editHoraires = findViewById(R.id.inputTimes);
        editTel = findViewById(R.id.inputPhone);
        editCommune = findViewById(R.id.inputCity);
        buttonValid = findViewById(R.id.btnReset);
        buttonCancel = findViewById(R.id.btnCancel);

        Fragment actionBar = new ActionBar();
        Bundle arguments = new Bundle();
        Intent i = getIntent();
        school = (School) i.getSerializableExtra("school");

        arguments.putString("title", "Détail " + school.getName());
        arguments.putString("menu", "ecoleDétail");
        arguments.putSerializable("school", school);
        this.populateDatas(school, adapter);

        actionBar.setArguments(arguments);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, actionBar).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return true;
    }

    private void populateDatas(School school, ArrayAdapter<CharSequence> adapter) {

        editName.setEnabled(false);
        editaddress.setEnabled(false);
        editNbStud.setEnabled(false);
        editLat.setEnabled(false);
        editLong.setEnabled(false);
        editMail.setEnabled(false);
        editPostal.setEnabled(false);
        editHoraires.setEnabled(false);
        editTel.setEnabled(false);
        editCommune.setEnabled(false);
        editType.setEnabled(false);


        buttonValid.setEnabled(false);
        buttonCancel.setEnabled(false);

        editName.setText(school.getName());
        editaddress.setText(school.getAddress());
        editNbStud.setText(Integer.toString(school.getNb_student()));
        editLat.setText(Double.toString(school.getLatitude()));
        editLong.setText(Double.toString(school.getLongitude()));
        editMail.setText(school.getMail());
        editPostal.setText(school.getPostal_code());
        editHoraires.setText(school.getSchedule());
        editTel.setText(school.getPhone_number());
        editCommune.setText(school.getCity());
        editType.setSelection(adapter.getPosition(school.getStatus()));


    }

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
