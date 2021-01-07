package com.example.timshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import classes.Business;
import classes.PUser;
import interfaces.user;

public class BusinessSignUp extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private TextInputEditText id, name, mail;
    private EditText pass;
    private Button create;
    private Button business;
    private FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_sign_up);
        create = (Button) findViewById(R.id.confirm_button);
        business = (Button) findViewById(R.id.button);

        name = (TextInputEditText)findViewById(R.id.User_Name_Entry);
        mail = (TextInputEditText)findViewById(R.id.Email_Entry);
        pass = (EditText)findViewById(R.id.Password_Entry);
        fAuth = FirebaseAuth.getInstance();


        create.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if (!checkInputs()){
                    return;
                }
                fAuth.createUserWithEmailAndPassword(mail.getText().toString(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(BusinessSignUp.this,"success",Toast.LENGTH_LONG).show();
                            FirebaseUser user = fAuth.getCurrentUser();
                            interfaces.user u = new Business(mail.getText().toString(),name.getText().toString());
                            database.getReference("Business").child(user.getUid()).setValue(u);
                            startActivity(new Intent(getApplicationContext(),Login.class));
                        }else{
                            Toast.makeText(BusinessSignUp.this,"failed: "+task.getException().toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });

    }

    private boolean checkInputs(){
        String username = name.getText().toString();
        String Email = mail.getText().toString();
        String password = pass.getText().toString();
        if (username.isEmpty()){
            name.setError("please choose a user name");
            return false;
        }
        if (Email.isEmpty()){
            mail.setError("Email is required");
            return false;
        }
        if (password.isEmpty()){
            pass.setError("passwords can't be empty!");
            return false;
        }
        if (password.length()<6){
            pass.setError("password must be 6 or more characters");
            return false;
        }
        return true;
    }


}