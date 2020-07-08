package com.cpsu.sports.app;

import android.app.Application;
import android.content.Context;

import com.cpsu.sports.data.DBHelper;
import com.cpsu.sports.data.DatabaseManager;


/**
 * Created by William on 12/17/2018.
 */

public class App extends Application {
    private static Context context;
    private static DBHelper dbHelper;

    @Override
    public void onCreate()    {
        super.onCreate();
        context = this.getApplicationContext();
        dbHelper = new DBHelper();
        DatabaseManager.initializeInstance(dbHelper);
        System.out.println("Database Created!");
    }

    public static Context getContext(){
        return context;
    }
}
