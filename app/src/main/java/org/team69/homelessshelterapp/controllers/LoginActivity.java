package org.team69.homelessshelterapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private String userID;
    private final Map<String, User> userList = new HashMap<>();
    private int failedLogins = 0;
    private DatabaseReference refDatabase = FirebaseDatabase.getInstance().getReference();


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
        final String username = userNameObj.toString();
        final String pass = passwordObj.toString();
        if (!checkUsingFile(username, pass)) {
            return;
        }
        refDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.child(username).child("password").getValue() != null
                        && dataSnapshot.child(username).child("password").getValue().toString()
                        .equals(pass)){
                    //Log.d("eyoy", "Password Correct!");
                    failedLogins = 0;
                    wrongLogin.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(getBaseContext(), ShelterListActivity.class);
                    //get the user detail
                    intent.putExtra("userID", userID);
                    startActivity(intent);
                } else {
                    //Log.d("eyoy", "Password Incorrect!");
                    //Log.d("eyoy", "Passed in word is: " + pass);
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

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("eyoy", "Failed to read value.", error.toException());
            }
        });



        /*
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

        }*/
    }

    private void goBackToWelcome() {
        Intent intent = new Intent(getBaseContext(), WelcomeActivity.class);
        startActivity(intent);
    }


    private boolean checkUsingFile(final String username, final String pass) {

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
        if (username.equals("") || pass.equals("")) {
            return false;
        }
        return true;
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
