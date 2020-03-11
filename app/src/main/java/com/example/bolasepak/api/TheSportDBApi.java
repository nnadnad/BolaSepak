package com.example.bolasepak.api;

import com.example.bolasepak.BuildConfig;

public class TheSportDBApi {
    public String getEvent(String league, String event) {
        return BuildConfig.BASE_URL + "api/v1/json/${BuildConfig.TSDB_API_KEY}" + "/"+event+".php?id="+league;
    }

    public String getEventDetail(String eventID) {
        return BuildConfig.BASE_URL + "api/v1/json/${BuildConfig.TSDB_API_KEY}" + "/lookupevent.php?id="+eventID;
    }
}
