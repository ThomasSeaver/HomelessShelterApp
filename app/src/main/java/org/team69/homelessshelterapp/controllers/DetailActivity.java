package org.team69.homelessshelterapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import org.team69.homelessshelterapp.R;
import org.team69.homelessshelterapp.model.Shelter;
import org.team69.homelessshelterapp.model.ShelterList;
import org.team69.homelessshelterapp.model.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


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
    private String userID;
    private EditText bedsToClaim;
    private Map<String, User> userList = new HashMap<>();
    private ShelterList list = new ShelterList();
    private User theUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_screen);

        bedsToClaim = findViewById(R.id.numBeds);

        shelterFullError = findViewById(R.id.shelterFullError);
        shelterFullError.setVisibility(View.INVISIBLE);
        onlyOneShelterError = findViewById(R.id.onlyOneShelterError);
        onlyOneShelterError.setVisibility(View.INVISIBLE);

        doneButton = findViewById(R.id.doneButton);
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
        userID = intent.getStringExtra("userID");
        readShelterFile();
        readUserFile();

        theUser = userList.get(userID);
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
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    private void checkDone() {
        //check if user already has beds claimed (check where they have them claimed)
        if (!theUser.getShelterClaimedID().equals(String.valueOf(shelterNum))
                && (!theUser.getShelterClaimedID().equals("-1"))
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
            writeNewShelterInfo(shelter);
            writeNewUserInfo();
            //SEND THE INTENT?
            goBackToShelterList();
        } else {
            shelter.claimRooms(Integer.parseInt(bedsToClaim.getText().toString()));
            theUser.setBedsClaimed(Integer.parseInt(bedsToClaim.getText().toString()));
            theUser.setShelterClaimedID(shelter.getName());
            writeNewShelterInfo(shelter);
            writeNewUserInfo();
            //SEND THE INTENT?
            goBackToShelterList();
        }
    }

    private void writeNewShelterInfo(Shelter shelterChange) {
        try {
            String filePath = this.getFilesDir().getPath().toString() + "/homeless_shelter_database.csv";
            //make writer
            CSVWriter writer = new CSVWriter(new FileWriter(filePath));
            for (Map.Entry<String, Shelter> shelter : ShelterList.getMap().entrySet())
            {
                if (String.valueOf(shelterNum) == shelter.getKey()) {
                    String[] record = (String.valueOf(shelterNum) + "," + shelterChange.getRecord()).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    writer.writeNext(record);

                } else {
                    String[] record = (shelter.getKey() + "," + shelter.getValue().getRecord()).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    writer.writeNext(record);
                }
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

}


    private void writeNewUserInfo() {
        try {
            //get file in memory
            String filePath = this.getFilesDir().getPath().toString() + "/user_pass_database.csv";
            //make writer, append set to true
            CSVWriter writer = new CSVWriter(new FileWriter(filePath));
            for (Map.Entry<String, User> user : userList.entrySet())
            {
                if (user.getKey().equals(userID)) {
                    //form
                    String [] record = (String.valueOf(userID) + "," + theUser.getUsername() + "," + theUser.getPassword() + "," + theUser.getShelterClaimedID() + "," + theUser.getBedsClaimed()).split(",");
                    writer.writeNext(record);
                } else {
                    //form
                    String [] record = (user.getKey() + "," + user.getValue().getUsername() + "," + user.getValue().getPassword() + "," + user.getValue().getShelterClaimedID() + "," + user.getValue().getBedsClaimed()).split(",");
                    writer.writeNext(record);
                }
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readUserFile() {

        try {
            String filePath = this.getFilesDir().getPath().toString() + "/user_pass_database.csv";
            Reader reader =  new BufferedReader(new FileReader(filePath));
            CSVReader csvReader = new CSVReader(reader);
            String traits[];
            while ((traits = csvReader.readNext()) != null) {
                userList.put(traits[0], new User(traits[1], traits[2], traits[3], Integer.parseInt(traits[4])));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void readShelterFile() {

        try {
            String filePath = this.getFilesDir().getPath().toString() + "/homeless_shelter_database.csv";
            File csv = new File(filePath);
            Reader reader =  new BufferedReader(new FileReader(csv.getPath()));
            CSVReader csvReader = new CSVReader(reader);
            String traits[];
            while ((traits = csvReader.readNext()) != null) {
                list.addShelter(traits[0], new Shelter(traits[1], traits[2], traits[3], traits[4], traits[5], traits[6], traits[7], traits.length > 8 ? traits[8] : "Not available"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}




