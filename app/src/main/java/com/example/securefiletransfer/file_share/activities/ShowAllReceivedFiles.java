package com.example.securefiletransfer.file_share.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securefiletransfer.R;
import com.example.securefiletransfer.file_share.adapter.ShowFileReceivedAdapter;
import com.example.securefiletransfer.file_share.model.FileShared;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowAllReceivedFiles extends AppCompatActivity {

    RecyclerView rv_fileShared;
    String username, id, email, image, sender;

    List<FileShared> fileSharedList = new ArrayList<>();
    ShowFileReceivedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_received_files);

        rv_fileShared = findViewById(R.id.rv_fileShared);
        rv_fileShared.setHasFixedSize(true);
        rv_fileShared.setLayoutManager(new LinearLayoutManager(ShowAllReceivedFiles.this));

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        getFileShared();

    }

    private void getFileShared() {
        
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        
        if (firebaseUser.getUid() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(UserRegister.FILE_RECEIVED).child(username);
            
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    fileSharedList.clear();
                    
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        FileShared model = dataSnapshot.getValue(FileShared.class);
                        fileSharedList.add(model);
                    }
                    
                    adapter = new ShowFileReceivedAdapter(ShowAllReceivedFiles.this, fileSharedList);
                    rv_fileShared.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ShowAllReceivedFiles.this, "error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}