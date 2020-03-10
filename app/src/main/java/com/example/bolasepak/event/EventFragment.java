package com.example.bolasepak.event;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class EventFragment extends Fragment {
    private static String event;

    public static EventFragment newInstance(String event) {
        
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        //args.putString("event",event);
        fragment.setArguments(args);
        return fragment;
    }

    public static void setEvent(String event) {
        EventFragment.event = event;
    }

    public void setArguments(Bundle args) {
        args.putString("event",event);
    }
}
