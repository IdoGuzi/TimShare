package com.example.timshare;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import interfaces.user;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.UserHolder>{
    private ArrayList<user> userArrayList;
    private HashMap<user ,String> keyHashMap;
     private AdapterView.OnItemClickListener onItemClickListener;
    public AdapterUser(ArrayList<user> userArrayList,HashMap<user ,String> keyHashMap){
        this.userArrayList=userArrayList;
        this.keyHashMap=keyHashMap;
    }
    @NonNull
    @Override
    public AdapterUser.UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card_holder,parent,false);
        return new AdapterUser.UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUser.UserHolder holder, int position) {
        holder.setUserName(userArrayList.get(position).getUserName());
        holder.setUserEmail(userArrayList.get(position).getEmail());
        holder.setProfileImage(userArrayList.get(position).getprofileimage());
        String key=keyHashMap.get(userArrayList.get(position));
        DatabaseReference  userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        userRef.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    if(snapshot.child("profileimage").exists())
                    {
                        String image=snapshot.child("profileimage").getValue().toString();
                        if(!image.isEmpty())
                        {
                            holder.setProfileImage(image);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }});
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent=new Intent(v.getContext(),ProfileActivity.class);
                String key=keyHashMap.get(userArrayList.get(holder.getBindingAdapterPosition()));
                profileIntent.putExtra("id",key);
                v.getContext().startActivity(profileIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.userArrayList.size();
    }


    class  UserHolder extends RecyclerView.ViewHolder  {
       View mView;
        public UserHolder(@NonNull View itemView) {
            super(itemView);
            this.mView=itemView;
        }
        void setUserName(String userNameStr)
        {
            TextView userName=mView.findViewById(R.id.user_name);
            userName.setText(userNameStr);
        }
        void setUserEmail(String userEmailStr)
        {
            TextView userEmail=mView.findViewById(R.id.user_email);
            userEmail.setText(userEmailStr);
        }
        void setProfileImage(String profileImage)
        {
            if(profileImage.isEmpty())return;
            CircleImageView imag=mView.findViewById(R.id.user_profile_image);
            Picasso.get().load(profileImage).placeholder(R.drawable.profile).into(imag);
        }

    }
}
