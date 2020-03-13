package com.example.bolasepak;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.bolasepak.event.MatchData;
import com.example.bolasepak.event.Notif;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLite extends SQLiteOpenHelper {
    private static final String name = "cache_database";

    public SQLite(@Nullable Context context) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String teams = "CREATE TABLE IF NOT EXISTS team" +
                "(" +
                "id_team TEXT NOT NULL" + " , " +
                "logo TEXT NOT NULL" +
                ")";
        String events = "CREATE TABLE IF NOT EXISTS event_match" +
                "(" +
                "id_match TEXT NOT NULL" + " , " +
                "id_home_team TEXT NOT NULL" + " , " +
                "id_away_team TEXT NOT NULL" + " , " +
                "nama_home_team TEXT NOT NULL" + " , " +
                "nama_away_team TEXT NOT NULL" + " , " +
                "score_home_team TEXT NOT NULL" + " , " +
                "score_away_team TEXT NOT NULL" + " , " +
                "logo_home_team TEXT NOT NULL" + " , " +
                "logo_away_team TEXT NOT NULL" + " , " +
                "shots_home_team TEXT NOT NULL" + " , " +
                "shots_away_team TEXT NOT NULL" + " , " +
                "goals_home_team TEXT NOT NULL" + " , " +
                "goals_away_team TEXT NOT NULL" + " , " +
                "match_date TEXT NOT NULL" + " , " +
                "match_time TEXT NOT NULL" +
                ")";

        db.execSQL(teams);
        db.execSQL(events);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE team") ;
        db.execSQL("DROP TABLE event_match") ;
        onCreate(db) ;
    }

    //Get Match Events
    public void deleteOldCachceEvents(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        sqLiteDatabase.execSQL("DELETE FROM teams");
    }
    public void addDataEvents(MatchData matchData){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        ContentValues contentValues = new ContentValues() ;

        contentValues.put("id_event",matchData.getIdEvent()) ;
        contentValues.put("id_home",matchData.getIdHomeTeam()) ;
        contentValues.put("id_away",matchData.getIdAwayTeam()) ;
        contentValues.put("name_home_team",matchData.getNameHomeTeam()) ;
        contentValues.put("name_away_team",matchData.getNameAwayTeam()) ;
        contentValues.put("home_score",matchData.getHomeScore()) ;
        contentValues.put("away_score",matchData.getAwayScore()) ;
        contentValues.put("url_home",matchData.getUrlLogoHome()) ;
        contentValues.put("url_away",matchData.getUrlLogoAway()) ;
        contentValues.put("home_shots",matchData.getIntHomeShots()) ;
        contentValues.put("away_shots",matchData.getIntAwayShots()) ;
        contentValues.put("goal_home",matchData.getHomeGoalsDetails()) ;
        contentValues.put("goal_away",matchData.getAwayGoalsDetails()) ;
        contentValues.put("date_event",matchData.getDateEvent()) ;
        contentValues.put("time_event",matchData.getTimeEvent()) ;

        sqLiteDatabase.insert("event_match",null, contentValues) ;
        sqLiteDatabase.close();

    }
    public MatchData getSingleEvent(String id_match){
        MatchData data = new MatchData() ;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM event_match WHERE id_match = " + id_match,null) ;

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

                data = matchData ;

            }while (cursor.moveToNext()) ;
        }
        return data ;
    }
    ArrayList<MatchData> getEvents(){
        ArrayList<MatchData> data = new ArrayList<>() ;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM event_match",null) ;

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
    ArrayList<MatchData> getEventsBySearch(String key){
        ArrayList<MatchData> data = new ArrayList<>() ;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM event_match WHERE nama_home_team LIKE '%" + key + "%' OR nama_away_team LIKE '%" + key + "%'",null) ;

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
        sqLiteDatabase.execSQL("DELETE FROM team");
    }
    public void addDataTableTeam(String idTeam,String urlLogo){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        ContentValues contentValues = new ContentValues() ;

        contentValues.put("id_team",idTeam) ;
        contentValues.put("logo",urlLogo) ;

        sqLiteDatabase.insert("team",null, contentValues) ;
        sqLiteDatabase.close();
    }
    public String getSingleUrlLogoTeam(String idTeam){
        String url = "" ;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM team WHERE id_team = " + idTeam,null) ;

        if (cursor.moveToFirst()){
            do{
                url = cursor.getString(1);
            }while (cursor.moveToNext()) ;
        }
        return url ;
    }
    public HashMap<String,String> getDataTableTeam(){
        HashMap<String,String> data = new HashMap<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM team",null) ;

        if (cursor.moveToFirst()){
            do{
                String idTeam = cursor.getString(0) ;
                String urlLogo = cursor.getString(1) ;
                data.put(idTeam,urlLogo) ;
            }while (cursor.moveToNext()) ;
        }
        return data ;
    }



    //Next Event
    public void createTableNextMatch(String idTeam){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        String sql = "CREATE TABLE IF NOT EXISTS team" + idTeam + "next_event" +
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
    public void deleteDataNextMatch(String idTeam){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        sqLiteDatabase.execSQL("DELETE FROM team" + idTeam + "next_event");
    }
    public void addDataNextMatch(MatchData matchData,String idTeam){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        ContentValues contentValues = new ContentValues() ;

        contentValues.put("id_event",matchData.getIdEvent()) ;
        contentValues.put("id_home",matchData.getIdHomeTeam()) ;
        contentValues.put("id_away",matchData.getIdAwayTeam()) ;
        contentValues.put("name_home_team",matchData.getNameHomeTeam()) ;
        contentValues.put("name_away_team",matchData.getNameAwayTeam()) ;
        contentValues.put("home_score",matchData.getHomeScore()) ;
        contentValues.put("away_score",matchData.getAwayScore()) ;
        contentValues.put("url_home",matchData.getUrlLogoHome()) ;
        contentValues.put("url_away",matchData.getUrlLogoAway()) ;
        contentValues.put("home_shots",matchData.getIntHomeShots()) ;
        contentValues.put("away_shots",matchData.getIntAwayShots()) ;
        contentValues.put("goal_home",matchData.getHomeGoalsDetails()) ;
        contentValues.put("goal_away",matchData.getAwayGoalsDetails()) ;
        contentValues.put("date_event",matchData.getDateEvent()) ;
        contentValues.put("time_event",matchData.getTimeEvent()) ;

        sqLiteDatabase.insert("team" + idTeam + "next_event",null, contentValues) ;
        sqLiteDatabase.close();

    }
    public ArrayList<MatchData> getEventsNextMatch(String idTeam){
        ArrayList<MatchData> data = new ArrayList<>() ;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM team" + idTeam + "next_event",null) ;
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
    public void createTableLastMatch(String idTeam){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        String sql = "CREATE TABLE IF NOT EXISTS team" + idTeam + "last_event" +
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
    public void deleteDataLastMatch(String idTeam){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        sqLiteDatabase.execSQL("DELETE FROM team" + idTeam + "last_event");
    }
    public void addDataLastMatch(MatchData matchData,String idTeam){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        ContentValues contentValues = new ContentValues() ;

        contentValues.put("id_event",matchData.getIdEvent()) ;
        contentValues.put("id_home",matchData.getIdHomeTeam()) ;
        contentValues.put("id_away",matchData.getIdAwayTeam()) ;
        contentValues.put("name_home_team",matchData.getNameHomeTeam()) ;
        contentValues.put("name_away_team",matchData.getNameAwayTeam()) ;
        contentValues.put("home_score",matchData.getHomeScore()) ;
        contentValues.put("away_score",matchData.getAwayScore()) ;
        contentValues.put("url_home",matchData.getUrlLogoHome()) ;
        contentValues.put("url_away",matchData.getUrlLogoAway()) ;
        contentValues.put("home_shots",matchData.getIntHomeShots()) ;
        contentValues.put("away_shots",matchData.getIntAwayShots()) ;
        contentValues.put("goal_home",matchData.getHomeGoalsDetails()) ;
        contentValues.put("goal_away",matchData.getAwayGoalsDetails()) ;
        contentValues.put("date_event",matchData.getDateEvent()) ;
        contentValues.put("time_event",matchData.getTimeEvent()) ;

        sqLiteDatabase.insert("team" + idTeam + "last_event",null, contentValues) ;
        sqLiteDatabase.close();

    }
    public ArrayList<MatchData> getEventsLastMatch(String idTeam){
        ArrayList<MatchData> data = new ArrayList<>() ;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM team" + idTeam + "last_event",null) ;
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

    public void AddNotif(String event_id, String team_id){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        ContentValues contentValues = new ContentValues() ;

        contentValues.put("id_event",event_id) ;
        contentValues.put("id_team",team_id) ;
        sqLiteDatabase.insert("notif",null, contentValues);
        sqLiteDatabase.close();

    }

    public Notif GetNotif(String event_id, String team_id){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM notif WHERE id_team=" + team_id +
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

    int GetNumberOfEventNotif(String event_id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(*) AS count FROM notif WHERE id_event="
                + event_id + ";" ,null);
        if (cursor.moveToFirst()){
            return Integer.parseInt(cursor.getString(0));
        }

        return 0;
    }

    void DeleteNotif(String event_id, String team_id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        String sql_query = "DELETE FROM notif WHERE id_event=" + event_id +
                " AND id_team=" + team_id + ";";
        sqLiteDatabase.execSQL(sql_query);

        sqLiteDatabase.close();
    }


    public void DeleteEventNotif(String event_id){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase() ;
        String sql_query = "DELETE FROM notif WHERE id_event=" + event_id + ";";
        sqLiteDatabase.execSQL(sql_query);
        sqLiteDatabase.close();

//        Log.d("Event Notification","All notification for event with id:" + event_id
//                + " has been deleted");

    }
    


}
