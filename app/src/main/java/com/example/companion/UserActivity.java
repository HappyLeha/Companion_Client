package com.example.companion;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {
TextView tviewDriver;
TextView tviewPassenger;
TextView tviewRating;
TextView tviewScore;
TextView tviewResult;
SeekBar seekBar;
String user;
String login;
int id;
boolean isComplieted;
Button buttonRating;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user);
    Bundle arguments = getIntent().getExtras();
    user = arguments.getString("user");
    login=arguments.getString("login");
    id = arguments.getInt("id");
    isComplieted=arguments.getBoolean("isComplieted");
    tviewDriver=findViewById(R.id.txtviewDriverResult);
    tviewPassenger=findViewById(R.id.txtviewPassengerResult);
    tviewRating=findViewById(R.id.txtviewRatingResult);
    tviewScore=findViewById(R.id.txtviewSeekBarResult);
    tviewResult=findViewById(R.id.txtviewSeekBar);
    seekBar=findViewById(R.id.seekBar);
    buttonRating=findViewById(R.id.buttonRating);
    if (isComplieted&&!login.equals(user)) {
        seekBar.setVisibility(View.VISIBLE);
        buttonRating.setVisibility(View.VISIBLE);
        tviewRating.setVisibility(View.VISIBLE);
        tviewResult.setVisibility(View.VISIBLE);
    }
    try(DatabaseAdapter databaseAdapter=new DatabaseAdapter(this)) {
        tviewDriver.setText(databaseAdapter.getCountOfDrivers(user)+"");
        tviewPassenger.setText(databaseAdapter.getCountOfPassengers(user)+"");
        Double rating=databaseAdapter.getRatingByUser(user);
        if (rating!=null) tviewRating.setText(rating+"");
        else tviewRating.setText("Нет рейтинга");
    }
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
        }});
    buttonRating.setOnClickListener((v)->{
        try(DatabaseAdapter databaseAdapter=new DatabaseAdapter(this)) {
            if (databaseAdapter.isRating(user,login)) {
                databaseAdapter.setRating(new Rating(user,login,Integer.parseInt(tviewScore.getText().toString())));
                Double rating=databaseAdapter.getRatingByUser(user);
                if (rating!=null) tviewRating.setText(rating+"");
                else tviewRating.setText("Нет рейтинга");
            } else {
                databaseAdapter.addRating(new Rating(user,login,Integer.parseInt(tviewScore.getText().toString())));
                Double rating=databaseAdapter.getRatingByUser(user);
                if (rating!=null) tviewRating.setText(rating+"");
                else tviewRating.setText("Нет рейтинга");
            }
        }
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
