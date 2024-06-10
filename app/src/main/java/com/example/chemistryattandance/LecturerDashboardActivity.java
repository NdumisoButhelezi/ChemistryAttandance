package com.example.chemistryattandance;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_dashboard);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Button createRoomButton = findViewById(R.id.createRoomButton);
        ImageView qrCodeImageView = findViewById(R.id.qrCodeImageView);

        createRoomButton.setOnClickListener(v -> createRoom(qrCodeImageView));
    }

    private void createRoom(ImageView qrCodeImageView) {
        String roomId = database.getReference("rooms").push().getKey();
        String lecturerId = mAuth.getCurrentUser().getUid();
        Map<String, Object> roomData = new HashMap<>();
        roomData.put("lecturerId", lecturerId);

        database.getReference("rooms").child(roomId).setValue(roomData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        generateQRCode(roomId, qrCodeImageView);
                    } else {
                        Toast.makeText(this, "Failed to create room", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void generateQRCode(String roomId, ImageView qrCodeImageView) {
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
}

