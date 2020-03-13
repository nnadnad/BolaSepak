package com.bolasepak.bolasepak.Event;

public class Notif {

    private String event_id;
    private String team_id;


    public Notif(){
    }

    public String getEventId(){
        return this.event_id;
    }

    public String getTeamId(){
        return this.team_id;
    }

    public void setTeamId(String new_id){
        this.team_id = new_id;
    }

    public void setEventId(String new_id){
        this.event_id = new_id;
    }




}
