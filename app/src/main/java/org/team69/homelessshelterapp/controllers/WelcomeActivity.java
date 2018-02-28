package org.team69.homelessshelterapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.team69.homelessshelterapp.R;

/**
 * Created by obecerra on 2/19/18.
 */

public class WelcomeActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registrationButton;

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
    }

    private void goToRegistration() {
        Intent intent = new Intent(getBaseContext(), RegistrationActivity.class);
        startActivity(intent);
    }

    private void goToLogin() {
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(intent);
    }

}
