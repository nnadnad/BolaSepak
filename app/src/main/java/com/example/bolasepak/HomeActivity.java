package com.example.bolasepak;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bolasepak.event.MatchData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<MatchData> matchData;
    HashMap<String,String> hashMapUrl;
    ProgressBar progressBar;

    //Untuk databse SQLLite
    SQLiteManager sqLiteManager ;
    SharedPreferences preferences ;

    String BASE_URL = "https://www.thesportsdb.com/api/v1/json/1";


    String url_past_15_events = BASE_URL + "/eventspastleague.php?id=4328";
    String url_next_15_events = BASE_URL + "/eventsnextleague.php?id=4328";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

    }

    public void getEventData() {
        matchData = new ArrayList<>();
        getEventLastData();
    }

    private void getEventLastData() {
        String url = url_past_15_events;
        RequestQueue requestQueue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonEvents = new JSONObject(response);
                    JSONArray jsonArray = jsonEvents.getJSONArray("events");
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i) ;
                        String id_match = jsonObject.getString("id_match") ;
                        String id_home_team = jsonObject.getString("id_home_team") ;
                        String id_away_team = jsonObject.getString("id_away_team") ;
                        String nama_home_team = jsonObject.getString("strHomeTeam") ;
                        String nama_away_team = jsonObject.getString("strAwayTeam") ;
                        String score_home_team = jsonObject.getString("score_home_team") ;
                        String score_away_team = jsonObject.getString("score_away_team") ;
                        String logo_home_team = hashMapUrl.get(id_home_team) ;
                        String logo_away_team = hashMapUrl.get(id_away_team) ;
                        String shots_home_team = jsonObject.getString("shots_home_team") ;
                        String shots_away_team = jsonObject.getString("shots_away_team") ;
                        String goals_home_team = jsonObject.getString("strHomeGoalDetails") ;
                        String goals_away_team = jsonObject.getString("strAwayGoalDetails") ;
                        String matchDate = jsonObject.getString("match_date") ;
                        String matchTime = jsonObject.getString("strTime") ;

                        MatchData matchData = new MatchData();
                        matchData.setId_match(id_match);
                        matchData.setId_home_team(id_home_team);
                        matchData.setId_away_team(id_away_team);
                        matchData.setNama_home_team(nama_home_team);

                    }
                } catch (Exception e) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setvisibilty(View.GONE);
                        }
                    }, 3000);
                }
            }
        });
    }

    private void getEventNextData() {

    }
}
