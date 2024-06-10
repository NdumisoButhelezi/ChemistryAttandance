package com.example.chemistryattandance;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class StudentDashboardActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Button scanQRButton = findViewById(R.id.scanQRButton);

        scanQRButton.setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(StudentDashboardActivity.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setPrompt("Scan a QR Code");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                // Handle the scanned QR code here
                String roomId = result.getContents();
                joinRoom(roomId);
            }
        }
    }

    private void joinRoom(String roomId) {
        String studentId = mAuth.getCurrentUser().getUid();
        String studentEmail = mAuth.getCurrentUser().getEmail();
        DatabaseReference roomRef = database.getReference("rooms").child(roomId).child("students").child(studentId);
        roomRef.setValue(studentEmail).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Joined room successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, RoomActivity.class);
                intent.putExtra("ROOM_ID", roomId);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Failed to join room", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
