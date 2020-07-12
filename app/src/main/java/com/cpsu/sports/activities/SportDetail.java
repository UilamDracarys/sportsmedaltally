package com.cpsu.sports.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.Toast;

import com.cpsu.sports.R;
import com.cpsu.sports.data.model.Sport;
import com.cpsu.sports.data.repo.SportRepo;
import com.google.android.material.textfield.TextInputEditText;

public class SportDetail extends AppCompatActivity {
    TextInputEditText mTxtSportName;
    Spinner mSpnSportCat, mSpnSportType;
    String title;
    private String sportID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_detail);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        getSupportActionBar().setTitle(title);

        init();

        if (title.equalsIgnoreCase("Edit Sport")) {
            SportRepo sportRepo = new SportRepo();
            sportID = intent.getStringExtra("sportID");
            Sport sport = sportRepo.getSportByID(sportID);
            mTxtSportName.setText(sport.getSportName());
            mSpnSportCat.setSelection(sport.getIdxByItem(getResources().getStringArray(R.array.sport_categories), sport.getSportCategory()));
            mSpnSportType.setSelection(sport.getIdxByItem(getResources().getStringArray(R.array.sport_types), sport.getSportType()));
        }
    }

    private void init() {
        mTxtSportName = findViewById(R.id.txtSportName);
        mSpnSportCat = findViewById(R.id.spnSportCat);
        mSpnSportType = findViewById(R.id.spnSportType);
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
        SportRepo sportRepo = new SportRepo();
        Sport sport = new Sport();
        String sportName = mTxtSportName.getText().toString().trim();
        String sportCategory = mSpnSportCat.getSelectedItem().toString().trim();
        String sportType = mSpnSportType.getSelectedItem().toString().trim();
        sport.setSportID(sportID);
        sport.setSportName(sportName);
        sport.setSportCategory(sportCategory);
        sport.setSportType(sportType);

        if (isDataValid()) {
            if (title.equalsIgnoreCase("Add Sport")) {
                sportRepo.insert(sport);
            } else {
                sportRepo.update(sport);
            }
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean isDataValid() {
        if (mTxtSportName.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Sport Name is required.", Toast.LENGTH_SHORT).show();
            mTxtSportName.requestFocus();
            return false;
        }

        if (mSpnSportCat.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Sport Category is required.", Toast.LENGTH_SHORT).show();
            mSpnSportCat.requestFocus();
            return false;
        }

        if (mSpnSportType.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Sport Type is required.", Toast.LENGTH_SHORT).show();
            mSpnSportType.requestFocus();
            return false;
        }


        return true;
    }
}