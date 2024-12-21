package com.example.securefiletransfer.file_share.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.securefiletransfer.R;
import com.example.securefiletransfer.file_share.model.Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UploadProfilePicture extends AppCompatActivity {
    private Button btnupload;
    private ImageView imageView;
    private ProgressBar progressBar;
    private static final int PICK_IMAGE_REQUREST = 2;

    // Firebase
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Images");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    //variable for uri image
    private Uri imageuri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_picture);

        btnupload = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        //apply on click listener on image view
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUREST);

        });

        //apply on click listener on upload btn
        btnupload.setOnClickListener(v -> {
            if (imageuri != null) {
                uploadToFirebase(imageuri);
            } else {
                Toast.makeText(UploadProfilePicture.this, "Please Select Image", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageuri = data.getData();
            imageView.setImageURI(imageuri);
        }

    }

    // create function to upload image
    private void uploadToFirebase(Uri uri) {

        StorageReference fileRef = reference.child(firebaseUser.getUid() + "." + getFileExtension(uri));

        fileRef.putFile(uri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                .addOnSuccessListener(uri1 -> {

                    Model model = new Model(uri1.toString());

                   // String modelId = root.push().getKey();
                    String id = firebaseUser.getUid();
                    root.child(id).setValue(model);

                    progressBar.setVisibility(View.INVISIBLE);

                    Toast.makeText(UploadProfilePicture.this, "Uploaded Successful", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(UploadProfilePicture.this, UserProfileActivity.class);
                    startActivity(i);

                })).addOnProgressListener(snapshot -> progressBar.setVisibility(View.VISIBLE)).addOnFailureListener(e -> {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(UploadProfilePicture.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
        });
    }

    private String getFileExtension(Uri muri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cr.getType(muri));
    }

}