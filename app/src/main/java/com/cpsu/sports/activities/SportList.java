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
import com.cpsu.sports.data.repo.SportRepo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class SportList extends AppCompatActivity {
    FloatingActionButton fab;
    ListView list;
    SearchableAdapter adapter;
    TextView txtSportID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_list);
        getSupportActionBar().setTitle("Sport List");

        init();
        loadSports();
    }

    private void init() {
        fab = findViewById(R.id.fabAdd);
        list = findViewById(R.id.sportList);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent addSport = new Intent(SportList.this, SportDetail.class);
                addSport.putExtra("title", "Add Sport");
                startActivity(addSport);
            }
        });
    }

    private void loadSports() {
        final SportRepo sportRepo = new SportRepo();
        ArrayList<HashMap<String, String>> sportList = sportRepo.getSportList();
        ListView lv = findViewById(R.id.sportList);
        lv.setFastScrollEnabled(true);
        if (sportList.size() != 0) {
            lv = findViewById(R.id.sportList);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    Intent intent = getIntent();
                    Bundle b = intent.getExtras();
                    System.out.println(intent.getExtras());

                    if (b != null) {

                        txtSportID = view.findViewById(R.id.sportID);
                        String sportID = txtSportID.getText().toString();

                        Intent objIndent;
                        objIndent = new Intent(getApplicationContext(), SportDetail.class);
                        objIndent.putExtra("sportID", sportID);
                        objIndent.putExtra("title", "Edit Sport");
                        startActivity(objIndent);
                    }
                }
            });
            adapter = new SearchableAdapter(SportList.this,
                    sportList,
                    R.layout.activity_sport_list_item,
                    new String[]{"ID", "Name", "Type"},
                    new int[]{R.id.sportID, R.id.sportName,R.id.sportType});
            lv.setAdapter(adapter);
        } else {
            adapter = null;
            lv.setAdapter(adapter);
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        loadSports();
    }
}