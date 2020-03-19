package com.bolasepak.bolasepak;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bolasepak.bolasepak.Adapter.EventAdapter;
import com.bolasepak.bolasepak.Event.MatchData;
import com.bolasepak.bolasepak.SQLite.SQLiteManager;
import com.bolasepak.bolasepak.Sensor.StepDetector;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class HomeScreen extends AppCompatActivity {

    RecyclerView recyclerView ;

    EventAdapter eventAdapter ;
    ArrayList<MatchData> matchDataArrayList;

    //StepCOunt
    TextView textCount;
    long numSteps;



    EditText etSearch ;

    String URL_PRIMER_ENGLISH_NEXT_15_EVENTS = "http://134.209.97.218:5050/api/v1/json/1/eventsnextleague.php?id=4328" ;
    String URL_PRIMER_ENGLISH_LAST_15_EVENTS = "http://134.209.97.218:5050/api/v1/json/1/eventspastleague.php?id=4328" ;

    ProgressBar progressBar ;

    HashMap<String,String> hashMapUrl ;

    SQLiteManager sqLiteManager ;
    SharedPreferences preferences ;


    boolean isDataLoaded = false ;
    boolean isSearch = false ;
    String searchKey = "" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        getSupportActionBar().hide();
        etSearch = findViewById(R.id.etSearch) ;
        progressBar = findViewById(R.id.progressBar) ;
        sqLiteManager = new SQLiteManager(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this) ;


        //INit stepcount
        textCount = findViewById(R.id.steps);

        recyclerView = findViewById(R.id.recyclerView) ;
        recyclerView.setHasFixedSize(true) ;
        final int columns = getResources().getInteger(R.integer.columns);
        recyclerView.setLayoutManager(new GridLayoutManager(this,columns));
        matchDataArrayList = new ArrayList<>() ;
        eventAdapter = new EventAdapter(this, matchDataArrayList,true) ;
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
        ArrayList<MatchData> cacheEvents = sqLiteManager.getEvents() ;


        boolean isCacheTeamExpire = cacheTeamExpire() ;
        boolean isCacheEventExpire = cacheEventExpire() ;

        if (cacheTeam.size() > 0 && !isCacheTeamExpire){
            hashMapUrl = cacheTeam ;

            if (cacheEvents.size() > 0 && !isCacheEventExpire){
                matchDataArrayList = cacheEvents;
                showData();
            }else{
                GetData() ;
            }
        }else{
            GetHashMapData() ;
        }

    }



    boolean cacheEventExpire(){
        long cacheEventTime = preferences.getLong("cache_event",0) ;
        if (cacheEventTime > 0){
            long currenTime = new Date().getTime() ;
            long difference = currenTime - cacheEventTime ;
            long seconds = difference / 1000 ;
            if (seconds > 1*60*60*24*30){
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
            if (seconds > 1*60*60){
                return true ;
            }
        }
        return false ;
    }

    void GetHashMapData(){
        hashMapUrl = new HashMap<>() ;
        String url = "https://www.thesportsdb.com/api/v1/json/1/search_all_teams.php?l=English%20Premier%20League" ;
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
        matchDataArrayList = new ArrayList<>() ;
        GetLastData() ;
    }

    void GetLastData(){
        String url = URL_PRIMER_ENGLISH_LAST_15_EVENTS ;
        RequestQueue requestQueue = Volley.newRequestQueue(this) ;
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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

                        MatchData matchData = new MatchData() ;
                        matchData.setNameHomeTeam(nameHomeTeam) ;
                        matchData.setNameAwayTeam(nameAwayTeam) ;
                        matchData.setHomeScore(intHomeScore) ;
                        matchData.setAwayScore(intAwayScore) ;
                        matchData.setDateEvent(dateEvent);
                        matchData.setTimeEvent(timeEvent);
                        matchData.setIdEvent(idEvent) ;
                        matchData.setIdHomeTeam(idHomeTeam);
                        matchData.setIdAwayTeam(idAwayTeam);
                        matchData.setUrlLogoHome(urlLogoHome) ;
                        matchData.setUrlLogoAway(urlLogoAway);

                        matchData.setIntHomeShots(homeShots);
                        matchData.setIntAwayShots(awayShots);

                        matchData.setHomeGoalsDetails(goalHomeDetails) ;
                        matchData.setAwayGoalsDetails(goalAwayDetails);

                        matchDataArrayList.add(matchData) ;
                    }
                    Collections.reverse(matchDataArrayList) ;

                    GetNextData() ;
                }catch (Exception e){
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    }, 4000) ;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        GetData() ;
                    }
                }, 4000) ;
            }
        }) ;

        stringRequest.setShouldCache(false) ;
        requestQueue.add(stringRequest) ;
    }

    void GetNextData(){
        String url = URL_PRIMER_ENGLISH_NEXT_15_EVENTS ;
        RequestQueue requestQueue = Volley.newRequestQueue(this) ;
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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

                        MatchData matchData = new MatchData() ;
                        matchData.setNameHomeTeam(nameHomeTeam) ;
                        matchData.setNameAwayTeam(nameAwayTeam) ;
                        matchData.setHomeScore(intHomeScore) ;
                        matchData.setAwayScore(intAwayScore) ;
                        matchData.setDateEvent(dateEvent);
                        matchData.setTimeEvent(timeEvent);
                        matchData.setIdEvent(idEvent) ;
                        matchData.setIdHomeTeam(idHomeTeam);
                        matchData.setIdAwayTeam(idAwayTeam);
                        matchData.setUrlLogoHome(urlLogoHome) ;
                        matchData.setUrlLogoAway(urlLogoAway);

                        matchData.setIntHomeShots(homeShots);
                        matchData.setIntAwayShots(awayShots);

                        matchData.setHomeGoalsDetails(goalHomeDetails) ;
                        matchData.setAwayGoalsDetails(goalAwayDetails);

                        matchDataArrayList.add(matchData) ;
                    }

                    sqLiteManager.deleteOldCachceEvents();
                    for(int i = 0; i < matchDataArrayList.size() ; i ++ ){
                        sqLiteManager.addDataEvents(matchDataArrayList.get(i));
                    }

                    preferences.edit().putLong("cache_event",new Date().getTime()).apply();
                    showData();
                }catch (Exception e){
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    }, 4000) ;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        GetData() ;
                    }
                }, 4000) ;
            }
        }) ;

        stringRequest.setShouldCache(false) ;
        requestQueue.add(stringRequest) ;
    }

    void showData(){
        if (isSearch){
            ArrayList<MatchData> matchDataArrToShow = sqLiteManager.getEventsBySearch(searchKey);
            eventAdapter.setFilter(matchDataArrToShow) ;
            progressBar.setVisibility(View.GONE);
            isDataLoaded = true ;
        }else {
            eventAdapter.setFilter(matchDataArrayList) ;
            progressBar.setVisibility(View.GONE);
            isDataLoaded = true ;
        }

    }

    // step function mulai

    @SuppressLint("StringFormatInvalid")
    @Override
    protected void onStart() {
        super.onStart();

        numSteps = preferences.getLong("steps", 0);

        textCount.setText(getResources().getString(R.string.langkah, numSteps));
    }

    @Override
    protected void onStop() {
        super.onStop();

        preferences.edit().putLong("step",numSteps).apply();
        //mSensorManager.unregisterListener((SensorEventListener) HomeScreen.this);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @SuppressLint("StringFormatInvalid")
    @Override
    protected void onResume() {
        super.onResume();

        numSteps = preferences.getLong("steps", 0);

        textCount.setText(getResources().getString(R.string.langkah, numSteps));


    }


    // sampe sini

}
