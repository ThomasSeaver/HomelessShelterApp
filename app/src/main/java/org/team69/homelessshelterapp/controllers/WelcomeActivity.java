package org.team69.homelessshelterapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.team69.homelessshelterapp.R;

import java.io.File;
import java.util.HashMap;

/**
 * Created by obecerra on 2/19/18.
 */

public class WelcomeActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registrationButton;
    private HashMap<String, String> theMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        loginButton =  findViewById(R.id.button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });

        registrationButton = findViewById(R.id.button2);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegistration();
            }
        });


        //Use on startup to clear database files in case you messed something up
        //Uncomment, run once to get to welcome screen, close emulator, recomment, start again
        //Would work without restarting but would redelete everything if you ever come back to welcome
        //Only should use if you are having issues with users/shelter lists, or if you want to clear
        //debugClear();

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


    private void debugClear() {
        String filePath = this.getFilesDir().getPath().toString() + "/user_pass_database.csv";
        File csv = new File(filePath);
        filePath = this.getFilesDir().getPath().toString() + "/homeless_shelter_database.csv";
        File csv2 = new File(filePath);

        csv.delete();
        csv2.delete();

    }
}
