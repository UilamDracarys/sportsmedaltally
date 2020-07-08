package com.cpsu.sports.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cpsu.sports.data.DBHelper;
import com.cpsu.sports.data.DatabaseManager;
import com.cpsu.sports.data.model.Sport;

import java.util.ArrayList;
import java.util.HashMap;

public class SportRepo {
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public SportRepo() {
        Sport sport = new Sport();
    }

    public static String createSportsTable() {
        String query = "CREATE TABLE IF NOT EXISTS " + Sport.TABLE_SPORTS + " (" +
                Sport.COL_SPORT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,  " +
                Sport.COL_SPORT_NAME + " TEXT, " +
                Sport.COL_SPORT_CATEGORY + " TEXT, " +
                Sport.COL_SPORT_TYPE + " TEXT)";
        return query;
    }

    public void insert(Sport sport) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Sport.COL_SPORT_NAME, sport.getSportName());
        values.put(Sport.COL_SPORT_CATEGORY, sport.getSportCategory());
        values.put(Sport.COL_SPORT_TYPE, sport.getSportType());

        // Inserting Row
        db.insert(Sport.TABLE_SPORTS, null, values);
        db.close();
    }

    public void update(Sport sport) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Sport.COL_SPORT_NAME, sport.getSportName());
        values.put(Sport.COL_SPORT_CATEGORY, sport.getSportCategory());
        values.put(Sport.COL_SPORT_TYPE, sport.getSportType());

        db.update(Sport.TABLE_SPORTS, values, Sport.COL_SPORT_ID + "= ? ", new String[]{String.valueOf(sport.getSportID())});
        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, String>> getSportList() {

        //Open connection to read only
        //db = DatabaseManager.getInstance().openDatabase();
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT " + Sport.COL_SPORT_ID + " as SportID, " +
                Sport.COL_SPORT_NAME + " as SportName, " +
                Sport.COL_SPORT_CATEGORY + " as SportCategory, " +
                Sport.COL_SPORT_TYPE + " as SportType " +
                "FROM " + Sport.TABLE_SPORTS + " ORDER BY SportName";

        ArrayList<HashMap<String, String>> sportList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> sports = new HashMap<>();
                sports.put("sportID", cursor.getString(cursor.getColumnIndex("SportID")));
                sports.put("sportName", cursor.getString(cursor.getColumnIndex("SportName")));
                sports.put("sportCategory", cursor.getString(cursor.getColumnIndex("SportCategory")));
                sports.put("sportType", cursor.getString(cursor.getColumnIndex("SportType")));
                sportList.add(sports);
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return sportList;
    }

    public Sport getSportByID(String id) {
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT " + Sport.COL_SPORT_ID + " as SportID, " +
                Sport.COL_SPORT_NAME + " as SportName, " +
                Sport.COL_SPORT_CATEGORY + " as SportCategory, " +
                Sport.COL_SPORT_TYPE + " as SportType " +
                "FROM " + Sport.TABLE_SPORTS + " ORDER BY SportName";

        Sport sport = new Sport();

        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        if (cursor.moveToFirst()) {
            do {
                sport.setSportID(cursor.getString(cursor.getColumnIndex("SportID")));
                sport.setSportName(cursor.getString(cursor.getColumnIndex("SportName")));
                sport.setSportCategory(cursor.getString(cursor.getColumnIndex("SportCategory")));
                sport.setSportType(cursor.getString(cursor.getColumnIndex("SportType")));
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return sport;
    }


}
