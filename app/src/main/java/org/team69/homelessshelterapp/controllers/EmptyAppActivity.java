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

public class EmptyAppActivity extends AppCompatActivity {

    private Button logoutButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emptyapp_screen);

        logoutButton =  findViewById(R.id.button5);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToLogin();
            }
        });
    }

    private void goToLogin() {
        Intent intent = new Intent(getBaseContext(), WelcomeActivity.class);
        startActivity(intent);
    }
}
