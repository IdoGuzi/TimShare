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

import java.util.ArrayList;

import classes.Business;
import classes.Notification;
import classes.PUser;
import interfaces.user;

import static classes.Request.friend;


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

    private Button friends,add_or_edit;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String name = user.getDisplayName();
    String email = user.getEmail();

    private Button eventsBtn;
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
    private boolean done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_profile);


        TextView username = findViewById(R.id.username);
        TextView usermail = findViewById(R.id.emailaddress);
        ProfileImage = findViewById(R.id.profileimage);
        friends = findViewById(R.id.profile_friends_button);
        eventsBtn=findViewById(R.id.viewAllEventsBtn);
        add_or_edit = findViewById(R.id.add_friend_or_edit);

        userID = getIntent().getExtras().getString("id");

        ref.child("Users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userToDisplay = snapshot.getValue(PUser.class);
                if (userToDisplay != null) {
                    username.setText(userToDisplay.getUserName());
                    usermail.setText(userToDisplay.getEmail());
                    //String image = userToDisplay.getprofileimage();

          if (snapshot.child("profileimage").exists()) {
                        String image = snapshot.child("profileimage").getValue().toString();
                        if(!image.isEmpty())
                            Picasso.get().load(image).placeholder(R.drawable.profile).into(ProfileImage);
                    }

                    if (user.getUid().equals(userID)) {
                        add_or_edit.setText("edit profile");
                        add_or_edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent editing = new Intent(getBaseContext(),SetupActivity.class);
                                startActivity(editing);
                            }
                        });
                    }else{
                        add_or_edit.setText("Add Friends");
                        add_or_edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Notification n = new Notification(user.getUid(),userID,friend);
                                userToDisplay.addNotification(n);
                                n.setActive(true);
                                DatabaseReference user_ref = ref.child("Users");
                                DatabaseReference usid_ref = user_ref.child(userID);
                                DatabaseReference notificaiton_ref = usid_ref.child("notifications");
                                DatabaseReference set_ref = notificaiton_ref.push();
                                set_ref.setValue(n);
                            }
                        });
                    }
                    friends.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent friends_list = new Intent(ProfileActivity.this, UserList.class);
                            friends_list.putStringArrayListExtra("userIDs",new ArrayList<String>(userToDisplay.getFriends()));
                            startActivity(new Intent(getApplicationContext(), UserList.class));
                        }
                    });





                    //if the user type is business!!!!!!!!!
                } else {
                    ref.child("Businesses").child(userID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userToDisplay = snapshot.getValue(Business.class);
                            if (userToDisplay == null)
                                throw new RuntimeException("ERROR: user is not in data base");

                            username.setText(userToDisplay.getUserName());
                            usermail.setText(userToDisplay.getEmail());
                            //String image = userToDisplay.getprofileimage();
                            String image = snapshot.child("profileimage").getValue().toString();
                            Picasso.get().load(image).placeholder(R.drawable.profile).into(ProfileImage);



                            if (user.getUid().equals(userID)) {
                                add_or_edit.setText("edit profile");
                                add_or_edit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent editing = new Intent(getBaseContext(),SetupActivity.class);
                                        startActivity(editing);
                                    }
                                });
                            }else{
                                add_or_edit.setText("add friend");
                                add_or_edit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Notification n = new Notification(user.getUid(),userID,friend);
                                        userToDisplay.addNotification(n);
                                        n.setActive(true);
                                        DatabaseReference user_ref = ref.child("Businesses");
                                        DatabaseReference usid_ref = user_ref.child(userID);
                                        DatabaseReference notificaiton_ref = usid_ref.child("notifications");
                                        DatabaseReference set_ref = notificaiton_ref.push();
                                        set_ref.setValue(n);
                                    }
                                });
                            }
                            friends.setText("Friends");
                            friends.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent friends_list = new Intent(ProfileActivity.this, UserList.class);
                                    friends_list.putStringArrayListExtra("userIDs",new ArrayList<>(userToDisplay.getFriends()));
                                    startActivity(new Intent(getApplicationContext(), UserList.class));
                                }
                            });


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });




        eventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allEventsIntet=new Intent(ProfileActivity.this,viewAllEventsActivity.class);
                allEventsIntet.putExtra("com.example.timshare.ALLEVENTID",userID);
                startActivity(allEventsIntet);
            }
        });
    }
}