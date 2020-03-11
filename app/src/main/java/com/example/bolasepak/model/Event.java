package com.example.bolasepak.model;

import com.google.gson.annotations.SerializedName;

public class Event {
    @SerializedName("idEvent")
    public String eventID = null;
    @SerializedName("dateEvent")
    public String eventDate = null;

    //home team

    @SerializedName("idHomeTeam")
    public String idHome = null;
    @SerializedName("strHomeTeam")
    public String teamHome = null;
    @SerializedName("intHomeScore")
    public String scoreHome = null;

    //away team

    @SerializedName("idAwayTeam")
    public String idAway = null;
    @SerializedName("strAwayTeam")
    public String teamAway = null;
    @SerializedName("intAwayScore")
    public String scoreAway = null;
}
