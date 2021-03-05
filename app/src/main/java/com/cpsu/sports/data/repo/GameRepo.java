package com.cpsu.sports.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cpsu.sports.data.DBHelper;
import com.cpsu.sports.data.model.Athlete;
import com.cpsu.sports.data.model.Game;

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
                Game.COL_LOSER_ID + " TEXT," +
                Game.COL_WIN_ATH_ID + " TEXT," +
                Game.COL_LOS_ATH_ID + " TEXT)";
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
        values.put(Game.COL_WIN_ATH_ID, game.getWinnerAthId());
        values.put(Game.COL_LOS_ATH_ID, game.getLoserAthId());

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
                "ON (g." + Game.COL_WIN_ATH_ID + " = a." + Athlete.COL_ATHLETE_ID + ")\n" +
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

    public String getCollegeId(String athleteId) {
        String collegeId = null;
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();

        String query = "SELECT " + Athlete.COL_ATHLETE_COL +
                " FROM " +  Athlete.TABLE_ATHLETES +
                " WHERE " +  Athlete.COL_ATHLETE_ID + "=?";

        Cursor cursor = db.rawQuery(query, new String[]{athleteId});

        if (cursor.moveToFirst()) {
            collegeId = cursor.getString(cursor.getColumnIndex(Athlete.COL_ATHLETE_COL));
        }

        db.close();
        return collegeId;
    }
}
