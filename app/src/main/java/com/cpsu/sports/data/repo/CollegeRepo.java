package com.cpsu.sports.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cpsu.sports.data.DBHelper;
import com.cpsu.sports.data.DatabaseManager;
import com.cpsu.sports.data.model.College;

import java.util.ArrayList;
import java.util.HashMap;

public class CollegeRepo {

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public CollegeRepo() {
        College college = new College();
    }

    public static String createCollegesTable() {
        String query = "CREATE TABLE IF NOT EXISTS " + College.TABLE_COLLEGES + " (" +
                College.COL_COLLEGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                College.COL_COLLEGE_CODE + " TEXT, " +
                College.COL_COLLEGE_NAME + " TEXT)";
        return query;
    }

    public void insert(College college) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(College.COL_COLLEGE_CODE, college.getCollegeCode());
        values.put(College.COL_COLLEGE_NAME, college.getCollegeName());

        // Inserting Row
        db.insert(College.TABLE_COLLEGES, null, values);
        db.close();
    }

    public void update(College college) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(College.COL_COLLEGE_CODE, college.getCollegeCode());
        values.put(College.COL_COLLEGE_NAME, college.getCollegeName());

        db.update(College.TABLE_COLLEGES, values, College.COL_COLLEGE_ID + "=?", new String[]{String.valueOf(college.getCollegeID())});
        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, String>> getCollegeList() {
        //Open connection to read only
        //db = DatabaseManager.getInstance().openDatabase();
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT " + College.COL_COLLEGE_ID + " as CollegeID, " +
                College.COL_COLLEGE_CODE + " as CollegeCode, " +
                College.COL_COLLEGE_NAME + " as CollegeName " +
                "FROM " + College.TABLE_COLLEGES + " ORDER BY CollegeName";

        ArrayList<HashMap<String, String>> collegeList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> colleges = new HashMap<>();
                colleges.put("ID", cursor.getString(cursor.getColumnIndex("CollegeID")));
                colleges.put("Code", cursor.getString(cursor.getColumnIndex("CollegeCode")));
                colleges.put("Name", cursor.getString(cursor.getColumnIndex("CollegeName")));
                collegeList.add(colleges);
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return collegeList;
    }

    public College getCollegeByID(String id) {
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT " + College.COL_COLLEGE_ID + " as CollegeID, " +
                College.COL_COLLEGE_CODE + " as CollegeCode, " +
                College.COL_COLLEGE_NAME + " as CollegeName " +
                "FROM " + College.TABLE_COLLEGES + " WHERE CollegeID = ?";

       College college = new College();

       Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        if (cursor.moveToFirst()) {
            do {
                college.setCollegeID(cursor.getString(cursor.getColumnIndex("CollegeID")));
                college.setCollegeCode(cursor.getString(cursor.getColumnIndex("CollegeCode")));
                college.setCollegeName(cursor.getString(cursor.getColumnIndex("CollegeName")));
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return college;
    }
}
