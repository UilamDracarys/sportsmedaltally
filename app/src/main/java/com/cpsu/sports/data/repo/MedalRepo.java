package com.cpsu.sports.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cpsu.sports.data.DBHelper;
import com.cpsu.sports.data.DatabaseManager;
import com.cpsu.sports.data.model.Medal;

import java.util.ArrayList;
import java.util.HashMap;

public class MedalRepo {
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public MedalRepo() {
        Medal medal = new Medal();
    }

    public static String createMedalsTable() {
        String query = "CREATE TABLE IF NOT EXISTS " + Medal.TABLE_MEDALS + " (" +
                Medal.COL_MEDAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Medal.COL_COL_ID + " TEXT, " +
                Medal.COL_ATH_ID + " TEXT, " +
                Medal.COL_SPORT_ID + " TEXT, " +
                Medal.COL_MEDAL_TYPE + " TEXT)";
        return query;
    }

    public void insert(Medal medal) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Medal.COL_COL_ID, medal.getColID());
        values.put(Medal.COL_ATH_ID, medal.getAthID());
        values.put(Medal.COL_SPORT_ID, medal.getSportID());
        values.put(Medal.COL_MEDAL_TYPE, medal.getMedalType());

        // Inserting Row
        db.insert(Medal.TABLE_MEDALS, null, values);
        db.close();
    }

    public void update(Medal medal) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(Medal.COL_COL_ID, medal.getColID());
        values.put(Medal.COL_ATH_ID, medal.getColID());
        values.put(Medal.COL_SPORT_ID, medal.getSportID());
        values.put(Medal.COL_MEDAL_TYPE, medal.getMedalType());


        db.update(Medal.TABLE_MEDALS, values, Medal.COL_MEDAL_ID + "= ? ", new String[]{String.valueOf(medal.getMedalID())});
        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, String>> getMedalList() {
        //Open connection to read only
        //db = DatabaseManager.getInstance().openDatabase();
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT " + Medal.COL_MEDAL_ID + " as MedalID, " +
                Medal.COL_COL_ID + " as ColID, " +
                Medal.COL_ATH_ID + " as AthID, " +
                Medal.COL_SPORT_ID + " as SportID, " +
                Medal.COL_MEDAL_TYPE + " as MedalType " +
                "FROM " + Medal.TABLE_MEDALS + " ORDER BY MedalID";

        ArrayList<HashMap<String, String>> medalList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> medals = new HashMap<>();
                medals.put("medalID", cursor.getString(cursor.getColumnIndex("MedalID")));
                medals.put("colID", cursor.getString(cursor.getColumnIndex("ColID")));
                medals.put("athID", cursor.getString(cursor.getColumnIndex("AthID")));
                medals.put("sportID", cursor.getString(cursor.getColumnIndex("SportID")));
                medals.put("medalType", cursor.getString(cursor.getColumnIndex("MedalType")));
                medalList.add(medals);
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return medalList;
    }

    public Medal getMedalByID(String id) {
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();


        String selectQuery = "SELECT " + Medal.COL_MEDAL_ID + " as MedalID, " +
                Medal.COL_COL_ID + " as ColID, " +
                Medal.COL_ATH_ID + " as AthID, " +
                Medal.COL_SPORT_ID + " as SportID, " +
                Medal.COL_MEDAL_TYPE + " as MedalType " +
                "FROM " + Medal.TABLE_MEDALS + " WHERE MedalID = ?";

        Medal medal = new Medal();

        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        if (cursor.moveToFirst()) {
            do {
                medal.setMedalID(cursor.getString(cursor.getColumnIndex("MedalID")));
                medal.setColID(cursor.getString(cursor.getColumnIndex("ColID")));
                medal.setAthID(cursor.getString(cursor.getColumnIndex("AthID")));
                medal.setSportID(cursor.getString(cursor.getColumnIndex("SportID")));
                medal.setMedalType(cursor.getString(cursor.getColumnIndex("MedalType")));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return medal;
    }

}
