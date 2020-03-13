package com.example.bolasepak;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.bolasepak.event.MatchData;

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

        contentValues.put("id_event",matchData.getId_match()) ;
        contentValues.put("id_home",matchData.getId_home_team()) ;
        contentValues.put("id_away",matchData.getId_away_team()) ;
        contentValues.put("name_home_team",matchData.getNama_home_team()) ;
        contentValues.put("name_away_team",matchData.getNama_away_team()) ;
        contentValues.put("home_score",matchData.getScore_home_team()) ;
        contentValues.put("away_score",matchData.getScore_away_team()) ;
        contentValues.put("url_home",matchData.getLogo_home_team()) ;
        contentValues.put("url_away",matchData.getLogo_away_team()) ;
        contentValues.put("home_shots",matchData.getShots_home_team()) ;
        contentValues.put("away_shots",matchData.getShots_away_team()) ;
        contentValues.put("goal_home",matchData.getGoals_home_team()) ;
        contentValues.put("goal_away",matchData.getGoals_away_team()) ;
        contentValues.put("date_event",matchData.getMatch_date()) ;
        contentValues.put("time_event",matchData.getMatch_time()) ;

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
                matchData.setId_match(cursor.getString(0));
                matchData.setId_home_team(cursor.getString(1));
                matchData.setId_away_team(cursor.getString(2));
                matchData.setNama_home_team(cursor.getString(3));
                matchData.setNama_away_team(cursor.getString(4));
                matchData.setScore_home_team(cursor.getString(5));
                matchData.setScore_away_team(cursor.getString(6));
                matchData.setLogo_home_team(cursor.getString(7));
                matchData.setLogo_away_team(cursor.getString(8));
                matchData.setShots_home_team(cursor.getString(9));
                matchData.setShots_away_team(cursor.getString(10));
                matchData.setGoals_home_team(cursor.getString(11));
                matchData.setGoals_away_team(cursor.getString(12));
                matchData.setMatch_date(cursor.getString(13));
                matchData.setMatch_time(cursor.getString(14));

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

                matchData.setId_match(cursor.getString(0));
                matchData.setId_home_team(cursor.getString(1));
                matchData.setId_away_team(cursor.getString(2));
                matchData.setNama_home_team(cursor.getString(3));
                matchData.setNama_away_team(cursor.getString(4));
                matchData.setScore_home_team(cursor.getString(5));
                matchData.setScore_away_team(cursor.getString(6));
                matchData.setLogo_home_team(cursor.getString(7));
                matchData.setLogo_away_team(cursor.getString(8));
                matchData.setShots_home_team(cursor.getString(9));
                matchData.setShots_away_team(cursor.getString(10));
                matchData.setGoals_home_team(cursor.getString(11));
                matchData.setGoals_away_team(cursor.getString(12));
                matchData.setMatch_date(cursor.getString(13));
                matchData.setMatch_time(cursor.getString(14));

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
                matchData.setId_match(cursor.getString(0));
                matchData.setId_home_team(cursor.getString(1));
                matchData.setId_away_team(cursor.getString(2));
                matchData.setNama_home_team(cursor.getString(3));
                matchData.setNama_away_team(cursor.getString(4));
                matchData.setScore_home_team(cursor.getString(5));
                matchData.setScore_away_team(cursor.getString(6));
                matchData.setLogo_home_team(cursor.getString(7));
                matchData.setLogo_away_team(cursor.getString(8));
                matchData.setShots_home_team(cursor.getString(9));
                matchData.setShots_away_team(cursor.getString(10));
                matchData.setGoals_home_team(cursor.getString(11));
                matchData.setGoals_away_team(cursor.getString(12));
                matchData.setMatch_date(cursor.getString(13));
                matchData.setMatch_time(cursor.getString(14));

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


}
