package com.cpsu.sports.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cpsu.sports.app.App;
import com.cpsu.sports.data.model.Medal;
import com.cpsu.sports.data.repo.AthleteRepo;
import com.cpsu.sports.data.repo.CollegeRepo;
import com.cpsu.sports.data.repo.GameRepo;
import com.cpsu.sports.data.repo.MedalRepo;
import com.cpsu.sports.data.repo.SportRepo;
import com.cpsu.sports.data.repo.UserRepo;


/**
 * Created by William on 1/7/2018.
 */

public class DBHelper extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "cpsusports.db";
    private static final String TAG = DBHelper.class.getSimpleName();

    public int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    public DBHelper() {
        super(App.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserRepo.createUsersTbl());
        db.execSQL(SportRepo.createSportsTable());
        db.execSQL(CollegeRepo.createCollegesTable());
        db.execSQL(AthleteRepo.createAthletesTable());
        db.execSQL(MedalRepo.createMedalsTable());
        db.execSQL(GameRepo.createTblGames());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, String.format("SQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));
        /*if (oldVersion <= 2) {
            db.execSQL("ALTER TABLE " + Medal.TABLE_MEDALS + " RENAME " + Medal.COL_OLD_ATHCOLID + " to " + Medal.COL_COL_ID);
            db.execSQL("ALTER TABLE " + Medal.TABLE_MEDALS + " ADD " + Medal.COL_ATH_ID + " TEXT");
        }*/
        onCreate(db);
    }
}
