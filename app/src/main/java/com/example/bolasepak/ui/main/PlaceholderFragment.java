package com.example.bolasepak.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bolasepak.Event;
import com.example.bolasepak.EventAdapter;
import com.example.bolasepak.MySingleton;
import com.example.bolasepak.R;
import com.example.bolasepak.TeamDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private ArrayList<Event> eventArrayList;

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_team_detail, container, false);

        String url = "https://www.thesportsdb.com/api/v1/json/1/eventsnext.php?id=133602";

        //request upcoming match
        JsonObjectRequest jsonObjectRequestUpcoming = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject responseUpcoming) {
                // Log.d("response: ", responseUpcoming.toString());
                try {
                    addData(responseUpcoming.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error JSONObjectresp: ", error.toString());
                        // TODO: Handle error
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this.getActivity()).addToRequestQueue(jsonObjectRequestUpcoming);

        try {
            addData("");
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        try {
//            Log.e("Error response: ", pageViewModel.getText());
//            addData(pageViewModel.getText());
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.e("Error onCreateView: ", e.toString());
//        }

        recyclerView = (RecyclerView) root.findViewById(R.id.event_recycler);

        adapter = new EventAdapter(eventArrayList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

//        final TextView textView = root.findViewById(R.id.event_text);
//        pageViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        return root;
    }

    void addData(String strPayload) throws JSONException {
        eventArrayList = new ArrayList<>();

        //Log.e("Error payload: ", strPayload);

        JSONObject response = new JSONObject(strPayload);
        JSONArray matchList = response.getJSONArray("events");

        for (int i = 0; i < matchList.length(); i++) {
            JSONObject matchObj = matchList.getJSONObject(i);

            String idHome = matchObj.getString("idHomeTeam");
            String idAway = matchObj.getString("idAwayTeam");

            //String urlBadgeHome = listTeams.get(idHome);
            //String urlBadgeAway = listTeams.get(idAway);

            String homeName = matchObj.getString("strHomeTeam");
            String awayName = matchObj.getString("strAwayTeam");

            String homeSkor = matchObj.getString("intHomeScore");
            String awaySkor = matchObj.getString("intAwayScore");

            String date = matchObj.getString("dateEvent");

            eventArrayList.add(new Event(homeName, awayName, homeSkor, awaySkor, date));
        }


//        eventArrayList.add(new Event("Barca", "Liverpool", "1", "3", "2 Februari 2020"));
//        eventArrayList.add(new Event("Real Madrid", "Arsenal", "2", "3", "11 Januari 2020"));
//        eventArrayList.add(new Event("Man United", "Man City", "7", "6", "1 Januari 2019"));
//        eventArrayList.add(new Event("AC Milan", "Chelsea", "5", "4", "21 Agustus 2019"));
    }
}