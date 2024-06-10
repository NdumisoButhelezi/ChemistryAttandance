package com.example.chemistryattandance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import java.util.HashMap;
import java.util.Map;

public class LecturerDashboardActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private ImageView qrCodeImageView;
    private String roomId;
    private CountDownTimer qrCodeTimer;

    private EditText moduleNameEditText, lectureNameEditText, startTimeEditText, endTimeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_dashboard);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        Button createRoomButton = findViewById(R.id.createRoomButton);
        Button saveStudentsButton = findViewById(R.id.saveStudentsButton);
        Button viewRoomsButton = findViewById(R.id.viewRoomsButton);

        moduleNameEditText = findViewById(R.id.moduleNameEditText);
        lectureNameEditText = findViewById(R.id.lectureNameEditText);
        startTimeEditText = findViewById(R.id.startTimeEditText);
        endTimeEditText = findViewById(R.id.endTimeEditText);

        createRoomButton.setOnClickListener(v -> createRoom());
        saveStudentsButton.setOnClickListener(v -> saveStudents());
        viewRoomsButton.setOnClickListener(v -> startActivity(new Intent(LecturerDashboardActivity.this, ViewRoomsActivity.class)));
    }

    private void createRoom() {
        roomId = String.valueOf(System.currentTimeMillis()); // Use current time as room ID
        String lecturerId = mAuth.getCurrentUser().getUid();
        String moduleName = moduleNameEditText.getText().toString();
        String lectureName = lectureNameEditText.getText().toString();
        String startTime = startTimeEditText.getText().toString();
        String endTime = endTimeEditText.getText().toString();

        if (moduleName.isEmpty() || lectureName.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> roomData = new HashMap<>();
        roomData.put("lecturerId", lecturerId);
        roomData.put("moduleName", moduleName);
        roomData.put("lectureName", lectureName);
        roomData.put("startTime", startTime);
        roomData.put("endTime", endTime);

        database.getReference("rooms").child(roomId).setValue(roomData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        generateQRCode();
                        startQRCodeTimer();
                    } else {
                        Toast.makeText(this, "Failed to create room", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveStudents() {
        DatabaseReference roomRef = database.getReference("rooms").child(roomId).child("students");
        // Logic to save students in the room
        // Example: roomRef.push().setValue(studentName);
        Toast.makeText(this, "Students saved successfully", Toast.LENGTH_SHORT).show();
    }

    private void generateQRCode() {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            int size = 512;
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);
            com.google.zxing.common.BitMatrix bitMatrix = qrCodeWriter.encode(roomId, BarcodeFormat.QR_CODE, size, size);
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? android.graphics.Color.BLACK : android.graphics.Color.WHITE);
                }
            }
            qrCodeImageView.setImageBitmap(bitmap);
            qrCodeImageView.setVisibility(android.view.View.VISIBLE);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void startQRCodeTimer() {
        if (qrCodeTimer != null) {
            qrCodeTimer.cancel();
        }
        qrCodeTimer = new CountDownTimer(15000, 15000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Not needed
            }

            @Override
            public void onFinish() {
                generateQRCode();
                startQRCodeTimer();
            }
        };
        qrCodeTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (qrCodeTimer != null) {
            qrCodeTimer.cancel();
        }
    }
}

