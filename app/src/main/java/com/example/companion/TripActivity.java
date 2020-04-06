package com.example.companion;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    Button buttonEdit;
    Button button;
    String driver;
    String login;
    boolean isFull;
    boolean isComplieted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
try(DatabaseAdapter databaseAdapter=new DatabaseAdapter(this)) {
    Trip trip = databaseAdapter.getTrip(id);
    tviewFrom.setText(trip.getFrom());
    tviewTo.setText(trip.getTo());
    tviewCount.setText(trip.getCountOfPlaces() + "");
    tviewFreeCount.setText(trip.getCountOfPlaces() - trip.getCurrentCountOfPlaces() + "");
    tviewTransport.setText(trip.getTransport());
    tviewCost.setText(trip.getCost() / (trip.getCurrentCountOfPlaces() + 1) + "");
    driver = trip.getDriver();
    tviewDateTimeFrom.setText(Mapper.convertCalendarToString(trip.getDateTimeFrom()));
    tviewDateTimeTo.setText(Mapper.convertCalendarToString(trip.getDateTimeTo()));
    Double raiting = databaseAdapter.getRatingByUser(driver);
    if (raiting != null) {
        buttonDriver.setText(driver + " " + raiting);
    } else {
        buttonDriver.setText(driver + " Нет рейтинга");
    }
    if (trip.getCountOfPlaces()==trip.getCurrentCountOfPlaces()) isFull=true;
    if (trip.getDateTimeFrom().compareTo(Calendar.getInstance())<0) isComplieted=true;
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
            try (DatabaseAdapter databaseAdapter=new DatabaseAdapter(this)) {
                Intent intent = new Intent(this, TripListActivity.class);
                intent.putExtra("login", login);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                databaseAdapter.removeTrip(id);
                startActivity(intent);
            }
        });

    } else {
        Intent intent = new Intent(this, TripActivity.class);
        intent.putExtra("login", login);
        intent.putExtra("id", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        boolean isPassenger;
        try(DatabaseAdapter databaseAdapter=new DatabaseAdapter(this)) {
            isPassenger=databaseAdapter.isConnection(login,id);
        }
        if (isPassenger) {
            button.setText("Выписаться");
            button.setOnClickListener((v) -> {
                try(DatabaseAdapter databaseAdapter=new DatabaseAdapter(this)) {
                    databaseAdapter.removeConnection(login,id);
                }
                startActivity(intent);
            });
        } else {
            if (isFull) button.setEnabled(false);
            button.setText("Записаться");
            button.setOnClickListener((v) -> {
                try(DatabaseAdapter databaseAdapter=new DatabaseAdapter(this)) {
                    databaseAdapter.addConnection(login,id);
                    /*Toast toast = Toast.makeText(this, login+" "+id, Toast.LENGTH_LONG);

                    toast.show();*/
                }
                startActivity(intent);
            });
        }
    }
    }
    public void onClickTripBack (View v) {
        Intent intent = new Intent(this, TripListActivity.class);
        intent.putExtra("login", login);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}





