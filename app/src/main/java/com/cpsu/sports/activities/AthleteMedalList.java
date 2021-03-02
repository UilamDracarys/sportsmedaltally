package com.cpsu.sports.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cpsu.sports.R;
import com.cpsu.sports.data.model.Game;
import com.cpsu.sports.data.repo.GameRepo;

import java.util.ArrayList;
import java.util.HashMap;

public class AthleteMedalList extends AppCompatActivity {
    TableLayout athMedalTable;
    String collegeId, sportId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete_medal_list);

        Intent intent = getIntent();
        sportId = intent.getStringExtra("sportId");
        collegeId = intent.getStringExtra("collegeId");

        init();
        GameRepo gameRepo = new GameRepo();
        populateTable(athMedalTable, gameRepo.getAthleteMedalList(sportId, collegeId), this);
    }

    private void init() {
        athMedalTable = findViewById(R.id.athMdlList);
    }
    private void populateTable(TableLayout athMedalTable, ArrayList<HashMap<String, String>> athMedalList, Context context) {
        athMedalTable.removeViews(1, athMedalTable.getChildCount()-1);
        for (int i=0; i<athMedalList.size(); i++) {
            athMedalTable.addView(addRow(athMedalList.get(i).get("AthleteName"), athMedalList.get(i).get("MedalType"), context));
        }

    }

    private TableRow addRow(String athName, String medal, Context context) {
        TableRow row = new TableRow(context);
        TextView tv1 = new TextView(context);
        TextView tv2 = new TextView(context);

        tv1.setText(athName);
        tv2.setText(medal);

        tv1.setPadding(10, 10, 10, 10);
        tv2.setPadding(10, 10, 10, 10);

        row.addView(tv1);
        row.addView(tv2);

        return row;
    }

}