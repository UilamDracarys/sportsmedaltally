package com.cpsu.sports.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cpsu.sports.R;
import com.cpsu.sports.Utils.SearchableAdapter;
import com.cpsu.sports.data.repo.AthleteRepo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class AthleteList extends AppCompatActivity {

    FloatingActionButton fab;
    ListView list;
    SearchableAdapter adapter;
    TextView txtAthleteID;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete_list);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");

        getSupportActionBar().setTitle(title);

        init();
        loadAthletes();
    }

    private void init() {
        fab = findViewById(R.id.fabAdd);
        list = findViewById(R.id.sportList);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent addAthlete = new Intent(AthleteList.this, AthleteDetail.class);
                addAthlete.putExtra("title", "New Athlete");
                startActivity(addAthlete);
            }
        });
    }

    private void loadAthletes() {
        final AthleteRepo athleteRepo = new AthleteRepo();
        ArrayList<HashMap<String, String>> athleteList = athleteRepo.getAthleteList();
        ListView lv = findViewById(R.id.sportList);
        lv.setFastScrollEnabled(true);
        if (athleteList.size() != 0) {
            lv = findViewById(R.id.sportList);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = getIntent();
                    Bundle b = intent.getExtras();

                    if (b != null) {

                        txtAthleteID = view.findViewById(R.id.id);
                        String athleteID = txtAthleteID.getText().toString();

                        Intent objIndent;
                        objIndent = new Intent(getApplicationContext(), AthleteDetail.class);
                        objIndent.putExtra("athleteID", athleteID);
                        objIndent.putExtra("title", "Edit Athlete");
                        startActivity(objIndent);
                    }
                }
            });
            adapter = new SearchableAdapter(AthleteList.this,
                    athleteList,
                    R.layout.activity_athlete_list_item,
                    new String[]{"ID", "Name", "College"},
                    new int[]{R.id.id, R.id.name, R.id.athleteCollege});
            lv.setAdapter(adapter);
        } else {
            adapter = null;
            lv.setAdapter(adapter);
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        loadAthletes();
    }
}