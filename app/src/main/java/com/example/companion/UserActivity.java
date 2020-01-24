package com.example.companion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity {
TextView tviewDriver;
TextView tviewPassenger;
TextView tviewRating;
TextView tviewScore;
SeekBar seekBar;
String[] progection;
String selection;
String[] selectionArgs;
String user;
String login;
int id;
Button buttonRating;
ContentValues cv;
    static final Uri CONTENT_VCOUNTDRIVER_TYPE = Uri.parse("content://" + "ru.authority" + "/" + "Companion"+"/vcountdriver");
    static final Uri CONTENT_VCOUNTPASSENGER_TYPE = Uri.parse("content://" + "ru.authority" + "/" + "Companion"+"/vcountpassenger");
    static final Uri CONTENT_VUSERRATING_TYPE = Uri.parse("content://" + "ru.authority" + "/" + "Companion"+"/vuserrating");
    static final Uri CONTENT_RATINGS_TYPE = Uri.parse("content://" + "ru.authority" + "/" + "Companion"+"/ratings");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Bundle arguments = getIntent().getExtras();

        user = arguments.getString("user");
        login=arguments.getString("login");
        id = arguments.getInt("id");
        tviewDriver=findViewById(R.id.txtviewDriverResult);
        tviewPassenger=findViewById(R.id.txtviewPassengerResult);
        tviewRating=findViewById(R.id.txtviewRatingResult);
        tviewScore=findViewById(R.id.txtviewSeekBarResult);
        seekBar=findViewById(R.id.seekBar);
        buttonRating=findViewById(R.id.buttonRating);
        progection=new String[]{"count"};
        selection="driver=?";
        selectionArgs=new String[]{user};

            Cursor cursor=getContentResolver().query(CONTENT_VCOUNTDRIVER_TYPE,progection,selection,selectionArgs,null);

            if (cursor.moveToFirst()) {

                tviewDriver.setText(cursor.getString(0));


            }
            else {
                tviewDriver.setText("0");
            }
            cursor.close();
            progection=new String[]{"count"};
            selection="user=?";
            selectionArgs=new String[]{user};
            cursor=getContentResolver().query(CONTENT_VCOUNTPASSENGER_TYPE,progection,selection,selectionArgs,null);

            if (cursor.moveToFirst()) {

                tviewPassenger.setText(cursor.getString(0));


            }
            else {
                tviewPassenger.setText("0");
            }
            cursor.close();
            progection=new String[]{"rating"};
            selection="login=?";
            selectionArgs=new String[]{user};
            cursor=getContentResolver().query(CONTENT_VUSERRATING_TYPE,progection,selection,selectionArgs,null);

            if (cursor.moveToFirst()) {

                tviewRating.setText(cursor.getString(0));


            }
            else {
                tviewRating.setText("Нет рейтинга");
            }
            cursor.close();
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    tviewScore.setText(String.valueOf(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            buttonRating.setOnClickListener((v)->{

                    progection=null;
                    selection="user_login_second=? AND user_login_first=?";
                    selectionArgs=new String[]{user,login};
                    Cursor cursorr = getContentResolver().query(CONTENT_RATINGS_TYPE,progection,selection,selectionArgs,null);

                    if (cursorr.moveToFirst()) {

                        cursorr.close();
                        cv=new ContentValues();
                        cv.put("rating",Integer.parseInt(tviewScore.getText().toString()));
                        selection="user_login_first=? AND user_login_second=?";
                        selectionArgs=new String[]{login,user};
                        getContentResolver().update(CONTENT_RATINGS_TYPE,cv,selection,selectionArgs);

                        progection=new String[]{"rating"};
                        selection="login=?";
                        selectionArgs=new String[]{user};
                        cursorr=getContentResolver().query(CONTENT_VUSERRATING_TYPE,progection,selection,selectionArgs,null);
                        if (cursorr.moveToFirst()) {

                            tviewRating.setText(cursorr.getString(0));


                        } else {
                            tviewRating.setText("Нет рейтинга");
                        }
                        cursorr.close();

                    } else {
                        cursorr.close();
                        cv=new ContentValues();
                        cv.put("user_login_first",login);
                        cv.put("user_login_second",user);
                        cv.put("rating",Integer.parseInt(tviewScore.getText().toString()));
                        getContentResolver().insert(CONTENT_RATINGS_TYPE,cv);
                        progection=new String[]{"rating"};
                        selection="login=?";
                        selectionArgs=new String[]{user};
                        cursorr=getContentResolver().query(CONTENT_VUSERRATING_TYPE,progection,selection,selectionArgs,null);


                        if (cursorr.moveToFirst()) {

                            tviewRating.setText(cursorr.getString(0));


                        } else {
                            tviewRating.setText("Нет рейтинга");
                        }
                    }
                    cursorr.close();


            });


    }
    public void onClickUserBack (View v) {
        Intent intent = new Intent(this, TripActivity.class);
        intent.putExtra("login", login);
        intent.putExtra("id", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
