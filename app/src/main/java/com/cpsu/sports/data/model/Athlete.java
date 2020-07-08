package com.cpsu.sports.data.model;

public class Athlete {
    public static final String TABLE_ATHLETES = "athletes";
    public static final String COL_ATHLETE_ID = "athlete_id";
    public static final String COL_ATHLETE_NAME = "athlete_name";
    public static final String COL_ATHLETE_ADD = "athlete_add";
    public static final String COL_ATHLETE_COL = "athlete_col";

    private String athleteID;
    private String athleteName;
    private String athleteAdd;
    private String athleteCol;

    public String getAthleteID() {
        return athleteID;
    }

    public void setAthleteID(String athleteID) {
        this.athleteID = athleteID;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }

    public String getAthleteAdd() {
        return athleteAdd;
    }

    public void setAthleteAdd(String athleteAdd) {
        this.athleteAdd = athleteAdd;
    }

    public String getAthleteCol() {
        return athleteCol;
    }

    public void setAthleteCol(String athleteCol) {
        this.athleteCol = athleteCol;
    }
}
