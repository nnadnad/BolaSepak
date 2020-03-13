package com.example.bolasepak;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.bolasepak.adapter.PageAdapter;
import com.example.bolasepak.event.MatchData;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class TeamDetail extends AppCompatActivity {

    TabLayout tabLayout ;
    ViewPager viewPager ;
    TabItem tabSekarang, tabSebelum ;
    public PageAdapter pageAdapter;

    String namaTim ;
    String urlLogoTim ;

    TextView tvNamaTim ;
    ImageView ivLogoTim ;

    String idTeam ;

    SQLite sqLiteManager ;

    SharedPreferences preferences ;

    private final static String NOTIFICATION_CHANNEL = "primary_notification_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_detail);

        Objects.requireNonNull(getSupportActionBar()).hide();
        sqLiteManager = new SQLite(this) ;

        GetIntentExtra() ;
        tvNamaTim = findViewById(R.id.tvNamaTim) ;
        ivLogoTim = findViewById(R.id.ivLogoTim) ;

        tvNamaTim.setText(namaTim) ;
        Picasso.get().load(urlLogoTim).into(ivLogoTim);

        tabLayout = findViewById(R.id.tabLayout) ;
        tabSekarang = findViewById(R.id.tabSekarang) ;
        tabSebelum = findViewById(R.id.tabSebelum) ;
        viewPager = findViewById(R.id.viewPager) ;

        pageAdapter = new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),idTeam) ;
        viewPager.setAdapter(pageAdapter) ;
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition()) ;
                if (tab.getPosition() == 0){
                    pageAdapter.notifyDataSetChanged();
                }else if (tab.getPosition() == 1){
                    pageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        final Button btnSubscribe = findViewById(R.id.btnSubscribe) ;
        preferences = PreferenceManager.getDefaultSharedPreferences(this) ;
        boolean isSubscribed = preferences.getBoolean("sub_" + idTeam,false) ;
        if (isSubscribed){
            btnSubscribe.setBackgroundResource(R.drawable.button_clicked);
        }

        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSubscribed = preferences.getBoolean("sub_" + idTeam,false) ;
                if (isSubscribed){
                    btnSubscribe.setBackgroundResource(R.drawable.button_released);
                    preferences.edit().putBoolean("sub_" + idTeam,false).apply();
                    deleteMatchesAlarms(idTeam);
                }else {
                    btnSubscribe.setBackgroundResource(R.drawable.button_clicked);
                    preferences.edit().putBoolean("sub_" + idTeam,true).apply();
                    createMatchesAlarm(idTeam);
                }
            }
        });
    }

    void GetIntentExtra(){
        idTeam = Objects.requireNonNull(getIntent().getExtras()).getString("id_team") ;
        namaTim = getIntent().getExtras().getString("name_team") ;
        urlLogoTim = sqLiteManager.getSingleUrlLogoTeam(idTeam) ;
    }


    void createMatchesAlarm(String idTeam) {

        // get all matches from db
        ArrayList<MatchData> matches = sqLiteManager.getEvents_NextEvent(idTeam);
        for (MatchData match : matches){
            createNotificationPendingIntent(match,idTeam);
            Log.d("PendingIntent","Create PendingIntent with request_code = " + match.getIdEvent());
        }

        // For > Oreo
        createNotificationChannel();

        Toast.makeText(TeamDetail.this, "Thank You for Your SUbscribe", Toast.LENGTH_SHORT).
                show();

    }

    void deleteMatchesAlarms(String idTeam) {

        Log.d("Info","Masuk ke deletion");

        // get all pendingIntent with eventIds
        final NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final AlarmManager mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent notifyIntent = new Intent(this, AlarmReceiver.class);

        // get list of matches
        ArrayList<MatchData>matches = sqLiteManager.getEvents_NextEvent(idTeam);

        for (MatchData match : matches){
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    Integer.parseInt(match.getIdEvent()),
                    notifyIntent,
                    PendingIntent.FLAG_NO_CREATE
            );
            if (pendingIntent!=null){

                //deletion
                sqLiteManager.DeleteNotif(match.getIdEvent(),idTeam);
                if(sqLiteManager.GetNumberOfEventNotif(match.getIdEvent())>0){
                    Log.d("Info","opponent team subscribed. Will not delete pending Intent");
                } else {
                    mNotificationManager.cancel(Integer.parseInt(match.getIdEvent()));
                    Log.d(
                            "PendingIntent deletion",
                            "PendingIntent" + match.getIdEvent() + " will be cancelled");
                    mAlarmManager.cancel(pendingIntent);
                    pendingIntent.cancel();
                }

            }
        }

        Toast.makeText(
                TeamDetail.this,
                "You've unsubscribed from this team",
                Toast.LENGTH_SHORT).show();
    }


    private void createNotificationChannel(){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            NotificationChannel mNotificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL,
                    "Match Notification",
                    NotificationManager.IMPORTANCE_HIGH
            );

            mNotificationChannel.enableLights(true);
            mNotificationChannel.enableVibration(true);
            mNotificationChannel.setLightColor(Color.RED);
            mNotificationChannel.setDescription(
                    "Notifies user 30 minutes before a subscribed team starts a match");

            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(mNotificationChannel);
        }
    }
    // helper functions, especially in creating notification pending intents
    public void createNotificationPendingIntent(MatchData match, String id_team){

        long time_diff;
        long trigger_time;
        Intent notifyIntent;
        PendingIntent pendingNotifyIntent;

        // get trigger time

        String match_time_str = match.getDateEvent() + " " + match.getTimeEvent();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try{

            Date match_time = format.parse(match_time_str);
            Date current_time = Calendar.getInstance().getTime();

            time_diff = getIntentAlarmTime(current_time,match_time);


        } catch (Exception e){
            Log.d("Exception : ",e.getMessage());
            time_diff = -1;

        }

        // register only if time valid
        if(time_diff>=0){

            notifyIntent = new Intent(this,AlarmReceiver.class);
            addNotificationIntentExtras(notifyIntent,match);

            trigger_time = time_diff + SystemClock.elapsedRealtime();

            // Add pending intent
            pendingNotifyIntent = PendingIntent.getBroadcast(
                    this,
                    Integer.parseInt(match.getIdEvent()),
                    notifyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );

            // register to database and alarm
            registerNotification(pendingNotifyIntent,match.getIdEvent(),id_team,trigger_time);


        }

    }
//    private long getIntentAlarmTime(Date current_time, Date match_time){
//
//        long time_diff = match_time.getTime() - current_time.getTime();
//        if(time_diff < 0){
//            time_diff = 0;
//        }
//
//        return time_diff;
//    }
//    private void addNotificationIntentExtras(Intent notifyIntent, MatchData match){
//        notifyIntent.putExtra("home",match.getNameHomeTeam());
//        notifyIntent.putExtra("away",match.getNameAwayTeam());
//        notifyIntent.putExtra("teamId",idTeam);
//        notifyIntent.putExtra("eventId",match.getIdEvent());
//    }
//    private void registerNotification(PendingIntent pendingIntent, String id_event, String id_team, long time){
//
//        final AlarmManager mAlarmManager = (AlarmManager)
//                getSystemService(ALARM_SERVICE);
//
//        // Register to alarm
//        mAlarmManager.set(
//                AlarmManager.RTC,
//                time,
//                pendingIntent
//        );
//
//        // Register to database
//        sqLiteManager.AddNotif(id_event,id_team);
//
//
//
//    }
}
