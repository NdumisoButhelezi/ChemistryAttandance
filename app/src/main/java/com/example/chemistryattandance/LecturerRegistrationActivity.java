package com.example.chemistryattandance;

// LecturerRegistrationActivity.java
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class LecturerRegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_registration);

        mAuth = FirebaseAuth.getInstance();

        EditText emailEditText = findViewById(R.id.lecturer_registration_email_edit_text);
        EditText passwordEditText = findViewById(R.id.lecturer_registration_password_edit_text);
        EditText staffNumberEditText = findViewById(R.id.staff_number_edit_text);
        Button registerButton = findViewById(R.id.lecturer_register_button);

        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String staffNumber = staffNumberEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(staffNumber)) {
                Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create lecturer account
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LecturerRegistrationActivity.this, task -> {
                        if (task.isSuccessful()) {
                            // Registration success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "Lecturer registration successful!", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Send email to verify account
                            sendVerificationEmail(email);
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(getApplicationContext(), "Invalid email address!", Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(getApplicationContext(), "Email address already in use!", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Lecturer registration failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
    }

    private void sendVerificationEmail(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email)); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Verify Your Email Address");
        intent.putExtra(Intent.EXTRA_TEXT, "Please click on the link below to verify your email address:\n\n<verification link here>");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "No email app found!", Toast.LENGTH_SHORT).show();
        }
    }
}