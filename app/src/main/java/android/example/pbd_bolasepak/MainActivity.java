package android.example.pbd_bolasepak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements SensorEventListener, StepListener {
    RecyclerView match_list;
    MatchRecyclerAdapter matchRecyclerAdapter;

    HashMap<String,String> listTeams;

    MatchDetails matchDetails;
    ArrayList<MatchDetails> listMatch;

    // step variable
    private TextView stepSum;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    // sampe sini

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listTeams = new HashMap<String, String>();
        listMatch = new ArrayList<MatchDetails>();

        match_list = findViewById(R.id.match_list_view);
        
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requestTeams();
            gridRecycler();
        }
        else {
            requestTeams();
            recycler();
        }

        // step initiation mulai
        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        numSteps = 0;
        sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);

        stepSum = findViewById(R.id.stepSum);
        // sampe sini
    }

    //portrait
    public void recycler(){
        matchRecyclerAdapter = new MatchRecyclerAdapter(MainActivity.this, listMatch);

        match_list.setHasFixedSize(true);
        match_list.setLayoutManager(new LinearLayoutManager(this));
        match_list.setAdapter(matchRecyclerAdapter);
    }

    //landscape
    public void gridRecycler(){
        matchRecyclerAdapter = new MatchRecyclerAdapter(MainActivity.this, listMatch);

        match_list.setHasFixedSize(true);
        match_list.setLayoutManager(new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false));
        match_list.setAdapter(matchRecyclerAdapter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putString("team","team");
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void requestTeams() {
        String urlTeams = "https://www.thesportsdb.com/api/v1/json/1/search_all_teams.php?l=English%20Premier%20League"; //teams

        //request team details
        JsonObjectRequest jsonObjectRequestTeams = new JsonObjectRequest(Request.Method.GET, urlTeams, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject responsePast) {
                // Log.d("response: ", responsePast.toString());
                try {
                    JSONArray matchList = responsePast.getJSONArray("teams");
                    for (int i = 0; i < matchList.length(); i++) {
                        JSONObject matchObj = matchList.getJSONObject(i);

                        String idTeams = matchObj.getString("idTeam");
                        String urlBadge = matchObj.getString("strTeamBadge");

                        listTeams.put(idTeams, urlBadge);
                    }
                    // calling request match function to get all the other components needed
                    requestMatch();
                }
                catch (JSONException error){
                    error.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequestTeams);
    }

    public void requestMatch(){
        String urlPast = "https://www.thesportsdb.com/api/v1/json/1/eventspastleague.php?id=4328"; //15 past events
        String urlUpcoming = "https://www.thesportsdb.com/api/v1/json/1/eventsnextleague.php?id=4328"; //15 upcoming events

        //request past match
        JsonObjectRequest jsonObjectRequestPast = new JsonObjectRequest(Request.Method.GET, urlPast, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject responsePast) {
                // Log.d("response: ", responsePast.toString());
                try {
                    JSONArray matchList = responsePast.getJSONArray("events");
                    for (int i = 0; i < matchList.length(); i++) {
                        JSONObject matchObj = matchList.getJSONObject(i);

                        String idHome = matchObj.getString("idHomeTeam");
                        String idAway = matchObj.getString("idAwayTeam");

                        String urlBadgeHome = listTeams.get(idHome);
                        String urlBadgeAway = listTeams.get(idAway);

                        String homeName = matchObj.getString("strHomeTeam");
                        String awayName = matchObj.getString("strAwayTeam");

                        String homeSkor = matchObj.getString("intHomeScore");
                        String awaySkor = matchObj.getString("intAwayScore");

                        String date = matchObj.getString("dateEvent");

                        matchDetails = new MatchDetails(homeName, homeSkor, urlBadgeHome, awayName, awaySkor, urlBadgeAway, date);
                        listMatch.add(matchDetails);
                    }
                    matchRecyclerAdapter = new MatchRecyclerAdapter(MainActivity.this, listMatch);
                    match_list.setAdapter(matchRecyclerAdapter);
                }
                catch (JSONException error){
                    error.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequestPast);

        //request upcoming match
        JsonObjectRequest jsonObjectRequestUpcoming = new JsonObjectRequest(Request.Method.GET, urlUpcoming, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject responseUpcoming) {
                // Log.d("response: ", responseUpcoming.toString());
                try {
                    JSONArray matchList = responseUpcoming.getJSONArray("events");
                    for (int i = 0; i < matchList.length(); i++) {
                        JSONObject matchObj = matchList.getJSONObject(i);

                        String idHome = matchObj.getString("idHomeTeam");
                        String idAway = matchObj.getString("idAwayTeam");

                        String urlBadgeHome = listTeams.get(idHome);
                        String urlBadgeAway = listTeams.get(idAway);

                        String homeName = matchObj.getString("strHomeTeam");
                        String awayName = matchObj.getString("strAwayTeam");

                        String date = matchObj.getString("dateEvent");

                        matchDetails = new MatchDetails(homeName, "-", urlBadgeHome, awayName, "-", urlBadgeAway, date);
                        listMatch.add(matchDetails);
                    }
                    matchRecyclerAdapter = new MatchRecyclerAdapter(MainActivity.this, listMatch);
                    match_list.setAdapter(matchRecyclerAdapter);
                }
                catch (JSONException error){
                    error.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequestUpcoming);
    }

    // step function mulai
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        stepSum.setText(numSteps);
    }
    // sampe sini
}
