package com.example.bolasepak.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bolasepak.EventActivity;
import com.example.bolasepak.OnSingleClickListener;
import com.example.bolasepak.R;
import com.example.bolasepak.event.MatchData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MyHolder>
{
    private Context context ;
    private ArrayList<MatchData> MatchDataArrayList ;
    private boolean isClickable ;

    public MatchAdapter(Context context, ArrayList<MatchData> MatchDataArrayList, boolean isClickable) {
        this.context = context;
        this.MatchDataArrayList = MatchDataArrayList;
        this.isClickable = isClickable;
    }

    public void setFilter(ArrayList<MatchData> data){
        this.MatchDataArrayList.clear();
        this.MatchDataArrayList.addAll(data) ;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.match_item,viewGroup,false) ;
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        final MatchData MatchData = MatchDataArrayList.get(i) ;

        try {
            Picasso.get().load(MatchData.getUrlLogoHome()).into(myHolder.logo_home_team);
        }catch (Exception ignored){

        }

        try {
            Picasso.get().load(MatchData.getUrlLogoAway()).into(myHolder.logo_away_team);
        }catch (Exception ignored){

        }

        myHolder.id_home_team.setText(MatchData.getNameHomeTeam());
        myHolder.id_away_team.setText(MatchData.getNameAwayTeam());

        final String idEvent = MatchData.getIdEvent() ;
        myHolder.match_container.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (isClickable){
                    Intent intent = new Intent(context, EventActivity.class) ;
                    intent.putExtra("id_event",idEvent) ;
                    context.startActivity(intent);
                }
            }
        }) ;

        if (MatchData.getHomeScore().equals("null")){
            myHolder.score_tim_home.setText("");
            myHolder.score_tim_away.setText("");
        }else{
            myHolder.score_tim_home.setText(MatchData.getHomeScore());
            myHolder.score_tim_away.setText(MatchData.getAwayScore());
        }

        myHolder.tgl_match.setText(MatchData.getDateEvent());

    }

    @Override
    public int getItemCount() {
        return MatchDataArrayList.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{
        ImageView logo_home_team ;
        ImageView logo_away_team ;
        TextView id_home_team ;
        TextView id_away_team ;
        TextView score_tim_home;
        TextView score_tim_away;
        TextView tgl_match ;
        LinearLayout match_container ;

        MyHolder(@NonNull View itemView) {
            super(itemView);
            logo_away_team = itemView.findViewById(R.id.logo_away_team) ;
            logo_home_team = itemView.findViewById(R.id.logo_home_team) ;
            id_home_team = itemView.findViewById(R.id.id_home_team) ;
            id_away_team = itemView.findViewById(R.id.id_away_team) ;

            score_tim_away = itemView.findViewById(R.id.score_tim_away) ;
            score_tim_home = itemView.findViewById(R.id.score_tim_home) ;
            tgl_match = itemView.findViewById(R.id.tgl_match) ;

            match_container = itemView.findViewById(R.id.match_container) ;
        }
    }
}
