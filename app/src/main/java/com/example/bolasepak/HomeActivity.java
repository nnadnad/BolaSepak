package com.example.bolasepak;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
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

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<MatchData> matchData;
    MatchAdapter matchAdapter;
    HashMap<String,String> hashMapUrl;
    ProgressBar progressBar;

    //Untuk Search
    EditText searchText;

    //Untuk databse SQLLite
    SQLite sqLiteManager ;
    SharedPreferences preferences ;

    String BASE_URL = "https://www.thesportsdb.com/api/v1/json/1";
    String url_past_15_events = BASE_URL + "/eventspastleague.php?id=4328";
    String url_next_15_events = BASE_URL + "/eventsnextleague.php?id=4328";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        getSupportActionBar().hide();
        etSearch = findViewById(R.id.etSearch) ;
        progressBar = findViewById(R.id.progressBar) ;
        sqLiteManager = new SQLiteManager(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this) ;

        recyclerView = findViewById(R.id.recyclerView) ;
        recyclerView.setHasFixedSize(true) ;
        final int columns = getResources().getInteger(R.integer.columns);
        recyclerView.setLayoutManager(new GridLayoutManager(this,columns));
        eventPertandinganArrayList = new ArrayList<>() ;
        eventAdapter = new EventAdapter(this,eventPertandinganArrayList,true) ;
        recyclerView.setAdapter(eventAdapter) ;

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isDataLoaded){
                    if (s.toString().trim().equals("")){
                        isSearch = false ;
                        showData();
                    }else {
                        isSearch = true ;
                        searchKey = s.toString().trim() ;
                        showData();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        HashMap<String,String> cacheTeam = sqLiteManager.getDataTableTeam() ;
        ArrayList<EventPertandingan> cacheEvents = sqLiteManager.getEvents() ;

        boolean isCacheTeamExpire = cacheTeamExpire() ;
        boolean isCacheEventExpire = cacheEventExpire() ;

        if (cacheTeam.size() > 0 && !isCacheTeamExpire){
            hashMapUrl = cacheTeam ;

            if (cacheEvents.size() > 0 && !isCacheEventExpire){
                eventPertandinganArrayList = cacheEvents;
                showData();
            }else{
                GetData() ;
            }
        }else{
            GetHashMapData() ;
        }

    }

    void GetHashMapData(){
        hashMapUrl = new HashMap<>() ;
        String url = BASE_URL + "/search_all_teams.php?l=English%20Premier%20League" ;
        RequestQueue requestQueue = Volley.newRequestQueue(this) ;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonEvent = new JSONObject(response) ;
                    sqLiteManager.deleteOldCachceTableTeam();
                    JSONArray jsonArray = jsonEvent.getJSONArray("teams") ;
                    for(int i = 0 ; i < jsonArray.length() ; i ++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i) ;
                        String idTeam = jsonObject.getString("idTeam") ;
                        String strTeamBadge = jsonObject.getString("strTeamBadge") ;
                        hashMapUrl.put(idTeam,strTeamBadge) ;
                        sqLiteManager.addDataTableTeam(idTeam,strTeamBadge) ;
                    }
                    preferences.edit().putLong("cache_team",new Date().getTime()).apply();
                    GetData() ;

                }catch (Exception e){
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            GetHashMapData();
                        }
                    }, 4000) ;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        GetHashMapData();
                    }
                }, 4000) ;
            }
        }) ;

        stringRequest.setShouldCache(false) ;
        requestQueue.add(stringRequest) ;
    }

    void GetData(){
        matchData = new ArrayList<>() ;
        GetLastEvent(); ;
    }

    void GetLastEvent(){
        String url = url_past_15_events ;
        RequestQueue requestQueue = Volley.newRequestQueue(this) ;
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try{

                JSONObject jsonEvent = new JSONObject(response) ;
                JSONArray jsonArray = jsonEvent.getJSONArray("events") ;
                for(int i = 0 ; i < jsonArray.length() ; i ++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i) ;
                    String idEvent = jsonObject.getString("id_match") ;
                    String idHomeTeam = jsonObject.getString("id_home_team") ;
                    String idAwayTeam = jsonObject.getString("id_away_team") ;
                    String nameHomeTeam = jsonObject.getString("strHomeTeam") ;
                    String nameAwayTeam = jsonObject.getString("strAwayTeam") ;
                    String intHomeScore = jsonObject.getString("intHomeScore") ;
                    String intAwayScore = jsonObject.getString("intAwayScore") ;
                    String urlLogoHome = hashMapUrl.get(idHomeTeam) ;
                    String urlLogoAway = hashMapUrl.get(idAwayTeam) ;

                    String homeShots = jsonObject.getString("shots_home_team") ;
                    String awayShots = jsonObject.getString("shots_away_team") ;
                    String goalHomeDetails = jsonObject.getString("strHomeGoalDetails") ;
                    String goalAwayDetails = jsonObject.getString("strAwayGoalDetails") ;

                    String dateEvent = jsonObject.getString("match_date") ;
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
                Collections.reverse(matchData) ;

                GetNextEvent() ;
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

    void GetNextEvent(){
        String url = url_next_15_events ;
        RequestQueue requestQueue = Volley.newRequestQueue(this) ;
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try{

                JSONObject jsonEvent = new JSONObject(response) ;
                JSONArray jsonArray = jsonEvent.getJSONArray("events") ;
                for(int i = 0 ; i < jsonArray.length() ; i ++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i) ;
                    String idEvent = jsonObject.getString("id_match") ;
                    String idHomeTeam = jsonObject.getString("id_home_team") ;
                    String idAwayTeam = jsonObject.getString("id_away_team") ;
                    String nameHomeTeam = jsonObject.getString("strHomeTeam") ;
                    String nameAwayTeam = jsonObject.getString("strAwayTeam") ;
                    String intHomeScore = jsonObject.getString("intHomeScore") ;
                    String intAwayScore = jsonObject.getString("intAwayScore") ;
                    String urlLogoHome = hashMapUrl.get(idHomeTeam) ;
                    String urlLogoAway = hashMapUrl.get(idAwayTeam) ;

                    String homeShots = jsonObject.getString("shots_home_team") ;
                    String awayShots = jsonObject.getString("shots_away_team") ;
                    String goalHomeDetails = jsonObject.getString("strHomeGoalDetails") ;
                    String goalAwayDetails = jsonObject.getString("strAwayGoalDetails") ;

                    String dateEvent = jsonObject.getString("match_date") ;
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

                sqLiteManager.deleteOldCachceEvents();
                for(int i = 0 ; i < eventPertandinganArrayList.size() ; i ++ ){
                    sqLiteManager.addDataEvents(eventPertandinganArrayList.get(i));
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
            ArrayList<EventPertandingan> eventPertandinganArrToShow = sqLiteManager.getEventsBySearch(searchKey);
            eventAdapter.setFilter(eventPertandinganArrToShow) ;
            progressBar.setVisibility(View.GONE);
            isDataLoaded = true ;
        }else {
            eventAdapter.setFilter(eventPertandinganArrayList) ;
            progressBar.setVisibility(View.GONE);
            isDataLoaded = true ;
        }

    }
}
