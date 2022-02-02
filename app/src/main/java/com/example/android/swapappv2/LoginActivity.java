package com.example.android.swapappv2;

import static com.example.android.swapappv2.HomeActivity.sCurrentUser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
// Extending MainActivity deems LoginActivity as the child activity and MainActivity as the parent activity
public class LoginActivity extends MainActivity {

    private EditText mUsername, mPassword;
    private Button mSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mSignIn = findViewById(R.id.signIn);

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                if (username.equals("") || password.equals(""))
                    Toast.makeText(LoginActivity.this, "Please enter all of the fields. Try again.", Toast.LENGTH_SHORT).show();
                else {
                    boolean loginResult = sUsers.checkUsernamePassword(username, password);

                    if (loginResult) {
                        sCurrentUser = username;
                        Log.i("LoginActivityUserCheck", "sCurrentUser = " + sCurrentUser);
                        // ^ Sets username as the static variable sCurrentUser so that the app stores the username of who's currently logged in
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        finishAffinity(); // Kills MainActivity and LoginActivity so that neither can be accessed by pressing the "back" button
                    }
                    else
                        Toast.makeText(LoginActivity.this, "Invalid login. Make sure you entered the correct username and password.",
                                Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}