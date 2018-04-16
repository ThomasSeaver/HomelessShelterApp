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
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);

        shelterName = findViewById(R.id.shelterNameText);

        ageRange = findViewById(R.id.ageRangeSpinner);
        String[] possibleValues1 = new String[4];
        possibleValues1[0] = "Families w/ newborns";
        possibleValues1[1] = "Children";
        possibleValues1[2] = "Young Adults";
        possibleValues1[3] = "Anyone";
        ArrayAdapter<String> adapter1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item, possibleValues1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageRange.setAdapter(adapter1);

        gender = findViewById(R.id.genderSpinner);
        String[] possibleValues2 = new String[3];
        possibleValues2[0] = "Men";
        possibleValues2[1] = "Women";
        possibleValues2[2] = "N/A";
        ArrayAdapter<String> adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item, possibleValues2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter2);

        doneButton =  findViewById(R.id.searchDone);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSearch();
            }
        });

        cancelButton =  findViewById(R.id.cancelDone);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToShelterListActivity();
            }
        });

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        theMap = (HashMap<String, String>) intent.getSerializableExtra("map");
    }

    private void checkSearch() {
        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
        String genderval = null;
        String ageval = null;
        String nameval = null;

        if (!gender.getSelectedItem().toString().equals("N/A")) {
            genderval = gender.getSelectedItem().toString();
        }
        if (!ageRange.getSelectedItem().toString().equals("Anyone")) {
            ageval = ageRange.getSelectedItem().toString();
        }
        if (shelterName.getText().toString().length() != 0) {
            nameval = shelterName.getText().toString();
        }

        HashMap<String, String> newMap = new HashMap<>();
        newMap.put("AgeRange", ageval);
        newMap.put("ShelterName", nameval);
        newMap.put("Gender", genderval);

        intent.putExtra("restrictionsMap", newMap);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    private void backToShelterListActivity() {
        Intent intent = new Intent(getBaseContext(), ShelterListActivity.class);
        intent.putExtra("map", theMap);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }


}
