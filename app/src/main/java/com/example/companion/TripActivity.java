package com.example.companion;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class TripActivity extends AppCompatActivity {
    ActionBar actionBar;
    TextView tviewDateTimeFrom;
    TextView tviewDateTimeTo;
    TextView tviewFrom;
    TextView tviewTo;
    TextView tviewCount;
    TextView tviewFreeCount;
    TextView tviewTransport;
    TextView tviewCost;
    Button buttonDriver;
    Button buttonEdit;
    Button button;
    String driver;
    String login;
    boolean isComplieted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        Bundle arguments = getIntent().getExtras();
        int id = arguments.getInt("id");
        login = arguments.getString("login");
        tviewDateTimeFrom = findViewById(R.id.txtviewDateTimeFromResult);
        tviewDateTimeTo = findViewById(R.id.txtviewDateTimeToResult);
        tviewFrom = findViewById(R.id.txtviewFromResult);
        tviewTo = findViewById(R.id.txtviewToResult);
        tviewCount = findViewById(R.id.txtviewCountResult);
        tviewFreeCount = findViewById(R.id.txtviewFreeCountResult);
        tviewTransport = findViewById(R.id.txtviewTransportResult);
        tviewCost = findViewById(R.id.txtviewCostResult);
        buttonDriver = findViewById(R.id.buttonDriver);
        buttonEdit=findViewById(R.id.buttonEdit);
        button=findViewById(R.id.button);
        actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
try {
    HTTP.TripGet httpGet=new HTTP.TripGet();
    Trip trip=null;

    httpGet.execute(id);

    if (httpGet.get()!=null&&httpGet.get().getClass()!=Trip.class) {
        Toast toast = Toast.makeText(this, "Сервер недоступен!", Toast.LENGTH_LONG);
        toast.show();
        onClickExit(null);

    }
    else {



        if (httpGet.get()==null) {

            trip=null;
            Toast toast = Toast.makeText(this, "Данная поездка не существует", Toast.LENGTH_LONG);
            toast.show();
            onClickTripBack(null);
        }

        else {
                trip=(Trip)httpGet.get();


        }
    }
    tviewFrom.setText(trip.getFrom());
    tviewTo.setText(trip.getTo());
    tviewCount.setText(trip.getCountOfPlaces() + "");
    tviewFreeCount.setText(trip.getCountOfPlaces() - trip.getCurrentCountOfPlaces() + "");
    tviewTransport.setText(trip.getTransport());
    tviewCost.setText(trip.getCost() / (trip.getCurrentCountOfPlaces() + 1) + "");
    driver = trip.getDriver();
    tviewDateTimeFrom.setText(Mapper.convertCalendarToString(trip.getDateTimeFrom()));
    tviewDateTimeTo.setText(Mapper.convertCalendarToString(trip.getDateTimeTo()));
    Double raiting;
    HTTP.RatingsGet httpGet1=new HTTP.RatingsGet();
    httpGet1.execute(driver);
    raiting=httpGet1.get();
    if (raiting==null) {
        Toast toast = Toast.makeText(this, "Сервер недоступен!", Toast.LENGTH_LONG);
        toast.show();
        onClickExit(null);
    }
    if (!raiting.equals(-1.0)) buttonDriver.setText(driver+" "+raiting);
    else buttonDriver.setText(driver+" Нет рейтинга");
    if (trip.getDateTimeFrom().compareTo(Calendar.getInstance())<0) isComplieted=true;
}
catch (Exception e) {
    Toast toast = Toast.makeText(this, "Сервер недоступен!", Toast.LENGTH_LONG);
    toast.show();
    onClickExit(null);
}
    buttonDriver.setOnClickListener((v) -> {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("user", driver);
        intent.putExtra("login", login);
        intent.putExtra("id", id);
        intent.putExtra("isComplieted", isComplieted);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    });
        buttonEdit.setOnClickListener((v) -> {
            Intent intent = new Intent(this, EditTripActivity.class);
            intent.putExtra("login", login);
            intent.putExtra("id", id);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        if (!isComplieted) button.setVisibility(View.VISIBLE);
    if (login.equals(driver)) {
        if (!isComplieted) buttonEdit.setVisibility(View.VISIBLE);
        button.setText("Удалить");
        button.setOnClickListener((v) -> {

            try {
                HTTP.TripDelete httpDelete = new HTTP.TripDelete();
                httpDelete.execute(id);
                if (httpDelete.get()) {
                    Intent intent = new Intent(this, TripListActivity.class);
                    intent.putExtra("login", login);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(this, "Сервер недоступен! Удаление поездки невозможно!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
            catch (Exception e) {
                Toast toast = Toast.makeText(this, "Сервер недоступен! Удаление поездки невозможно!", Toast.LENGTH_LONG);
                toast.show();
            }
        });

    } else {
        Intent intent = new Intent(this, TripActivity.class);
        intent.putExtra("login", login);
        intent.putExtra("id", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        boolean isPassenger=true;
        try {
            //isPassenger=databaseAdapter.isConnection(new Connection(login,id));
            HTTP.ConnectionHead httpHead = new HTTP.ConnectionHead();
            httpHead.execute(new Connection(login, id));
            Toast toast;
            switch (httpHead.get()) {
                case 204:

                    onClickTripBack(null);
                    break;
                case 404:


                    isPassenger=false;
                    break;
                case -1:
                    toast = Toast.makeText(this, "Сервер недоступен!", Toast.LENGTH_LONG);
                    toast.show();
                    onClickExit(null);
                    break;
            }
        }
        catch (Exception e) {
            Toast toast = Toast.makeText(this, "Сервер недоступен!", Toast.LENGTH_LONG);
            toast.show();
            onClickExit(null);
        }
        if (isPassenger) {
            button.setText("Выписаться");

            button.setOnClickListener((v) -> {
                Toast toast;
                try {
                    HTTP.ConnectionDelete httpDelete = new HTTP.ConnectionDelete();
                    httpDelete.execute(new Connection(login,id));
                    switch(httpDelete.get()) {
                        case 200:
                            startActivity(intent);
                            break;
                        case 204:
                            toast = Toast.makeText(this, "Данная поездка не существует", Toast.LENGTH_LONG);
                            toast.show();
                            onClickTripBack(null);
                            break;
                        case -1:
                            toast = Toast.makeText(this, "Сервер недоступен! Удаление записи невозможно!", Toast.LENGTH_LONG);
                            toast.show();
                            break;
                    }
                }
                catch (Exception e) {
                    toast = Toast.makeText(this, "Сервер недоступен! Удаление записи невозможно!", Toast.LENGTH_LONG);
                    toast.show();
                }

            });
        } else {
            button.setText("Записаться");
            button.setOnClickListener((v) -> {
                try {
                    HTTP.ConnectionPost httpPost = new HTTP.ConnectionPost();
                    httpPost.execute(new Connection(login,id));
                    Toast toast;
                    switch(httpPost.get()) {
                        case 201:
                            startActivity(intent);
                            break;
                        case 204:

                                toast = Toast.makeText(this, "Данная поездка не существует", Toast.LENGTH_LONG);
                                toast.show();
                                onClickTripBack(null);

                            break;
                        case 409:
                            toast = Toast.makeText(this, "Мест нет!", Toast.LENGTH_LONG);
                            toast.show();
                            startActivity(intent);
                            break;
                        case -1:
                            toast = Toast.makeText(this, "Сервер недоступен! Запись на поездку невозможна!", Toast.LENGTH_LONG);
                            toast.show();
                            break;
                    }
                }
                catch (Exception e) {
                    Toast toast = Toast.makeText(this, "Сервер недоступен! Запись на поездку невозможна!", Toast.LENGTH_LONG);
                    toast.show();
                }

            });
        }
    }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void onClickTripBack (View v) {
        Intent intent = new Intent(this, TripListActivity.class);
        intent.putExtra("login", login);
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
                onClickTripBack(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}





