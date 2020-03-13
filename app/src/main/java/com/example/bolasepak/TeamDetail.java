package com.example.bolasepak;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bolasepak.ui.main.SectionsPagerAdapter;

import org.json.JSONObject;

public class TeamDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    public String getRequest(int input) {
        String url = "";

        if (input == 1) {
            url = "https://www.thesportsdb.com/api/v1/json/1/eventsnext.php?id=133602";
        }
        else if (input == 2) {
            url = "https://www.thesportsdb.com/api/v1/json/1/eventslast.php?id=133602";
        }

        final String[] stringResponse = {url};

//        //request upcoming match
//        JsonObjectRequest jsonObjectRequestUpcoming = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject responseUpcoming) {
//                // Log.d("response: ", responseUpcoming.toString());
//                stringResponse[0] = responseUpcoming.toString();
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Error JSONObjectresp: ", error.toString());
//                        // TODO: Handle error
//                    }
//                });
//
//        // Access the RequestQueue through your singleton class.
//        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequestUpcoming);

        return stringResponse[0];
    }
}