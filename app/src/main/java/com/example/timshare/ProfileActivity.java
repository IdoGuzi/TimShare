package com.example.timshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//    String name = user.getDisplayName();
//    String email = user.getEmail();

    private CircleImageView ProfileImage;
    private TextView ProfileName;
    private DatabaseReference UsersRef;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        TextView username = findViewById(R.id.username);
//        TextView usermail = findViewById(R.id.emailaddress);
//        String name = fAuth.toString();
//        username.setText(name);
//        usermail.setText(email);

        fAuth = FirebaseAuth.getInstance();
        currentUserID = fAuth.getCurrentUser().getUid();
        ProfileImage = findViewById(R.id.profileimage);
        ProfileName = findViewById(R.id.username);

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        //.child(currentUserID)
        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String fullname = snapshot.child("fullname").getValue().toString();
                    String image = snapshot.child("profileimage").getValue().toString();
                    ProfileName.setText(fullname);
                    Picasso.get().load(image).placeholder(R.drawable.profile).into(ProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}