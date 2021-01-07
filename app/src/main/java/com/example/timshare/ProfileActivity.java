package com.example.timshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;


import java.util.ArrayList;

import classes.Business;
import classes.PUser;
import interfaces.user;


/**
 * This class handle the user profile activity.
 * pass id to the class with key "id".
 */
public class ProfileActivity extends AppCompatActivity {
    private String userID;
    private FirebaseAuth fAuth;
    private Button friends;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //    String name = user.getDisplayName();
//    String email = user.getEmail();
    private CircleImageView ProfileImage;
    private TextView ProfileName;
    private DatabaseReference UsersRef;
    String currentUserID;
    final static int Gallery_Pick = 1;
    private ProgressDialog loadingBar;
    private StorageReference UserProfileImageRef;
    String otherUserID;
//    String name = user.getDisplayName();
//    String email = user.getEmail();
    private boolean privateUser;
    private interfaces.user userToDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        fAuth = FirebaseAuth.getInstance();
        currentUserID = fAuth.getCurrentUser().getUid();
        ProfileImage = findViewById(R.id.profileimage);
        ProfileName = findViewById(R.id.username);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        loadingBar = new ProgressDialog(this);
        userID = getIntent().getExtras().getString("id");
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        friends = findViewById(R.id.profile_friends_button);

        //.child(currentUserID)

        if (currentUserID.equals(userID)) {
            UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        friends.setText("edit profile");
                        userToDisplay = snapshot.getValue(PUser.class);
                        String fullname = userToDisplay.getUserName();
//                        String image = userToDisplay.getprofileimage();
                        String image = snapshot.child("profileimage").getValue().toString();

                        ProfileName.setText(fullname);
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(ProfileImage);

                        friends.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent editing = new Intent(getBaseContext(),SetupActivity.class);
                                startActivity(editing);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        } else {
            UsersRef.child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        friends.setText("Friends");
                        String fullname = snapshot.child("fullname").getValue().toString();
                        String image = snapshot.child("profileimage").getValue().toString();
                        ProfileName.setText(fullname);
//                        String image = userToDisplay.getprofileimage();
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(ProfileImage);

                        friends.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent editing = new Intent(getBaseContext(),SetupActivity.class);
                                startActivity(editing);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}