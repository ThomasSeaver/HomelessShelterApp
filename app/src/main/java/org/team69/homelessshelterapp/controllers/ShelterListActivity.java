package org.team69.homelessshelterapp.controllers;

import android.content.Intent;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by TomStuff on 3/6/18.
 */

public class ShelterListActivity extends AppCompatActivity {

    private Button logoutButton;
    private Button seachButton;
    private RecyclerView listView;
    private HashMap<String, String> theMap;
    private HashMap<String, String> restrictionsMap;
    private ShelterList list = new ShelterList();
    private User theUser;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shelterlist_screen);

        logoutButton =  findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToLogin();
            }
        });

        seachButton =  findViewById(R.id.searchButton);
        seachButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToSearch();
            }
        });

        Intent intent = getIntent();
        theMap = (HashMap<String, String>) intent.getSerializableExtra("map");
        restrictionsMap = (HashMap<String, String>) intent.getSerializableExtra("restrictionsMap");
        theUser = (User) intent.getSerializableExtra("theUser");

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
            ShelterListAdapter adapter = new ShelterListAdapter(list.getMap(), theMap);
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
            ShelterListAdapter adapter = new ShelterListAdapter(list.getByRestriction(restrictionsMap.get("Gender"), restrictionsMap.get("AgeRange"), restrictionsMap.get("ShelterName")), theMap);
            listView.setAdapter(adapter);
        }
    }
    private void goToSearch() {
        Intent intent = new Intent(getBaseContext(), SearchActivity.class);
        intent.putExtra("map", theMap);
        intent.putExtra("theUser", theUser);
        startActivity(intent);
    }

    private void goToLogin() {
        Intent intent = new Intent(getBaseContext(), WelcomeActivity.class);
        intent.putExtra("map", theMap);
        startActivity(intent);
    }

    private void readShelterFile() {

        try {
            String filePath = this.getFilesDir().getPath().toString() + "/homeless_shelter_database.csv";
            File csv = new File(filePath);
            if (!csv.exists()) {
                try {
                    InputStream is = getResources().openRawResource(R.raw.homeless_shelter_database);
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                    String line;
                    br.readLine();
                    while ((line = br.readLine()) != null) {
                        String[] traits = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                        for (int i = 0; i < traits.length; i++) {
                            if(traits[i] == null || traits[i].length() == 0) {
                                traits[i] = "Not available";
                            } else if (traits[i].charAt(0) == '"' && traits[i].charAt(traits[i].length() - 1) == '"'){
                                traits[i] = traits[i].substring(1, traits[i].length() - 1);
                            }
                        }
                        list.addShelter(traits[0], new Shelter(traits[1], traits[2], traits[3], traits[4], traits[5], traits[6], traits[8], traits.length > 9 ? traits[9] : "Not available"));
                    }
                    br.close();
                } catch (IOException e) {
                }
                try {
                    //make writer, append set to true
                    CSVWriter writer = new CSVWriter(new FileWriter(filePath));
                    for (Map.Entry<String, Shelter> shelter : ShelterList.getMap().entrySet())
                    {
                        //form
                        String [] record = (shelter.getKey() + "," + shelter.getValue().getRecord()).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
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
                while ((traits = csvReader.readNext()) != null) {
                    list.addShelter(traits[0], new Shelter(traits[1], traits[2], traits[3], traits[4], traits[5], traits[6], traits[7], traits.length > 8 ? traits[8] : "Not available"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
