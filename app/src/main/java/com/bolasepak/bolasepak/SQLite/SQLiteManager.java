package com.bolasepak.bolasepak.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bolasepak.bolasepak.Event.MatchData;
import com.bolasepak.bolasepak.Event.Notif;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteManager extends SQLiteOpenHelper
{
    public SQLiteManager(Context context){
        super(context,"android_cache",null,1) ;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS tb_team" + "(id TEXT NOT NULL, url TEXT NOT NULL);";
        String sql_2 = "CREATE TABLE IF NOT EXISTS tb_events" +
                "(" +
                "id_event TEXT NOT NULL" + " , " +
                "id_home TEXT NOT NULL" + " , " +
                "id_away TEXT NOT NULL" + " , " +
                "name_home_team TEXT NOT NULL" + " , " +
                "name_away_team TEXT NOT NULL" + " , " +
                "home_score TEXT NOT NULL" + " , " +
                "away_score TEXT NOT NULL" + " , " +
                "url_home TEXT NOT NULL" + " , " +
                "url_away TEXT NOT NULL" + " , " +
                "home_shots TEXT NOT NULL" + " , " +
                "away_shots TEXT NOT NULL" + " , " +
                "goal_home TEXT NOT NULL" + " , " +
                "goal_away TEXT NOT NULL" + " , " +
                "date_event TEXT NOT NULL" + " , " +
                "time_event TEXT NOT NULL" +
                ")";

        String notiftable = "CREATE TABLE IF NOT EXISTS tb_notifs(id_event TEXT NOT NULL , id_team TEXT NOT NULL);";

        db.execSQL(sql);
        db.execSQL(sql_2);
        db.execSQL(notiftable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE tb_team") ;
        db.execSQL("DROP TABLE tb_events") ;
        db.execSQL("DROP TABLE tb_notifs");
        onCreate(db) ;
    }

    //Next Event
    public void createTableTeamEvents_NextEvent(String idTeam){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        String sql = "CREATE TABLE IF NOT EXISTS tb_team_" + idTeam + "_next_event" +
                "(" +
                "id_event TEXT NOT NULL" + " , " +
                "id_home TEXT NOT NULL" + " , " +
                "id_away TEXT NOT NULL" + " , " +
                "name_home_team TEXT NOT NULL" + " , " +
                "name_away_team TEXT NOT NULL" + " , " +
                "home_score TEXT NOT NULL" + " , " +
                "away_score TEXT NOT NULL" + " , " +
                "url_home TEXT NOT NULL" + " , " +
                "url_away TEXT NOT NULL" + " , " +
                "home_shots TEXT NOT NULL" + " , " +
                "away_shots TEXT NOT NULL" + " , " +
                "goal_home TEXT NOT NULL" + " , " +
                "goal_away TEXT NOT NULL" + " , " +
                "date_event TEXT NOT NULL" + " , " +
                "time_event TEXT NOT NULL" +
                ")";
        sqLiteDatabase.execSQL(sql);
    }
    public void deleteOldCachceEvents_NextEvent(String idTeam){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        sqLiteDatabase.execSQL("DELETE FROM tb_team_" + idTeam + "_next_event");
    }
    public void addDataEvent_NextEvent(MatchData matchData, String idTeam){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        ContentValues contentValues = new ContentValues() ;

        contentValues.put("id_event", matchData.getIdEvent()) ;
        contentValues.put("id_home", matchData.getIdHomeTeam()) ;
        contentValues.put("id_away", matchData.getIdAwayTeam()) ;
        contentValues.put("name_home_team", matchData.getNameHomeTeam()) ;
        contentValues.put("name_away_team", matchData.getNameAwayTeam()) ;
        contentValues.put("home_score", matchData.getHomeScore()) ;
        contentValues.put("away_score", matchData.getAwayScore()) ;
        contentValues.put("url_home", matchData.getUrlLogoHome()) ;
        contentValues.put("url_away", matchData.getUrlLogoAway()) ;
        contentValues.put("home_shots", matchData.getIntHomeShots()) ;
        contentValues.put("away_shots", matchData.getIntAwayShots()) ;
        contentValues.put("goal_home", matchData.getHomeGoalsDetails()) ;
        contentValues.put("goal_away", matchData.getAwayGoalsDetails()) ;
        contentValues.put("date_event", matchData.getDateEvent()) ;
        contentValues.put("time_event", matchData.getTimeEvent()) ;

        sqLiteDatabase.insert("tb_team_" + idTeam + "_next_event",null, contentValues) ;
        sqLiteDatabase.close();

    }
    public ArrayList<MatchData> getEvents_NextEvent(String idTeam){
        ArrayList<MatchData> data = new ArrayList<>() ;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM tb_team_" + idTeam + "_next_event",null) ;
        if (cursor.moveToFirst()){
            do{
                MatchData matchData = new MatchData() ;

                matchData.setIdEvent(cursor.getString(0));
                matchData.setIdHomeTeam(cursor.getString(1));
                matchData.setIdAwayTeam(cursor.getString(2));
                matchData.setNameHomeTeam(cursor.getString(3));
                matchData.setNameAwayTeam(cursor.getString(4));
                matchData.setHomeScore(cursor.getString(5));
                matchData.setAwayScore(cursor.getString(6));
                matchData.setUrlLogoHome(cursor.getString(7));
                matchData.setUrlLogoAway(cursor.getString(8));
                matchData.setIntHomeShots(cursor.getString(9));
                matchData.setIntAwayShots(cursor.getString(10));
                matchData.setHomeGoalsDetails(cursor.getString(11));
                matchData.setAwayGoalsDetails(cursor.getString(12));
                matchData.setDateEvent(cursor.getString(13));
                matchData.setTimeEvent(cursor.getString(14));

                data.add(matchData);
            }while (cursor.moveToNext()) ;
        }
        return data ;
    }

    //Last Event
    public void createTableTeamEvents_LastEvent(String idTeam){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        String sql = "CREATE TABLE IF NOT EXISTS tb_team_" + idTeam + "_last_event" +
                "(" +
                "id_event TEXT NOT NULL" + " , " +
                "id_home TEXT NOT NULL" + " , " +
                "id_away TEXT NOT NULL" + " , " +
                "name_home_team TEXT NOT NULL" + " , " +
                "name_away_team TEXT NOT NULL" + " , " +
                "home_score TEXT NOT NULL" + " , " +
                "away_score TEXT NOT NULL" + " , " +
                "url_home TEXT NOT NULL" + " , " +
                "url_away TEXT NOT NULL" + " , " +
                "home_shots TEXT NOT NULL" + " , " +
                "away_shots TEXT NOT NULL" + " , " +
                "goal_home TEXT NOT NULL" + " , " +
                "goal_away TEXT NOT NULL" + " , " +
                "date_event TEXT NOT NULL" + " , " +
                "time_event TEXT NOT NULL" +
                ")";
        sqLiteDatabase.execSQL(sql);
    }
    public void deleteOldCachceEvents_LastEvent(String idTeam){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        sqLiteDatabase.execSQL("DELETE FROM tb_team_" + idTeam + "_last_event");
    }
    public void addDataEvent_LastEvent(MatchData matchData, String idTeam){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        ContentValues contentValues = new ContentValues() ;

        contentValues.put("id_event", matchData.getIdEvent()) ;
        contentValues.put("id_home", matchData.getIdHomeTeam()) ;
        contentValues.put("id_away", matchData.getIdAwayTeam()) ;
        contentValues.put("name_home_team", matchData.getNameHomeTeam()) ;
        contentValues.put("name_away_team", matchData.getNameAwayTeam()) ;
        contentValues.put("home_score", matchData.getHomeScore()) ;
        contentValues.put("away_score", matchData.getAwayScore()) ;
        contentValues.put("url_home", matchData.getUrlLogoHome()) ;
        contentValues.put("url_away", matchData.getUrlLogoAway()) ;
        contentValues.put("home_shots", matchData.getIntHomeShots()) ;
        contentValues.put("away_shots", matchData.getIntAwayShots()) ;
        contentValues.put("goal_home", matchData.getHomeGoalsDetails()) ;
        contentValues.put("goal_away", matchData.getAwayGoalsDetails()) ;
        contentValues.put("date_event", matchData.getDateEvent()) ;
        contentValues.put("time_event", matchData.getTimeEvent()) ;

        sqLiteDatabase.insert("tb_team_" + idTeam + "_last_event",null, contentValues) ;
        sqLiteDatabase.close();

    }
    public ArrayList<MatchData> getEvents_LastEvent(String idTeam){
        ArrayList<MatchData> data = new ArrayList<>() ;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM tb_team_" + idTeam + "_last_event",null) ;
        if (cursor.moveToFirst()){
            do{
                MatchData matchData = new MatchData() ;

                matchData.setIdEvent(cursor.getString(0));
                matchData.setIdHomeTeam(cursor.getString(1));
                matchData.setIdAwayTeam(cursor.getString(2));
                matchData.setNameHomeTeam(cursor.getString(3));
                matchData.setNameAwayTeam(cursor.getString(4));
                matchData.setHomeScore(cursor.getString(5));
                matchData.setAwayScore(cursor.getString(6));
                matchData.setUrlLogoHome(cursor.getString(7));
                matchData.setUrlLogoAway(cursor.getString(8));
                matchData.setIntHomeShots(cursor.getString(9));
                matchData.setIntAwayShots(cursor.getString(10));
                matchData.setHomeGoalsDetails(cursor.getString(11));
                matchData.setAwayGoalsDetails(cursor.getString(12));
                matchData.setDateEvent(cursor.getString(13));
                matchData.setTimeEvent(cursor.getString(14));

                data.add(matchData);
            }while (cursor.moveToNext()) ;
        }
        return data ;
    }

    //Events
    public void deleteOldCachceEvents(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        sqLiteDatabase.execSQL("DELETE FROM tb_events");
    }
    public void addDataEvents(MatchData matchData){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        ContentValues contentValues = new ContentValues() ;

        contentValues.put("id_event", matchData.getIdEvent()) ;
        contentValues.put("id_home", matchData.getIdHomeTeam()) ;
        contentValues.put("id_away", matchData.getIdAwayTeam()) ;
        contentValues.put("name_home_team", matchData.getNameHomeTeam()) ;
        contentValues.put("name_away_team", matchData.getNameAwayTeam()) ;
        contentValues.put("home_score", matchData.getHomeScore()) ;
        contentValues.put("away_score", matchData.getAwayScore()) ;
        contentValues.put("url_home", matchData.getUrlLogoHome()) ;
        contentValues.put("url_away", matchData.getUrlLogoAway()) ;
        contentValues.put("home_shots", matchData.getIntHomeShots()) ;
        contentValues.put("away_shots", matchData.getIntAwayShots()) ;
        contentValues.put("goal_home", matchData.getHomeGoalsDetails()) ;
        contentValues.put("goal_away", matchData.getAwayGoalsDetails()) ;
        contentValues.put("date_event", matchData.getDateEvent()) ;
        contentValues.put("time_event", matchData.getTimeEvent()) ;

        sqLiteDatabase.insert("tb_events",null, contentValues) ;
        sqLiteDatabase.close();

    }
    public MatchData getSingleEvent(String idEvent){
        MatchData data = new MatchData() ;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM tb_events WHERE id_event = " + idEvent,null) ;

        if (cursor.moveToFirst()){
            do{
                MatchData matchData = new MatchData() ;
                matchData.setIdEvent(cursor.getString(0));
                matchData.setIdHomeTeam(cursor.getString(1));
                matchData.setIdAwayTeam(cursor.getString(2));
                matchData.setNameHomeTeam(cursor.getString(3));
                matchData.setNameAwayTeam(cursor.getString(4));
                matchData.setHomeScore(cursor.getString(5));
                matchData.setAwayScore(cursor.getString(6));
                matchData.setUrlLogoHome(cursor.getString(7));
                matchData.setUrlLogoAway(cursor.getString(8));
                matchData.setIntHomeShots(cursor.getString(9));
                matchData.setIntAwayShots(cursor.getString(10));
                matchData.setHomeGoalsDetails(cursor.getString(11));
                matchData.setAwayGoalsDetails(cursor.getString(12));
                matchData.setDateEvent(cursor.getString(13));
                matchData.setTimeEvent(cursor.getString(14));

                data = matchData;

            }while (cursor.moveToNext()) ;
        }
        return data ;
    }
    public ArrayList<MatchData> getEvents(){
        ArrayList<MatchData> data = new ArrayList<>() ;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM tb_events",null) ;

        if (cursor.moveToFirst()){
            do{
                MatchData matchData = new MatchData() ;

                matchData.setIdEvent(cursor.getString(0));
                matchData.setIdHomeTeam(cursor.getString(1));
                matchData.setIdAwayTeam(cursor.getString(2));
                matchData.setNameHomeTeam(cursor.getString(3));
                matchData.setNameAwayTeam(cursor.getString(4));
                matchData.setHomeScore(cursor.getString(5));
                matchData.setAwayScore(cursor.getString(6));
                matchData.setUrlLogoHome(cursor.getString(7));
                matchData.setUrlLogoAway(cursor.getString(8));
                matchData.setIntHomeShots(cursor.getString(9));
                matchData.setIntAwayShots(cursor.getString(10));
                matchData.setHomeGoalsDetails(cursor.getString(11));
                matchData.setAwayGoalsDetails(cursor.getString(12));
                matchData.setDateEvent(cursor.getString(13));
                matchData.setTimeEvent(cursor.getString(14));

                data.add(matchData);
            }while (cursor.moveToNext()) ;
        }
        return data ;
    }
    public ArrayList<MatchData> getEventsBySearch(String key){
        ArrayList<MatchData> data = new ArrayList<>() ;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM tb_events WHERE name_home_team LIKE '%" + key + "%' OR name_away_team LIKE '%" + key + "%'",null) ;

        if (cursor.moveToFirst()){
            do{
                MatchData matchData = new MatchData() ;

                matchData.setIdEvent(cursor.getString(0));
                matchData.setIdHomeTeam(cursor.getString(1));
                matchData.setIdAwayTeam(cursor.getString(2));
                matchData.setNameHomeTeam(cursor.getString(3));
                matchData.setNameAwayTeam(cursor.getString(4));
                matchData.setHomeScore(cursor.getString(5));
                matchData.setAwayScore(cursor.getString(6));
                matchData.setUrlLogoHome(cursor.getString(7));
                matchData.setUrlLogoAway(cursor.getString(8));
                matchData.setIntHomeShots(cursor.getString(9));
                matchData.setIntAwayShots(cursor.getString(10));
                matchData.setHomeGoalsDetails(cursor.getString(11));
                matchData.setAwayGoalsDetails(cursor.getString(12));
                matchData.setDateEvent(cursor.getString(13));
                matchData.setTimeEvent(cursor.getString(14));

                data.add(matchData);
            }while (cursor.moveToNext()) ;
        }
        return data ;
    }

    //TableTeam
    public void deleteOldCachceTableTeam(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        sqLiteDatabase.execSQL("DELETE FROM tb_team");
    }
    public void addDataTableTeam(String idTeam,String urlLogo){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        ContentValues contentValues = new ContentValues() ;

        contentValues.put("id",idTeam) ;
        contentValues.put("url",urlLogo) ;

        sqLiteDatabase.insert("tb_team",null, contentValues) ;
        sqLiteDatabase.close();
    }
    public String getSingleUrlLogoTeam(String idTeam){
        String url = "" ;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM tb_team WHERE id = " + idTeam,null) ;

        if (cursor.moveToFirst()){
            do{
                String urlLogo = cursor.getString(1) ;
                url = urlLogo ;
            }while (cursor.moveToNext()) ;
        }
        return url ;
    }
    public HashMap<String,String> getDataTableTeam(){
        HashMap<String,String> data = new HashMap<String,String>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM tb_team",null) ;

        if (cursor.moveToFirst()){
            do{
                String idTeam = cursor.getString(0) ;
                String urlLogo = cursor.getString(1) ;
                data.put(idTeam,urlLogo) ;
            }while (cursor.moveToNext()) ;
        }
        return data ;
    }

    public void AddNotif(String event_id, String team_id){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        ContentValues contentValues = new ContentValues() ;

        contentValues.put("id_event",event_id) ;
        contentValues.put("id_team",team_id) ;
        sqLiteDatabase.insert("tb_notifs",null, contentValues);
        sqLiteDatabase.close();

    }

    public Notif GetNotif(String event_id, String team_id){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM tb_notifs WHERE id_team=" + team_id +
                " AND id_event = " + event_id + ";",null);
        if(cursor.moveToFirst()){
            Notif n = new Notif();
            do {
                n.setEventId(cursor.getString(0));
                n.setTeamId(cursor.getString(1));
            } while (cursor.moveToNext());

            return n;

        } else {

            return null;
        }
    }

    // Get number of notif in database
    // needed for deletion of notif of two subscribed team
    public int GetNumberOfEventNotif(String event_id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(*) AS count FROM tb_notifs WHERE id_event="
                + event_id + ";" ,null);
        if (cursor.moveToFirst()){
            return Integer.parseInt(cursor.getString(0));
        }

        return 0;
    }

    public void DeleteNotif(String event_id, String team_id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        String sql_query = "DELETE FROM tb_notifs WHERE id_event=" + event_id +
                " AND id_team=" + team_id + ";";
        sqLiteDatabase.execSQL(sql_query);

        sqLiteDatabase.close();
    }


    public void DeleteEventNotif(String event_id){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        String sql_query = "DELETE FROM tb_notifs WHERE id_event=" + event_id + ";";
        sqLiteDatabase.execSQL(sql_query);
        sqLiteDatabase.close();

        Log.d("Event Notification","All notification for event with id:" + event_id
                + " has been deleted");

    }

}
