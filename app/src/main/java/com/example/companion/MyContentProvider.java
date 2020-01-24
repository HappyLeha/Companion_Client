package com.example.companion;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import static android.content.Context.MODE_PRIVATE;

public class MyContentProvider extends ContentProvider {
    static String table;


    static final String AUTHORITY = "ru.authority";
    static final String PATH = "Companion";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);

    static final String CONTENT_USERS_TYPE = "vnd.android.cursor.users/vnd." + AUTHORITY + "." + PATH;

    static final String CONTENT_TRIPS_TYPE = "vnd.android.cursor.trips/vnd." + AUTHORITY + "." + PATH;

    static final String CONTENT_CONNECTIONS_TYPE = "vnd.android.cursor.connections/vnd." + AUTHORITY + "." + PATH;

    static final String CONTENT_RATINGS_TYPE = "vnd.android.cursor.ratings/vnd." + AUTHORITY + "." + PATH;

    static final String CONTENT_VUSERRATING_TYPE = "vnd.android.cursor.vuserrating/vnd." + AUTHORITY + "." + PATH;

    static final String CONTENT_VCOUNTDRIVER_TYPE = "vnd.android.cursor.vcountdriver/vnd." + AUTHORITY + "." + PATH;

    static final String CONTENT_VCOUNTPASSENGER_TYPE = "vnd.android.cursor.vcountpassenger/vnd." + AUTHORITY + "." + PATH;

    static final int URI_USERS = 1;

    static final int URI_TRIPS = 2;

    static final int URI_CONNECTIONS = 3;
  ;
    static final int URI_RATINGS = 4;

    static final int URI_VUSERRATING = 5;

    static final int URI_VCOUNTDRIVER = 6;

    static final int URI_VCOUNTPASSENGER = 7;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH+"/users", URI_USERS);

        uriMatcher.addURI(AUTHORITY, PATH+"/trips", URI_TRIPS);

        uriMatcher.addURI(AUTHORITY, PATH+"/connections", URI_CONNECTIONS);

        uriMatcher.addURI(AUTHORITY, PATH+"/ratings", URI_RATINGS);

        uriMatcher.addURI(AUTHORITY, PATH+"/vuserrating", URI_VUSERRATING);

        uriMatcher.addURI(AUTHORITY, PATH+"/vcountdriver", URI_VCOUNTDRIVER);

        uriMatcher.addURI(AUTHORITY, PATH+"/vcountpassenger", URI_VCOUNTPASSENGER);

    }


    private static Context context;

    public static void createBD(Context context) {
        try (SQLiteDatabase db = context.openOrCreateDatabase("app.db", MODE_PRIVATE, null);) {
            db.execSQL("CREATE TABLE IF NOT EXISTS users (login TEXT primary key,password INTEGER)");
            db.execSQL("CREATE TABLE IF NOT EXISTS trips (id INTEGER primary key,datetimeFrom TEXT,datetimeTo TEXT,fromPlace TEXT,toPlace TEXT,countOfPlaces INTEGER,currentCountOfPlaces INTEGER,transport TEXT,cost REAL,driver TEXT,Constraint User_Trip foreign key (driver) references users on delete Cascade on update Cascade)  ");
            db.execSQL("CREATE TABLE IF NOT EXISTS connections (trip_id INTEGER,user_login TEXT,Constraint User_Connection foreign key (user_login) references users on delete Cascade on update Cascade, Constraint Trip_Connection foreign key (trip_id) references trips on delete Cascade on update Cascade)");
            db.execSQL("CREATE TABLE IF NOT EXISTS ratings(user_login_first TEXT,user_login_second TEXT,rating INTEGER,Constraint User_Rating_First foreign key (user_login_first) references users on delete Cascade on update Cascade,Constraint User_Rating_Second foreign key (user_login_second) references users on delete Cascade on update Cascade)");
            db.execSQL("CREATE VIEW IF NOT EXISTS userrating as Select user_login_second as login,avg(rating) as rating from ratings group by user_login_second");
            db.execSQL("CREATE VIEW IF NOT EXISTS countdriver as Select driver,count(driver) as count from trips group by driver");
            db.execSQL("CREATE VIEW IF NOT EXISTS countpassenger as Select user_login as user,count(user_login) as count from connections group by user_login");
            db.execSQL("CREATE TRIGGER IF NOT EXISTS insert_trigger BEFORE INSERT on connections BEGIN UPDATE trips set currentCountOfPlaces=currentCountOfPlaces+1 where id=NEW.trip_id; END");
            db.execSQL("CREATE TRIGGER IF NOT EXISTS delete_trigger BEFORE DELETE on connections BEGIN UPDATE trips set currentCountOfPlaces=currentCountOfPlaces-1 where id=OLD.trip_id; END");
            MyContentProvider.context=context;
            try {


                    User user=new User("First","first".hashCode());
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(user);
                    FileOutputStream fileOutputStream = context.openFileOutput("json.json", Context.MODE_PRIVATE);
                    fileOutputStream.write(jsonString.getBytes());
                    fileOutputStream.close();
                    FileInputStream fileInputStream = context.openFileInput("json.json");
                    InputStreamReader streamReader = new InputStreamReader(fileInputStream);

                    user = gson.fromJson(streamReader, User.class);
                    db.execSQL("INSERT into users values('"+user.getLogin()+"',"+user.getPassword()+")");

            }
            catch (Exception e) {

            }
        }
    }

    public boolean onCreate() {




        return true;
    }


    /**
     * Для получения данных в провайдере определен метод query()
     *
     * @param uri           - путь запроса
     * @param projection    - набор столбцов, данные для которых нужно получить
     * @param selection     - выражение для выборки типа "WHERE Name = ? ...."
     * @param selectionArgs - набор значений для параметров из selection (вставляются вместо знаков вопроса)
     * @param sortOrder     - критерий сортировки, в качестве которого выступает имя столбца
     * @return {Cursor}
     */
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {


        switch (uriMatcher.match(uri)) {
            case URI_USERS:


                table="users";
                break;

            case URI_TRIPS:

                table="trips";
                break;
            case URI_CONNECTIONS:

                table="connections";
                break;
            case URI_RATINGS:

                table="ratings";
                break;
            case URI_VUSERRATING:

                table="userrating";

                break;
            case URI_VCOUNTDRIVER:

                table="countdriver";

                break;
            case URI_VCOUNTPASSENGER:

                table="countpassenger";

                break;
            default:

                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        SQLiteDatabase db = context.openOrCreateDatabase("app.db", MODE_PRIVATE, null);
            Cursor cursor = db.query(table, projection, selection, selectionArgs, null, null, null);


            return cursor;

    }


    public String getType(Uri uri) {


        switch (uriMatcher.match(uri)) {
            case URI_USERS:
                return CONTENT_USERS_TYPE;

            case URI_TRIPS:
                return CONTENT_TRIPS_TYPE;

            case URI_CONNECTIONS:
                return CONTENT_CONNECTIONS_TYPE;

            case URI_RATINGS:
                return CONTENT_RATINGS_TYPE;
            case URI_VUSERRATING:
                return CONTENT_VUSERRATING_TYPE;
            case URI_VCOUNTDRIVER:
                return CONTENT_VCOUNTDRIVER_TYPE;
            case URI_VCOUNTPASSENGER:
                return CONTENT_VCOUNTPASSENGER_TYPE;
            default:
                return null;
        }
    }


    /**
     * @param uri           - путь запроса
     * @param values        - объект ContentValues, который определяет новые значения
     * @param selection     - выражение для выборки типа "WHERE Name = ? ...."
     * @param selectionArgs - набор значений для параметров из selection (вставляются вместо знаков вопроса)
     * @return Количество обновленных строк
     */
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {


        switch (uriMatcher.match(uri)) {
            case URI_USERS:


                table="users";
                break;

            case URI_TRIPS:

                table="trips";
                break;
            case URI_CONNECTIONS:

                table="connections";
                break;
            case URI_RATINGS:

                table="ratings";
                break;

            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        try (SQLiteDatabase db = context.openOrCreateDatabase("app.db", MODE_PRIVATE, null);) {
            int cnt = db.update(table, values, selection, selectionArgs);


            return cnt;
        }
    }


    /**
     * @param uri    - путь запроса
     * @param values - объект ContentValues, через который передаются добавляемые данные
     * @return С помощью этого идентификатора создается и возвращается путь Uri к созданному объекту
     */
    public Uri insert(Uri uri, ContentValues values) {


        switch (uriMatcher.match(uri)) {
            case URI_USERS:


                table="users";
                break;

            case URI_TRIPS:

                table="trips";
                break;
            case URI_CONNECTIONS:

                table="connections";
                break;
            case URI_RATINGS:

                table="ratings";
                break;

            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        try (SQLiteDatabase db = context.openOrCreateDatabase("app.db", MODE_PRIVATE, null);) {
            long rowID = db.insert(table, null, values);
            Uri resultUri = ContentUris.withAppendedId(CONTENT_URI, rowID);




            return resultUri;
        }
    }


    /**
     * @param uri           - путь запроса
     * @param selection     - выражение для выборки типа "WHERE Name = ? ...."
     * @param selectionArgs - набор значений для параметров из selection (вставляются вместо знаков вопроса)
     * @return Результатом удаления является количество удаленных строк в таблице.
     */
    public int delete(Uri uri, String selection, String[] selectionArgs) {


        switch (uriMatcher.match(uri)) {
            case URI_USERS:


                table="users";
                break;

            case URI_TRIPS:

                table="trips";
                break;
            case URI_CONNECTIONS:

                table="connections";
                break;
            case URI_RATINGS:

                table="ratings";
                break;

            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        try (SQLiteDatabase db = context.openOrCreateDatabase("app.db", MODE_PRIVATE, null);) {
            int cnt = db.delete(table, selection, selectionArgs);


            return cnt;
        }
    }
}
