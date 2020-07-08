package com.cpsu.sports.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cpsu.sports.data.DBHelper;
import com.cpsu.sports.data.model.User;

public class UserRepo {
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public UserRepo() {
        User user = new User();
    }

    public static String createUsersTbl() {
        String query = "CREATE TABLE IF NOT EXISTS " + User.TABLE_USERS + " (" +
                User.COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                User.COL_USER_FULLNAME + " TEXT, " +
                User.COL_USERNAME + " TEXT, " +
                User.COL_USERPASS + " TEXT)";
        return query;
    }

    public boolean checkUser(String username, String password){
        String[] columns = {
                User.COL_USER_ID
        };
        DBHelper dbHelper = new DBHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = User.COL_USERNAME + " = ?" + " AND " + User.COL_USERPASS + " =?";
        String[] selectionArgs = { username, password };

        Cursor cursor = db.query(User.TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }

    public boolean checkForExistingUser(String username){
        String[] columns = {
                User.COL_USER_ID
        };
        DBHelper dbHelper = new DBHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = User.COL_USERNAME + " = ?";
        String[] selectionArgs = { username};

        Cursor cursor = db.query(User.TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }

    public void registerUser(User user) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(User.COL_USER_FULLNAME, user.getUserFullname());
        values.put(User.COL_USERNAME, user.getUserName());
        values.put(User.COL_USERPASS, user.getUserPass());

        // Inserting Row
        db.insert(User.TABLE_USERS, null, values);
        db.close();
    }

}


    /*public void insert(Contact contact) {

        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Contact.COL_NAME, contact.getContName());
        values.put(Contact.COL_NUM, contact.getContNum());
        values.put(Contact.COL_FARMCODE, contact.getContFarmCode());

        db.insert(Contact.TABLE_CONTACT, null, values);
        db.close();
    }

    public void update(Contact contact) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Contact.COL_NAME, contact.getContName());
        values.put(Contact.COL_NUM, contact.getContNum());

        db.update(Contact.TABLE_CONTACT, values, Contact.COL_FARMCODE + "= ? ", new String[]{String.valueOf(contact.getContFarmCode())});
        db.close(); // Closing database connection
    }

    public Contact getContactByFarm(String farmCode) {

       *//* dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();

        String query = "SELECT " + Contact.COL_FARMCODE + " as FarmCode, " +
                Contact.COL_NAME + " as ContactName, " +
                Contact.COL_NUM + " as ContactNum " +
                "FROM " + Contact.TABLE_CONTACT + " WHERE " + Contact.COL_FARMCODE + "=?";

        Cursor cursor = db.rawQuery(query, new String[]{farmCode});
        Contact contact = new Contact();

        if (cursor.moveToFirst()) {
            do {
                contact.setContName(cursor.getString(cursor.getColumnIndex("ContactName")));
                contact.setContNum(cursor.getString(cursor.getColumnIndex("ContactNum")));
                contact.setContFarmCode(cursor.getString(cursor.getColumnIndex("FarmCode")));
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return contact;*//*
    }*/
