package com.example.android.swapappv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    // Get EditText and Button user input
    private EditText mUsername, mPassword, mVerifyPassword, mEmail, mPhone;
    private Button mSignUp, mSignIn;

    static UserDBHelper sUsers;
    static ItemDBHelper sItems;
    static NotificationsDBHelper sNotifications;
    static String sUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mVerifyPassword = findViewById(R.id.verifyPassword);
        mEmail = findViewById(R.id.email);
        mPhone = findViewById(R.id.phone);

        mSignUp = findViewById(R.id.signUp);
        mSignIn = findViewById(R.id.signIn);

        sUsers = new UserDBHelper(this);
        sUserID = UUID.randomUUID().toString();

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                String verifyPassword = mVerifyPassword.getText().toString();
                String email = mEmail.getText().toString();
                String phone = mPhone.getText().toString();
                int admin = 0;
                String userID = sUserID;
                Uri defaultProfilePicture = Uri.parse("android.resource://com.example.android.swapappv2/drawable/profile_icon");
                String defaultProfilePictureString = defaultProfilePicture.toString();

                if (username.equals("") || password.equals("") || verifyPassword.equals("") ||
                        email.equals("") || phone.equals(""))
                    Toast.makeText(MainActivity.this, "Fill in all the fields.", Toast.LENGTH_SHORT).show();
                else {
                    if (password.equals(verifyPassword)) {

                        boolean userCheckResult = sUsers.checkUsername(username);

                        if (userCheckResult == false) {

                            boolean regResult = sUsers.insertData(username, password, email, phone, admin, userID, defaultProfilePictureString);

                            if (regResult) {
                                Toast.makeText(MainActivity.this, "Sign up successful! You may sign in.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                MainActivity.this.finish(); // Kills MainActivity so that it cannot be accessed by pressing the "back" button
                            }
                            else
                                Toast.makeText(MainActivity.this, "Registration failed! Try again.", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(MainActivity.this, "User already exists.", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(MainActivity.this, "Passwords did not match. Try again.",
                                Toast.LENGTH_SHORT).show();
                }

            }
        });

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                // MainActivity not killed in the case that the user wants to go back and sign up
            }
        });
    }

}