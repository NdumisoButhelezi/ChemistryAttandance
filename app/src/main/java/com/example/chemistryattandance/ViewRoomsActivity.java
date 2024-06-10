package com.example.chemistryattandance;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ViewRoomsActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private ListView roomsListView;
    private Button generateReportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rooms);

        database = FirebaseDatabase.getInstance();
        //roomsListView = findViewById(R.id.roomsListView);
        //generateReportButton = findViewById(R.id.generateReportButton);

        loadRooms();

        generateReportButton.setOnClickListener(v -> generateReport());
    }

    private void loadRooms() {
        DatabaseReference roomsRef = database.getReference("rooms");
        roomsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> roomsList = new ArrayList<>();
                for (DataSnapshot roomSnapshot : snapshot.getChildren()) {
                    roomsList.add(roomSnapshot.getKey()); // Add room ID to the list
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewRoomsActivity.this, android.R.layout.simple_list_item_1, roomsList);
                roomsListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewRoomsActivity.this, "Failed to load rooms", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateReport() {
        // Logic to generate attendance report
        // Example: Save the report to a file or display it on screen
        Toast.makeText(this, "Report generated successfully", Toast.LENGTH_SHORT).show();
    }
}
