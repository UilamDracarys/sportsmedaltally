package com.cpsu.sports.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.cpsu.sports.R;
import com.cpsu.sports.data.repo.AthleteRepo;
import com.cpsu.sports.data.repo.CollegeRepo;
import com.cpsu.sports.data.repo.SportRepo;

import java.util.ArrayList;
import java.util.HashMap;

public class MedalDetail extends AppCompatActivity {
    Spinner mSpnMedalType, mSpnSport, mSpnAthCol;
    String title, sportID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medal_detail);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        getSupportActionBar().setTitle(title);

        init();
        loadSpinnerData(mSpnSport, "Sport");
    }

    private void init() {
        mSpnMedalType = findViewById(R.id.spnMedalType);
        mSpnSport = findViewById(R.id.spnSport);
        mSpnAthCol = findViewById(R.id.spnAthCol);
        mSpnSport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SportRepo sportRepo = new SportRepo();
                TextView txtSportID = mSpnSport.getSelectedView().findViewById(R.id.ID);
                sportID = txtSportID.getText().toString().trim();
                String sportType = sportRepo.getSportType(sportID);
                if (sportType.equalsIgnoreCase("Individual")) {
                    loadSpinnerData(mSpnAthCol, "Athlete");
                } else if (sportType.equalsIgnoreCase("Team")) {
                    loadSpinnerData(mSpnAthCol, "College");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }




    private void loadSpinnerData(Spinner spinner, String list) {
        System.out.println("ListType: " + list);

        final SportRepo sportRepo = new SportRepo();
        final CollegeRepo collegeRepo = new CollegeRepo();
        final AthleteRepo athleteRepo = new AthleteRepo();
        ArrayList<HashMap<String, String>> main = new ArrayList<>();
        HashMap<String, String> firstItem = new HashMap<>();
        firstItem.put("ID", "");
        firstItem.put("Name", "--- Select " + list + " ---");
        main.add(firstItem);
        ArrayList<HashMap<String, String>> listType = null;
        if (list.equalsIgnoreCase("Sport")) {
            listType = sportRepo.getSportList();
        } else if (list.equalsIgnoreCase("College")) {
            listType = collegeRepo.getCollegeList();
        } else if (list.equalsIgnoreCase("Athlete")) {
            listType = athleteRepo.getAthleteList();
        }
        main.addAll(listType);

        ListAdapter adapter;
        if (listType.size() != 0) {
            adapter = new SimpleAdapter(this, main, R.layout.activity_spinner_item,
                    new String[] {"ID", "Name"},
                    new int[] {R.id.ID, R.id.Name});
            spinner.setAdapter((SpinnerAdapter) adapter);
        } else {
            adapter = null;
            spinner.setAdapter((SpinnerAdapter) adapter);
        }
    }

}