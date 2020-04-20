package com.example.companion;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "app.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS users (login TEXT primary key,password INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS trips (id INTEGER primary key,datetimeFrom TEXT,datetimeTo TEXT,fromPlace TEXT,toPlace TEXT,countOfPlaces INTEGER,currentCountOfPlaces INTEGER,transport TEXT,cost REAL,driver TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS connections (trip_id INTEGER,user_login TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS ratings(user_login_first TEXT,user_login_second TEXT,rating INTEGER)");
        db.execSQL("CREATE VIEW IF NOT EXISTS userrating as Select user_login_second as login,avg(rating) as rating from ratings group by user_login_second");
        db.execSQL("CREATE VIEW IF NOT EXISTS countdriver as Select driver,count(driver) as count from trips group by driver");
        db.execSQL("CREATE VIEW IF NOT EXISTS countpassenger as Select user_login as user,count(user_login) as count from connections group by user_login");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS insert_trigger BEFORE INSERT on connections BEGIN UPDATE trips set currentCountOfPlaces=currentCountOfPlaces+1 where id=NEW.trip_id; END");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS delete_trigger BEFORE DELETE on connections BEGIN UPDATE trips set currentCountOfPlaces=currentCountOfPlaces-1 where id=OLD.trip_id; END");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+"app.db");
        onCreate(db);
    }
}
