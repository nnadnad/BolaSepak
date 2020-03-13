package com.example.bolasepak.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bolasepak.R;
import com.example.bolasepak.SQLite;
import com.example.bolasepak.adapter.MatchAdapter;
import com.example.bolasepak.event.MatchData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Sebelum extends Fragment {

    public String idTeam ;
    String URL_PRIMER_ENGLISH_LAST_5_EVENTS = "https://www.thesportsdb.com/api/v1/json/1/eventslast.php?id=" ;

    public Sebelum() {
        // Required empty public constructor
    }

    RecyclerView recyclerView ;
    MatchAdapter eventAdapter;
    ArrayList<MatchData> matchDataArrayList ;
    ProgressBar progressBar ;
    HashMap<String,String> hashMapUrl ;
    SQLite sqLiteManager ;
    SharedPreferences preferences ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.sebelum, container, false);

        progressBar = view.findViewById(R.id.progressBar) ;
        sqLiteManager = new SQLite(getActivity());
        recyclerView = view.findViewById(R.id.recyclerView) ;
        recyclerView.setHasFixedSize(true) ;
        final int columns = getResources().getInteger(R.integer.columns);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),columns));

        matchDataArrayList = new ArrayList<>() ;

        eventAdapter = new MatchAdapter(getActivity(),matchDataArrayList,false) ;
        recyclerView.setAdapter(eventAdapter) ;

        hashMapUrl = sqLiteManager.getDataTableTeam() ;

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity()) ;

        sqLiteManager.createTableTeamEvents_LastEvent(idTeam); ;
        ArrayList<MatchData> cache = sqLiteManager.getEvents_LastEvent(idTeam) ;
        boolean isCacheExpire = false ;
        long cacheTime = preferences.getLong("cache_team_events" + idTeam,0) ;
        if (cacheTime > 0){
            long currenTime = new Date().getTime() ;
            long difference = currenTime - cacheTime ;
            long seconds = difference / 1000 ;
            if (seconds > 40){
                isCacheExpire = true ;
            }
        }
        if (cache.size() > 0 && !isCacheExpire){
            matchDataArrayList = cache ;
            ShowData();
        }else{
            GetData();
        }


        return view ;
    }

    void GetData(){
        matchDataArrayList = new ArrayList<>() ;
        String url = URL_PRIMER_ENGLISH_LAST_5_EVENTS + idTeam;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity()) ;
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonEvent = new JSONObject(response) ;
                    JSONArray jsonArray = jsonEvent.getJSONArray("results") ;
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
                        if (urlLogoHome == null){
                            urlLogoHome = "" ;
                        }

                        String urlLogoAway = hashMapUrl.get(idAwayTeam) ;
                        if (urlLogoAway == null){
                            urlLogoAway = "" ;
                        }

                        String homeShots = jsonObject.getString("intHomeShots") ;
                        String awayShots = jsonObject.getString("intAwayShots") ;
                        String goalHomeDetails = jsonObject.getString("strHomeGoalDetails") ;
                        String goalAwayDetails = jsonObject.getString("strAwayGoalDetails") ;

                        String dateEvent = jsonObject.getString("dateEvent") ;
                        String timeEvent = jsonObject.getString("strTime") ;

                        MatchData eventPertandingan = new MatchData() ;
                        eventPertandingan.setNameHomeTeam(nameHomeTeam) ;
                        eventPertandingan.setNameAwayTeam(nameAwayTeam) ;
                        eventPertandingan.setHomeScore(intHomeScore) ;
                        eventPertandingan.setAwayScore(intAwayScore) ;
                        eventPertandingan.setDateEvent(dateEvent);
                        eventPertandingan.setTimeEvent(timeEvent);
                        eventPertandingan.setIdEvent(idEvent) ;
                        eventPertandingan.setIdHomeTeam(idHomeTeam);
                        eventPertandingan.setIdAwayTeam(idAwayTeam);
                        eventPertandingan.setUrlLogoHome(urlLogoHome) ;
                        eventPertandingan.setUrlLogoAway(urlLogoAway);

                        eventPertandingan.setIntHomeShots(homeShots);
                        eventPertandingan.setIntAwayShots(awayShots);

                        eventPertandingan.setHomeGoalsDetails(goalHomeDetails) ;
                        eventPertandingan.setAwayGoalsDetails(goalAwayDetails);

                        matchDataArrayList.add(eventPertandingan) ;
                    }

                    sqLiteManager.deleteOldCachceEvents_LastEvent(idTeam);
                    for(int i = 0 ; i < matchDataArrayList.size() ; i ++ ){
                        sqLiteManager.addDataEvent_LastEvent(matchDataArrayList.get(i),idTeam);
                    }

                    preferences.edit().putLong("cache_team_events" + idTeam,new Date().getTime()).apply();
                    ShowData();
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

    private void ShowData(){
        eventAdapter.setFilter(matchDataArrayList) ;
        progressBar.setVisibility(View.GONE);
    }

}