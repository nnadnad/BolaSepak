package com.bolasepak.bolasepak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bolasepak.bolasepak.Adapter.GoalsAdapter;
import com.bolasepak.bolasepak.Event.MatchData;
import com.bolasepak.bolasepak.SQLite.SQLiteManager;
import com.bolasepak.bolasepak.View.OnSingleClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class EventActivity extends AppCompatActivity {

    ImageView ivLogoTimHome ;
    ImageView ivLogoTimAway ;

    String namaTimHome ;
    String namaTimAway ;
    String skorTimHome ;
    String skorTimAway ;

    TextView tvNamaTimHome ;
    TextView tvNamaTimAway ;

    TextView tvSkorTimHome ;
    TextView tvSkorTimAway ;

    TextView tvTanggalPertandingan ;

    String tanggalPertandingan ;

    String urlLogoTimHome ;
    String urlLogoTimAway ;

    TextView tvShotsHome ;
    TextView tvShotsAway ;

    String shotsHome ;
    String shotsAway ;

    ArrayList<String> arrGoalsHome ;
    RecyclerView rvHome ;
    GoalsAdapter goalsAdapterHome;

    ArrayList<String> arrGoalsAway ;
    RecyclerView rvAway ;
    GoalsAdapter goalsAdapterAway;

    String idHome ;
    String idAway ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail);

        getSupportActionBar().hide();
        GetIntentExtra() ;

        tvNamaTimHome = findViewById(R.id.tvNamaTimHome) ;
        tvNamaTimAway = findViewById(R.id.tvNamaTimAway) ;
        tvSkorTimHome = findViewById(R.id.tvSkorTimHome) ;
        tvSkorTimAway = findViewById(R.id.tvSkorTimAway) ;
        tvTanggalPertandingan = findViewById(R.id.tvTanggalPertandingan) ;
        ivLogoTimHome = findViewById(R.id.ivLogoTimHome) ;
        ivLogoTimAway = findViewById(R.id.ivLogoTimAway) ;
        tvShotsHome = findViewById(R.id.tvShotHome) ;
        tvShotsAway = findViewById(R.id.tvShotAway) ;
        rvHome = findViewById(R.id.recyclerViewGoalHome) ;
        rvAway = findViewById(R.id.recyclerViewGoalAway) ;

        tvNamaTimHome.setText(namaTimHome) ;
        tvNamaTimAway.setText(namaTimAway) ;
        tvSkorTimHome.setText(""+skorTimHome) ;
        tvSkorTimAway.setText(""+skorTimAway) ;

        tvTanggalPertandingan.setText(tanggalPertandingan );

        Picasso.get().load(urlLogoTimHome).into(ivLogoTimHome);
        Picasso.get().load(urlLogoTimAway).into(ivLogoTimAway);
        tvShotsHome.setText(""+shotsHome);
        tvShotsAway.setText(""+shotsAway);

        rvHome.setHasFixedSize(true) ;
        rvHome.setLayoutManager(new GridLayoutManager(this,1));
        goalsAdapterHome = new GoalsAdapter(this,arrGoalsHome) ;
        goalsAdapterHome.notifyDataSetChanged();
        rvHome.setAdapter(goalsAdapterHome) ;

        rvAway.setHasFixedSize(true) ;
        rvAway.setLayoutManager(new GridLayoutManager(this,1));
        goalsAdapterAway = new GoalsAdapter(this,arrGoalsAway) ;
        goalsAdapterAway.notifyDataSetChanged();
        rvAway.setAdapter(goalsAdapterAway) ;

        ivLogoTimHome.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TeamDetail.class) ;
                intent.putExtra("id_team",idHome) ;
                intent.putExtra("name_team",namaTimHome) ;
                startActivity(intent);
            }
        });

        ivLogoTimAway.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TeamDetail.class) ;
                intent.putExtra("id_team",idAway) ;
                intent.putExtra("name_team",namaTimAway) ;
                startActivity(intent);
            }
        });
    }

    void GetIntentExtra(){
        String  idEvent = getIntent().getExtras().getString("id_event") ;
        SQLiteManager sqLiteManager = new SQLiteManager(this) ;
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
