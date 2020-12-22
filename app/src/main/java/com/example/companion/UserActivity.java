package com.example.companion;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity {
    TextView tviewUser;
TextView tviewDriver;
TextView tviewPassenger;
TextView tviewRating;
TextView tviewScore;
TextView tviewResult;
ActionBar actionBar;
SeekBar seekBar;
String user;
String login;
int id;
boolean isComplieted;
Button buttonRating;
@Override
protected void onCreate(Bundle savedInstanceState) {
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user);
    Bundle arguments = getIntent().getExtras();
    user = arguments.getString("user");
    login=arguments.getString("login");
    id = arguments.getInt("id");
    isComplieted=arguments.getBoolean("isComplieted");
    tviewUser=findViewById(R.id.txtviewUser);
    tviewDriver=findViewById(R.id.txtviewDriverResult);
    tviewPassenger=findViewById(R.id.txtviewPassengerResult);
    tviewRating=findViewById(R.id.txtviewRatingResult);
    tviewScore=findViewById(R.id.txtviewSeekBarResult);
    tviewResult=findViewById(R.id.txtviewSeekBar);
    seekBar=findViewById(R.id.seekBar);
    buttonRating=findViewById(R.id.buttonRating);
    tviewUser.setText(user);
    actionBar =getSupportActionBar();
    actionBar.setHomeButtonEnabled(true);
    actionBar.setDisplayHomeAsUpEnabled(true);
    try {
        Double raiting;
        Toast toast;
        HTTP.RatingsGet httpGet=new HTTP.RatingsGet();
        httpGet.execute(user);
        raiting=httpGet.get();
        if (!login.equals(user)) {
            seekBar.setVisibility(View.VISIBLE);
            buttonRating.setVisibility(View.VISIBLE);
            tviewRating.setVisibility(View.VISIBLE);
            tviewResult.setVisibility(View.VISIBLE);
            tviewScore.setVisibility(View.VISIBLE);
        }
        HTTP.UserCountPost httpPost=new HTTP.UserCountPost();
        httpPost.execute(user);
        Count count=(Count)httpPost.get();
        if (raiting==null||count==null) {
            toast = Toast.makeText(this, "Сервер недоступен!", Toast.LENGTH_LONG);
            toast.show();
            onClickExit(null);
        }
        if (count.getDriver()==null) {
            onClickUserBack(null);
        }
        tviewDriver.setText(count.getDriver()+"");
        tviewPassenger.setText(count.getPassenger()+"");
        if (!raiting.equals(-1.0)) tviewRating.setText(raiting+"");
        else tviewRating.setText("Нет рейтинга");
    }
    catch (Exception e) {
        Toast toast = Toast.makeText(this, "Сервер недоступен!", Toast.LENGTH_LONG);
        toast.show();
        onClickExit(null);
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
        try {
            HTTP.RatingPost httpPost = new HTTP.RatingPost();
            httpPost.execute(new Rating(user,login,Integer.parseInt(tviewScore.getText().toString())));
            Toast toast;
            HTTP.RatingsGet httpGet;
            switch(httpPost.get()) {
                case 201:
                    httpGet=new HTTP.RatingsGet();
                    httpGet.execute(user);
                    Double raiting=httpGet.get();
                    if (raiting==null) {
                        toast = Toast.makeText(this, "Сервер недоступен!", Toast.LENGTH_LONG);
                        toast.show();
                        onClickExit(null);
                    }
                    if (!raiting.equals(-1.0)) tviewRating.setText(raiting+"");
                    else tviewRating.setText("Нет рейтинга");
                    break;
                case 204:
                    onClickUserBack(null);
                    break;
                case -1:
                    toast = Toast.makeText(this, "Сервер недоступен! Изменение оценки невозможно!", Toast.LENGTH_LONG);
                    toast.show();
                    break;
            }


        }
        catch (Exception e) {
            Toast toast = Toast.makeText(this, "Сервер недоступен!", Toast.LENGTH_LONG);
            toast.show();
            onClickExit(null);
        }
    });
}
    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
public void onClickUserBack (View v) {
        Intent intent = new Intent(this, TripActivity.class);
        intent.putExtra("login", login);
        intent.putExtra("id", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
}
    public void onClickExit (View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onClickUserBack(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
