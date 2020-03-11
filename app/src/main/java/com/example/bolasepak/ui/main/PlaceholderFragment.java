package com.example.bolasepak.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bolasepak.Event;
import com.example.bolasepak.EventAdapter;
import com.example.bolasepak.R;
import com.example.bolasepak.TeamDetail;

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

        addData();

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

    void addData() {
        eventArrayList = new ArrayList<>();
        eventArrayList.add(new Event("Barca", "Liverpool", "1", "3"));
        eventArrayList.add(new Event("Real Madrid", "Arsenal", "2", "3"));
        eventArrayList.add(new Event("AC Milan", "Chelsea", "5", "4"));
    }
}