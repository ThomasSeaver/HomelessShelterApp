package org.team69.homelessshelterapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.widget.Spinner;

import org.team69.homelessshelterapp.R;

import java.util.HashMap;

/**
 * Search activity for users who are searching for specific shelters
 *
 * Created by obecerra on 3/13/18.
 */

public class SearchActivity extends AppCompatActivity {

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
        ArrayAdapter adapter1 = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, possibleValues1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageRange.setAdapter(adapter1);

        gender = findViewById(R.id.genderSpinner);
        String[] possibleValues2 = new String[3];
        possibleValues2[0] = "Men";
        possibleValues2[1] = "Women";
        possibleValues2[2] = "N/A";
        ArrayAdapter adapter2 = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, possibleValues2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter2);

        Button doneButton = findViewById(R.id.searchDone);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSearch();
            }
        });

        Button cancelButton = findViewById(R.id.cancelDone);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToShelterListActivity();
            }
        });

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
    }

    private void checkSearch() {
        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
        String genderval = null;
        String ageval = null;
        String nameval = null;

        Object genderObj = gender.getSelectedItem();
        if (!"N/A".equals(genderObj.toString())) {
            genderval = genderObj.toString();
        }
        Object ageObject = ageRange.getSelectedItem();
        if (!"Anyone".equals(ageObject.toString())) {
            ageval = ageObject.toString();
        }
        Editable shelterObj = shelterName.getText();
        String shelterData = shelterObj.toString();
        if (!shelterData.isEmpty()) {
            nameval = shelterData;
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
        intent.putExtra("userID", userID);
        startActivity(intent);
    }


}
