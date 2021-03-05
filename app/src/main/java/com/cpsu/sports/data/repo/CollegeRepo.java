package com.cpsu.sports.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.JsonToken;

import com.cpsu.sports.data.DBHelper;
import com.cpsu.sports.data.DatabaseManager;
import com.cpsu.sports.data.model.College;
import com.cpsu.sports.data.model.Game;
import com.cpsu.sports.data.model.Sport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollegeRepo {

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    ArrayList<String> champs, second, third;
    double goldWt = 1.0, silverWt = 0.5, bronzeWt = 0.25;

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

    public ArrayList<String> getCollegesForSpinner() {
        //Open connection to read only
        //db = DatabaseManager.getInstance().openDatabase();
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT " + College.COL_COLLEGE_ID + " as CollegeID, " +
                College.COL_COLLEGE_CODE + " as CollegeCode, " +
                College.COL_COLLEGE_NAME + " as CollegeName " +
                "FROM " + College.TABLE_COLLEGES + " ORDER BY CollegeName";

        ArrayList<String> collegeList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String collegeID = cursor.getString(cursor.getColumnIndex("CollegeID"));
                String collegeName = cursor.getString(cursor.getColumnIndex("CollegeName"));
                String item = collegeName + " [" + collegeID + "]";
                collegeList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return collegeList;
    }

    public int getMedalCount(String collegeID, String sportID, String medalType) {
        //Open connection to read only
        //db = DatabaseManager.getInstance().openDatabase();
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();

        int medals = 0;
        SportRepo sportRepo = new SportRepo();
        String sportType = sportRepo.getSportType(sportID);
        String selectQuery = "";
        if (sportType.equalsIgnoreCase("Team")) {
            /*selectQuery = "select count(*) as Count\n" +
                    "from medals\n" +
                    "where ath_col_id = \"" + collegeID + "\"\n" +
                    "and sport_id = \"" + sportID + "\"\n" +
                    "and medal_type = \"" + medalType + "\" \n" +
                    "group by ath_col_id, sport_id, medal_type";*/

            selectQuery = "select count(*) as Count\n" +
                    "from games\n" +
                    "where winner_id = \"" + collegeID + "\"\n" +
                    "and sport_id = \"" + sportID + "\"\n" +
                    "and medal_type = \"" + medalType + "\" \n" +
                    "group by winner_id, sport_id, medal_type";

        } else if (sportType.equalsIgnoreCase("Individual")) {
            selectQuery = "select count(*) as Count\n" +
                    "from games m\n" +
                    "left join athletes a\n" +
                    "on a.athlete_id = m.winner_id\n" +
                    "where a.athlete_col = \"" + collegeID + "\"\n" +
                    "and sport_id = \"" + sportID + "\"\n" +
                    "and medal_type = \"" + medalType + "\"\n" +
                    "group by winner_id, sport_id, medal_type";
        }

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                medals = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Count")));
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return medals;

    }


    public ArrayList<HashMap<String, String>> getCollegeMedals(String sportID) {
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT c.college_id as CollegeID, " +
                "c." + College.COL_COLLEGE_CODE + " as CollegeCode, \n" +
                "c." + College.COL_COLLEGE_NAME + " as CollegeName, \n" +
                "COUNT(CASE WHEN g." + Game.COL_MEDAL_TYPE + " = 'Gold' AND s." + Sport.COL_SPORT_ID + " ='" + sportID + "' THEN 1 END) Gold,\n" +
                "COUNT(CASE WHEN g." + Game.COL_MEDAL_TYPE + " = 'Silver' AND s." + Sport.COL_SPORT_ID + " ='" + sportID + "' THEN 1 END) Silver,\n" +
                "COUNT(CASE WHEN g." + Game.COL_MEDAL_TYPE + " = 'Bronze' AND s." + Sport.COL_SPORT_ID + " ='" + sportID + "' THEN 1 END) Bronze,\n" +
                "COUNT(CASE WHEN s." + Sport.COL_SPORT_ID + " ='" + sportID + "' THEN 1 END) AS medal_count,\n" +
                "(COUNT(CASE WHEN g.medal_type = 'Gold' AND s.sport_id ='" + sportID + "' THEN 1 END)) * " + goldWt + " as GoldWt,\n" +
                "(COUNT(CASE WHEN g.medal_type = 'Silver' AND s.sport_id ='" + sportID + "' THEN 1 END)) * " + silverWt + " as SilverWt,\n" +
                "(COUNT(CASE WHEN g.medal_type = 'Bronze' AND s.sport_id ='" + sportID + "' THEN 1 END)) * " + bronzeWt + " as BronzeWt,\n" +
                "((COUNT(CASE WHEN g.medal_type = 'Gold' AND s.sport_id ='" + sportID + "' THEN 1 END)) * " + goldWt + ")+" +
                "((COUNT(CASE WHEN g.medal_type = 'Silver' AND s.sport_id ='" + sportID + "' THEN 1 END)) * " + silverWt + ")+" +
                "((COUNT(CASE WHEN g.medal_type = 'Bronze' AND s.sport_id ='" + sportID + "' THEN 1 END)) * " +  bronzeWt + ") as TotalWt\n" +
                "FROM " + College.TABLE_COLLEGES + " c\n" +
                "LEFT JOIN (" + Game.TABLE_GAMES + " g\n" +
                "JOIN " + Sport.TABLE_SPORTS + " s)\n" +
                "ON c." + College.COL_COLLEGE_ID + " = g." + Game.COL_WINNER_ID + "\n" +
                "AND g." + Game.COL_SPORT_ID + " = s." + Sport.COL_SPORT_ID + "\n" +
                "GROUP BY c." + College.COL_COLLEGE_ID + "\n" +
                "ORDER BY TotalWt DESC;";

        System.out.println(selectQuery);

        ArrayList<HashMap<String, String>> collegeList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            int rowNumber = 1;
            int ranking = 1;

            double prevMedalWt = cursor.getInt(cursor.getColumnIndex("TotalWt"));
            do {
                HashMap<String, String> map = new HashMap<>();
                String rankWord = "";
                String collegeID = cursor.getString(cursor.getColumnIndex("CollegeID"));
                String collegeCode = cursor.getString(cursor.getColumnIndex("CollegeCode"));
                String collegeName = cursor.getString(cursor.getColumnIndex("CollegeName"));
                int gold = cursor.getInt(cursor.getColumnIndex("Gold"));
                int silver = cursor.getInt(cursor.getColumnIndex("Silver"));
                int bronze = cursor.getInt(cursor.getColumnIndex("Bronze"));
                double totalWt = cursor.getDouble(cursor.getColumnIndex("TotalWt"));
                String total = cursor.getString(cursor.getColumnIndex("medal_count"));

                map.put("ID", collegeID);
                map.put("Code", collegeCode);
                map.put("Name", collegeName);
                map.put("Gold", String.valueOf(gold));
                map.put("Silver", String.valueOf(silver));
                map.put("Bronze", String.valueOf(bronze));
                map.put("Total", total);
                map.put("TotalWt", String.valueOf(totalWt));
                map.put("Medals", "Total: " + total + "\n" +
                        "Gold: " + gold + "\n" +
                        "Silver: " + silver + "\n" +
                        "Bronze: " + bronze);

                if (rowNumber > 1) {
                    if (totalWt < prevMedalWt) {
                        ranking += 1;
                    }
                }
                map.put("Rank", String.valueOf(ranking));
                if (ranking == 1 && totalWt > 0) {
                    rankWord = "Champion";
                } else if (ranking == 2 && totalWt > 0) {
                    rankWord = "2nd Placer";
                } else if (ranking == 3  && totalWt > 0) {
                    rankWord = "3rd Placer";
                } else {
                    rankWord = "-";
                }
                map.put("RankWord", rankWord);
                collegeList.add(map);
                rowNumber++;
                prevMedalWt = totalWt;
                System.out.println("Map: " + map);

            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return collegeList;
    }
}
