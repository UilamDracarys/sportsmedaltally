package com.cpsu.sports.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cpsu.sports.R;
import com.cpsu.sports.Utils.PreferenceUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    String username;
    TextView mTextWelcome;
    Boolean isFabOpen;
    FloatingActionButton fab, fabAddAthlete, fabAddSport, fabAddMedal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportActionBar().hide();

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        isFabOpen = false;
        init();
        mTextWelcome.setText("Welcome, " + username + "!");

    }

    private void init() {
        mTextWelcome = findViewById(R.id.txtWelcome);
        fab = findViewById(R.id.fabAdd);
        fabAddAthlete = findViewById(R.id.fabAddAthlete);
        fabAddSport = findViewById(R.id.fabAddSport);
        fabAddMedal = findViewById(R.id.fabAddMedal);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!isFabOpen) {
                    showFabMenu();
                } else {
                    closeFabMenu();
                }
            }
        });
    }


    private void showFabMenu() {
        isFabOpen = true;
        fabAddMedal.animate().translationY(-160);
        fabAddAthlete.animate().translationY(-315);
        fabAddSport.animate().translationY(-470);
    }

    private void closeFabMenu() {
        isFabOpen = false;
        fabAddAthlete.animate().translationY(0);
        fabAddSport.animate().translationY(0);
        fabAddMedal.animate().translationY(0);
    }

    @Override
    public void onBackPressed() {
        PreferenceUtils.deleteUsername(this);
        finish();
    }


}