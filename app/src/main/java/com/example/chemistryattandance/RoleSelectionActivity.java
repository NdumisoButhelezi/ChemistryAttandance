package com.example.chemistryattandance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class RoleSelectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        // Initialize buttons
        Button adminButton = findViewById(R.id.adminButton);
        Button studentButton = findViewById(R.id.studentButton);
        Button lecturerButton = findViewById(R.id.lecturerButton);

        // Set onClickListener for admin button
        adminButton.setOnClickListener(v -> {
            Intent intent = new Intent(RoleSelectionActivity.this, AdminLoginActivity.class);
            intent.putExtra("role", "admin");
            startActivity(intent);
        });

        // Set onClickListener for student button
        studentButton.setOnClickListener(v -> {
            Intent intent = new Intent(RoleSelectionActivity.this, StudentLoginActivity.class);
            intent.putExtra("role", "student");
            startActivity(intent);
        });

        // Set onClickListener for lecturer button
        lecturerButton.setOnClickListener(v -> {
            Intent intent = new Intent(RoleSelectionActivity.this, LecturerLoginActivity.class);
            intent.putExtra("role", "lecturer");
            startActivity(intent);
        });
    }
}