package org.team69.homelessshelterapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.io.Reader;
import java.util.Map;
import java.util.HashMap;

import org.team69.homelessshelterapp.R;
import org.team69.homelessshelterapp.model.User;

/**
 * Created by obecerra on 2/19/18.
 */

public class LoginActivity extends AppCompatActivity {

    private Button doneButton;
    private Button cancelButton;
    private EditText usernameInput;
    private EditText passwordInput;
    private EditText wrongLogin;
    private HashMap<String, String> theMap;
    private Map<String, User> userList = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        readUserFile();

        doneButton =  findViewById(R.id.button3);
        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkUserPass();
            }
        });

        cancelButton = findViewById(R.id.button4);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goBackToWelcome();
            }
        });

        usernameInput = (EditText) findViewById(R.id.userBox);
        passwordInput = (EditText) findViewById(R.id.passBox);
        wrongLogin = (EditText) findViewById(R.id.wrongLoginText);

        Intent intent = getIntent();
        theMap = (HashMap<String, String>) intent.getSerializableExtra("map");
    }

    private void checkUserPass() {
        if (checkUsingFile(usernameInput.getText().toString(), passwordInput.getText().toString())) {
            wrongLogin.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(getBaseContext(), ShelterListActivity.class);
            //get the user details
            startActivity(intent);
        } else {
            wrongLogin.setVisibility(View.VISIBLE);
        }
    }

    private void goBackToWelcome() {
        Intent intent = new Intent(getBaseContext(), WelcomeActivity.class);
        intent.putExtra("map", theMap);
        startActivity(intent);
    }

    private boolean checkUsingFile(String username, String pass) {
        for (User user : userList.values()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(pass)) {
                return true;
            }
        }
        return false;
    }

    private void readUserFile() {

        try {
            String filePath = this.getFilesDir().getPath().toString() + "/user_database.csv";
            Reader reader =  new BufferedReader(new FileReader(filePath));
            CSVReader csvReader = new CSVReader(reader);
            String traits[];
            while ((traits = csvReader.readNext()) != null) {
                userList.put(traits[0], new User(traits[1], traits[2]));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
