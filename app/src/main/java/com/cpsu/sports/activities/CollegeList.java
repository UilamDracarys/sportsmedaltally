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
import com.cpsu.sports.data.repo.CollegeRepo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class CollegeList extends AppCompatActivity {
    FloatingActionButton fab;
    ListView list;
    SearchableAdapter adapter;
    TextView txtCollegeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_list);
        getSupportActionBar().setTitle("College List");

        init();
        loadColleges();
    }

    private void init() {
        fab = findViewById(R.id.fabAdd);
        list = findViewById(R.id.collegeList);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent addCollege = new Intent(CollegeList.this, CollegeDetail.class);
                addCollege.putExtra("title", "New College");
                startActivity(addCollege);
            }
        });
    }

    private void loadColleges() {
        final CollegeRepo collegeRepo = new CollegeRepo();
        ArrayList<HashMap<String, String>> collegeList = collegeRepo.getCollegeList();
        ListView lv = findViewById(R.id.collegeList);
        lv.setFastScrollEnabled(true);
        if (collegeList.size() != 0) {
            lv = findViewById(R.id.collegeList);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    System.out.println("Item Click");

                    Intent intent = getIntent();
                    Bundle b = intent.getExtras();
                    System.out.println(intent.getExtras());

                    if (b != null) {

                        System.out.println("Item Click: b != null");
                        txtCollegeID = view.findViewById(R.id.collegeID);
                        String collegeID = txtCollegeID.getText().toString();

                        Intent objIndent;
                        objIndent = new Intent(getApplicationContext(), CollegeDetail.class);
                        objIndent.putExtra("collegeID", collegeID);
                        objIndent.putExtra("title", "Edit College");
                        startActivity(objIndent);
                    }
                }
            });
            adapter = new SearchableAdapter(CollegeList.this,
                    collegeList,
                    R.layout.activity_college_list_item,
                    new String[]{"ID", "Code", "Name"},
                    new int[]{R.id.collegeID, R.id.collegeCode, R.id.collegeName});
            lv.setAdapter(adapter);
        } else {
            adapter = null;
            lv.setAdapter(adapter);
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        loadColleges();
    }
}