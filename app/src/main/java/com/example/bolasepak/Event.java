package com.example.bolasepak;

public class Event {
    private String home;
    private String away;
    private String home_score;
    private String away_score;

    public Event(String home, String away, String home_score, String away_score) {
        this.home = home;
        this.away = away;
        this.home_score = home_score;
        this.away_score = away_score;
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
}
