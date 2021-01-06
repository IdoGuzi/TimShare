package com.example.timshare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import interfaces.user;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.UserHolder>{
    private ArrayList<user> userArrayList;
     private AdapterView.OnItemClickListener onItemClickListener;
    public AdapterUser(ArrayList<user> userArrayList){
        this.userArrayList=userArrayList;
    }
    @NonNull
    @Override
    public AdapterUser.UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder,parent,false);
        return new AdapterUser.UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUser.UserHolder holder, int position) {
        holder.userName.setText(userArrayList.get(position).getUserName());
        holder.userDescription.setText(userArrayList.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return this.userArrayList.size();
    }


    class  UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView userName,userDescription;
        public UserHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.nameView);
            userDescription=itemView.findViewById(R.id.descriptionItemView);
            userName.setOnClickListener(this);
            userDescription.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(null, v, getBindingAdapterPosition(), v.getId());
        }
    }
}
