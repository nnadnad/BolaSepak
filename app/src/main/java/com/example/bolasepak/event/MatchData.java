package com.example.bolasepak.event;

public class MatchData {
    private String id_match;
    private String nama_home_team, nama_away_team;
    private String score_home_team, score_away_team;
    private String match_date, match_time;
    private String id_home_team, id_away_team;
    private String logo_home_team, logo_away_team;
    private String shots_home_team, shots_away_team;
    private String goals_home_team, goals_away_team;


    //Method
    public MatchData(){}

    public String getId_match(){
        return id_match;
    }
    public void setId_match(String id_match){
        this.id_match = id_match;
    }

    public String getNama_home_team(){
        return nama_home_team;
    }
    public void setNama_home_team(String nama_home_team) {
        this.nama_home_team = nama_home_team;
    }

    public String getNama_away_team(){
        return nama_away_team;
    }
    public void setNama_away_team(String nama_away_team) {
        this.nama_away_team = nama_away_team;
    }

    public String getScore_home_team(){
        return score_home_team;
    }
    public void setScore_home_team(String score_home_team){
        this.score_home_team = score_home_team;
    }

    public String getScore_away_team(){
        return score_away_team;
    }
    public void setScore_away_team(String score_away_team){
        this.score_away_team = score_away_team;
    }

    public String getMatch_date(){
        return match_date;
    }
    public void setMatch_date(String match_date){
        this.match_date = match_date;
    }

    public String getMatch_time(){
        return match_time;
    }
    public void setMatch_time(String match_time){
        this.match_time = match_time;
    }

    public String getId_home_team(){
        return id_home_team;
    }
    public void setId_home_team(String id_home_team){
        this.id_home_team = id_home_team;
    }

    public String getId_away_team(){
        return id_away_team;
    }
    public void setId_away_team(String id_away_team){
        this.id_away_team = id_away_team;
    }

    public String getLogo_home_team(){
        return logo_home_team;
    }
    public void setLogo_home_team(String logo_home_team){
        this.logo_home_team = logo_home_team;
    }

    public String getLogo_away_team(){
        return logo_away_team;
    }
    public void setLogo_away_team(String logo_away_team){
        this.logo_away_team = logo_away_team;
    }

    public String getShots_home_team(){
        return shots_home_team;
    }
    public void setShots_home_team(String shots_home_team){
        this.shots_home_team = shots_home_team;
    }

    public String getShots_away_team(){
        return shots_away_team;
    }
    public void setShots_away_team(String shots_away_team){
        this.shots_away_team = shots_away_team;
    }

    public String getGoals_home_team(){
        return goals_home_team;
    }
    public void setGoals_home_team(String goals_home_team){
        this.goals_home_team = goals_home_team;
    }

    public String getGoals_away_team(){
        return goals_away_team;
    }
    public void setGoals_away_team(String goals_away_team){
        this.goals_away_team = goals_away_team;
    }



}
