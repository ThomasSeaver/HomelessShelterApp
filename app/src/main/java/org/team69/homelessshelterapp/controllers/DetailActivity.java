package org.team69.homelessshelterapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private User theUser;
    private EditText bedsToClaim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_screen);

        bedsToClaim = findViewById(R.id.numBeds);

        shelterFullError = findViewById(0);
        shelterFullError.setVisibility(View.INVISIBLE);
        onlyOneShelterError = findViewById(0);
        onlyOneShelterError.setVisibility(View.INVISIBLE);

        doneButton = findViewById(R.id.claimButton);
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
        theUser = (User) intent.getSerializableExtra("theUser");
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
        intent.putExtra("theUser", theUser);
        startActivity(intent);
    }

    private void checkDone() {
        //check if user already has beds claimed (check where they have them claimed)
        if (!theUser.getShelterClaimedID().equals(shelter.getName())
                && Integer.parseInt(bedsToClaim.getText().toString()) != 0) {
            shelterFullError.setVisibility(View.INVISIBLE);
            onlyOneShelterError.setVisibility(View.VISIBLE);
            return;
        }

        //check if shelter has no capacity specified
        if (shelter.getCapacity().equals("Not available")) {
            shelterFullError.setVisibility(View.VISIBLE);
            onlyOneShelterError.setVisibility(View.INVISIBLE);
            return;
        }

        //check if user claim is more than vacancies
        int vacancy = Integer.parseInt(shelter.getCapacity())
                - Integer.parseInt(shelter.getClaimedRooms());

        if (Integer.parseInt(bedsToClaim.getText().toString()) > vacancy) {
            shelterFullError.setVisibility(View.VISIBLE);
            onlyOneShelterError.setVisibility(View.INVISIBLE);
            return;
        }

        //update this shelters vacancy if no errors and the user
        if (Integer.parseInt(bedsToClaim.getText().toString()) == 0) {
            shelter.releaseRooms(theUser.getBedsClaimed());
            theUser.setBedsClaimed(0);
            theUser.setShelterClaimedID("");
            //SEND THE INTENT?
            goBackToShelterList();
        } else {
            shelter.claimRooms(Integer.parseInt(bedsToClaim.getText().toString()));
            theUser.setBedsClaimed(Integer.parseInt(bedsToClaim.getText().toString()));
            theUser.setShelterClaimedID(shelter.getName());
            //SEND THE INTENT?
            goBackToShelterList();
        }
    }

}
