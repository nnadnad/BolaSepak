package com.bolasepak.bolasepak.Event;

public class MatchData
{
    private String idEvent ;
    private String nameHomeTeam, nameAwayTeam ;
    private String homeScore, awayScore ;
    private String dateEvent ;
    private String timeEvent ;

    private String idHomeTeam , idAwayTeam ;
    private String urlLogoHome , urlLogoAway ;

    private String intHomeShots , intAwayShots;
    private String homeGoalsDetails, awayGoalsDetails ;

    public MatchData() {
    }

    public String getIntHomeShots() {
        return intHomeShots;
    }

    public void setIntHomeShots(String intHomeShots) {
        this.intHomeShots = intHomeShots;
    }

    public String getIntAwayShots() {
        return intAwayShots;
    }

    public void setIntAwayShots(String intAwayShots) {
        this.intAwayShots = intAwayShots;
    }

    public String getHomeGoalsDetails() {
        return homeGoalsDetails;
    }

    public void setHomeGoalsDetails(String homeGoalsDetails) {
        this.homeGoalsDetails = homeGoalsDetails;
    }

    public String getAwayGoalsDetails() {
        return awayGoalsDetails;
    }

    public void setAwayGoalsDetails(String awayGoalsDetails) {
        this.awayGoalsDetails = awayGoalsDetails;
    }

    public String getUrlLogoHome() {
        return urlLogoHome;
    }

    public void setUrlLogoHome(String urlLogoHome) {
        this.urlLogoHome = urlLogoHome;
    }

    public String getUrlLogoAway() {
        return urlLogoAway;
    }

    public void setUrlLogoAway(String urlLogoAway) {
        this.urlLogoAway = urlLogoAway;
    }

    public String getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public String getIdHomeTeam() {
        return idHomeTeam;
    }

    public void setIdHomeTeam(String idHomeTeam) {
        this.idHomeTeam = idHomeTeam;
    }

    public String getIdAwayTeam() {
        return idAwayTeam;
    }

    public void setIdAwayTeam(String idAwayTeam) {
        this.idAwayTeam = idAwayTeam;
    }

    public String getTimeEvent() {
        return timeEvent;
    }

    public void setTimeEvent(String timeEvent) {
        this.timeEvent = timeEvent;
    }

    public String getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(String dateEvent) {
        this.dateEvent = dateEvent;
    }

    public String getNameHomeTeam() {
        return nameHomeTeam;
    }

    public void setNameHomeTeam(String nameHomeTeam) {
        this.nameHomeTeam = nameHomeTeam;
    }

    public String getNameAwayTeam() {
        return nameAwayTeam;
    }

    public void setNameAwayTeam(String nameAwayTeam) {
        this.nameAwayTeam = nameAwayTeam;
    }

    public String getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(String homeScore) {
        this.homeScore = homeScore;
    }

    public String getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(String awayScore) {
        this.awayScore = awayScore;
    }
}
