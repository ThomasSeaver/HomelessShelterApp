package org.team69.homelessshelterapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;

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

    private TextView usernameText;
    private TextView passwordText;
    private EditText usernameInput;
    private EditText passwordInput;
    private EditText wrongLogin;
    private EditText lockoutText;
    private Button doneButton;
    private Button cancelButton;
    private final Map<String, User> userList = new HashMap<>();
    private String userID;
    private int failedLogins = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        readUserFile();

        doneButton = findViewById(R.id.button3);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserPass();
            }
        });

        cancelButton = findViewById(R.id.button4);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToWelcome();
            }
        });

        usernameText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);

        usernameInput = findViewById(R.id.userBox);
        passwordInput = findViewById(R.id.passBox);
        wrongLogin = findViewById(R.id.wrongLoginText);
        lockoutText = findViewById(R.id.lockoutText);
    }

    private void checkUserPass() {
        Editable userNameObj = usernameInput.getText();
        Editable passwordObj = passwordInput.getText();
        if (checkUsingFile(userNameObj.toString(), passwordObj.toString())) {
            failedLogins = 0;
            wrongLogin.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(getBaseContext(), ShelterListActivity.class);
            //get the user detail
            intent.putExtra("userID", userID);
            startActivity(intent);
        } else {
            if (failedLogins < 3) {
                wrongLogin.setVisibility(View.VISIBLE);
                failedLogins++;
            } else {
                wrongLogin.setVisibility(View.INVISIBLE);
                lockoutText.setVisibility(View.VISIBLE);
                doneButton.setVisibility(View.INVISIBLE);
                cancelButton.setVisibility(View.INVISIBLE);
                usernameInput.setVisibility(View.INVISIBLE);
                passwordInput.setVisibility(View.INVISIBLE);
                usernameText.setVisibility(View.INVISIBLE);
                passwordText.setVisibility(View.INVISIBLE);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lockoutText.setVisibility(View.INVISIBLE);
                        doneButton.setVisibility(View.VISIBLE);
                        cancelButton.setVisibility(View.VISIBLE);
                        usernameInput.setVisibility(View.VISIBLE);
                        passwordInput.setVisibility(View.VISIBLE);
                        usernameText.setVisibility(View.VISIBLE);
                        passwordText.setVisibility(View.VISIBLE);
                        failedLogins = 0;
                    }
                }, 15000);
            }
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
