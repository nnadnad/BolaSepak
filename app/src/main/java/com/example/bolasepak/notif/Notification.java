package com.example.bolasepak.notif;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.bolasepak.EventActivity;
import com.example.bolasepak.R;
import com.example.bolasepak.SQLite;

public class Notification extends BroadcastReceiver {



    private static final String NOTIFICATION_CHANNEL = "primary_notification_channel";
    NotificationManager mNotificationManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        deliverNotification(context,intent);

    }

    private void deliverNotification(Context context, Intent intent){



        String teamId = intent.getStringExtra("teamId");
        String home  = intent.getStringExtra("home");
        String away = intent.getStringExtra("away");
        String eventId = intent.getStringExtra("eventId");


        //Log.d("Broadcast received","Event ID : " + eventId);

        // build notification

        Intent eventIntent = new Intent(context, EventActivity.class);
        eventIntent.putExtra("id_event",eventId);

        PendingIntent eventPendingIntent = PendingIntent.getActivity(
                context,0,eventIntent,PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context, NOTIFICATION_CHANNEL
        )
                //.setSmallIcon(R.drawable.logo_app_bola_sepak)
                .setContentTitle(home + " vs. " + away)
                .setContentText(home + " vs. " + away + " in 30 minutes !")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(eventPendingIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        // delete event from database
        SQLite sqLiteManager = new SQLite(context);
        sqLiteManager.DeleteEventNotif(eventId);


        // deliver
        mNotificationManager.notify(Integer.parseInt(teamId),builder.build());


    }
}