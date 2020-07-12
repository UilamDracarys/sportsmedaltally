package com.cpsu.sports.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.cpsu.sports.R;
import com.cpsu.sports.data.model.College;
import com.cpsu.sports.data.repo.CollegeRepo;
import com.google.android.material.textfield.TextInputEditText;

public class CollegeDetail extends AppCompatActivity {
    TextInputEditText mTxtCollegeCode, mTxtCollegeName;
    String title, collegeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_detail);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        collegeID = intent.getStringExtra("collegeID");
        getSupportActionBar().setTitle(title);

        init();

        if (title.equalsIgnoreCase("New College")) {
            mTxtCollegeCode.setText("");
            mTxtCollegeName.setText("");
        } else {
            CollegeRepo collegeRepo = new CollegeRepo();
            College college = collegeRepo.getCollegeByID(collegeID);
            mTxtCollegeCode.setText(college.getCollegeCode());
            mTxtCollegeName.setText(college.getCollegeName());
        }

    }

    private void init() {
        mTxtCollegeCode = findViewById(R.id.txtCollegeCode);
        mTxtCollegeName = findViewById(R.id.txtCollegeName);
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
        College college = new College();
        CollegeRepo collegeRepo = new CollegeRepo();
        String collegeCode = mTxtCollegeCode.getText().toString().trim().toUpperCase();
        String collegeName = mTxtCollegeName.getText().toString().trim();
        college.setCollegeID(collegeID);
        college.setCollegeCode(collegeCode);
        college.setCollegeName(collegeName);

        if (isDataValid()) {
            if (title.equalsIgnoreCase("New College")) {
                collegeRepo.insert(college);
            } else {
                collegeRepo.update(college);
            }
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private boolean isDataValid() {
        if (mTxtCollegeCode.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please assign a college code.", Toast.LENGTH_SHORT).show();
            mTxtCollegeCode.requestFocus();
            return false;
        }

        if (mTxtCollegeName.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "College name is required.", Toast.LENGTH_SHORT).show();
            mTxtCollegeName.requestFocus();
            return false;
        }
        return true;
    }
}