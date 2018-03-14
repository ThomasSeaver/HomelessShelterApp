package org.team69.homelessshelterapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.widget.Spinner;

import org.team69.homelessshelterapp.R;

import java.util.HashMap;

/**
 * Created by obecerra on 3/13/18.
 */

public class SearchActivity extends AppCompatActivity {

    private Button doneButton;
    private Button cancelButton;
    private HashMap<String, String> theMap;
    private EditText shelterName;
    private Spinner ageRange;
    private Spinner gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);

        shelterName = (EditText) findViewById(R.id.shelterNameText);

        ageRange = (Spinner) findViewById(R.id.ageRangeSpinner);
        String[] possibleValues1 = new String[4];
        possibleValues1[0] = "Families w/ newborns";
        possibleValues1[1] = "Children";
        possibleValues1[2] = "Young Adults";
        possibleValues1[3] = "Anyone";
        ArrayAdapter<String> adapter1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item, possibleValues1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageRange.setAdapter(adapter1);

        gender = (Spinner) findViewById(R.id.genderSpinner);
        String[] possibleValues2 = new String[2];
        possibleValues2[0] = "Men";
        possibleValues2[1] = "Women";
        ArrayAdapter<String> adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item, possibleValues2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter2);

        doneButton =  findViewById(R.id.searchDone);
        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkSearch();
            }
        });

        cancelButton =  findViewById(R.id.cancelDone);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                backToShelterListActivity();
            }
        });

        Intent intent = getIntent();
        theMap = (HashMap<String, String>) intent.getSerializableExtra("map");

    }

    private void checkSearch() {
        Intent intent = new Intent(getBaseContext(), ShelterListActivity.class);

        HashMap<String, String> newMap = new HashMap<>();
        newMap.put("AgeRange", ageRange.getSelectedItem().toString());
        newMap.put("ShelterName", shelterName.getText().toString());
        newMap.put("Gender", gender.getSelectedItem().toString());

        intent.putExtra("restrictionsMap", newMap);
        startActivity(intent);
    }

    private void backToShelterListActivity() {
        Intent intent = new Intent(getBaseContext(), ShelterListActivity.class);
        intent.putExtra("map", theMap);
        startActivity(intent);
    }

}
