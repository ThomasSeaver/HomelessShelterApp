package org.team69.homelessshelterapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.HashMap;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import org.team69.homelessshelterapp.R;
import org.team69.homelessshelterapp.model.User;
/**
 * Created by obecerra on 2/19/18.
 */

public class RegistrationActivity extends AppCompatActivity {

    private Button doneButton;
    private Button cancelButton;
    private EditText usernameInput;
    private EditText passwordInput;
    private Spinner adminOrUserSpinner;
    private HashMap<String, String> theMap;
    private Map<String, User> userList = new HashMap<>();
    private int lastUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        lastUserID = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_screen);

        readUserFile();

        doneButton =  findViewById(R.id.RegistrationDoneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (usernameInput.getText().toString() != null
                        && passwordInput.getText().toString() != null) {
                    createUser();
                    goBackToLogin();
                }
            }
        });

        cancelButton = findViewById(R.id.RegistrationCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goBackToWelcome();
            }
        });

        usernameInput = (EditText) findViewById(R.id.RegistrationUsernameText);
        passwordInput = (EditText) findViewById(R.id.RegistrationPasswordText);

        adminOrUserSpinner = (Spinner) findViewById(R.id.AdminOrUserSpinner);
        String[] possibleValues = new String[2];
        possibleValues[0] = "User";
        possibleValues[1] = "Admin";
        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, possibleValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adminOrUserSpinner.setAdapter(adapter);
        Intent intent = getIntent();
        theMap = (HashMap<String, String>) intent.getSerializableExtra("map");
    }

    private void createUser() {
        if (!userNameAvailable(usernameInput.getText().toString())) {
            //if username already taken
        } else {
            try {
                //get file in memory
                String filePath = this.getFilesDir().getPath().toString() + "/user_database.csv";
                //make writer, append set to true
                CSVWriter writer = new CSVWriter(new FileWriter(filePath, true));
                //form
                String [] record = (String.valueOf(lastUserID) + "," + usernameInput.getText().toString() + "," + passwordInput.getText().toString() + ",,0").split(",");
                writer.writeNext(record);

                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean userNameAvailable(String name) {
        for (User user : userList.values()) {
            if (user.getUsername().equals(name)) {
                return false;
            }
        }
        return true;
    }

    private void goBackToLogin() {
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        intent.putExtra("map", theMap);
        startActivity(intent);
    }
    private void goBackToWelcome() {
        Intent intent = new Intent(getBaseContext(), WelcomeActivity.class);
        intent.putExtra("map", theMap);
        startActivity(intent);
    }

    private void readUserFile() {

        try {
            String filePath = this.getFilesDir().getPath().toString() + "/user_database.csv";
            File csv = new File(filePath);
            if (!csv.exists()) {
                try {
                    csv.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Reader reader =  new BufferedReader(new FileReader(csv.getPath()));
            CSVReader csvReader = new CSVReader(reader);
            String traits[];
            while ((traits = csvReader.readNext()) != null) {
                userList.put(traits[0], new User(traits[1], traits[2]));
                lastUserID++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
