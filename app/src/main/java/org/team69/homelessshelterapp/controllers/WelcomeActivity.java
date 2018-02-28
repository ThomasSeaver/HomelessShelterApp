package org.team69.homelessshelterapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.team69.homelessshelterapp.R;
import org.team69.homelessshelterapp.model.UserPassMap;

import java.util.HashMap;

/**
 * Created by obecerra on 2/19/18.
 */

public class WelcomeActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registrationButton;
    private HashMap<String, String> theMap;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        loginButton =  findViewById(R.id.button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToLogin();
            }
        });

        registrationButton = findViewById(R.id.button2);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToRegistration();
            }
        });

        Intent intent = getIntent();
        theMap = (HashMap<String, String>) intent.getSerializableExtra("map");
    }

    private void goToRegistration() {
        Intent intent = new Intent(getBaseContext(), RegistrationActivity.class);
        intent.putExtra("map", theMap);
        startActivity(intent);
    }

    private void goToLogin() {
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        intent.putExtra("map", theMap);
        startActivity(intent);
    }

}
