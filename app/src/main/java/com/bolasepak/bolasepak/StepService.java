package com.bolasepak.bolasepak;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bolasepak.bolasepak.Sensor.StepDetector;
import com.bolasepak.bolasepak.Sensor.StepListener;

public class StepService extends Service implements SensorEventListener, StepListener {
    SensorManager mSensorManager;
    Sensor mAccel;
    StepDetector simpleStepDetector;
    long numSteps;
    SharedPreferences preferences ;

    public static final String
            ACTION_STEP_BROADCAST = StepService.class.getName() + "StepCount",
            STEPCOUNT = "step";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        mSensorManager.registerListener(StepService.this, mAccel, SensorManager.SENSOR_DELAY_FASTEST);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service destroyed by user.", Toast.LENGTH_LONG).show();
        mSensorManager.unregisterListener(StepService.this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
//        cal = Calendar.getInstance();
//        int nowHour = cal.get(Calendar.HOUR_OF_DAY);
//        int nowMinute = cal.get(Calendar.MINUTE);
//        int nowSecond = cal.get(Calendar.SECOND);
//
//        if ((nowHour == 23) && (nowMinute == 59) && (nowSecond == 59)) {
//            preferences.edit().remove("steps").apply();
//        }

        numSteps = preferences.getLong("steps", 0);
        numSteps++;
        preferences.edit().putLong("steps",numSteps).apply();
        sendBroadcastMessage(numSteps);

    }

    private void sendBroadcastMessage(long steps) {
        Intent intent = new Intent(ACTION_STEP_BROADCAST);
        intent.putExtra(STEPCOUNT, steps);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
