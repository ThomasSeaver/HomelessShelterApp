package org.team69.homelessshelterapp.controllers;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import android.content.Context;


import org.team69.homelessshelterapp.R;
import org.team69.homelessshelterapp.model.Shelter;
import org.team69.homelessshelterapp.model.User;
import org.team69.homelessshelterapp.model.UserPassMap;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                File path = this.getFilesDir();
                File file = new File(path, "user-pass-data.txt");

                FileOutputStream stream = new FileOutputStream(file);
                String toWrite = usernameInput.getText().toString() + "," + passwordInput.getText().toString() + "," + "," + '\n';
                try {
                    stream.write(toWrite.getBytes());
                } finally {
                    stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //theMap.put(usernameInput.getText().toString(), passwordInput.getText().toString());
    }

    private boolean userNameAvailable(String name) {
        for (User user : userList.values()) {
            if (user.getUsername().equals(name)) {
                return false;
            }
        }
        return true;
    }

    private void createMap() {
        theMap = new HashMap<>();
        theMap.put(usernameInput.getText().toString(), passwordInput.getText().toString());
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
            File path = this.getFilesDir();
            File file = new File(path, "user-pass-data.txt");

            FileInputStream in = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
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
                userList.put(traits[0], new User(traits[1], traits[2]));
            }
            br.close();
        } catch (IOException e) {
        }
    }


}
