package com.cpsu.sports.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cpsu.sports.data.DBHelper;
import com.cpsu.sports.data.model.Athlete;
import com.cpsu.sports.data.model.College;
import com.cpsu.sports.data.model.Game;
import com.cpsu.sports.data.model.Medal;
import com.cpsu.sports.data.model.Sport;

import java.util.ArrayList;
import java.util.HashMap;

public class GameRepo {
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public GameRepo() {
        Game game = new Game();
    }

    public static String createTblGames() {
        String query = "CREATE TABLE IF NOT EXISTS " + Game.TABLE_GAMES + " (" +
                Game.COL_GAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Game.COL_GAME_NO + " TEXT, " +
                Game.COL_SPORT_ID + " TEXT, " +
                Game.COL_MEDAL_TYPE + " TEXT, " +
                Game.COL_WINNER_ID + " TEXT, " +
                Game.COL_LOSER_ID + " TEXT)";
        return query;
    }

    public void insert(Game game) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Game.COL_GAME_NO, game.getGameNo());
        values.put(Game.COL_SPORT_ID, game.getSportId());
        values.put(Game.COL_MEDAL_TYPE, game.getMedalType());
        values.put(Game.COL_WINNER_ID, game.getWinnerId());
        values.put(Game.COL_LOSER_ID, game.getLoserId());

        // Inserting Row
        db.insert(Game.TABLE_GAMES, null, values);
        db.close();
    }

    public boolean isGameExists(String sportID, String gameNo) {
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT " + Game.COL_SPORT_ID + " as SportID, " +
                Game.COL_GAME_NO + " as GameNo FROM " +
                Game.TABLE_GAMES + " WHERE SportID = \'" +
                sportID + "\' AND GameNo = \'" +
                gameNo + "\'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            return true;
        }
        db.close();
        return false;
    }

    public ArrayList<HashMap<String, String>> getAthleteMedalList(String sportId, String collegeId) {
        ArrayList<HashMap<String, String>> athMdlList = new ArrayList<>();
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + Game.TABLE_GAMES + " g\n" +
                "LEFT JOIN " + Athlete.TABLE_ATHLETES + " a\n" +
                "ON (g." + Game.COL_WINNER_ID + " = a." + Athlete.COL_ATHLETE_ID + ")\n" +
                "WHERE a." + Athlete.COL_ATHLETE_COL + " =\'" + collegeId + "\'\n" +
                "AND g." + Game.COL_SPORT_ID + " = \'" + sportId + "\'";
        System.out.println(query);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("AthleteName", cursor.getString(cursor.getColumnIndex(Athlete.COL_ATHLETE_NAME)));
                map.put("MedalType", cursor.getString(cursor.getColumnIndex(Game.COL_MEDAL_TYPE)));
                athMdlList.add(map);
            } while (cursor.moveToNext());
        }

        db.close();
        return athMdlList;
    }

    public ArrayList<HashMap<String, String>> getPlacers(String sportId, String medalType) {
        ArrayList<HashMap<String, String>> placer = new ArrayList<>();
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();

        String filter = "";
        if (medalType.equalsIgnoreCase("All")) {
            filter =  "";
        } else if (medalType.equalsIgnoreCase("Gold")) {
            filter = "AND g." + Game.COL_MEDAL_TYPE + " = 'Gold'\n";
        } else if (medalType.equalsIgnoreCase("Silver")) {
            filter = "AND g." + Game.COL_MEDAL_TYPE + " = 'Silver'\n";
        } else if (medalType.equalsIgnoreCase("Bronze")) {
            filter = "AND g." + Game.COL_MEDAL_TYPE + " = 'Bronze'\n";
        }

       String query = "SELECT c." + College.COL_COLLEGE_NAME + ", \n" +
                "COUNT(g." + Game.COL_MEDAL_TYPE + ") AS medal_count\n" +
                "FROM " + College.TABLE_COLLEGES + " c\n" +
                "LEFT JOIN " + Game.TABLE_GAMES + " g\n" +
                "JOIN " + Sport.TABLE_SPORTS + " s\n" +
                "ON c." + College.COL_COLLEGE_ID + " = g." + Game.COL_WINNER_ID + "\n" +
                "AND g." + Game.COL_SPORT_ID + " = s." + Sport.COL_SPORT_ID + "\n" +
                "WHERE s." + Sport.COL_SPORT_ID + " = '" + sportId + "' \n" + filter +
                "GROUP BY g." + Game.COL_WINNER_ID + "\n" +
                "ORDER BY medal_count DESC";
        System.out.println(query);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int rowNumber = 1;
            int ranking = 1;
            int prevMedalCount = cursor.getInt(cursor.getColumnIndex("medal_count"));
            do {
                HashMap<String, String> map = new HashMap<>();
                String c = cursor.getString(cursor.getColumnIndex(College.COL_COLLEGE_NAME));
                int m = cursor.getInt(cursor.getColumnIndex("medal_count"));

                map.put("R", String.valueOf(rowNumber));
                map.put("C", c);
                map.put("M", String.valueOf(m));

                if (rowNumber > 1) {
                    if (m < prevMedalCount) {
                        ranking += 1;
                    }
                }
                map.put("Rank", String.valueOf(ranking));
                placer.add(map);
                System.out.println(rowNumber + ": " + map + "\n");
                rowNumber++;
                prevMedalCount = m;
            } while (cursor.moveToNext());
        }
        db.close();
        return placer;
    }
}
