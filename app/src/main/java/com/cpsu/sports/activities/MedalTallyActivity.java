package com.cpsu.sports.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cpsu.sports.R;
import com.cpsu.sports.data.model.Sport;
import com.cpsu.sports.data.repo.CollegeRepo;
import com.cpsu.sports.data.repo.GameRepo;
import com.cpsu.sports.data.repo.SportRepo;

import java.util.ArrayList;
import java.util.HashMap;

public class MedalTallyActivity extends AppCompatActivity {
    ListView list;
    String sportID, title;
    Sport sport;
    ArrayList<HashMap<String, String>> placers;
    ArrayList<String> champion, second, third;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medal_tally);

        Intent intent = getIntent();
        sportID = intent.getStringExtra("sportID");
        title = intent.getStringExtra("title");

        getSupportActionBar().setTitle(title);
        init();
        loadColleges();
        SportRepo sportRepo = new SportRepo();
        sport = sportRepo.getSportByID(sportID);
        //showPlacers();
    }

    private void init() {
        list = findViewById(R.id.collegeList);
    }


    private void loadColleges() {
        final CollegeRepo collegeRepo = new CollegeRepo();
        ArrayList<HashMap<String, String>> collegeList = collegeRepo.getCollegeMedals(sportID);
        ListView lv = findViewById(R.id.collegeList);
        lv.setFastScrollEnabled(true);
        ListAdapter adapter;
        if (collegeList.size() != 0) {
            lv = findViewById(R.id.collegeList);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (sport.getSportType().equalsIgnoreCase("Individual")) {
                        TextView textView = view.findViewById(R.id.collegeID);
                        String collegeId = textView.getText().toString();
                        Intent intent = new Intent(MedalTallyActivity.this, AthleteMedalList.class);
                        intent.putExtra("sportId", sportID);
                        intent.putExtra("collegeId", collegeId);
                        startActivity(intent);
                    }
                }
            });

            adapter = new SimpleAdapter(MedalTallyActivity.this,
                    collegeList,
                    R.layout.activity_medal_tally_item,
                    new String[]{"ID", "Name", "RankWord", "Medals"},
                    new int[] {R.id.collegeID, R.id.collegeName, R.id.ranking, R.id.collegeMedals});
            lv.setAdapter(adapter);
        } else {
            adapter = null;
            lv.setAdapter(adapter);
        }
    }


}