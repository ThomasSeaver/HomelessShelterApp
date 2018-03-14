package org.team69.homelessshelterapp.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.util.Map;
import java.util.HashMap;

import org.team69.homelessshelterapp.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

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
        if (checkUsingMap(usernameInput.getText().toString(), passwordInput.getText().toString())) {
            wrongLogin.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(getBaseContext(), ShelterListActivity.class);
            intent.putExtra("map", theMap);
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
}
