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
 * Registration activity for users needing to create their account for the first time. Creates user
 * and writes them to the user database stored on phone
 *
 * Created by obecerra on 2/19/18.
 */

public class RegistrationActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private final Map<String, User> userList = new HashMap<>();
    private int lastUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        lastUserID = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_screen);

        readUserFile();

        Button doneButton = findViewById(R.id.RegistrationDoneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable usernameObj = usernameInput.getText();
                Editable passwordObj = passwordInput.getText();
                if (!("").equals(usernameObj.toString())
                        && !("").equals(passwordObj.toString())) {
                    createUser();
                    goBackToLogin();
                }
            }
        });

        Button cancelButton = findViewById(R.id.RegistrationCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToWelcome();
            }
        });

        usernameInput = findViewById(R.id.RegistrationUsernameText);
        passwordInput = findViewById(R.id.RegistrationPasswordText);

        Spinner adminOrUserSpinner = findViewById(R.id.AdminOrUserSpinner);
        String[] possibleValues = new String[2];
        possibleValues[0] = "User";
        possibleValues[1] = "Admin";
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, possibleValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adminOrUserSpinner.setAdapter(adapter);
        Intent intent = getIntent();
    }

    private void createUser() {
        Editable usernameObj = usernameInput.getText();
        if (userNameAvailable(usernameObj.toString())) {
            try {
                //get file in memory
                File fileDir = this.getFilesDir();
                String filePath = fileDir.getPath() + "/user_pass_database.csv";
                //make writer, append set to true
                CSVWriter writer = new CSVWriter(new FileWriter(filePath, true));
                //form
                Editable username = usernameInput.getText();
                Editable password = passwordInput.getText();
                String [] record = (String.valueOf(lastUserID) + ","
                        + username.toString() + ","
                        + password.toString() + ",-1,0").split(",");
                writer.writeNext(record);

                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean userNameAvailable(String name) {
        for (User user : userList.values()) {
            String username = user.getUsername();
            if (username.equals(name)) {
                return false;
            }
        }
        return true;
    }

    private void goBackToLogin() {
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(intent);
    }
    private void goBackToWelcome() {
        Intent intent = new Intent(getBaseContext(), WelcomeActivity.class);
        startActivity(intent);
    }

    private void readUserFile() {

        try {
            File fileDir = this.getFilesDir();
            String filePath = fileDir.getPath() + "/user_pass_database.csv";
            File csv = new File(filePath);
            if (csv.exists()) {
                try {
                    csv.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Reader reader =  new BufferedReader(new FileReader(csv.getPath()));
            CSVReader csvReader = new CSVReader(reader);
            String traits[];
            traits = csvReader.readNext();
            while (traits != null) {
                userList.put(traits[0], new User(traits[1], traits[2], traits[3],
                        Integer.parseInt(traits[4])));
                lastUserID++;
                traits = csvReader.readNext();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
