package com.example.d308project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d308project.R;

public class LoginActivity extends AppCompatActivity {

    EditText usernameField, passwordField;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = findViewById(R.id.editUsername);
        passwordField = findViewById(R.id.editPassword);
        loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(v -> {

            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();


            if (username.equals("admin") && password.equals("1234")) {

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // prevents going back to login

            } else {
                Toast.makeText(this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ✅ Prevent bypassing login with back button
    @Override
    public void onBackPressed() {
        // Do nothing
    }
}