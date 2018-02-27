package org.team69.homelessshelterapp.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.widget.Spinner;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import org.team69.homelessshelterapp.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_screen);


        doneButton =  findViewById(R.id.RegistrationDoneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (usernameInput.getText().toString() != null
                        && passwordInput.getText().toString() != null) {
                    createUser();
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
    }

    private void createUser() {
        try {
            FileInputStream fileInputStream = new FileInputStream("theUserPassMap.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            UserPassMap myNewlyReadInMap = (UserPassMap) objectInputStream.readObject();
            objectInputStream.close();

            myNewlyReadInMap.addTo(usernameInput.getText().toString(), passwordInput.getText().toString());

            FileOutputStream fileOutputStream = new FileOutputStream("myMap.whateverExtension");
            ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(myNewlyReadInMap);
            objectOutputStream.close();

        } catch (IOException i) {
            createMap();
        } catch (ClassNotFoundException c) {
            return;
        }
    }

    private void createMap() {
        try {
            UserPassMap newMap = new UserPassMap();
            newMap.addTo(usernameInput.getText().toString(), passwordInput.getText().toString());
            FileOutputStream fileOutputStream = new FileOutputStream("myMap.whateverExtension");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(newMap);
            objectOutputStream.close();
        } catch (IOException i) {
            return;
        }
    }

    private void goBackToWelcome() {
        Intent intent = new Intent(getBaseContext(), WelcomeActivity.class);
        startActivity(intent);
    }
}
