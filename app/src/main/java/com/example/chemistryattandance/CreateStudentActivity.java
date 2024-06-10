package com.example.chemistryattandance;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateStudentActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText userEmail, userPassword, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);
        userName = findViewById(R.id.userName);
        Button createUserButton = findViewById(R.id.createUserButton);
        Button sendEmailButton = findViewById(R.id.sendEmailButton);

        createUserButton.setOnClickListener(v -> createUser());

        sendEmailButton.setOnClickListener(v -> {
            String email = userEmail.getText().toString();
            String password = userPassword.getText().toString();
            String name = userName.getText().toString();
            sendEmailIntent(email, password, name);
        });
    }

    private void createUser() {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();
        String name = userName.getText().toString();

        if (!validateInputs(email, password, name)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(profileUpdateTask -> {
                                        if (profileUpdateTask.isSuccessful()) {
                                            saveUserRole(user.getUid(), "student");
                                            Toast.makeText(CreateStudentActivity.this, "Student account created successfully.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(CreateStudentActivity.this, "User creation failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserRole(String userId, String role) {
        mDatabase.child("users").child(userId).child("role").setValue(role);
    }

    private boolean validateInputs(String email, String password, String name) {
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Name is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void sendEmailIntent(String email, String password, String name) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your Student Account Details");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello " + name + ",\n\nYour student account has been created. Here are your login details:\n\nEmail: " + email + "\nPassword: " + password + "\n\nBest regards,\nAdmin");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CreateStudentActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}

