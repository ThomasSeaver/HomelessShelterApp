package org.team69.homelessshelterapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.team69.homelessshelterapp.R;
import org.team69.homelessshelterapp.model.Shelter;
import org.team69.homelessshelterapp.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


/**
 * Created by TomStuff on 3/6/18.
 */

public class DetailActivity extends AppCompatActivity {

    private static ArrayList<Shelter> shelterList;
    private static int shelterNum;
    private Shelter shelter;
    private Button cancelButton;
    private HashMap<String, String> theMap;
    private Button doneButton;
    private TextView shelterFullError;
    private TextView onlyOneShelterError;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_screen);

        shelterFullError = findViewById(0);
        shelterFullError.setVisibility(View.INVISIBLE);
        onlyOneShelterError = findViewById(0);
        onlyOneShelterError.setVisibility(View.INVISIBLE);

        doneButton = findViewById(0);
        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkDone();
            }
        });

        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goBackToShelterList();
            }
        });

        Intent intent = getIntent();
        theMap = (HashMap<String, String>) intent.getSerializableExtra("map");
        shelterList = (ArrayList<Shelter>) intent.getSerializableExtra("shelterList");
        shelterNum = (int) intent.getSerializableExtra("shelterNum");
        shelter = shelterList.get(shelterNum);
        populateDetails();

    }

    private void populateDetails() {
        TextView shelterName = findViewById(R.id.shelterName);
        TextView capacity = findViewById(R.id.capacityValue);
        TextView gender = findViewById(R.id.genderValue);
        TextView longitude = findViewById(R.id.longitudeValue);
        TextView latitude = findViewById(R.id.latitudeValue);
        TextView address = findViewById(R.id.addressValue);
        TextView phoneNum = findViewById(R.id.phoneNumberValue);

        String curCapacity = shelter.getCapacity().equals("Not available") ? shelter.getCapacity() : String.valueOf(Integer.parseInt(shelter.getCapacity()) - Integer.parseInt(shelter.getClaimedRooms()));

        shelterName.setText(shelter.getName());
        capacity.setText(curCapacity);
        gender.setText(shelter.getGender());
        longitude.setText(shelter.getLongitude());
        latitude.setText(shelter.getLatitude());
        address.setText(shelter.getAddress());
        phoneNum.setText(shelter.getPhoneNumber());

    }

    private void goBackToShelterList() {
        Intent intent = new Intent(getBaseContext(), ShelterListActivity.class);
        intent.putExtra("map", theMap);
        startActivity(intent);
    }

    private void checkDone() {
        //check if user already has beds claimed (check where they have them claimed)

        //check if user claim is more than vacancies

        //update this shelters vacancy if no errors
    }

}
