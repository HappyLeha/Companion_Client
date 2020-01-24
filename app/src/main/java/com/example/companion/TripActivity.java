package com.example.companion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class TripActivity extends AppCompatActivity {
    TextView tviewDateTimeFrom;
    TextView tviewDateTimeTo;
    TextView tviewFrom;
    TextView tviewTo;
    TextView tviewCount;
    TextView tviewFreeCount;
    TextView tviewTransport;
    TextView tviewCost;
    Button buttonDriver;
    Button button;

    String driver;
    String user;
    static final Uri CONTENT_TRIPS_TYPE = Uri.parse("content://" + "ru.authority" + "/" + "Companion"+"/trips");
    static final Uri CONTENT_VUSERRATING_TYPE = Uri.parse("content://" + "ru.authority" + "/" + "Companion"+"/vuserrating");
    static final Uri CONTENT_CONNECTIONS_TYPE = Uri.parse("content://" + "ru.authority" + "/" + "Companion"+"/connections");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        String[] progection;
        String selection;
        String[] selectionArgs;
        Bundle arguments = getIntent().getExtras();
        int id = arguments.getInt("id");
        user = arguments.getString("login");
        tviewDateTimeFrom = findViewById(R.id.txtviewDateTimeFromResult);
        tviewDateTimeTo = findViewById(R.id.txtviewDateTimeToResult);
        tviewFrom = findViewById(R.id.txtviewFromResult);
        tviewTo = findViewById(R.id.txtviewToResult);
        tviewCount = findViewById(R.id.txtviewCountResult);
        tviewFreeCount = findViewById(R.id.txtviewFreeCountResult);
        tviewTransport = findViewById(R.id.txtviewTransportResult);
        tviewCost = findViewById(R.id.txtviewCostResult);
        buttonDriver = findViewById(R.id.buttonDriver);
        button=findViewById(R.id.button);

        buttonDriver.setOnClickListener((v) -> {
            Intent intent = new Intent(this, UserActivity.class);
            intent.putExtra("user", driver);
            intent.putExtra("login", user);
            intent.putExtra("id", id);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        String[] dateTimeFrom;
        String[] dateTimeTo;

            progection=new String[]{"id","dateTimeFrom","dateTimeTo","fromPlace","toPlace","countOfPlaces","countOfPlaces-currentCountOfPlaces","transport","cost/(currentCountOfPlaces+1)","driver"};
            selection="id=?";
            selectionArgs=new String[]{id+""};
            Cursor cursor = getContentResolver().query(CONTENT_TRIPS_TYPE,progection,selection,selectionArgs,null);

            if (cursor.moveToFirst()) {

                dateTimeFrom=cursor.getString(1).split(" ");
                dateTimeTo=cursor.getString(2).split(" ");
                tviewFrom.setText(cursor.getString(3));
                tviewTo.setText(cursor.getString(4));
                tviewCount.setText(cursor.getString(5));
                tviewFreeCount.setText( Integer.parseInt(cursor.getString(6)) + "");
                tviewTransport.setText(cursor.getString(7));
                tviewCost.setText(Double.parseDouble(cursor.getString(8)) + "");
                driver = cursor.getString(9);
                tviewDateTimeFrom.setText(dateTimeFrom[0]+"."+dateTimeFrom[1]+"."+dateTimeFrom[2]+" "+dateTimeFrom[3]+":"+dateTimeFrom[4]);
                tviewDateTimeTo.setText(dateTimeTo[0]+"."+dateTimeTo[1]+"."+dateTimeTo[2]+" "+dateTimeTo[3]+":"+dateTimeTo[4]);


            }
            cursor.close();
            progection=new String[]{"rating"};
            selection="login=?";
            selectionArgs=new String[]{driver};
            cursor =  getContentResolver().query(CONTENT_VUSERRATING_TYPE,progection,selection,selectionArgs,null);

            if (cursor.moveToFirst()) {
                buttonDriver.setText(driver + " " + cursor.getString(0));


            } else {
                buttonDriver.setText(driver + " Нет рейтинга");
            }
            cursor.close();

            if (user.equals(driver)) {
                button.setText("Удалить");

                button.setOnClickListener((v) -> {
                    Intent intent = new Intent(this, AllListActivity.class);

                    intent.putExtra("driver", user);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    String selectionL="id=?";
                    String[] selectionArgsL=new String[]{id+""};
                    getContentResolver().delete(CONTENT_TRIPS_TYPE,selectionL,selectionArgsL);
                    startActivity(intent);
                });

            }
            else {
                Intent intent = new Intent(this, TripActivity.class);

                intent.putExtra("login", user);
                intent.putExtra("id", id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                progection=null;
                selection="user_login=? AND trip_id=?";
                selectionArgs=new String[]{user,id+""};
                cursor=getContentResolver().query(CONTENT_CONNECTIONS_TYPE,progection,selection,selectionArgs,null);
                if (cursor.moveToFirst()) {
                    button.setText("Выписаться");
                    button.setOnClickListener((v) -> {

                        String selectionL="user_login=? AND trip_id=?";
                        String[] selectionArgsL=new String[]{user,id+""};


                        getContentResolver().delete(CONTENT_CONNECTIONS_TYPE,selectionL,selectionArgsL);
                        startActivity(intent);
                    });
                }
                else {
                    button.setText("Записаться");

                    button.setOnClickListener((v) -> {

                        ContentValues cv=new ContentValues();
                        cv.put("user_login",user);
                        cv.put("trip_id",id);
                        getContentResolver().insert(CONTENT_CONNECTIONS_TYPE,cv);
                        startActivity(intent);
                    });
                }
            }




    }
    public void onClickTripBack (View v) {
        Intent intent = new Intent(this, AllListActivity.class);
        intent.putExtra("driver", user);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}





