package com.cpsu.sports.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cpsu.sports.R;
import com.cpsu.sports.data.model.Athlete;
import com.cpsu.sports.data.model.Game;
import com.cpsu.sports.data.model.Sport;
import com.cpsu.sports.data.repo.AthleteRepo;
import com.cpsu.sports.data.repo.CollegeRepo;
import com.cpsu.sports.data.repo.GameRepo;
import com.cpsu.sports.data.repo.SportRepo;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class MedalDetail extends AppCompatActivity implements View.OnClickListener {
    Spinner mSpnMedalType, mSpnSport;
    String title, sportID, winnerID, loserID, winnerAthId, loserAthId, clickedText;
    SpinnerDialog mSpnAthCol;
    ArrayList<String> athColList;
    TextInputEditText mTxtGameNo;
    TextInputEditText mTxtWinner;
    TextInputEditText mTxtLoser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medal_detail);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        getSupportActionBar().setTitle(title);

        init();
        loadSportsSpinner();
    }

    private void init() {
        clickedText = "";
        final AthleteRepo athleteRepo = new AthleteRepo();
        final CollegeRepo collegeRepo = new CollegeRepo();
        athColList = new ArrayList<>();
        mSpnMedalType = findViewById(R.id.spnMedalType);
        mSpnSport = findViewById(R.id.spnSport);
        mSpnSport.requestFocus();
        mTxtGameNo = findViewById(R.id.txtGameNo);
        mTxtWinner = findViewById(R.id.txtWinner);
        mTxtLoser = findViewById(R.id.txtLoser);
        mSpnSport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTxtWinner.setText("Tap to Set");
                mTxtLoser.setText("Tap to Set");
                final SportRepo sportRepo = new SportRepo();
                final GameRepo gameRepo = new GameRepo();
                TextView txtSportID = mSpnSport.getSelectedView().findViewById(R.id.ID);
                sportID = txtSportID.getText().toString().trim();
                String sportType = sportRepo.getSportType(sportID);
                String listType = "";
                if (sportType.equals("Team")) {
                    athColList = collegeRepo.getCollegesForSpinner();
                    listType = "College";
                } else if (sportType.equalsIgnoreCase("Individual")) {
                    athColList = athleteRepo.getAthletesForSpinner();
                    listType = "Athlete";
                } else {
                    athColList = null;
                }
                mSpnAthCol = new SpinnerDialog(MedalDetail.this, athColList, "Select " + listType);
                final String finalListType = listType;
                mSpnAthCol.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String s, int i) {
                        if (s != null) {

                            if (clickedText.equalsIgnoreCase("W")) {
                                String winnerId = s.substring(s.indexOf("[") + 1, s.indexOf("]"));
                                if (finalListType.equalsIgnoreCase("College")) {
                                    winnerID = winnerId;
                                } else {
                                    winnerID = gameRepo.getCollegeId(winnerId);
                                    winnerAthId = winnerId;
                                }

                                mTxtWinner.setText(s);
                            } else {
                                String loserId = s.substring(s.indexOf("[") + 1, s.indexOf("]"));

                                if (finalListType.equalsIgnoreCase("College")) {
                                    loserID = loserId;
                                } else {
                                    loserID = gameRepo.getCollegeId(loserId);
                                    loserAthId = loserId;
                                }
                                mTxtLoser.setText(s);
                            }

                        } else {
                            System.out.println("Nothing selected.");
                        }
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mTxtWinner.setOnClickListener(this);
        mTxtLoser.setOnClickListener(this);
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

    private void loadSportsSpinner() {

        final SportRepo sportRepo = new SportRepo();
        ArrayList<HashMap<String, String>> main = new ArrayList<>();
        HashMap<String, String> firstItem = new HashMap<>();
        firstItem.put("ID", "");
        firstItem.put("Name", "--- Select Sport ---");
        main.add(firstItem);
        ArrayList<HashMap<String, String>> listType = sportRepo.getSportList();
        main.addAll(listType);

        ListAdapter adapter;
        if (listType.size() != 0) {
            System.out.println("");
            adapter = new SimpleAdapter(this, main, R.layout.activity_spinner_item,
                    new String[] {"ID", "Name"},
                    new int[] {R.id.ID, R.id.Name});
            mSpnSport.setAdapter((SpinnerAdapter) adapter);
        } else {
            adapter = null;
            mSpnSport.setAdapter((SpinnerAdapter) adapter);
        }
    }

    private boolean isDataValid() {

        if (mTxtGameNo.getText() == null || mTxtGameNo.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please input the game number.", Toast.LENGTH_SHORT).show();
            mTxtGameNo.requestFocus();
            return false;
        }

        if (mSpnMedalType.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select the medal type.", Toast.LENGTH_SHORT).show();
            mSpnMedalType.requestFocus();
            return false;
        }

        if (mSpnSport.getSelectedItem() == null || mSpnSport.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select the sport.", Toast.LENGTH_SHORT).show();
            mSpnSport.requestFocus();
            return false;
        }


        if (mTxtWinner.getText().toString().equalsIgnoreCase("Tap to Set")) {
            Toast.makeText(this, "Please select the winning athlete/college.", Toast.LENGTH_SHORT).show();
            mTxtWinner.requestFocus();
            return false;
        }

        if (mTxtLoser.getText().toString().equalsIgnoreCase("Tap to Set")) {
            Toast.makeText(this, "Please select the losing athlete/college.", Toast.LENGTH_SHORT).show();
            mTxtLoser.requestFocus();
            return false;
        }

        String txtWinner = mTxtWinner.getText().toString();
        String txtLoser = mTxtLoser.getText().toString();

        String winnerID = txtWinner.substring(txtWinner.indexOf("[") + 1, txtWinner.indexOf("]"));
        String loserID = txtLoser.substring(txtLoser.indexOf("[") + 1, txtLoser.indexOf("]"));

        if (winnerID.equalsIgnoreCase(loserID)) {
            Toast.makeText(this, "Winner and loser can't be the same athlete/college.", Toast.LENGTH_SHORT).show();
            mTxtWinner.requestFocus();
            return false;
        }

        SportRepo sportRepo = new SportRepo();
        Sport sport = sportRepo.getSportByID(sportID);
        System.out.println("Sport: " + sport.getSportName() + ", " + sport.getSportID());
        if (sport.getSportType().equalsIgnoreCase("Individual")) {
            AthleteRepo athleteRepo = new AthleteRepo();
            Athlete winner = athleteRepo.getAthleteByID(winnerID);
            Athlete loser = athleteRepo.getAthleteByID(loserID);
            if (winner.getAthleteCol().equalsIgnoreCase(loser.getAthleteCol())) {
                Toast.makeText(this, "Winner and loser can't be in the same college.", Toast.LENGTH_SHORT).show();
                mTxtWinner.requestFocus();
                return false;
            }
        }
        GameRepo gameRepo = new GameRepo();
        String gameNo = mTxtGameNo.getText().toString();

        if (gameRepo.isGameExists(sportID, gameNo)) {
            Toast.makeText(this, "Game number entered already exists for the selected sport.", Toast.LENGTH_SHORT).show();
            mTxtGameNo.requestFocus();
            return false;
        }
        return true;
    }

    private void save() {

        if (isDataValid()) {

            Game game = new Game();
            GameRepo gameRepo = new GameRepo();
            String gameNo = mTxtGameNo.getText().toString().trim();
            String medalType = mSpnMedalType.getSelectedItem().toString().trim();
            String sportId = sportID;
            String wnID = winnerID;
            String lsID = loserID;
            String wAthID = winnerAthId;
            String lAthID = loserAthId;

            game.setGameNo(gameNo);
            game.setMedalType(medalType);
            game.setSportId(sportId);
            game.setWinnerId(wnID);
            game.setLoserId(lsID);
            game.setWinnerAthId(wAthID);
            game.setLoserAthId(lAthID);

            SportRepo sportRepo = new SportRepo();
            Sport sport = sportRepo.getSportByID(sportId);
            String sportName = sport.getSportName();
            String sportCategory = sport.getSportCategory();
            String sportDisplayName = sportName;
            if (!sportCategory.equalsIgnoreCase("None")) {
                sportDisplayName = sportName + " (" + sportCategory + ")";
            }

            if (title.equalsIgnoreCase("Add Game")) {
                gameRepo.insert(game);
                Toast.makeText(this, "Game recorded!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MedalDetail.this, MedalTallyActivity.class);
                intent.putExtra("sportID", sportId);
                intent.putExtra("title", sportDisplayName);
                startActivity(intent);
                finish();
            }
        }
    }

   /* private void save() {
        Medal medal = new Medal();
        MedalRepo medalRepo = new MedalRepo();
        String medalType = mSpnMedalType.getSelectedItem().toString().trim();
        String sportId = sportID;
        String athColId = athColID;
        medal.setMedalType(medalType);
        medal.setSportID(sportId);
        medal.setAthColID(athColId);

        if (mSpnSport.getSelectedItem() == null) {
            Toast.makeText(this, "Please set sports first.", Toast.LENGTH_SHORT).show();
            mSpnSport.requestFocus();
            return;
        }

        SportRepo sportRepo = new SportRepo();
        Sport sport = sportRepo.getSportByID(sportId);
        String sportName = sport.getSportName();
        String sportCategory = sport.getSportCategory();
        String sportDisplayName = sportName;
        if (!sportCategory.equalsIgnoreCase("None")) {
            sportDisplayName = sportName + " (" + sportCategory + ")";
        }

        if (isDataValid()) {
            if (title.equalsIgnoreCase("Add Medal")) {
                medalRepo.insert(medal);
                Toast.makeText(this, "Medal recorded!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MedalDetail.this, MedalTallyActivity.class);
                intent.putExtra("sportID", sportId);
                intent.putExtra("title", sportDisplayName);
                startActivity(intent);
                finish();
            }
        }*/

    @Override
    public void onClick(View v) {
        if (v ==  findViewById(R.id.txtWinner) || v == findViewById(R.id.txtLoser)) {
            if (v == findViewById(R.id.txtWinner)) {
                clickedText = "W";
            } else {
                clickedText = "L";
            }
            if (mSpnSport.getSelectedItemPosition() != 0) {
                mSpnAthCol.showSpinerDialog();
            } else {
                Toast.makeText(MedalDetail.this, "Please select a sport first.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}