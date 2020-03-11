package com.example.bolasepak;

public class Event {
    private String home;
    private String away;
    private String home_score;
    private String away_score;
    private String event_date;

    public Event(String home, String away, String home_score, String away_score, String event_date) {
        this.home = home;
        this.away = away;
        this.home_score = home_score;
        this.away_score = away_score;
        this.event_date = event_date;
    }

    public String getHome() {
        return home;
    }

    public String getAway() {
        return away;
    }

    public String getHome_score() {
        return home_score;
    }

    public String getAway_score() {
        return away_score;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public void setHome_score(String home_score) {
        this.home_score = home_score;
    }

    public void setAway_score(String away_score) {
        this.away_score = away_score;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }
}
