package com.example.bolasepak;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bolasepak.adapter.MatchAdapter;
import com.example.bolasepak.event.MatchData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<MatchData> matchData;
    MatchAdapter matchAdapter;
    HashMap<String,String> hashMapUrl;
    ProgressBar progressBar;

    //Untuk Search
    EditText searchText;
    boolean isSearch = false;
    boolean isLoad = false;
    String SearchInput = "";

    //Untuk databse SQLLite
    SQLite sqLite ;
    SharedPreferences preferences ;

    String BASE_URL = "https://www.thesportsdb.com/api/v1/json/1";
    String url_past_15_events = BASE_URL + "/eventspastleague.php?id=4328";
    String url_next_15_events = BASE_URL + "/eventsnextleague.php?id=4328";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        Objects.requireNonNull(getSupportActionBar()).hide();
        searchText = findViewById(R.id.search_bar) ;
        progressBar = findViewById(R.id.progressBar) ;
        sqLite = new SQLite(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this) ;

        recyclerView = findViewById(R.id.recycler) ;
        recyclerView.setHasFixedSize(true) ;
        final int columns = getResources().getInteger(R.integer.columns);
        recyclerView.setLayoutManager(new GridLayoutManager(this,columns));
        matchData = new ArrayList<>() ;
        matchAdapter = new MatchAdapter(this,matchData,true) ;
        recyclerView.setAdapter(matchAdapter) ;

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isLoad){
                    if (s.toString().trim().equals("")){
                        isSearch = false ;
                        showData();
                    }else {
                        isSearch = true ;
                         SearchInput = s.toString().trim() ;
                        showData();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        HashMap<String,String> cacheTeam = sqLite.getDataTableTeam() ;
        ArrayList<MatchData> cacheEvents = sqLite.getEvents() ;


        boolean isCacheTeamExpire = cacheTeamExpire() ;
        boolean isCacheEventExpire = cacheEventExpire() ;


        if (cacheTeam.size() > 0 && !isCacheTeamExpire){
            hashMapUrl = cacheTeam ;

            if (cacheEvents.size() > 0 && !isCacheEventExpire){
                matchData = cacheEvents;
                showData();
            }else{
                GetData() ;
            }
        }else{
            GetHashMapData() ;
        }

    }


    void GetData(){
        matchData = new ArrayList<>() ;
        GetLastMatch();
    }

    void GetLastMatch(){
        String url = url_past_15_events ;
        RequestQueue requestQueue = Volley.newRequestQueue(this) ;
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {

                JSONObject jsonEvent = new JSONObject(response);
                JSONArray jsonArray = jsonEvent.getJSONArray("events");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String idEvent = jsonObject.getString("idEvent") ;
                    String idHomeTeam = jsonObject.getString("idHomeTeam") ;
                    String idAwayTeam = jsonObject.getString("idAwayTeam") ;
                    String nameHomeTeam = jsonObject.getString("strHomeTeam") ;
                    String nameAwayTeam = jsonObject.getString("strAwayTeam") ;
                    String intHomeScore = jsonObject.getString("intHomeScore") ;
                    String intAwayScore = jsonObject.getString("intAwayScore") ;
                    String urlLogoHome = hashMapUrl.get(idHomeTeam) ;
                    String urlLogoAway = hashMapUrl.get(idAwayTeam) ;

                    String homeShots = jsonObject.getString("intHomeShots") ;
                    String awayShots = jsonObject.getString("intAwayShots") ;
                    String goalHomeDetails = jsonObject.getString("strHomeGoalDetails") ;
                    String goalAwayDetails = jsonObject.getString("strAwayGoalDetails") ;

                    String dateEvent = jsonObject.getString("dateEvent") ;
                    String timeEvent = jsonObject.getString("strTime") ;

                    MatchData matchData1 = new MatchData();
                    matchData1.setNama_home_team(nameHomeTeam);
                    matchData1.setNama_away_team(nameAwayTeam);
                    matchData1.setScore_home_team(intHomeScore);
                    matchData1.setScore_away_team(intAwayScore);
                    matchData1.setMatch_date(dateEvent);
                    matchData1.setMatch_time(timeEvent);
                    matchData1.setId_match(idEvent);
                    matchData1.setId_home_team(idHomeTeam);
                    matchData1.setId_away_team(idAwayTeam);
                    matchData1.setLogo_home_team(urlLogoHome);
                    matchData1.setLogo_away_team(urlLogoAway);

                    matchData1.setShots_home_team(homeShots);
                    matchData1.setShots_away_team(awayShots);

                    matchData1.setGoals_home_team(goalHomeDetails);
                    matchData1.setGoals_away_team(goalAwayDetails);

                    matchData.add(matchData1);
                }
                Collections.reverse(matchData);

                HomeActivity.this.GetNextMatch();
            } catch (Exception e) {
                new Handler().postDelayed(() -> progressBar.setVisibility(View.GONE), 3000);
            }
        }, error -> {
            new Handler().postDelayed(() -> {
                progressBar.setVisibility(View.GONE);
                GetData();
            }, 3000);
        }) ;

        stringRequest.setShouldCache(false) ;
        requestQueue.add(stringRequest) ;
    }

    void GetNextMatch(){
        String url = url_next_15_events ;
        RequestQueue requestQueue = Volley.newRequestQueue(this) ;
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try{
                JSONObject jsonEvent = new JSONObject(response) ;
                JSONArray jsonArray = jsonEvent.getJSONArray("events") ;
                for(int i = 0 ; i < jsonArray.length() ; i ++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i) ;
                    String idEvent = jsonObject.getString("idEvent") ;
                    String idHomeTeam = jsonObject.getString("idHomeTeam") ;
                    String idAwayTeam = jsonObject.getString("idAwayTeam") ;
                    String nameHomeTeam = jsonObject.getString("strHomeTeam") ;
                    String nameAwayTeam = jsonObject.getString("strAwayTeam") ;
                    String intHomeScore = jsonObject.getString("intHomeScore") ;
                    String intAwayScore = jsonObject.getString("intAwayScore") ;
                    String urlLogoHome = hashMapUrl.get(idHomeTeam) ;
                    String urlLogoAway = hashMapUrl.get(idAwayTeam) ;

                    String homeShots = jsonObject.getString("intHomeShots") ;
                    String awayShots = jsonObject.getString("intAwayShots") ;
                    String goalHomeDetails = jsonObject.getString("strHomeGoalDetails") ;
                    String goalAwayDetails = jsonObject.getString("strAwayGoalDetails") ;

                    String dateEvent = jsonObject.getString("dateEvent") ;
                    String timeEvent = jsonObject.getString("strTime") ;

                    MatchData matchData1 = new MatchData() ;
                    matchData1.setNama_home_team(nameHomeTeam);
                    matchData1.setNama_away_team(nameAwayTeam);
                    matchData1.setScore_home_team(intHomeScore);
                    matchData1.setScore_away_team(intAwayScore) ;
                    matchData1.setMatch_date(dateEvent);
                    matchData1.setMatch_time(timeEvent);
                    matchData1.setId_match(idEvent);
                    matchData1.setId_home_team(idHomeTeam);
                    matchData1.setId_away_team(idAwayTeam);
                    matchData1.setLogo_home_team(urlLogoHome);
                    matchData1.setLogo_away_team(urlLogoAway);

                    matchData1.setShots_home_team(homeShots);
                    matchData1.setShots_away_team(awayShots);

                    matchData1.setGoals_home_team(goalHomeDetails);
                    matchData1.setGoals_away_team(goalAwayDetails);

                    matchData.add(matchData1) ;
                }

                sqLite.deleteOldCachceEvents();
                for(int i = 0 ; i < matchData.size() ; i ++ ){
                    sqLite.addDataEvents(matchData.get(i));
                }

                preferences.edit().putLong("cache_event",new Date().getTime()).apply();
                showData();
            }catch (Exception e){
                new Handler().postDelayed(() -> progressBar.setVisibility(View.GONE), 3000) ;
            }
        }, error -> new Handler().postDelayed(() -> {
            progressBar.setVisibility(View.GONE);
            GetData() ;
        }, 3000)) ;

        stringRequest.setShouldCache(false) ;
        requestQueue.add(stringRequest) ;
    }

    void showData(){
        if (isSearch){
            ArrayList<MatchData> matchDataArrayList = sqLite.getEventsBySearch(SearchInput);
            matchAdapter.filterData(matchDataArrayList); ;
            progressBar.setVisibility(View.GONE);
            isLoad = true ;
        }else {
            matchAdapter.filterData(matchData); ;
            progressBar.setVisibility(View.GONE);
            isLoad = true ;
        }

    }

    void GetHashMapData(){
        hashMapUrl = new HashMap<>() ;
        String url = "https://www.thesportsdb.com/api/v1/json/1/search_all_teams.php?l=English%20Premier%20League" ;
        RequestQueue requestQueue = Volley.newRequestQueue(this) ;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try{
                JSONObject jsonEvent = new JSONObject(response) ;
                sqLite.deleteOldCachceTableTeam();
                JSONArray jsonArray = jsonEvent.getJSONArray("teams") ;
                for(int i = 0 ; i < jsonArray.length() ; i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i) ;
                    String idTeam = jsonObject.getString("idTeam") ;
                    String strTeamBadge = jsonObject.getString("strTeamBadge") ;
                    hashMapUrl.put(idTeam,strTeamBadge) ;
                    sqLite.addDataTableTeam(idTeam,strTeamBadge) ;
                }
                preferences.edit().putLong("cache_team",new Date().getTime()).apply();
                GetData() ;

            }catch (Exception e){
                new Handler().postDelayed(() -> GetHashMapData(), 3000) ;
            }
        }, error -> new Handler().postDelayed(() -> GetHashMapData(), 3000)) ;

        stringRequest.setShouldCache(false) ;
        requestQueue.add(stringRequest) ;
    }

    boolean cacheEventExpire(){
        long cacheEventTime = preferences.getLong("cache_event",0) ;
        if (cacheEventTime > 0){
            long currentTime = new Date().getTime() ;
            long difference = currentTime - cacheEventTime ;
            long seconds = difference / 1000 ;
            if (seconds > 60 * 60 * 24 * 30){
                return true ;
            }
        }

        return false ;
    }

    boolean cacheTeamExpire(){
        long cacheTeamTime = preferences.getLong("cache_team",0) ;
        if (cacheTeamTime > 0){
            long currenTime = new Date().getTime() ;
            long difference = currenTime - cacheTeamTime ;
            long seconds = difference / 1000 ;
            if (seconds > 60 * 60){
                return true ;
            }
        }
        return false ;
    }
}
