package org.team69.homelessshelterapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
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
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;


/**
 * Detail activity created for individual shelters when list is pressed for single shelter info
 * Created by TomStuff on 3/6/18.
 */

public class DetailActivity extends AppCompatActivity {

    private static int shelterNum;
    private Shelter shelter;
    private TextView shelterFullError;
    private TextView onlyOneShelterError;
    private String userID;
    private EditText bedsToClaim;
    private final Map<String, User> userList = new HashMap<>();
    private final ShelterList list = new ShelterList();
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

        Button doneButton = findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDone();
            }
        });

        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToShelterList();
            }
        });

        Intent intent = getIntent();
        //should be able to just use this as an int but comes out null I don't know intents what lol
        shelterNum = (int) intent.getSerializableExtra("shelterNum");
        Log.d("shelterNum", String.valueOf(shelterNum));
        //subtract by 2 because initial pos is 2 higher than it should be im not sure why
        shelterNum -= 2;
        if(shelterNum == -1) {
            shelterNum = 12;
        }
        if(shelterNum == -2) {
            shelterNum = 11;
        }
        Log.d("shelterNum", String.valueOf(shelterNum));
        userID = intent.getStringExtra("userID");
        readShelterFile();
        readUserFile();

        theUser = userList.get(userID);
        shelter = list.findShelterByID(String.valueOf(shelterNum));
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

        String curCapacity = "Not available".equals(shelter.getCapacity())
                ? shelter.getCapacity() : String.valueOf(Integer.parseInt(shelter.getCapacity())
                - Integer.parseInt(shelter.getClaimedRooms()));

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
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    private void checkDone() {
        //check if user already has beds claimed (check where they have them claimed)
        String shelterID = theUser.getShelterClaimedID();
        Editable bedsClaiming = bedsToClaim.getText();
        if (!shelterID.equals(String.valueOf(shelterNum))
                && (!"-1".equals(theUser.getShelterClaimedID()))
                && (Integer.parseInt(bedsClaiming.toString()) != 0)) {
            shelterFullError.setVisibility(View.INVISIBLE);
            onlyOneShelterError.setVisibility(View.VISIBLE);
            return;
        }

        //check if shelter has no capacity specified
        if ("Not available".equals(shelter.getCapacity())) {
            shelterFullError.setVisibility(View.VISIBLE);
            onlyOneShelterError.setVisibility(View.INVISIBLE);
            return;
        }

        //check if user claim is more than vacancies
        int vacancy = Integer.parseInt(shelter.getCapacity())
                - Integer.parseInt(shelter.getClaimedRooms());

        if (Integer.parseInt(bedsClaiming.toString()) > vacancy) {
            shelterFullError.setVisibility(View.VISIBLE);
            onlyOneShelterError.setVisibility(View.INVISIBLE);
            return;
        }

        //update this shelters vacancy if no errors and the user
        if (Integer.parseInt(bedsClaiming.toString()) == 0) {
            shelter.releaseRooms(theUser.getBedsClaimed());
            theUser.setBedsClaimed(0);
            theUser.setShelterClaimedID("-1");
            writeNewShelterInfo(shelter);
            writeNewUserInfo();
            goBackToShelterList();
        } else {
            int curClaim = theUser.getBedsClaimed();
            if (curClaim < Integer.parseInt(bedsClaiming.toString())) {
                int toClaim = Integer.parseInt(bedsClaiming.toString()) - curClaim;
                shelter.claimRooms(toClaim);
            } else if (curClaim > Integer.parseInt(bedsClaiming.toString())){
                int toRelease = curClaim - Integer.parseInt(bedsClaiming.toString());
                shelter.releaseRooms(toRelease);
            }
            theUser.setBedsClaimed(Integer.parseInt(bedsClaiming.toString()));
            theUser.setShelterClaimedID(String.valueOf(shelterNum));
            writeNewShelterInfo(shelter);
            writeNewUserInfo();
            goBackToShelterList();
        }
    }

    private void writeNewShelterInfo(Shelter shelterChange) {
        try {
            File fileDir = this.getFilesDir();
            String filePath = fileDir.getPath() + "/homeless_shelter_database.csv";
            //make writer
            CSVWriter writer = new CSVWriter(new FileWriter(filePath));
            String regex = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
            Map<String, Shelter> map = ShelterList.getMap();
            for (Map.Entry<String, Shelter> shelter : map.entrySet())
            {
                String shelterID = String.valueOf(shelterNum);
                Shelter value = shelter.getValue();
                if (shelterID.equals(shelter.getKey())) {
                    String[] record = (shelterID + ","
                            + shelterChange.getRecord()).split(regex);
                    writer.writeNext(record);

                } else {
                    String[] record = (shelter.getKey() + ","
                            + value.getRecord()).split(regex);
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
            File fileDir = this.getFilesDir();
            String filePath = fileDir.getPath() + "/user_pass_database.csv";
            //make writer, append set to true
            CSVWriter writer = new CSVWriter(new FileWriter(filePath));
            for (Map.Entry<String, User> user : userList.entrySet())
            {
                String key = user.getKey();
                if (key.equals(userID)) {
                    //form
                    String [] record = (String.valueOf(userID) + "," + theUser.getUsername() + ","
                            + theUser.getPassword() + "," + theUser.getShelterClaimedID() + ","
                            + theUser.getBedsClaimed()).split(",");
                    writer.writeNext(record);
                } else {
                    //form
                    User value = user.getValue();
                    String [] record = (user.getKey() + "," + value.getUsername() + ","
                            + value.getPassword() + ","
                            + value.getShelterClaimedID() + ","
                            + value.getBedsClaimed()).split(",");
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
            File fileDir = this.getFilesDir();
            String filePath = fileDir.getPath() + "/user_pass_database.csv";
            Reader reader =  new BufferedReader(new FileReader(filePath));
            CSVReader csvReader = new CSVReader(reader);
            String traits[];
            traits = csvReader.readNext();
            while (traits != null) {
                userList.put(traits[0], new User(traits[1], traits[2], traits[3],
                        Integer.parseInt(traits[4])));
                traits = csvReader.readNext();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void readShelterFile() {

        try {
            File fileDir = this.getFilesDir();
            String filePath = fileDir.getPath() + "/homeless_shelter_database.csv";
            File csv = new File(filePath);
            Reader reader =  new BufferedReader(new FileReader(csv.getPath()));
            CSVReader csvReader = new CSVReader(reader);
            String traits[];
            traits = csvReader.readNext();
            while (traits != null) {
                list.addShelter(traits[0], new Shelter(traits[1], traits[2], traits[3], traits[4],
                        traits[5], traits[6], traits[7],
                        (traits.length > 8) ? traits[8] : "Not available"));
                traits = csvReader.readNext();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}




