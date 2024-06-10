package com.example.chemistryattandance;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LecturerLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_login);

        mAuth = FirebaseAuth.getInstance();

        EditText emailEditText = findViewById(R.id.lecturer_email_edit_text);
        EditText passwordEditText = findViewById(R.id.lecturer_password_edit_text);
        Button loginButton = findViewById(R.id.lecturer_login_button);
        Button registerButton = findViewById(R.id.lecturer_registration_button); // Reference the registration button

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Login user
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LecturerLoginActivity.this, task -> {
                        if (task.isSuccessful()) {
                            // Login success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "Lecturer login successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LecturerLoginActivity.this, LecturerDashboardActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Handle registration button click
        registerButton.setOnClickListener(v -> {
            // Redirect to the registration activity
            startActivity(new Intent(LecturerLoginActivity.this, LecturerRegistrationActivity.class));
        });
    }
}

