package com.example.chemistryattandance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        Button createStudentButton = findViewById(R.id.createStudentButton);
        Button createLecturerButton = findViewById(R.id.createLecturerButton);

        createStudentButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, CreateStudentActivity.class);
            startActivity(intent);
        });

        createLecturerButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, CreateLecturerActivity.class);
            startActivity(intent);
        });
    }
}