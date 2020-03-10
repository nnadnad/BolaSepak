package com.example.bolasepak;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bolasepak.event.EventFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.prev_game:
                        EventFragment fragment = EventFragment.newInstance("eventspastleague");
                        addFragment(fragment);
                        return true;
                    case R.id.next_game:
                        EventFragment fragment2 = EventFragment.newInstance("eventsnextleague");
                        addFragment(fragment2);
                        return true;

                }
                return false;
            }

        });
    }

    @SuppressLint("PrivateResource")
    private void addFragment(EventFragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
                .replace(R.id.content, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

}
