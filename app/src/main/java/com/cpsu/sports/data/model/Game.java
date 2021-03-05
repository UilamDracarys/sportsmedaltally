package com.cpsu.sports.data.model;

import android.database.sqlite.SQLiteDatabase;

import com.cpsu.sports.data.DBHelper;

public class Game {

    public static final String TABLE_GAMES = "games";
    public static final String COL_GAME_ID = "game_id";
    public static final String COL_GAME_NO = "game_no";
    public static final String COL_SPORT_ID = "sport_id";
    public static final String COL_MEDAL_TYPE = "medal_type";
    public static final String COL_WINNER_ID = "winner_id";
    public static final String COL_LOSER_ID = "loser_id";
    public static final String COL_WIN_ATH_ID = "win_ath_id";
    public static final String COL_LOS_ATH_ID = "los_ath_id";

    private String gameId;
    private String gameNo;
    private String sportId;
    private String medalType;
    private String winnerId;
    private String loserId;
    private String winnerAthId;
    private String loserAthId;

    public String getWinnerAthId() {
        return winnerAthId;
    }

    public void setWinnerAthId(String winnerAthId) {
        this.winnerAthId = winnerAthId;
    }

    public String getLoserAthId() {
        return loserAthId;
    }

    public void setLoserAthId(String loserAthId) {
        this.loserAthId = loserAthId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameNo() {
        return gameNo;
    }

    public void setGameNo(String gameNo) {
        this.gameNo = gameNo;
    }

    public String getSportId() {
        return sportId;
    }

    public void setSportId(String sportId) {
        this.sportId = sportId;
    }

    public String getMedalType() {
        return medalType;
    }

    public void setMedalType(String medalType) {
        this.medalType = medalType;
    }

    public String getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(String winnerId) {
        this.winnerId = winnerId;
    }

    public String getLoserId() {
        return loserId;
    }

    public void setLoserId(String loserId) {
        this.loserId = loserId;
    }
}
