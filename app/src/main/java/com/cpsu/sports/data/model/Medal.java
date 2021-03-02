package com.cpsu.sports.data.model;

public class Medal {
    public static final String TABLE_MEDALS = "medals";
    public static final String COL_MEDAL_ID = "medal_id";

    //Renamed column from ath_col_id to col_id;
    public static final String COL_OLD_ATHCOLID = "ath_col_id";
    public static final String COL_COL_ID = "col_id";
    public static final String COL_ATH_ID = "ath_id";
    public static final String COL_SPORT_ID = "sport_id";
    public static final String COL_MEDAL_TYPE = "medal_type";

    private String medalID;
    private String colID;
    private String athID;
    private String sportID;
    private String medalType;

    public String getAthID() {
        return athID;
    }

    public void setAthID(String athID) {
        this.athID = athID;
    }

    public String getMedalID() {
        return medalID;
    }

    public void setMedalID(String medalID) {
        this.medalID = medalID;
    }

    public String getColID() {
        return colID;
    }

    public void setColID(String colID) {
        this.colID = colID;
    }

    public String getSportID() {
        return sportID;
    }

    public void setSportID(String sportID) {
        this.sportID = sportID;
    }

    public String getMedalType() {
        return medalType;
    }

    public void setMedalType(String medalType) {
        this.medalType = medalType;
    }
}
