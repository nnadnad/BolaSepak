package com.example.bolasepak;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private ArrayList<Event> datalist;

    public EventAdapter(ArrayList<Event> datalist) {
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.EventViewHolder holder, int position) {
        holder.txtHome.setText(datalist.get(position).getHome());
        holder.txtAway.setText(datalist.get(position).getAway());
        holder.txtHomeScore.setText(datalist.get(position).getHome_score());
        holder.txtAwayScore.setText(datalist.get(position).getAway_score());
    }

    @Override
    public int getItemCount() {
        return (datalist != null) ? datalist.size() : 0;
    }

    public class EventViewHolder extends RecyclerView.ViewHolder{
        private TextView txtHome, txtAway, txtHomeScore, txtAwayScore;

        public EventViewHolder(View itemView) {
            super(itemView);
            txtHome = (TextView) itemView.findViewById(R.id.home);
            txtAway = (TextView) itemView.findViewById(R.id.away);
            txtHomeScore = (TextView) itemView.findViewById(R.id.home_score);
            txtAwayScore = (TextView) itemView.findViewById(R.id.away_score);
        }

    }
}
