package com.cpsu.sports.data.model;

public class Medal {
    public static final String TABLE_MEDALS = "medals";
    public static final String COL_MEDAL_ID = "medal_id";
    public static final String COL_ATH_COL_ID = "ath_col_id";
    public static final String COL_SPORT_ID = "sport_id";
    public static final String COL_MEDAL_TYPE = "medal_type";

    private String medalID;
    private String athColID;
    private String sportID;
    private String medalType;

    public String getMedalID() {
        return medalID;
    }

    public void setMedalID(String medalID) {
        this.medalID = medalID;
    }

    public String getAthColID() {
        return athColID;
    }

    public void setAthColID(String athColID) {
        this.athColID = athColID;
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
