package com.example.bolasepak;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bolasepak.adapter.GoalsAdapter;
import com.example.bolasepak.event.MatchData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class EventActivity extends AppCompatActivity {

    ImageView logo_home_team ;
    ImageView logo_away_team ;

    String namaTimHome ;
    String namaTimAway ;
    String skorTimHome ;
    String skorTimAway ;

    TextView id_home_team ;
    TextView id_away_team ;

    TextView score_tim_home ;
    TextView score_tim_away ;

    TextView tgl_match ;

    String tanggalPertandingan ;

    String urlLogoTimHome ;
    String urlLogoTimAway ;

    TextView tvShotsHome ;
    TextView tvShotsAway ;

    String shotsHome ;
    String shotsAway ;

    ArrayList<String> arrGoalsHome ;
    RecyclerView rvHome ;
    GoalsAdapter GoalsAdapterHome ;

    ArrayList<String> arrGoalsAway ;
    RecyclerView rvAway ;
    GoalsAdapter GoalsAdapterAway ;

    String idHome ;
    String idAway ;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail);

        getSupportActionBar().hide();
        GetIntentExtra() ;

        id_home_team = findViewById(R.id.id_home_team) ;
        id_away_team = findViewById(R.id.id_away_team) ;
        score_tim_home = findViewById(R.id.score_tim_home) ;
        score_tim_away = findViewById(R.id.score_tim_away) ;
        tgl_match = findViewById(R.id.tgl_match) ;
        logo_home_team = findViewById(R.id.logo_home_team) ;
        logo_away_team = findViewById(R.id.logo_away_team) ;
        tvShotsHome = findViewById(R.id.shots_home_team) ;
        tvShotsAway = findViewById(R.id.shots_away_team) ;
        rvHome = findViewById(R.id.list_goal_home_team) ;
        rvAway = findViewById(R.id.list_goal_away_team) ;

        id_home_team.setText(namaTimHome) ;
        id_away_team.setText(namaTimAway) ;
        score_tim_home.setText(""+skorTimHome) ;
        score_tim_away.setText(""+skorTimAway) ;

        tgl_match.setText(tanggalPertandingan );

        Picasso.get().load(urlLogoTimHome).into(logo_home_team);
        Picasso.get().load(urlLogoTimAway).into(logo_away_team);
        tvShotsHome.setText(""+shotsHome);
        tvShotsAway.setText(""+shotsAway);

        rvHome.setHasFixedSize(true) ;
        rvHome.setLayoutManager(new GridLayoutManager(this,1));
        GoalsAdapterHome = new GoalsAdapter(this,arrGoalsHome) ;
        GoalsAdapterHome.notifyDataSetChanged();
        rvHome.setAdapter(GoalsAdapterHome) ;

        rvAway.setHasFixedSize(true) ;
        rvAway.setLayoutManager(new GridLayoutManager(this,1));
        GoalsAdapterAway = new GoalsAdapter(this,arrGoalsAway) ;
        GoalsAdapterAway.notifyDataSetChanged();
        rvAway.setAdapter(GoalsAdapterAway) ;

        logo_home_team.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TeamDetail.class) ;
                intent.putExtra("id_team",idHome) ;
                intent.putExtra("name_team",namaTimHome) ;
                startActivity(intent);
            }
        });

        logo_away_team.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TeamDetail.class) ;
                intent.putExtra("id_team",idAway) ;
                intent.putExtra("name_team",namaTimAway) ;
                startActivity(intent);
            }
        });
    }

    void GetIntentExtra(){
        String  idEvent = Objects.requireNonNull(getIntent().getExtras()).getString("id_event") ;
        SQLite sqLiteManager = new SQLite(this) ;
        MatchData matchData = sqLiteManager.getSingleEvent(idEvent) ;

        namaTimHome = matchData.getNameHomeTeam() ;
        namaTimAway = matchData.getNameAwayTeam() ;

        idHome = matchData.getIdHomeTeam() ;
        idAway = matchData.getIdAwayTeam() ;

        skorTimHome = matchData.getHomeScore() ;
        if (skorTimHome.equals("null")){
            skorTimHome = "" ;
        }

        skorTimAway = matchData.getAwayScore() ;
        if (skorTimAway.equals("null")){
            skorTimAway = "" ;
        }

        tanggalPertandingan = matchData.getDateEvent() ; ;

        urlLogoTimHome = matchData.getUrlLogoHome() ;
        urlLogoTimAway = matchData.getUrlLogoAway() ;

        shotsHome = matchData.getIntHomeShots() ;
        if (shotsHome.equals("null")){
            shotsHome = "0" ;
        }

        shotsAway = matchData.getIntAwayShots();
        if (shotsAway.equals("null")){
            shotsAway = "0" ;
        }

        arrGoalsHome = new ArrayList<>() ;
        if (matchData.getHomeGoalsDetails().equals("null")){
            arrGoalsHome.add("");
        }else {
            String[] strArray = matchData.getHomeGoalsDetails().split(";") ;
            arrGoalsHome.addAll(Arrays.asList(strArray));
        }

        arrGoalsAway = new ArrayList<>() ;
        if (matchData.getAwayGoalsDetails().equals("null")){
            arrGoalsAway.add("");
        }else {
            String[] strArray = matchData.getAwayGoalsDetails().split(";") ;
            arrGoalsAway.addAll(Arrays.asList(strArray));
        }

    }
}
