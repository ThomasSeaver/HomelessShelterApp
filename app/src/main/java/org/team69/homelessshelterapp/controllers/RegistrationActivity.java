package org.team69.homelessshelterapp.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.widget.Spinner;


import org.team69.homelessshelterapp.R;

/**
 * Created by obecerra on 2/19/18.
 */

public class RegistrationActivity extends AppCompatActivity {

    private Button doneButton;
    private Button cancelButton;
    private EditText usernameInput;
    private EditText passwordInput;
    private EditText idInput;
    private Spinner adminOrUser;
    private EditText wrongLogin;

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
//        idInput = (EditText) findViewById(R.id.idBox);
//        adminOrUser = (Spinner) findViewById(R.id.spinner);
        wrongLogin = (EditText) findViewById(R.id.wrongLoginText);

    }

    private void checkUserPass() {
        if (usernameInput.getText().toString().equals("") &&
                passwordInput.getText().toString().equals("") &&
                idInput.getText().toString().equals("") ) {
            wrongLogin.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(getBaseContext(), EmptyAppActivity.class);
            startActivity(intent);
        } else {
            wrongLogin.setVisibility(View.VISIBLE);
        }
    }

    private void goBackToWelcome() {
        Intent intent = new Intent(getBaseContext(), WelcomeActivity.class);
        startActivity(intent);
    }
}
