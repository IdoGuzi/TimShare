package com.example.timshare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import classes.Event;

public class AdapterEvent extends RecyclerView.Adapter<AdapterEvent.EventHolder> {
    ArrayList<Event> eventArrayList;
    public  AdapterEvent(ArrayList<Event> eventArrayList){
        this.eventArrayList=eventArrayList;
    }
    @NonNull
    @Override
    public AdapterEvent.EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder,parent,false);
        return new EventHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEvent.EventHolder holder, int position) {
        holder.eventName.setText(eventArrayList.get(position).getEventName());
        holder.eventDescription.setText(eventArrayList.get(position).toString());

    }

    @Override
    public int getItemCount() {
        return this.eventArrayList.size();
    }
    class  EventHolder extends RecyclerView.ViewHolder {
        TextView eventName,eventDescription;
        public EventHolder(@NonNull View itemView) {
            super(itemView);
            eventName=itemView.findViewById(R.id.nameView);
            eventDescription=itemView.findViewById(R.id.descriptionItemView);
        }
    }
}
