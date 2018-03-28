package org.team69.homelessshelterapp.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

import java.nio.charset.StandardCharsets;
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
        /*if (checkUsingMap(usernameInput.getText().toString(), passwordInput.getText().toString())) {
            wrongLogin.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(getBaseContext(), ShelterListActivity.class);
            intent.putExtra("map", theMap);
            //get the user details
            startActivity(intent);
        }*/
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

    private boolean checkUsingMap(String user, String pass) {
        if (theMap == null) {
            return false;
        } else if (theMap.get(user) != null && theMap.get(user).equals(pass)) {
            return true;
        }
        return false;
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
