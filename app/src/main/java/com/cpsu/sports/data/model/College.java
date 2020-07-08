package com.cpsu.sports.data.model;

public class College {
    public static final String TABLE_COLLEGES = "colleges";
    public static final String COL_COLLEGE_ID = "college_id";
    public static final String COL_COLLEGE_NAME = "college_name";

    private String collegeID;
    private String collegeName;

    public String getCollegeID() {
        return collegeID;
    }

    public void setCollegeID(String collegeID) {
        this.collegeID = collegeID;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }
}
