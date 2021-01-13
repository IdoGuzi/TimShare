package com.example.timshare;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import classes.Event;
import classes.PUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterEvent extends RecyclerView.Adapter<AdapterEvent.EventHolder> {
    private ArrayList<Event> eventArrayList;
    private AdapterView.OnItemClickListener onItemClickListener;
    public  AdapterEvent(ArrayList<Event> eventArrayList){
        this.eventArrayList=eventArrayList;
    }
    @NonNull
    @Override
    public AdapterEvent.EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_holder,parent,false);
        return new EventHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEvent.EventHolder holder, int position) {
        Event e=eventArrayList.get(position);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getDisplayName();
        holder.setEventUser(name);
        holder.setEventName(e.getEventName());
        holder.setEventDate("Date: "+e.getEventStartingDate().toString());
        holder.setEventTime("Location: "+e.getEventLocation());
        holder.setEventDescription(e.toString());
        DatabaseReference  userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        userRef.child(e.getEventOwnerID()).addValueEventListener(new ValueEventListener() {
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
                Intent eventIntent=new Intent(v.getContext(),ViewEventActivity.class);
                eventIntent.putExtra("com.example.timshare.EVENTID",eventArrayList.get(holder.getBindingAdapterPosition()).getEventID());
                v.getContext().startActivity(eventIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.eventArrayList.size();
    }


    class  EventHolder extends RecyclerView.ViewHolder  {
         View mView;
        public EventHolder(@NonNull View itemView) {
            super(itemView);
           mView=itemView;
        }

        void setEventUser(String eventUserStr)
        {
            TextView eventUser=mView.findViewById(R.id.event_user_name);
            eventUser.setText(eventUserStr);
        }
        void setEventName(String eventNameStr)
        {
            TextView eventName=mView.findViewById(R.id.event_name);
            eventName.setText(eventNameStr);
        }
        void setEventDate(String eventDateStr)
        {
            TextView eventDate=mView.findViewById(R.id.event_date);
            eventDate.setText(eventDateStr);
        }
        void setEventTime(String eventTimeStr)
        {
            TextView eventTime=mView.findViewById(R.id.event_time);
            eventTime.setText(eventTimeStr);
        }
        void setEventDescription(String eventDescriptionStr)
        {
            TextView eventDescription=mView.findViewById(R.id.event_description_text);
            eventDescription.setText(eventDescriptionStr);
        }
        void setProfileImage(String profileImage)
        {
            if(profileImage.isEmpty())return;
            CircleImageView imag=mView.findViewById(R.id.event_profile_image);
            Picasso.get().load(profileImage).placeholder(R.drawable.profile).into(imag);
        }


    }
}
