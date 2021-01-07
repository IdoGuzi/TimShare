package com.example.timshare;

import classes.PUser;
import interfaces.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    public static final String TAG = "TAG";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private TextInputEditText id, name, mail;
    private EditText pass;
    private Button create;
    private Button business;
    FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        create = (Button) findViewById(R.id.confirm_button);
        business = (Button) findViewById(R.id.button);
        name = (TextInputEditText) findViewById(R.id.User_Name_Entry);
        mail = (TextInputEditText) findViewById(R.id.Email_Entry);
        pass = (EditText) findViewById(R.id.Password_Entry);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!SignUp.this.checkInputs()) {
                    return;
                }
                fAuth.createUserWithEmailAndPassword(mail.getText().toString(), pass.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "success", Toast.LENGTH_LONG).show();
                                    FirebaseUser user = fAuth.getCurrentUser();
                                    user u = new PUser(mail.getText().toString(), name.getText().toString());
                                    database.getReference("Users").child(user.getUid()).setValue(u);
                                    SendUserToSetupActivity();
//                            startActivity(new Intent(getApplicationContext(), Login.class));
                                } else {
                                    Toast.makeText(SignUp.this, "failed: " + task.getException().toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }

    private void SendUserToSetupActivity() {
        Intent setupIntent = new Intent(SignUp.this, SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }

    private boolean checkInputs() {
        String username = name.getText().toString();
        String Email = mail.getText().toString();
        String password = pass.getText().toString();
        if (username.isEmpty()) {
            name.setError("please choose a user name");
            return false;
        }
        if (Email.isEmpty()) {
            mail.setError("Email is required");
            return false;
        }
        if (password.isEmpty()) {
            pass.setError("passwords can't be empty!");
            return false;
        }
        if (password.length() < 6) {
            pass.setError("password must be 6 or more characters");
            return false;
        }
        return true;
    }


}