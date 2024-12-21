package com.example.securefiletransfer.file_share.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.securefiletransfer.R;
import com.example.securefiletransfer.file_share.model.Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {
    TextView txt_name, txt_email, txt_phoneno, txt_welcome;
    String name, email, phoneno, imgUri;
    CircleImageView profilepic;
    Button btnLogout;
    private FirebaseUser firebaseUser;
    Uri uri;
    int maxId = 0;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    static final int PICK_IMAGE_REQUREST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        btnLogout = findViewById(R.id.btnLogout);
        txt_name = findViewById(R.id.textview_name);
        txt_email = findViewById(R.id.textview_email);
        txt_welcome = findViewById(R.id.Welcome);
        profilepic = findViewById(R.id.profilepic);

        databaseReference = FirebaseDatabase.getInstance().getReference("Images");

        txt_name.setText(UserLogin.username);
        txt_email.setText(UserLogin.email);

        if (firebaseUser == null) {
            Toast.makeText(this, "No user Logged in", Toast.LENGTH_SHORT).show();
        } else {

            fetchImageFromFirebase();
        }

        profilepic.setOnClickListener(view -> {

            Intent intent = new Intent(UserProfileActivity.this, UploadProfilePicture.class);
            startActivity(intent);

        });

        btnLogout.setOnClickListener(v -> {

            mAuth.signOut();
            startActivity(new Intent(this, UserLogin.class));
        });


    }

   /* public void showUserProfile(FirebaseUser firebaseUser) {
        String userId = firebaseUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");


        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ReadwriteUserDetails readwriteUserDetails = snapshot.getValue(ReadwriteUserDetails.class);
                if (readwriteUserDetails != null) {

                    name = readwriteUserDetails.Name;
                    email = readwriteUserDetails.Email;
                    phoneno = readwriteUserDetails.PhoneNo;
                    txt_welcome.setText(name + "!");
                    txt_name.setText(name);
                    txt_email.setText(email);
                    txt_phoneno.setText(phoneno);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }*/


    /*    public void passUserData() {

        String UserName = txt_name.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Students");

        Query checkQuery = reference.orderByChild("Name").equalTo(UserName);

        checkQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String namefrom = snapshot.child(UserName).child("Name").getValue(String.class);
                    String emailfrom = snapshot.child(UserName).child("Email").getValue(String.class);
                    String phonenofrom = snapshot.child(UserName).child("PhoneNo").getValue(String.class);
                    String Dobfrom = snapshot.child(UserName).child("dob").getValue(String.class);

                    Intent intent = new Intent(UserProfile.this, Update_Profile_Activity.class);
                    intent.putExtra("Name", namefrom);
                    intent.putExtra("Email", emailfrom);
                    intent.putExtra("PhoneNo", phonenofrom);
                    intent.putExtra("dob", Dobfrom);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/


    // TODO: 4/21/2024 check parameters.
    private void fetchImageFromFirebase() {

        String userID = firebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Images");

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Model model = snapshot.getValue(Model.class);

                if (model != null) {
                    String imageUrl = model.getImageUrl();

                    // Load image into ImageView using Glide
                    Glide.with(UserProfileActivity.this).load(imageUrl).into(profilepic);


                } else {
                    Toast.makeText(UserProfileActivity.this, "Image Url not found", Toast.LENGTH_SHORT).show();
                }




       /* mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    // Fetching the latest image URL
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);

                        if (imageUrl != null) {

                            // Load image into ImageView using Glide
                            Glide.with(UserProfileActivity.this).load(imageUrl).into(profilepic);

                            return; // Exit the loop after loading the latest image
                        } else {
                            Toast.makeText(UserProfileActivity.this, "Image Url not found", Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    // Database snapshot does not exist
                    Toast.makeText(UserProfileActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Database operation cancelled
                Log.e("EventPic", "Database operation cancelled: " + error.getMessage());
                Toast.makeText(UserProfileActivity.this,  error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Database operation cancelled
                Log.e("EventPic", "Database operation cancelled: " + error.getMessage());
                Toast.makeText(UserProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }


        });
    }
}