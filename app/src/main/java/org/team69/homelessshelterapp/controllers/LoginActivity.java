package org.team69.homelessshelterapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.io.Reader;
import java.util.Map;
import java.util.HashMap;

import org.team69.homelessshelterapp.R;
import org.team69.homelessshelterapp.model.User;

/**
 * Login activity for users who have previously created accounts
 * Created by obecerra on 2/19/18.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private EditText wrongLogin;
    private final Map<String, User> userList = new HashMap<>();
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        readUserFile();

        Button doneButton = findViewById(R.id.button3);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserPass();
            }
        });

        Button cancelButton = findViewById(R.id.button4);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToWelcome();
            }
        });

        usernameInput = findViewById(R.id.userBox);
        passwordInput = findViewById(R.id.passBox);
        wrongLogin = findViewById(R.id.wrongLoginText);

    }

    private void checkUserPass() {
        Editable userNameObj = usernameInput.getText();
        Editable passwordObj = passwordInput.getText();
        if (checkUsingFile(userNameObj.toString(), passwordObj.toString())){
            wrongLogin.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(getBaseContext(), ShelterListActivity.class);
            //get the user detail
            intent.putExtra("userID", userID);
            startActivity(intent);
        } else {
            wrongLogin.setVisibility(View.VISIBLE);
        }
    }

    private void goBackToWelcome() {
        Intent intent = new Intent(getBaseContext(), WelcomeActivity.class);
        startActivity(intent);
    }

    private boolean checkUsingFile(String username, String pass) {
        for (Map.Entry<String, User> user : userList.entrySet())
        {
            User value = user.getValue();
            String checkUserName = value.getUsername();
            String checkPassword = value.getPassword();
            if (checkUserName.equals(username) && checkPassword.equals(pass)) {
                userID = user.getKey();
                return true;
            }
        }
        return false;
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
}
