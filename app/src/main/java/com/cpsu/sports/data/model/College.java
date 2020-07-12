package com.cpsu.sports.data.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class College {
    public static final String TABLE_COLLEGES = "colleges";
    public static final String COL_COLLEGE_ID = "college_id";
    public static final String COL_COLLEGE_CODE = "college_code";
    public static final String COL_COLLEGE_NAME = "college_name";

    private String collegeID;
    private String collegeCode;
    private String collegeName;

    public String getCollegeCode() {
        return collegeCode;
    }

    public void setCollegeCode(String collegeCode) {
        this.collegeCode = collegeCode;
    }

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
