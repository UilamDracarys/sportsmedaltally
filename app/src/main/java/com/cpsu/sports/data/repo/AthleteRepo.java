package com.cpsu.sports.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cpsu.sports.data.DBHelper;
import com.cpsu.sports.data.DatabaseManager;
import com.cpsu.sports.data.model.Athlete;
import com.cpsu.sports.data.model.Sport;

import java.util.ArrayList;
import java.util.HashMap;

public class AthleteRepo {
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public AthleteRepo() {
        Athlete athlete = new Athlete();
    }

    public static String createAthletesTable() {
        String query = "CREATE TABLE IF NOT EXISTS " + Athlete.TABLE_ATHLETES + " (" +
                Athlete.COL_ATHLETE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Athlete.COL_ATHLETE_NAME + " TEXT, " +
                Athlete.COL_ATHLETE_ADD + " TEXT, " +
                Athlete.COL_ATHLETE_COL + " TEXT)";
        return query;
    }

    public void insert(Athlete athlete) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Athlete.COL_ATHLETE_NAME, athlete.getAthleteName());
        values.put(Athlete.COL_ATHLETE_ADD, athlete.getAthleteCol());
        values.put(Athlete.COL_ATHLETE_COL, athlete.getAthleteCol());

        // Inserting Row
        db.insert(Athlete.TABLE_ATHLETES, null, values);
        db.close();
    }

    public void update(Athlete athlete) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Athlete.COL_ATHLETE_NAME, athlete.getAthleteName());
        values.put(Athlete.COL_ATHLETE_ADD, athlete.getAthleteCol());
        values.put(Athlete.COL_ATHLETE_COL, athlete.getAthleteCol());

        db.update(Athlete.TABLE_ATHLETES, values, Athlete.COL_ATHLETE_ID+ "= ? ", new String[]{String.valueOf(athlete.getAthleteID())});
        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, String>> getAthleteList() {
        //Open connection to read only
        //db = DatabaseManager.getInstance().openDatabase();
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT " + Athlete.COL_ATHLETE_ID + " as AthleteID, " +
                Athlete.COL_ATHLETE_NAME + " as AthleteName, " +
                Athlete.COL_ATHLETE_ADD + " as AthleteAdd, " +
                Athlete.COL_ATHLETE_COL + " as AthleteCol " +
                "FROM " + Athlete.TABLE_ATHLETES + " ORDER BY AthleteName";

        ArrayList<HashMap<String, String>> athleteList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> athletes = new HashMap<>();
                athletes.put("athleteID", cursor.getString(cursor.getColumnIndex("AthleteID")));
                athletes.put("athleteName", cursor.getString(cursor.getColumnIndex("AthleteName")));
                athletes.put("athleteAdd", cursor.getString(cursor.getColumnIndex("AthleteAdd")));
                athletes.put("athleteCol", cursor.getString(cursor.getColumnIndex("AthleteCol")));
                athleteList.add(athletes);
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return athleteList;
    }

    public Athlete getAthleteByID(String id) {
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT " + Athlete.COL_ATHLETE_ID + " as AthleteID, " +
                Athlete.COL_ATHLETE_NAME + " as AthleteName, " +
                Athlete.COL_ATHLETE_ADD + " as AthleteAdd, " +
                Athlete.COL_ATHLETE_COL + " as AthleteCol " +
                "FROM " + Athlete.TABLE_ATHLETES + " ORDER BY AthleteName";

        Athlete athlete = new Athlete();

        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        if (cursor.moveToFirst()) {
            do {
                athlete.setAthleteID(cursor.getString(cursor.getColumnIndex("AthleteID")));
                athlete.setAthleteName(cursor.getString(cursor.getColumnIndex("AthleteName")));
                athlete.setAthleteAdd(cursor.getString(cursor.getColumnIndex("AthleteAdd")));
                athlete.setAthleteCol(cursor.getString(cursor.getColumnIndex("AthleteCol")));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return athlete;
    }

}
