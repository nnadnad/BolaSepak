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

import com.example.bolasepak.R;
import com.example.bolasepak.event.MatchData;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MyHolder> {
    private Context context;
    private ArrayList<MatchData> matchData;
    private boolean isClick;


    public MatchAdapter(Context context, ArrayList<MatchData> matchData, boolean isClick) {
        this.context = context;
        this.matchData = matchData;
        this.isClick = isClick;
    }

    public void filterData(ArrayList<MatchData> mData) {
        this.matchData.clear();
        this.matchData.addAll(mData);
        notifyDataSetChanged();
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        ImageView image_logo_home_team ;
        ImageView image_logo_away_team ;
        TextView nama_home_team ;
        TextView nama_away_team ;
        TextView score_home_team;
        TextView score_away_team;
        TextView match_date ;
        LinearLayout match_container ;

        MyHolder(@NonNull View itemView) {
            super(itemView);
            image_logo_home_team = itemView.findViewById(R.id.logo_home_team) ;
            image_logo_away_team = itemView.findViewById(R.id.logo_away_team) ;
            nama_home_team = itemView.findViewById(R.id.id_home_team) ;
            nama_away_team = itemView.findViewById(R.id.id_away_team) ;
            score_home_team = itemView.findViewById(R.id.score_tim_home) ;
            score_away_team = itemView.findViewById(R.id.score_tim_away) ;
            match_date = itemView.findViewById(R.id.tgl_match) ;

            match_container = itemView.findViewById(R.id.match_container) ;
        }
    }


    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int count) {
        View view = LayoutInflater.from(context).inflate(R.layout.match_item,viewGroup,false);
        return new MyHolder(view);
    }

    public void onBindViewHolder(@NotNull MyHolder holder, int count) {
        final MatchData matchData1 = matchData.get(count);
        // try catch get logo team
        try {
            Picasso.get().load(matchData1.getLogo_home_team()).into(holder.image_logo_home_team);
        } catch (Exception ignored) {
        }
        try {
            Picasso.get().load(matchData1.getLogo_away_team()).into(holder.image_logo_away_team);
        } catch (Exception ignored) {
        }

        //adapter to get nama tim
        holder.nama_home_team.setText(matchData1.getNama_home_team());
        holder.nama_away_team.setText(matchData1.getNama_away_team());

        //adapter id tiap event
        final String id_match = matchData1.getId_match();
        // agar tiap recycler view bisa di click
        holder.match_container.setOnClickListener(new OnSigleClickListener() {
            @Override
            public void singleClick(View V) {
                if (isClick) {
                    Intent intent = new Intent(
                            context,
                            EventActivity.class
                    );
                    intent.putExtra("id_match",id_match);
                    context.startActivity(intent);
                }
            }
        });

        if(matchData1.getScore_home_team().equals("null")) {
            holder.score_home_team.setText("");
            holder.score_away_team.setText("");
        } else {
            holder.score_home_team.setText(matchData1.getScore_home_team());
            holder.score_away_team.setText(matchData1.getScore_away_team());
        }
        holder.match_date.setText(matchData1.getMatch_date());
    }


    @Override
    public int getItemCount() {
        return matchData.size();
    }







}
