package com.cpsu.sports.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.cpsu.sports.R;
import com.cpsu.sports.Utils.PreferenceUtils;
import com.cpsu.sports.Utils.SearchableAdapter;
import com.cpsu.sports.data.model.Medal;
import com.cpsu.sports.data.model.Sport;
import com.cpsu.sports.data.repo.SportRepo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

import static com.cpsu.sports.app.App.getContext;

public class MainActivity extends AppCompatActivity {
    String username;
    TextView mTextWelcome, txtSportID, txtSportName;
    Boolean isFabOpen;
    FloatingActionButton fab, fabAddAthlete, fabAddMedal, fabAddSport;
    SearchableAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportActionBar().hide();

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        isFabOpen = false;
        init();
        loadSports();

    }

    private void init() {
        mTextWelcome = findViewById(R.id.txtWelcome);
        mTextWelcome.setText("Welcome, " + username + "!");
        fab = findViewById(R.id.fabAdd);
        fabAddAthlete = findViewById(R.id.fabAddAthlete);
        fabAddMedal = findViewById(R.id.fabAddMedal);
        /*fabAddSport = findViewById(R.id.fabAddSport);*/
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!isFabOpen) {
                    showFabMenu();
                } else {
                    closeFabMenu();
                }
            }
        });

        /*fabAddSport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!isFabOpen) {
                    showFabMenu();
                } else {
                    closeFabMenu();
                }
                Intent intent = new Intent(MainActivity.this, SportDetail.class);
                intent.putExtra("title", "Add Sport");
                startActivity(intent);
            }
        });*/

        fabAddMedal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!isFabOpen) {
                    showFabMenu();
                } else {
                    closeFabMenu();
                }
                Intent intent = new Intent(MainActivity.this, MedalDetail.class);
                intent.putExtra("title", "Add Game");
                startActivity(intent);
            }
        });

        fabAddAthlete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!isFabOpen) {
                    showFabMenu();
                } else {
                    closeFabMenu();
                }
                Intent intent = new Intent(MainActivity.this, AthleteDetail.class);
                intent.putExtra("title", "New Athlete");
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the save_cancel; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_colleges:
                Intent collegeList = new Intent(MainActivity.this, CollegeList.class);
                collegeList.putExtra("title", "College List");
                startActivity(collegeList);
                closeFabMenu();
                return true;
            case R.id.action_athletes:
                Intent athleteList = new Intent(MainActivity.this, AthleteList.class);
                athleteList.putExtra("title", "Athlete List");
                startActivity(athleteList);
                closeFabMenu();
                return true;
            case R.id.action_sports:
                Intent sportList = new Intent(MainActivity.this, SportList.class);
                sportList.putExtra("title", "Sport List");
                startActivity(sportList);
                closeFabMenu();
                return true;
            case R.id.action_log_out:
                logOUt();
                closeFabMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showFabMenu() {
        isFabOpen = true;
        fabAddMedal.animate().translationY(-160);
        fabAddAthlete.animate().translationY(-315);

        //fabAddSport.animate().translationY(-470);
    }

    private void closeFabMenu() {
        isFabOpen = false;
        fabAddAthlete.animate().translationY(0);
        fabAddMedal.animate().translationY(0);
        //fabAddSport.animate().translationY(0);
    }

    @Override
    public void onBackPressed() {
        logOUt();
    }

    private void logOUt() {
        new AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage(
                        "Are you sure you want to log out of the application?")
                .setIcon(
                        getResources().getDrawable(
                                android.R.drawable.ic_dialog_alert
                        ))
                .setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                PreferenceUtils.deleteUsername(getContext());
                                finish();
                            }
                        })
                .setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).show();

        /*PreferenceUtils.deleteUsername(getContext());
        finish();*/
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

                    txtSportID = view.findViewById(R.id.sportID);
                    txtSportName = view.findViewById(R.id.sportName);
                    String sportID = txtSportID.getText().toString();
                    String sportName = txtSportName.getText().toString();

                    Intent intent = new Intent(MainActivity.this, MedalTallyActivity.class);
                    intent.putExtra("sportID", sportID);
                    intent.putExtra("title", sportName);
                    startActivity(intent);
                    closeFabMenu();

                }
            });

            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                    PopupMenu popup = new PopupMenu(MainActivity.this, view);
                    popup.inflate(R.menu.popup_menu_edit);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_edit:
                                    txtSportID = view.findViewById(R.id.sportID);
                                    String sportID = txtSportID.getText().toString();

                                    Intent objIndent;
                                    objIndent = new Intent(getApplicationContext(), SportDetail.class);
                                    objIndent.putExtra("sportID", sportID);
                                    objIndent.putExtra("title", "Edit Sport");
                                    startActivity(objIndent);
                                    closeFabMenu();
                                    return true;
                                default:
                                    return onOptionsItemSelected(item);
                            }


                        }
                    });
                    popup.show();
                    return true;
                }

            });
            adapter = new SearchableAdapter(MainActivity.this,
                    sportList,
                    R.layout.activity_sport_list_item,
                    new String[]{"ID", "Name", "Type"},
                    new int[]{R.id.sportID, R.id.sportName, R.id.sportType});
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