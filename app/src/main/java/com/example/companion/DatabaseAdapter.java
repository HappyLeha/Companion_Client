package com.example.companion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import lombok.NonNull;

public class DatabaseAdapter implements AutoCloseable{
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    Context context;
    public DatabaseAdapter(Context context){
        dbHelper = new DatabaseHelper(context.getApplicationContext());
        database = dbHelper.getWritableDatabase();
        this.context=context;
    }

    /*public DatabaseAdapter open(){
        database = dbHelper.getWritableDatabase();
        return this;
    }*/

    public void close(){
        dbHelper.close();
    }
    public void refreshUsers(ArrayList<User> users) {
        database.delete("users",null,null);
        for (User user:users) {
            addUser(user);
        }
    }
    public User getUser(@NonNull  String login) {
        String[] progection;
        String selection;
        String[] selectionArgs;
        progection=new String[]{"login","password"};
        selection="login = ?";
        selectionArgs=new String[]{login};
        User user=null;
        try (Cursor cursor=database.query("users",progection,selection,selectionArgs,null,null,null)) {
            while(cursor.moveToNext()) {
               user=new User(cursor.getString(0),cursor.getInt(1));
            }
        }
        return user;
    }
    public Double getRatingByUser(@NonNull String login) {
        String[] progection = new String[]{"rating"};
        String selection = "login=?";
        String[] selectionArgs = new String[]{login};
        try (Cursor cursor = database.query("userrating",progection,selection,selectionArgs,null,null,null)) {
            if (cursor.moveToNext()) return cursor.getDouble(0);
            else return null;
        }
    }
    public void setRating(Rating rating) {
        ContentValues cv = new ContentValues();
        cv.put("rating", rating.getRating());
        String selection = "user_login_first=? AND user_login_second=?";
        String[] selectionArgs = new String[]{rating.getLogin(), rating.getUser()};
        database.update("ratings",cv,selection,selectionArgs);
    }
    public void setTrip(Trip trip) {
        ContentValues cv=new ContentValues();
        cv.put("datetimeFrom",Mapper.convertCalendarToString(trip.getDateTimeFrom()));
        cv.put("datetimeTo",Mapper.convertCalendarToString(trip.getDateTimeTo()));
        cv.put("fromPlace",trip.getFrom());
        cv.put("toPlace",trip.getTo());
        cv.put("countOfPlaces",trip.getCountOfPlaces());
        cv.put("currentCountOfPlaces",0);
        cv.put("transport",trip.getTransport());
        cv.put("cost",trip.getCost());
        cv.put("driver",trip.getDriver());
        String selection="id=?";
        String[] selectionArgs=new String[]{trip.getId()+""};
        database.update("trips",cv,selection,selectionArgs);
    }
    public boolean setUser(@NonNull String login,@NonNull User user) {
        if (user.getLogin().equals(login)&&user.getPassword()==null) return true;
        if (user.getLogin().equals(login)&&user.getPassword()!=null) {
            ContentValues cv=new ContentValues();
            cv.put("login",user.getLogin());
            cv.put("password",user.getPassword());
            String selection="login=?";
            String[] selectionArgs=new String[]{login};
            database.update("users",cv,selection,selectionArgs);
            return true;
        }
        if (!user.getLogin().equals(login)) {
            if (getUser(user.getLogin())==null) {
                ContentValues cv=new ContentValues();
                cv.put("login",user.getLogin());
                cv.put("password",user.getPassword());
                String selection="login=?";
                String[] selectionArgs=new String[]{login};
                database.update("users",cv,selection,selectionArgs);
                return true;
            }
            else return false;
        }
        return false;
    }
    public void addRating(Rating rating) {
        ContentValues cv = new ContentValues();
        cv.put("user_login_first", rating.getLogin());
        cv.put("user_login_second",rating.getUser());
        cv.put("rating", rating.getRating());
        database.insert("ratings",null,cv);
    }
    public boolean addUser(@NonNull User user) {
        if (getUser(user.getLogin())!=null) return false;
        ContentValues cv = new ContentValues();
        cv.put("login", user.getLogin());
        cv.put("password", user.getPassword());
        database.insert("users",null,cv);
        return true;
    }
    public void addTrip(@NonNull Trip trip){
        ContentValues cv=new ContentValues();
        cv.put("datetimeFrom",Mapper.convertCalendarToString(trip.getDateTimeFrom()));
        cv.put("datetimeTo",Mapper.convertCalendarToString(trip.getDateTimeTo()));
        cv.put("fromPlace",trip.getFrom());
        cv.put("toPlace",trip.getTo());
        cv.put("countOfPlaces",trip.getCountOfPlaces());
        cv.put("currentCountOfPlaces",0);
        cv.put("transport",trip.getTransport());
        cv.put("cost",trip.getCost());
        cv.put("driver",trip.getDriver());
        database.insert("trips",null,cv);
    }
    public void addConnection(@NonNull String login,int id) {
        ContentValues cv = new ContentValues();
        cv.put("user_login", login);
        cv.put("trip_id", id);
        database.insert("connections",null,cv);
    }
    public Trip getTrip(int id) {
        String[] progection=new String[]{"id","datetimeFrom","datetimeTo",
                "fromPlace","toPlace","countOfPlaces","currentCountOfPlaces",
                "transport","cost","driver"};
        String selection="id=?";
        String[] selectionArgs=new String[]{id+""};
        Trip trip=null;

        try(Cursor cursor=database.query("trips",progection,selection,selectionArgs,null,null,null)) {
            while(cursor.moveToNext()) {
                Calendar dateTimeFrom=Mapper.convertStringToCalendar(cursor.getString(1));
                Calendar dateTimeTo=Mapper.convertStringToCalendar(cursor.getString(2));
                trip=new Trip(cursor.getInt(0),dateTimeFrom,dateTimeTo,cursor.getString(3),cursor.getString(4),
                        cursor.getInt(5), cursor.getInt(6),cursor.getString(7),cursor.getDouble(8),
                        cursor.getString(9));
            }
        }
        return  trip;
    }
    public ArrayList<Trip> getSearchedTrips() {
       String query="Select id,driver,fromPlace,toPlace from trips " +
               "where datetimeFrom>'"+Mapper.convertCalendarToString(Calendar.getInstance())+"'";
       if (TripForm.getDateFrom()!=null) {
           query+=" and date(datetimeFrom)=date('"+Mapper.convertCalendarToString(TripForm.getDateFrom())+"')";
       }
       if (TripForm.getDateTo()!=null) {
           query+=" and date(datetimeTo)=date('"+Mapper.convertCalendarToString(TripForm.getDateTo())+"')";
       }
       if (TripForm.getTimeStart()!=null) {
           query+=" and strftime('%H:%M',datetimeFrom)>=strftime('%H:%M','"+Mapper.convertCalendarToString(TripForm.getTimeStart())+"')";
       }
       if (TripForm.getTimeEnd()!=null) {
            query+=" and strftime('%H:%M',datetimeFrom)<=strftime('%H:%M','"+Mapper.convertCalendarToString(TripForm.getTimeEnd())+"')";
       }
        if (TripForm.getFrom()!=null) {
            query+=" and fromPlace='"+TripForm.getFrom()+"'";
        }
        if (TripForm.getTo()!=null) {
            query+=" and toPlace='"+TripForm.getTo()+"'";
        }
        if (TripForm.getCost()!=null) {
            query+=" and cost<="+TripForm.getCost();
        }
        try (Cursor cursor=database.rawQuery(query,null)) {
           ArrayList<Trip> trips = new ArrayList();
           while (cursor.moveToNext()) {
               trips.add(new Trip(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3))); //
           }
           return trips;
       }
    }
    public ArrayList<Trip> getComplietedTrips(String driver) {
        String query="Select id,fromPlace,toPlace,driver from trips where datetimeFrom<'"+Mapper.convertCalendarToString(Calendar.getInstance())+"' and (driver='"+driver
                +"' or exists (select * from connections where trip_id=id and user_login='"+driver+"'))";
        try (Cursor cursor=database.rawQuery(query,null)) {
            ArrayList<Trip> trips = new ArrayList();
            while (cursor.moveToNext()) {
                trips.add(new Trip(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3))); //
            }
            return trips;
        }

    }
    public ArrayList<Trip> getFutureTrips(String driver) {
        String query="Select id,fromPlace,toPlace,driver from trips where datetimeFrom>='"+Mapper.convertCalendarToString(Calendar.getInstance())+"' and (driver='"+driver
                +"' or exists (select * from connections where trip_id=id and user_login='"+driver+"'))";
        try (Cursor cursor=database.rawQuery(query,null)) {
            ArrayList<Trip> trips = new ArrayList();
            while (cursor.moveToNext()) {
                trips.add(new Trip(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3))); //
            }
            return trips;
        }

    }
    public int getCountOfDrivers(@NonNull String driver) {
        String[] progection=new String[]{"count"};
        String selection="driver=?";
        String[] selectionArgs=new String[]{driver};
        try(Cursor cursor=database.query("countdriver",progection,selection,selectionArgs,null,null,null)) {
            if (cursor.moveToNext()) {
                return cursor.getInt(0);
            } else return 0;
        }
    }
    public int getCountOfPassengers(@NonNull String user) {
        String[] progection=new String[]{"count"};
        String selection="user=?";
        String[] selectionArgs=new String[]{user};
        try (Cursor cursor=database.query("countpassenger",progection,selection,selectionArgs,null,null,null)) {
            if (cursor.moveToNext()) {
                return cursor.getInt(0);
            } else return 0;
        }
    }
    public boolean isRating(@NonNull String user,@NonNull String login) {
        String[] progection = null;
        String selection = "user_login_second=? AND user_login_first=?";
        String[] selectionArgs = new String[]{user, login};
        Cursor cursor=database.query("ratings",progection,selection,selectionArgs,null,null,null);
        return cursor.moveToNext();
    }
    /*public Double getRatingOfUser(@NonNull String user) {
        String[] progection = new String[]{"rating"};
        String selection = "login=?";
        String[] selectionArgs = new String[]{user};
        try(Cursor cursor = database.query("userrating",progection,selection,selectionArgs,null,null,null)) {
            if (cursor.moveToFirst()) return cursor.getDouble(0);
            return null;
        }
    }*/
    public boolean isConnection(@NonNull String login,int id) {
        String[] progection = null;
        String selection = "user_login=? AND trip_id=?";
        String[] selectionArgs = new String[]{login, id + ""};
        try (Cursor cursor=database.query("connections",progection,selection,selectionArgs,null,null,null)) {
            return cursor.moveToNext();
        }
    }
    public void removeConnection(@NonNull String login,int id) {
        String selection = "user_login=? AND trip_id=?";
        String[] selectionArgs = new String[]{login, id + ""};
        database.delete("connections",selection,selectionArgs);
    }
    public void removeUser(@NonNull String login){
        String selection="login=?";
        String[] selectionArgs=new String[]{login};
        database.delete("users",selection,selectionArgs);
    }
    public void removeTrip(int id) {
        String selection = "id=?";
        String[] selectionArgs = new String[]{id + ""};
        database.delete("trips",selection,selectionArgs);
    }

    /*public void test(){
        String selection="Select datetime('now') from trips";
        try (Cursor cursor=database.rawQuery(selection,null)) {
            cursor.moveToNext();
            Toast toast = Toast.makeText(context, cursor.getString(0), Toast.LENGTH_LONG);
            toast.show();
        }
    }*/
}
