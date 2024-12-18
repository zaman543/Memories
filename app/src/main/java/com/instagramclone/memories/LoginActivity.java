package com.instagramclone.memories;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";
    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(ParseUser.getCurrentUser() != null) {
            goToActivity(new Intent(this, MainActivity.class));
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnToSignup = findViewById(R.id.btnToSignup);
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            loginUser(username, password);
        });

        btnToSignup.setOnClickListener(v -> goToActivity(new Intent(this, SignupActivity.class)));
    }

    private void loginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, (user, e) -> {
            if(e != null) {
                Log.e(TAG, "Issue with login", e);
                //update error handling to show a text on the screen with e.getMessage()
                return;
            }
            goToActivity(new Intent(this, MainActivity.class));
        });
    }

    private void goToActivity(Intent intent) {
        startActivity(intent);
        finish();
    }
}