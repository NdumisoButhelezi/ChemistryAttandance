package com.example.chemistryattandance;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminLoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        mAuth = FirebaseAuth.getInstance();

        EditText adminEmail = findViewById(R.id.adminEmail);
        EditText adminPassword = findViewById(R.id.adminPassword);
        Button adminLoginButton = findViewById(R.id.adminLoginButton);

        adminLoginButton.setOnClickListener(v -> {
            String email = adminEmail.getText().toString();
            String password = adminPassword.getText().toString();
            signIn(email, password);
        });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(AdminLoginActivity.this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
                            // Redirect to Admin Dashboard
                            Intent intent = new Intent(AdminLoginActivity.this, AdminDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(AdminLoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}