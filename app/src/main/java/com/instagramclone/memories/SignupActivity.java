package com.instagramclone.memories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

public class SignupActivity extends AppCompatActivity {
    public static final String TAG = "SignupActivity";
    private EditText etUsername;
    private EditText etPassword;
    private EditText etEmail; //to reduce code in the future, use a hidden textbox to make this field appear and disappear

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if(ParseUser.getCurrentUser() != null) {
            goToActivity(new Intent(SignupActivity.this, MainActivity.class));
        }

        etEmail = findViewById(R.id.etsEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        Button btnSignup = findViewById(R.id.btnSignup);
        Button btnToLogin = findViewById(R.id.btnToLogin);

        btnSignup.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            String email = etEmail.getText().toString();
            signupUser(username, password, email);
        });

        btnToLogin.setOnClickListener(v -> goToActivity(new Intent(SignupActivity.this, LoginActivity.class)));
    }

    private void signupUser(String username, String password, String email) {
        ParseUser user = new ParseUser();
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);

        user.signUpInBackground(e -> {
            if(e == null) {
                goToActivity(new Intent(SignupActivity.this, MainActivity.class));
            } else {
                Log.e(TAG, "Sign up error: " + e.getMessage(), e);
                //update error handling to show a text on the screen with e.getMessage()
            }
        });
    }

    private void goToActivity(Intent activityIntent) {
        startActivity(activityIntent);
        finish();
    }
}