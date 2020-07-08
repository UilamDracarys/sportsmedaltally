package com.cpsu.sports.data.model;

public class Sport {
    public static final String TABLE_SPORTS = "sports";
    public static final String COL_SPORT_ID = "sport_id";
    public static final String COL_SPORT_NAME = "sport_name";
    public static final String COL_SPORT_CATEGORY = "sport_category";
    public static final String COL_SPORT_TYPE = "sport_type";

    private String sportID;
    private String sportName;
    private String sportCategory;
    private String sportType;

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getSportCategory() {
        return sportCategory;
    }

    public void setSportCategory(String sportCategory) {
        this.sportCategory = sportCategory;
    }

    public String getSportID() {
        return sportID;
    }

    public void setSportID(String sportID) {
        this.sportID = sportID;
    }
}
