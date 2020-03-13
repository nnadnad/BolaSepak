package com.bolasepak.bolasepak.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bolasepak.bolasepak.Event.MatchData;
import com.bolasepak.bolasepak.EventActivity;
import com.bolasepak.bolasepak.R;
import com.bolasepak.bolasepak.View.OnSingleClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyHolder>
{
    Context context ;
    ArrayList<MatchData> matchDataArrayList;
    boolean isClickable ;

    public EventAdapter(Context context, ArrayList<MatchData> matchDataArrayList, boolean isClickable) {
        this.context = context;
        this.matchDataArrayList = matchDataArrayList;
        this.isClickable = isClickable;
    }

    public void setFilter(ArrayList<MatchData> data){
        this.matchDataArrayList.clear();
        this.matchDataArrayList.addAll(data) ;
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
        final MatchData matchData = matchDataArrayList.get(i) ;

        try {
            Picasso.get().load(matchData.getUrlLogoHome()).into(myHolder.ivLogoTimHome);
        }catch (Exception e){

        }

        try {
            Picasso.get().load(matchData.getUrlLogoAway()).into(myHolder.ivLogoTimAway);
        }catch (Exception e){

        }

        myHolder.tvNamaTimHome.setText(matchData.getNameHomeTeam());
        myHolder.tvNamaTimAway.setText(matchData.getNameAwayTeam());

        final String idEvent = matchData.getIdEvent() ;
        myHolder.lnParent.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (isClickable){
                    Intent intent = new Intent(context, EventActivity.class) ;
                    intent.putExtra("id_event",idEvent) ;
                    context.startActivity(intent);
                }
            }
        }) ;

        if (matchData.getHomeScore().equals("null")){
            myHolder.tvSkorTimHome.setText("");
            myHolder.tvSkorTimAway.setText("");
        }else{
            myHolder.tvSkorTimHome.setText(matchData.getHomeScore());
            myHolder.tvSkorTimAway.setText(matchData.getAwayScore());
        }

        myHolder.tvTanggalPertandingan.setText(matchData.getDateEvent());

    }

    @Override
    public int getItemCount() {
        return matchDataArrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        public ImageView ivLogoTimHome ;
        public ImageView ivLogoTimAway ;
        public TextView tvNamaTimHome ;
        public TextView tvNamaTimAway ;
        public TextView tvSkorTimHome;
        public TextView tvSkorTimAway;
        public TextView tvTanggalPertandingan ;
        public LinearLayout lnParent ;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ivLogoTimAway = itemView.findViewById(R.id.ivLogoTimAway) ;
            ivLogoTimHome = itemView.findViewById(R.id.ivLogoTimHome) ;
            tvNamaTimHome = itemView.findViewById(R.id.tvNamaTimHome) ;
            tvNamaTimAway = itemView.findViewById(R.id.tvNamaTimAway) ;

            tvSkorTimAway = itemView.findViewById(R.id.tvSkorTimAway) ;
            tvSkorTimHome = itemView.findViewById(R.id.tvSkorTimHome) ;
            tvTanggalPertandingan = itemView.findViewById(R.id.tvTanggalPertandingan) ;

            lnParent = itemView.findViewById(R.id.lnParent) ;
        }
    }
}
