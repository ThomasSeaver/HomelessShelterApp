package org.team69.homelessshelterapp.controllers;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;


import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import org.team69.homelessshelterapp.R;
import org.team69.homelessshelterapp.model.Shelter;
import org.team69.homelessshelterapp.model.ShelterList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * List of shelters activity; holds individual shelter buttons in list for users to press to go
 * to detail activity to receive more individual information about shelters
 * Created by TomStuff on 3/6/18.
 */

public class ShelterListActivity extends AppCompatActivity {

    private final ShelterList list = new ShelterList();
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shelterlist_screen);

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });

        Button seachButton = findViewById(R.id.searchButton);
        seachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearch();
            }
        });

        Button mapButton = findViewById(R.id.showMap);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMap();
            }
        });

        Intent intent = getIntent();
        Map<String, String> restrictionsMap = (Map<String, String>)
                intent.getSerializableExtra("restrictionsMap");
        userID = intent.getStringExtra("userID");

        RecyclerView listView;
        if (restrictionsMap == null) {
            //copy shelter files into shelterlist and shelter models
            readShelterFile();

            //get handle for shelter recycler view
            listView = findViewById(R.id.listShelters);
            listView.setHasFixedSize(true); //increases performance

            //set layout
            LinearLayoutManager layout = new LinearLayoutManager(this);
            listView.setLayoutManager(layout);

            //set adapter
            ShelterListAdapter adapter = new ShelterListAdapter(ShelterList.getMap(), userID);
            listView.setAdapter(adapter);
        } else {
            //copy shelter files into shelterlist and shelter models
            readShelterFile();

            //get handle for shelter recycler view
            listView = findViewById(R.id.listShelters);
            listView.setHasFixedSize(true); //increases performance

            //set layout
            LinearLayoutManager layout = new LinearLayoutManager(this);
            listView.setLayoutManager(layout);

            //set adapter
            ShelterListAdapter adapter = new ShelterListAdapter(
                    list.getByRestriction(restrictionsMap.get("Gender"),
                            restrictionsMap.get("AgeRange"),
                            restrictionsMap.get("ShelterName")), userID);
            listView.setAdapter(adapter);
        }
    }

    private void goToMap() {
        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    private void goToSearch() {
        Intent intent = new Intent(getBaseContext(), SearchActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    private void goToLogin() {
        Intent intent = new Intent(getBaseContext(), WelcomeActivity.class);
        startActivity(intent);
    }

    private void readShelterFile() {

        try {
            File fileDir = this.getFilesDir();
            String filePath = fileDir.getPath() + "/homeless_shelter_database.csv";
            File csv = new File(filePath);
            if (!csv.exists()) {
                try {
                    Resources resources = getResources();
                    InputStream is = resources.openRawResource(R.raw.homeless_shelter_database);
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(is, StandardCharsets.UTF_8));
                    String line;
                    br.readLine();
                    line = br.readLine();
                    while (line != null) {
                        String[] traits = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                        for (int i = 0; i < traits.length; i++) {
                            if((traits[i] == null) || traits[i].isEmpty()) {
                                traits[i] = "Not available";
                            } else if ((traits[i].charAt(0) == '"')
                                    && (traits[i].charAt(traits[i].length() - 1) == '"')){
                                traits[i] = traits[i].substring(1, traits[i].length() - 1);
                            }
                        }
                        list.addShelter(traits[0], new Shelter(traits[1], traits[2], traits[3],
                                traits[4], traits[5], traits[6], traits[8],
                                (traits.length > 9) ? traits[9] : "Not available"));
                        line = br.readLine();
                    }
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    //make writer, append set to true
                    CSVWriter writer = new CSVWriter(new FileWriter(filePath));
                    String regex = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
                    Map<String, Shelter> shelterMap = ShelterList.getMap();
                    for (Map.Entry<String, Shelter> shelter : shelterMap.entrySet())
                    {
                        //form
                        Shelter value = shelter.getValue();
                        String [] record = (shelter.getKey() + ","
                                + value.getRecord()).split(regex);
                        writer.writeNext(record);
                    }
                    writer.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Reader reader =  new BufferedReader(new FileReader(csv.getPath()));
                CSVReader csvReader = new CSVReader(reader);
                String traits[];
                traits = csvReader.readNext();
                while (traits != null) {
                    list.addShelter(traits[0], new Shelter(traits[1], traits[2], traits[3],
                            traits[4], traits[5], traits[6], traits[7],
                            (traits.length > 8) ? traits[8] : "Not available"));
                    traits = csvReader.readNext();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
