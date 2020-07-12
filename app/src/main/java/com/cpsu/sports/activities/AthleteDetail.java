package com.cpsu.sports.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cpsu.sports.R;
import com.cpsu.sports.Utils.SearchableAdapter;
import com.cpsu.sports.data.DBHelper;
import com.cpsu.sports.data.model.Athlete;
import com.cpsu.sports.data.model.College;
import com.cpsu.sports.data.repo.AthleteRepo;
import com.cpsu.sports.data.repo.CollegeRepo;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AthleteDetail extends AppCompatActivity {
    TextInputEditText mTextName, mTextAddress;
    Spinner mSpnCollege;
    String athleteID, title, collegeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete_detail);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        getSupportActionBar().setTitle(title);

        init();

        if (title.equalsIgnoreCase("New Athlete")) {
            loadColleges();
        } else {
            athleteID = intent.getStringExtra("athleteID");
            AthleteRepo athleteRepo = new AthleteRepo();
            Athlete athlete = athleteRepo.getAthleteByID(athleteID);
            mTextName.setText(athlete.getAthleteName());
            mTextAddress.setText(athlete.getAthleteAdd());
            collegeID = athlete.getAthleteCol();
            loadColleges();
        }
    }

    private void init() {
        mTextName = findViewById(R.id.txtAthleteName);
        mTextAddress = findViewById(R.id.txtAthleteAdd);
        mSpnCollege = findViewById(R.id.spnCollege);
        mSpnCollege.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView txtCollegeID =  mSpnCollege.getSelectedView().findViewById(R.id.ID);
                collegeID = txtCollegeID.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the save_cancel; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void save() {
        Athlete athlete = new Athlete();
        AthleteRepo athleteRepo = new AthleteRepo();
        String athleteName = mTextName.getText().toString().trim();
        String athleteAdd = mTextAddress.getText().toString().trim();
        String athleteCol = "";
        if (mSpnCollege.getSelectedItem() != null) {
            athleteCol = collegeID;
        }

        athlete.setAthleteID(athleteID);
        athlete.setAthleteName(athleteName);
        athlete.setAthleteAdd(athleteAdd);
        athlete.setAthleteCol(athleteCol);

        if (isDataValid()) {
            if (title.equalsIgnoreCase("New Athlete")) {
                athleteRepo.insert(athlete);
            } else {
                athleteRepo.update(athlete);
            }
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private boolean isDataValid() {
        if (mTextName.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "Athlete Name is required.", Toast.LENGTH_SHORT).show();
            mTextName.requestFocus();
            return false;
        }

        if (mTextAddress.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "Athlete Address is required.", Toast.LENGTH_SHORT).show();
            mTextAddress.requestFocus();
            return false;
        }

        if (mSpnCollege.getSelectedItem() == null) {
            Toast.makeText(this, "Please configure the list of colleges first.", Toast.LENGTH_SHORT).show();
            mSpnCollege.requestFocus();
            return false;
        }

        if (mSpnCollege.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a college.", Toast.LENGTH_SHORT).show();
            mSpnCollege.requestFocus();
            return false;
        }

        return true;
    }

    private void loadColleges() {
        final CollegeRepo collegeRepo = new CollegeRepo();
        ArrayList<HashMap<String, String>> main = new ArrayList<>();
        HashMap<String, String> firstItem = new HashMap<>();
        firstItem.put("ID", "0");
        firstItem.put("Name", "--- Select College ---");
        main.add(firstItem);
        ArrayList<HashMap<String, String>> collegeList = collegeRepo.getCollegeList();
        main.addAll(collegeList);


        System.out.println("CollegeID: " + collegeID);
        System.out.println("Main: " + main.get(1).get("ID"));



        ListAdapter adapter;
        if (collegeList.size() != 0) {
            adapter = new SimpleAdapter(this, main, R.layout.activity_spinner_item,
                    new String[] {"ID", "Name"},
                    new int[] {R.id.ID, R.id.Name});
            mSpnCollege.setAdapter((SpinnerAdapter) adapter);

        } else {
            adapter = null;
            mSpnCollege.setAdapter((SpinnerAdapter) adapter);
        }

        if (title.equalsIgnoreCase("Edit Athlete")) {
            int size = main.size();
            int post;
            for (int i = 0; i <size; i++) {
                if (main.get(i).get("ID").equalsIgnoreCase(collegeID)) {
                    post = i;
                    mSpnCollege.setSelection(post);
                }
            }

        }
    }

}