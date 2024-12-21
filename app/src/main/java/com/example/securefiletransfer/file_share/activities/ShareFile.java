package com.example.securefiletransfer.file_share.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securefiletransfer.R;
import com.example.securefiletransfer.file_share.adapter.ShowFileSharedAdapter;
import com.example.securefiletransfer.file_share.classes.MyEncryptionClass;
import com.example.securefiletransfer.file_share.model.FileShared;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShareFile extends AppCompatActivity {

    FloatingActionButton fab_addFile;
    RecyclerView rv_fileShared;
    TextView tv_appBarName;

    String username, id, email, image, sender, phone;

    ProgressDialog dialog;
    Uri imageuri = null;
    String filename = "";
    DatabaseReference reference;

    List<FileShared> fileSharedList = new ArrayList<>();
    ShowFileSharedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_file);

        fab_addFile = findViewById(R.id.fab_addFile);
        tv_appBarName = findViewById(R.id.tv_appBarName);
        rv_fileShared = findViewById(R.id.rv_fileShared);

        rv_fileShared.setHasFixedSize(true);
        rv_fileShared.setLayoutManager(new LinearLayoutManager(ShareFile.this));

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        id = intent.getStringExtra("id");
        email = intent.getStringExtra("email");
        image = intent.getStringExtra("image");
        sender = intent.getStringExtra("sender");
        phone = intent.getStringExtra("phone");

        Toast.makeText(ShareFile.this, "sender: " + sender, Toast.LENGTH_SHORT).show();

        tv_appBarName.setText("File Shared with " + username);

        getFileShared();

        fab_addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareFile();
            }
        });


    }

    private void shareFile() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // We will be redirected to choose pdf
        galleryIntent.setType("application/pdf");
        startActivityForResult(galleryIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            dialog = new ProgressDialog(this);
            dialog.setMessage("Encrypting & Uploading");

            dialog.show();
            imageuri = data.getData();
            filename = getFileName(imageuri);

            Uri uri = data.getData();
            File file = null;


            Toast.makeText(ShareFile.this, "filename : " + filename, Toast.LENGTH_SHORT).show();

            final String timestamp = "" + System.currentTimeMillis();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            final String messagePushID = timestamp;

            final StorageReference filepath = storageReference.child(messagePushID + "." + "pdf");

            filepath.putFile(imageuri).continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        dialog.dismiss();

                        Uri uri = task.getResult();
                        String myurl;
                        myurl = uri.toString();

                        showKeyDialog(file);

                        reference = FirebaseDatabase.getInstance().getReference().child(UserRegister.FILE_SHARED).child(username).push();

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", id);
                        hashMap.put("username", username);
                        hashMap.put("sender", sender);
                        hashMap.put("filename", filename);
                        hashMap.put("fileUrl", myurl);
                        hashMap.put("phone", phone);
                        // Key
                        String fileCode = reference.getKey();
                        hashMap.put("key", fileCode);


                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    filename = "";
                                    Toast.makeText(ShareFile.this, fileCode, Toast.LENGTH_SHORT).show();
                                    //sendSMS(phone, fileCode);
                                    getFileShared();
                                    Toast.makeText(ShareFile.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        dialog.dismiss();
                        Toast.makeText(ShareFile.this, "UploadedFailed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showKeyDialog(File file) {
        String keyAES = "AES" + username + filename;
        String keyDES = "DES" + username + filename;

        MyEncryptionClass.encryptionAES(file, keyAES);
        MyEncryptionClass.encryptionDES(file, keyDES);
    }

    private void decryptDialog(File file) {
        String keyAES = "AES" + username + filename;
        String keyDES = "DES" + username + filename;

        try {
            MyEncryptionClass.decryptionAES(file, keyAES);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            MyEncryptionClass.decryptionDES(file, keyDES);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void getFileShared() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser.getUid() != null) {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(UserRegister.FILE_SHARED).child(username);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    fileSharedList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        FileShared model = dataSnapshot.getValue(FileShared.class);
                        if (model.getSender().equals(sender)) {
                            fileSharedList.add(model);
                        }
                    }
                    adapter = new ShowFileSharedAdapter(ShareFile.this, fileSharedList);
                    rv_fileShared.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}