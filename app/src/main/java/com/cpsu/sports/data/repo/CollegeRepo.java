package com.cpsu.sports.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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


    public ArrayList<HashMap<String, String>> getCollegeMedalList(String sportID, String medalType) {
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();

        String filter = "";
        if (medalType.equalsIgnoreCase("All")) {
            filter =  "";
        } else if (medalType.equalsIgnoreCase("Gold")) {
            filter = "WHERE g." + Game.COL_MEDAL_TYPE + " = 'Gold'\n";
        } else if (medalType.equalsIgnoreCase("Silver")) {
            filter = "WHERE g." + Game.COL_MEDAL_TYPE + " = 'Silver'\n";
        } else if (medalType.equalsIgnoreCase("Bronze")) {
            filter = "WHERE g." + Game.COL_MEDAL_TYPE + " = 'Bronze'\n";
        }

        String selectQuery = "SELECT c.college_id as CollegeID, " +
                "c." + College.COL_COLLEGE_CODE + " as CollegeCode, \n" +
                "c." + College.COL_COLLEGE_NAME + " as CollegeName, \n" +
                "COUNT(CASE WHEN g." + Game.COL_MEDAL_TYPE + " = 'Gold' AND s." + Sport.COL_SPORT_ID + " ='" + sportID + "' THEN 1 END) Gold,\n" +
                "COUNT(CASE WHEN g." + Game.COL_MEDAL_TYPE + " = 'Silver' AND s." + Sport.COL_SPORT_ID + " ='" + sportID + "' THEN 1 END) Silver,\n" +
                "COUNT(CASE WHEN g." + Game.COL_MEDAL_TYPE + " = 'Bronze' AND s." + Sport.COL_SPORT_ID + " ='" + sportID + "' THEN 1 END) Bronze,\n" +
                "COUNT(CASE WHEN s." + Sport.COL_SPORT_ID + " ='" + sportID + "' THEN 1 END) AS medal_count\n" +
                "FROM " + College.TABLE_COLLEGES + " c\n" +
                "LEFT JOIN " + Game.TABLE_GAMES + " g\n" +
                "JOIN " + Sport.TABLE_SPORTS + " s\n" +
                "ON c." + College.COL_COLLEGE_ID + " = g." + Game.COL_WINNER_ID + "\n" +
                "AND g." + Game.COL_SPORT_ID + " = s." + Sport.COL_SPORT_ID + "\n" +
                filter +
                "GROUP BY g." + Game.COL_WINNER_ID + "\n" +
                "ORDER BY medal_count DESC";

        System.out.println(selectQuery);

        ArrayList<HashMap<String, String>> collegeList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            int rowNumber = 1;
            int ranking = 1;

            int prevMedalCount = cursor.getInt(cursor.getColumnIndex("medal_count"));
            ArrayList<HashMap<String, String>> finalRanking = getRanking(sportID);
            do {
                HashMap<String, String> map = new HashMap<>();
                String collegeID = cursor.getString(cursor.getColumnIndex("CollegeID"));
                String rankWord = "";
                map.put("Row", String.valueOf(rowNumber));
                map.put("ID", collegeID);
                map.put("Code", cursor.getString(cursor.getColumnIndex("CollegeCode")));
                map.put("Name", cursor.getString(cursor.getColumnIndex("CollegeName")));
                int gold = cursor.getInt(cursor.getColumnIndex("Gold"));
                int silver = cursor.getInt(cursor.getColumnIndex("Silver"));
                int bronze = cursor.getInt(cursor.getColumnIndex("Bronze"));
                int total = cursor.getInt(cursor.getColumnIndex("medal_count"));
                String medals = "Total: " + total + "\n" +
                        "Gold: " + gold + "\n" +
                        "Silver: " + silver + "\n" +
                        "Bronze: " + bronze;
                map.put("Medals", medals);
                /*if (rowNumber > 1) {
                    if (total < prevMedalCount) {
                        ranking += 1;
                    }
                }
                map.put("Rank", String.valueOf(ranking));
                if (ranking == 1 && total > 0) {
                    rankWord = "Champion";
                    champs.add(collegeID);
                } else if (ranking == 2 && total > 0) {
                    rankWord = "2nd Placer";
                    second.add(collegeID);
                } else if (ranking == 3  && total > 0) {
                    rankWord = "3rd Placer";
                    third.add(collegeID);
                }*/

                for (int i=0; i<finalRanking.size();i++){
                    if (finalRanking.get(i).get("ID").equalsIgnoreCase(collegeID)) {
                        rankWord = finalRanking.get(i).get("RankWord");
                    }
                }

                map.put("RankWord", rankWord);

                collegeList.add(map);
                rowNumber++;
                prevMedalCount = total;
                System.out.println(rowNumber + ": " + map + "\n");

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return collegeList;
    }

    public ArrayList<HashMap<String, String>> getCollegeMedals(String sportID, String medalType) {
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();
        
        double weight = 0;
        String sort = "";
        if (medalType.equalsIgnoreCase("All")) {
            sort =  "medal_count";
            weight = 1;
        } else if (medalType.equalsIgnoreCase("Gold")) {
            sort = "Gold";
            weight = 1;
        } else if (medalType.equalsIgnoreCase("Silver")) {
            sort = "Silver";
            weight = 1;
        } else if (medalType.equalsIgnoreCase("Bronze")) {
            sort = "Bronze";
            weight = 1;
        }


        String selectQuery = "SELECT c.college_id as CollegeID, " +
                "c." + College.COL_COLLEGE_CODE + " as CollegeCode, \n" +
                "c." + College.COL_COLLEGE_NAME + " as CollegeName, \n" +
                "COUNT(CASE WHEN g." + Game.COL_MEDAL_TYPE + " = 'Gold' AND s." + Sport.COL_SPORT_ID + " ='" + sportID + "' THEN 1 END) Gold,\n" +
                "COUNT(CASE WHEN g." + Game.COL_MEDAL_TYPE + " = 'Silver' AND s." + Sport.COL_SPORT_ID + " ='" + sportID + "' THEN 1 END) Silver,\n" +
                "COUNT(CASE WHEN g." + Game.COL_MEDAL_TYPE + " = 'Bronze' AND s." + Sport.COL_SPORT_ID + " ='" + sportID + "' THEN 1 END) Bronze,\n" +
                "COUNT(CASE WHEN s." + Sport.COL_SPORT_ID + " ='" + sportID + "' THEN 1 END) AS medal_count\n" +
                "FROM " + College.TABLE_COLLEGES + " c\n" +
                "LEFT JOIN " + Game.TABLE_GAMES + " g\n" +
                "JOIN " + Sport.TABLE_SPORTS + " s\n" +
                "ON c." + College.COL_COLLEGE_ID + " = g." + Game.COL_WINNER_ID + "\n" +
                "AND g." + Game.COL_SPORT_ID + " = s." + Sport.COL_SPORT_ID + "\n" +
                "GROUP BY g." + Game.COL_WINNER_ID + "\n" +
                "ORDER BY " + sort + " DESC";

        System.out.println(selectQuery);

        ArrayList<HashMap<String, String>> collegeList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            int rowNumber = 1;
            double ranking = weight;

            int prevMedalCount = cursor.getInt(cursor.getColumnIndex(sort));
            do {
                HashMap<String, String> map = new HashMap<>();
                String collegeID = cursor.getString(cursor.getColumnIndex("CollegeID"));
                String collegeCode = cursor.getString(cursor.getColumnIndex("CollegeCode"));
                map.put("ID", collegeID);
                map.put("Code", collegeCode);
                map.put("Gold", cursor.getString(cursor.getColumnIndex("Gold")));
                map.put("Silver", cursor.getString(cursor.getColumnIndex("Silver")));
                map.put("Bronze", cursor.getString(cursor.getColumnIndex("Bronze")));
                map.put("Total", cursor.getString(cursor.getColumnIndex("medal_count")));
                int medals = cursor.getInt(cursor.getColumnIndex(sort));
                map.put("Medals", String.valueOf(medals));
                if (rowNumber > 1) {
                    if (medals < prevMedalCount) {
                        ranking += weight;
                    }
                }
                map.put("Rank", String.valueOf(ranking));
                collegeList.add(map);
                System.out.println(rowNumber + ": " + map + "\n");
                rowNumber++;
                prevMedalCount = medals;

            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return collegeList;
    }

    public ArrayList<HashMap<String, String>> getRanking(String sportID) {
        ArrayList<HashMap<String, String>> totalRank = getCollegeMedals(sportID, "All");
        ArrayList<HashMap<String, String>> goldRank = getCollegeMedals(sportID, "Gold");
        ArrayList<HashMap<String, String>> silverRank = getCollegeMedals(sportID, "Silver");
        ArrayList<HashMap<String, String>> bronzeRank = getCollegeMedals(sportID, "Bronze");
        ArrayList<HashMap<String, String>> finalRank = new ArrayList<>();

        System.out.println("GoldRank: " + goldRank);
        System.out.println("SilverRank: " + silverRank);
        System.out.println("BronzeRank: " + bronzeRank);

        for (int i=0;i<totalRank.size(); i++){
            HashMap<String, String> map = new HashMap<>();
            String ID = totalRank.get(i).get("ID");
            double tRank = Double.parseDouble(totalRank.get(i).get("Rank"));
            double gRank = 0, sRank = 0, bRank = 0;
            double finRank;
            map.put("ID", ID);
            for (int j=0;j<goldRank.size(); j++) {
                if (ID.equalsIgnoreCase(goldRank.get(j).get("ID"))) {
                    gRank = Double.parseDouble(goldRank.get(j).get("Rank"));
                }
            }

            for (int j=0;j<silverRank.size(); j++) {
                if (ID.equalsIgnoreCase(silverRank.get(j).get("ID"))) {
                    sRank = Double.parseDouble(silverRank.get(j).get("Rank"));
                }
            }

            for (int j=0;j<bronzeRank.size(); j++) {
                if (ID.equalsIgnoreCase(bronzeRank.get(j).get("ID"))) {
                    bRank = Double.parseDouble(bronzeRank.get(j).get("Rank"));
                }
            }

            finRank = tRank + gRank + sRank + bRank;
            map.put("FinalRank", String.valueOf(finRank));

            System.out.println(map);
            finalRank.add(map);
        }

        Collections.sort(finalRank, new MapComparator("FinalRank"));

        System.out.println("FinalRanking:\n");

        int rowNumber = 1;
        double ranking = 1;
        double prevRanking = Double.parseDouble(finalRank.get(0).get("FinalRank"));

        for (int i=0;i<finalRank.size();i++) {
            double finalRanking = Double.parseDouble(finalRank.get(i).get("FinalRank"));
            String rankWord = "";
            if (rowNumber > 1) {
                if (finalRanking > prevRanking) {
                    ranking += 1;
                }
            }

            finalRank.get(i).put("Rank", String.valueOf(ranking));
            if (ranking == 1 && finalRanking > 0) {
                rankWord = "Champion";
            } else if (ranking == 2 && finalRanking > 0) {
                rankWord = "2nd Placer";
            } else if (ranking == 3  && finalRanking > 0) {
                rankWord = "3rd Placer";
            }
            finalRank.get(i).put("RankWord", rankWord);
            System.out.println(finalRank.get(i));
            rowNumber++;
            prevRanking = finalRanking;
        }

        return finalRank;

    }

    class MapComparator implements Comparator<Map<String, String>>
    {
        private final String key;

        public MapComparator(String key)
        {
            this.key = key;
        }

        public int compare(Map<String, String> first,
                           Map<String, String> second)
        {
            // TODO: Null checking, both for maps and values
            String firstValue = first.get(key);
            String secondValue = second.get(key);
            return firstValue.compareTo(secondValue);
        }
    }
}
