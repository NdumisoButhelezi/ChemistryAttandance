package com.example.chemistryattandance;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends AppCompatActivity {
    private FirebaseDatabase database;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        database = FirebaseDatabase.getInstance();

        TextView roomCodeTextView = findViewById(R.id.roomCodeTextView);
        ListView peopleListView = findViewById(R.id.peopleListView);

        String roomId = getIntent().getStringExtra("ROOM_ID");
        roomCodeTextView.setText("Room Code: " + roomId);

        DatabaseReference roomRef = database.getReference("rooms").child(roomId).child("students");

        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> peopleList = new ArrayList<>();
                for (DataSnapshot studentSnapshot : snapshot.getChildren()) {
                    String studentEmail = studentSnapshot.getValue(String.class);
                    peopleList.add(studentEmail);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(RoomActivity.this, android.R.layout.simple_list_item_1, peopleList);
                peopleListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}
